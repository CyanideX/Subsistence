package com.subsistence.common.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

public final class FluidUtils {
    public static boolean fillTankWithContainer(IFluidTank tank, EntityPlayer player) {
        ItemStack stack = player.getCurrentEquippedItem();
        FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(stack);

        if (liquid == null) {
            return false;
        }

        if (tank.fill(liquid, false) != liquid.amount && !player.capabilities.isCreativeMode) {
            return false;
        }

        tank.fill(liquid, true);

        if (!player.capabilities.isCreativeMode) {
            Item item = stack.getItem();
            if (item.hasContainerItem(stack)) {
                ItemStack container = item.getContainerItem(stack);
                player.inventory.setInventorySlotContents(player.inventory.currentItem, container);
            } else {
                player.inventory.decrStackSize(player.inventory.currentItem, 1);
            }
        }

        player.inventoryContainer.detectAndSendChanges();
        return true;
    }

    public static boolean emptyTankIntoContainer(IFluidTank tank, EntityPlayer player, FluidStack fluid) {
        ItemStack stack = player.getCurrentEquippedItem();

        if (!FluidContainerRegistry.isEmptyContainer(stack)) {
            return false;
        }

        ItemStack filled = FluidContainerRegistry.fillFluidContainer(fluid, stack);
        FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(filled);
        if (liquid == null || filled == null) {
            return false;
        }

        tank.drain(liquid.amount, true);

        if (!player.capabilities.isCreativeMode) {
            if (stack.stackSize == 1) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, filled);
            } else if (player.inventory.addItemStackToInventory(filled)) {
                stack.stackSize--;
            } else {
                return false;
            }
        }

        player.inventoryContainer.detectAndSendChanges();
        return true;
    }
}