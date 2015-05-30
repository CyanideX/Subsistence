package subsistence.common.block.machine;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import subsistence.common.block.SubsistenceBlocks;
import subsistence.common.block.prefab.SubsistenceTileMultiBlock;
import subsistence.common.fluid.SubsistenceFluids;
import subsistence.common.item.SubsistenceItems;
import subsistence.common.lib.client.EnumParticle;
import subsistence.common.particle.SteamFX;
import subsistence.common.tile.machine.TileBarrel;
import subsistence.common.util.ArrayHelper;

import java.util.Random;

public final class BlockBarrel extends SubsistenceTileMultiBlock {

    private static final String[] NAMES = new String[]{"wood", "stone", "nether"};

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
        if (!world.isRemote) {
            TileBarrel tileBarrel = (TileBarrel) world.getTileEntity(x, y, z);
            int metadata = world.getBlockMetadata(x, y, z);
            if (tileBarrel != null) {
                ItemStack held = player.getHeldItem();

                if (held != null) {
                    if (held.getItem() == SubsistenceItems.barrelLid) {
                        if (held.getItemDamage() == metadata) {
                            if (!tileBarrel.hasLid) {
                                player.setCurrentItemOrArmor(0, null);
                                ((EntityPlayerMP)player).updateHeldItem();

                                tileBarrel.hasLid = true;
                                tileBarrel.markForUpdate();
                            }
                        } else {
                            return true;
                        }
                    } else {
                        if (!tileBarrel.hasLid) {
                            if (FluidContainerRegistry.isFilledContainer(held)) {
                                if (held.stackSize == 1) {
                                    if (tileBarrel.addFluid(FluidContainerRegistry.getFluidForFilledItem(held))) {
                                        player.setCurrentItemOrArmor(0, FluidContainerRegistry.drainFluidContainer(held).copy());
                                        ((EntityPlayerMP)player).updateHeldItem();
                                    }
                                } else {
                                    return true;
                                }
                            } else if (FluidContainerRegistry.isEmptyContainer(held)) {
                                if (held.stackSize == 1) {
                                    ItemStack filled = FluidContainerRegistry.fillFluidContainer(tileBarrel.fluidContents, held);
                                    if (filled != null) {
                                        player.setCurrentItemOrArmor(0, filled);
                                        ((EntityPlayerMP)player).updateHeldItem();

                                        tileBarrel.fluidContents.amount -= (FluidContainerRegistry.getContainerCapacity(filled));
                                        if (tileBarrel.fluidContents.amount <= 0)
                                            tileBarrel.fluidContents = null;
                                        tileBarrel.markForUpdate();
                                    }
                                } else {
                                    return true;
                                }
                            } else {
                                ItemStack copy = held.copy();
                                copy.stackSize = 1;

                                if (tileBarrel.addItem(copy)) {
                                    held.stackSize--;
                                    if (held.stackSize <= 0) {
                                        player.setCurrentItemOrArmor(0, null);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (tileBarrel.hasLid && player.isSneaking()) {
                        player.setCurrentItemOrArmor(0, new ItemStack(SubsistenceItems.barrelLid, 1, metadata));
                        ((EntityPlayerMP)player).updateHeldItem();

                        tileBarrel.hasLid = false;
                        tileBarrel.markForUpdate();
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer entityPlayer) {
        if (!world.isRemote) {
            TileBarrel tileBarrel = (TileBarrel) world.getTileEntity(x, y, z);

            if (tileBarrel != null && !tileBarrel.hasLid  && entityPlayer.isSneaking()) {
                ItemStack held = entityPlayer.getHeldItem();

                if (held != null) {
                    for (int i=tileBarrel.itemContents.length - 1; i>=0; i--) {
                        ItemStack itemStack = tileBarrel.itemContents[i];
                        if (itemStack != null) {
                            if (held.isItemEqual(itemStack) && (held.stackSize + itemStack.stackSize) <= held.getMaxStackSize()){
                                held.stackSize += itemStack.stackSize;
                                ((EntityPlayerMP)entityPlayer).updateHeldItem();

                                tileBarrel.itemContents[i] = null;
                                tileBarrel.markForUpdate();

                                break;
                            }
                        }
                    }
                } else {
                    for (int i=tileBarrel.itemContents.length - 1; i>=0; i--) {
                        ItemStack itemStack = tileBarrel.itemContents[i];
                        if (itemStack != null) {
                            entityPlayer.setCurrentItemOrArmor(0, itemStack.copy());
                            ((EntityPlayerMP)entityPlayer).updateHeldItem();

                            tileBarrel.itemContents[i] = null;
                            tileBarrel.markForUpdate();

                            break;
                        }
                    }
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

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        TileBarrel tileBarrel = (TileBarrel) world.getTileEntity(x, y, z);
        if (tileBarrel != null && tileBarrel.fluidContents != null) {
            return tileBarrel.fluidContents.getFluid().getLuminosity();
        } else {
            return 0;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        super.randomDisplayTick(world, x, y, z, rand);

        TileBarrel tileBarrel = (TileBarrel) world.getTileEntity(x, y, z);
        if (tileBarrel != null && tileBarrel.fluidContents != null && tileBarrel.fluidContents.getFluid() == SubsistenceFluids.boilingWaterFluid) {
            EnumParticle.BUBBLE.display(world, x + rand.nextDouble(), y, z + rand.nextDouble(), 0, 0.5, 0);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(new SteamFX(world, x + rand.nextDouble(), y + 1, z + rand.nextDouble(), rand.nextInt(2), 0));
        }
    }

    @Override
    public int quantityDropped(Random rand) {
        return 0;
    }

    @Override
    public Item getItemDropped(int i, Random rand, int j) {
        return null;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        ItemStack ret = new ItemStack(SubsistenceBlocks.barrel, 1, world.getTileEntity(x, y, z).getBlockMetadata());
//        ret.setTagCompound(world.getTileEntity(x, y, z));
        return ret;
    }
}