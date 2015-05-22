package subsistence.common.block.machine;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.block.prefab.SubsistenceTileMultiBlock;
import subsistence.common.tile.machine.TileCompost;
import subsistence.common.util.ArrayHelper;


public class BlockCompost extends SubsistenceTileMultiBlock {

    private static final String[] NAMES = new String[]{"wood", "stone"};

    public BlockCompost() {
        super(Material.wood);
    }

    @Override
    public int[] getSubtypes() {
        return ArrayHelper.getArrayIndexes(NAMES);
    }

    @Override
    public String getNameForType(int type) {
        return ArrayHelper.safeGetArrayIndex(NAMES, type);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileCompost();
    }

    @Override
    public boolean useCustomRender() {
        return true;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float fx, float fy, float fz) {
        TileCompost tileCompost = (TileCompost) world.getTileEntity(x, y, z);

        if (tileCompost != null && !world.isRemote) {
            ItemStack held = player.getHeldItem();

            // A player can only open/close the barrel while sneaking
            if (player.isSneaking()) {
                tileCompost.lidOpen = !tileCompost.lidOpen;
                tileCompost.markForUpdate();
                return false;
            }

            // Interaction code
            if (tileCompost.lidOpen && side == 1) {
                if (held != null) {
                    if (FluidContainerRegistry.isEmptyContainer(held)) {
                        FluidStack fluidStack = tileCompost.fluid;

                        if (fluidStack != null) {
                            ItemStack filled = FluidContainerRegistry.fillFluidContainer(fluidStack, held);
                            if (filled != null) {
                                player.setCurrentItemOrArmor(0, filled);
                                tileCompost.fluid = null;
                                tileCompost.updateContents();
                            }
                        }
                    } else if (FluidContainerRegistry.isFilledContainer(held)) {
                        FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(held);

                        if (fluidStack != null && fluidStack.getFluid() == FluidRegistry.WATER && fluidStack.amount == FluidContainerRegistry.BUCKET_VOLUME) {
                            if (tileCompost.fluid == null) {
                                tileCompost.fluid = fluidStack.copy();
                                tileCompost.markForUpdate();

                                player.setCurrentItemOrArmor(0, FluidContainerRegistry.drainFluidContainer(held));
                            }
                        }
                    } else {
                        ItemStack copy = held.copy();
                        copy.stackSize = 1;

                        if (tileCompost.addItem(copy)) {
                            held.stackSize--;
                            if (held.stackSize <= 0) {
                                player.setCurrentItemOrArmor(0, null);
                            }
                            tileCompost.markForUpdate();
                        }
                    }
                }
            }
        }

        return !player.isSneaking();
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer entityPlayer) {
        if (!world.isRemote) {
            TileCompost tileCompost = (TileCompost) world.getTileEntity(x, y, z);

            if (tileCompost != null && entityPlayer.isSneaking()) {
                ItemStack held = entityPlayer.getHeldItem();

                if (held != null) {
                    ItemStack output = tileCompost.inventoryPeek();
                    if (output != null) {
                        if (held.isItemEqual(output) && (held.stackSize + output.stackSize) <= held.getMaxStackSize()) {
                            held.stackSize += output.stackSize;
                            tileCompost.inventoryPop();
                            tileCompost.updateContents();
                        }
                    }
                } else {
                    entityPlayer.setCurrentItemOrArmor(0, tileCompost.inventoryPop());
                    tileCompost.updateContents();
                }
            }
        }
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {
        return this.removedByPlayer(world, player, x, y, z, false);
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
        if (player.capabilities.isCreativeMode && player.isSneaking()) {
            this.onBlockClicked(world, x, y, z, player);
            return false;
        } else {
            return world.setBlockToAir(x, y, z);
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        // Replace with proper textures mimicking model textures?
        switch (meta) {
            case 1:
                return Blocks.stone.getIcon(0, 0);
            default:
                return Blocks.planks.getIcon(0, 0);
        }
    }
}
