package subsistence.common.block.machine;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.block.prefab.SubsistenceTileMultiBlock;
import subsistence.common.item.SubsistenceItems;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.tile.machine.TileBarrel;
import subsistence.common.util.ArrayHelper;

import java.util.Random;

public final class BlockBarrel extends SubsistenceTileMultiBlock {

    private static final String[] NAMES = new String[]{"wood", "stone"};

    public BlockBarrel() {
        super(Material.wood);
        this.setHardness(0.5f);
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
        return new TileBarrel();
    }

    @Override
    public boolean useCustomRender() {
        return true;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getCurrentEquippedItem();
        TileBarrel tile = (TileBarrel) world.getTileEntity(x, y, z);

        if (stack == null && tile.hasLid()) {
            tile.toggleLid();

            if (!world.isRemote)
                player.setCurrentItemOrArmor(0, new ItemStack(SubsistenceItems.barrelLid, 1, tile.getBlockMetadata()));
            return true;
        }
        ItemStack held = player.getHeldItem();

        if (tile != null)
            if (side == 1 && !tile.hasLid()) { //only on top
                if (FluidContainerRegistry.isFilledContainer(held)) { //put fluid in
                    System.out.println("put fluid in");
                    FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(held);
                    System.out.println("fluid stack is: "+fluidStack.getLocalizedName());
                    if (tile.addFluid(fluidStack)) {
                        System.out.println("actually put things in");
                        if (!player.capabilities.isCreativeMode) {
                            player.setCurrentItemOrArmor(0, FluidContainerRegistry.drainFluidContainer(held));
                            //TODO: container does not empty when used on partially full barrel
                        }
                    } else {
                        System.out.println("apparently you cant...");
                    }
                } else if (tile.fluid != null && FluidContainerRegistry.isEmptyContainer(held)) { //take fluid out
                    System.out.println("take fluid out");
                    ItemStack container = FluidContainerRegistry.fillFluidContainer(tile.fluid, FluidContainerRegistry.EMPTY_BUCKET);
                    FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(container);
                    if (container != null && tile.reduceFluid(fluidStack)) {
                        if (!player.capabilities.isCreativeMode) {
                            if (player.getCurrentEquippedItem().stackSize == 1) {
                                player.setCurrentItemOrArmor(0, container);
                            } else {
                                player.getCurrentEquippedItem().stackSize--;
                                if (player.inventory.addItemStackToInventory(container)) {
                                } else {
                                    player.func_146097_a(container,false,false);
                                }
                            }
                        }
                    }
                } else if (held != null && Block.getBlockFromItem(held.getItem()) != Blocks.air) { //add blocks
                    System.out.println("add blocks");
                    ItemStack itemCopy = held.copy();
                    if (SubsistenceRecipes.BARREL.isAllowed(itemCopy)) {
                        itemCopy.stackSize = 1;
                        if (tile.addItemToStack(itemCopy)) {
                            held.stackSize--;
                            if (held.stackSize <= 0) {
                                player.setCurrentItemOrArmor(0, null);
                            }
                            tile.markForUpdate();
                        }
                    }
                } else {
                    if (tile.contents != null && tile.contents.length > 0) {
                        player.setCurrentItemOrArmor(0, tile.removeItemFromStack());
                    }
                }

            }
        return true;
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean harvest) {
        if (!player.capabilities.isCreativeMode && !world.isRemote && this.canHarvestBlock(player, world.getBlockMetadata(x, y, z))) {

            float motion = 0.7F;
            double motX = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;
            double motY = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;
            double motZ = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;

            EntityItem item = new EntityItem(world, x + motX, y + motY, z + motZ, this.getPickBlock(null, world, x, y, z, player));
            world.spawnEntityInWorld(item);
        }

        return world.setBlockToAir(x, y, z);
    }


    @Override
    public int quantityDropped(Random rand) {
        return 0;
    }

    @Override
    public Item getItemDropped(int i, Random rand, int j) {
        return null;
    }
}