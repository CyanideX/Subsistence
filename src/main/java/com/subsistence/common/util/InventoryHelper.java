package com.subsistence.common.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

/**
 * @author dmillerw
 */
public class InventoryHelper {

    public static void ejectItem(World world, int x, int y, int z, ForgeDirection side, ItemStack item, Random random) {
        if (item != null) {
            TileEntity tile = world.getTileEntity(x + side.offsetX, y + side.offsetY, z + side.offsetZ);

            if (tile != null && tile instanceof IInventory) {
                ItemStack result = TileEntityHopper.func_145889_a((IInventory) tile, item, side.getOpposite().ordinal());
                if (result != null) {
                    dropItem(world, x, y, z, side, result, random);
                }
            } else {
                dropItem(world, x, y, z, side, item, random);
            }
        }
    }

    public static void dropItem(World world, int x, int y, int z, ForgeDirection side, ItemStack stack, Random random) {
        double spawnX = x + 0.5D + 1.5D * side.offsetX;
        double spawnY = y + 0.5D + 1.5D * side.offsetY;
        double spawnZ = z + 0.5D + 1.5D * side.offsetZ;
        EntityItem entity = new EntityItem(world, spawnX, spawnY, spawnZ, stack);

        if (stack.hasTagCompound()) {
            entity.getEntityItem().setTagCompound((NBTTagCompound) stack.getTagCompound().copy());
        }

        world.spawnEntityInWorld(entity);
    }
}
