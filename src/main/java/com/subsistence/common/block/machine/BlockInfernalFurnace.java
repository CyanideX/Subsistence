package com.subsistence.common.block.machine;

import com.subsistence.Subsistence;
import com.subsistence.common.block.prefab.SubsistenceTileBlock;
import com.subsistence.common.core.handler.GuiHandler;
import com.subsistence.common.lib.SubsistenceProps;
import com.subsistence.common.tile.machine.TileInfernalFurnace;
import com.subsistence.common.util.InventoryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class BlockInfernalFurnace extends SubsistenceTileBlock {

    private IIcon[] icons;

    public BlockInfernalFurnace() {
        super(Material.rock);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float fx, float fy, float fz) {
        if (!world.isRemote && !player.isSneaking()) {
            if (player.getHeldItem() != null && player.getHeldItem().getItem() == Items.flint_and_steel) {
                TileInfernalFurnace tile = (TileInfernalFurnace) world.getTileEntity(x, y, z);

                if (tile != null && !tile.lit) {
                    tile.lit = true;

                    world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "fire.ignite", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
                    player.getHeldItem().damageItem(1, player);

                    tile.markForUpdate();
                }
            } else {
                player.openGui(Subsistence.instance, GuiHandler.GUI_INFERNAL_FURNACE, world, x, y, z);
            }
        }
        return !player.isSneaking();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileInfernalFurnace tile = (TileInfernalFurnace) world.getTileEntity(x, y, z);

        if (tile != null) {
            for (int i = 0; i < tile.getSizeInventory(); i++) {
                ItemStack stack = tile.getStackInSlot(i);

                if (stack != null) {
                    InventoryHelper.dropItem(world, x, y, z, ForgeDirection.UNKNOWN, stack, world.rand);
                }
            }
        }

        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z) == 1 ? (int) (0.875F * 16) : 0;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileInfernalFurnace();
    }

    @Override
    public boolean useCustomRender() {
        return false;
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        TileInfernalFurnace tile = (TileInfernalFurnace) world.getTileEntity(x, y, z);

        if (tile != null) {
            if (tile.orientation.ordinal() == side) {
                return icons[world.getBlockMetadata(x, y, z) == 1 ? 2 : 1];
            }
        }

        if (side == 1) {
            return icons[3];
        } else {
            return icons[0];
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (side == ForgeDirection.SOUTH.ordinal()) {
            return icons[1];
        } else if (side == 1) {
            return icons[3];
        } else {
            return icons[0];
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        icons = new IIcon[4];

        icons[0] = iconRegister.registerIcon(SubsistenceProps.RESOURCE_PREFIX + "machine/infernalFurnace_side");
        icons[1] = iconRegister.registerIcon(SubsistenceProps.RESOURCE_PREFIX + "machine/infernalFurnace_front");
        icons[2] = iconRegister.registerIcon(SubsistenceProps.RESOURCE_PREFIX + "machine/infernalFurnace_front_ON");
        icons[3] = iconRegister.registerIcon(SubsistenceProps.RESOURCE_PREFIX + "machine/infernalFurnace_top");
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(this, 1, ForgeDirection.SOUTH.ordinal()));
    }
}