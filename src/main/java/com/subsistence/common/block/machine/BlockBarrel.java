package com.subsistence.common.block.machine;

import com.subsistence.common.block.SubsistenceBlocks;
import com.subsistence.common.block.prefab.SubsistenceTileMultiBlock;
import com.subsistence.common.item.SubsistenceItems;
import com.subsistence.common.item.ItemBarrelLid;
import com.subsistence.common.lib.IBarrel;
import com.subsistence.common.tile.machine.TileBarrel;
import com.subsistence.common.util.ArrayHelper;
import com.subsistence.common.util.FluidUtils;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.Random;

public final class BlockBarrel extends SubsistenceTileMultiBlock {

    private static final String[] NAMES = new String[]{"Wood", "Stone"};

    private int rain;

    public BlockBarrel() {
        super(Material.wood);
        this.setTickRandomly(true);
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
    public void registerBlockIcons(IIconRegister iconRegister) {

    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getCurrentEquippedItem();
        TileBarrel tile = (TileBarrel) world.getTileEntity(x, y, z);

        if (stack == null && tile.hasLid()) {
            tile.toggleLid();

            if (!world.isRemote)
                player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(SubsistenceItems.barrelLid, 1, tile.getBlockMetadata()));
            return true;
        }

        if (stack != null && stack.getItem() instanceof ItemBarrelLid) {
            return true;
        } else if (stack != null && !tile.hasLid()) {
            if (FluidContainerRegistry.isFilledContainer(stack)) {
                FluidUtils.fillTankWithContainer(tile, player);
            } else if (FluidContainerRegistry.isEmptyContainer(stack)) {
                FluidUtils.emptyTankIntoContainer(tile, player, tile.getFluid());
            } else {
                if (tile.getStackInSlot(0) == null) {
                    tile.setInventorySlotContents(0, new ItemStack(stack.getItem(), 1, stack.getItemDamage()));
                    player.inventory.decrStackSize(player.inventory.currentItem, 1);
                } else if (tile.getStackInSlot(1) == null) {
                    tile.setInventorySlotContents(1, new ItemStack(stack.getItem(), 1, stack.getItemDamage()));
                    player.inventory.decrStackSize(player.inventory.currentItem, 1);
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase living, ItemStack stack) {
        TileBarrel tile = (TileBarrel) world.getTileEntity(x, y, z);
        tile.setLid(((IBarrel) stack.getItem()).hasLid(stack));
        if (tile.hasLid()) {
            tile.setInput(((IBarrel) stack.getItem()).getInput(stack));
            tile.setFluid(((IBarrel) stack.getItem()).getFluid(stack));
        }
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
    public ItemStack getPickBlock(MovingObjectPosition mop, World world, int x, int y, int z, EntityPlayer player) {
        TileBarrel tile = (TileBarrel) world.getTileEntity(x, y, z);
        ItemStack stack = new ItemStack(SubsistenceBlocks.barrel, 1, tile.getBlockMetadata());
        IBarrel barrel = (IBarrel) stack.getItem();

        barrel.setLid(stack, tile.hasLid());
        if (tile.hasLid()) {
            if (tile.getInput() != null) {
                barrel.setInput(stack, tile.getInput());
            }

            if (tile.getFluid() != null) {
                barrel.setFluid(stack, tile.getFluid());
            }
        }

        return stack;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {

        if (world.isRaining() && world.getTopBlock(x, z) == this) {
            rain++;
            rainWater(world, x, y, z);
        }
    }

    private void rainWater(World world, int x, int y, int z) {
        TileBarrel barrel = (TileBarrel) world.getTileEntity(x, y, z);

        if (barrel != null && !barrel.hasLid() && rain == TileBarrel.rain) {
            //if (barrel.itemStack == null) {
            if (barrel.getFluid() == null) {
                barrel.setFluid(new FluidStack(FluidRegistry.WATER, 100));
            } else {
                if (barrel.getFluid().getFluid() == FluidRegistry.WATER && !(barrel.getFluid().amount > barrel.getCapacity())) {
                    barrel.getFluid().amount += 100;
                    if (barrel.getFluid().amount > barrel.getCapacity()) {
                        barrel.getFluid().amount = barrel.getCapacity();
                    }
                }
            }
            // }

            barrel.markForUpdate();
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
}