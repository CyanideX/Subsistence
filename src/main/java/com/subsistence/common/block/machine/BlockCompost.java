package com.subsistence.common.block.machine;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import com.subsistence.common.block.prefab.SubsistenceTileMultiBlock;
import com.subsistence.common.tile.machine.TileCompost;
import com.subsistence.common.util.ArrayHelper;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Thlayli
 */
public class BlockCompost extends SubsistenceTileMultiBlock {

    private static final String[] NAMES = new String[]{"Wood", "Stone"};

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

        if (tileCompost != null) {


            ItemStack held = player.getHeldItem();

            if (player.isSneaking()) {
                tileCompost.lidOpen = !tileCompost.lidOpen;
                tileCompost.sendPoke();
                tileCompost.markForUpdate();
            }

            if (side == 1 && tileCompost.lidOpen) {

                if (tileCompost.fluid == null && FluidContainerRegistry.isFilledContainer(held)) {
                    FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(held);
                    if (fluidStack != null) {
                        if (!player.capabilities.isCreativeMode) {
                            player.setCurrentItemOrArmor(0, FluidContainerRegistry.EMPTY_BUCKET);
                        }
                        tileCompost.addFluid(fluidStack);
                    }
                } else if (tileCompost.fluid != null && FluidContainerRegistry.isEmptyContainer(held)) {
                    ItemStack container = FluidContainerRegistry.fillFluidContainer(tileCompost.fluid, FluidContainerRegistry.EMPTY_BUCKET);
                    if (container != null) {
                        if (!player.capabilities.isCreativeMode) {
                            player.setCurrentItemOrArmor(0, container);
                        }
                        tileCompost.removeFluid();
                    }
                } else if (held != null && Block.getBlockFromItem(held.getItem()) != Blocks.air) {
                    ItemStack itemCopy = held.copy();
                    itemCopy.stackSize = 1;
                    if (tileCompost.addItemToStack(itemCopy)) {
                        held.stackSize--;
                        if (held.stackSize <= 0) {
                            player.setCurrentItemOrArmor(0, null);
                        }
                        tileCompost.markForUpdate();
                    }
                } else {
                    if (tileCompost.contents.length > 0) {
                        player.setCurrentItemOrArmor(0, tileCompost.removeItemFromStack());
                    }
                }
            }
        }
        return !player.isSneaking();
    }
}
