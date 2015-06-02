package subsistence.common.core.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.fluid.SubsistenceFluids;

public class BoilingWaterHandler {

    @SubscribeEvent
    public void onEnterNether(EntityJoinWorldEvent event) {
        if (event.world.provider.dimensionId != -1) {
            return;
        }

        if (event.entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)event.entity;
            InventoryPlayer inventoryPlayer = player.inventory;
            for (int i=0; i < inventoryPlayer.getSizeInventory(); i++) {
                ItemStack item = inventoryPlayer.getStackInSlot(i);
                if (item != null) {
                    FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(item);
                    if (fluidStack != null && fluidStack.getFluid() == FluidRegistry.WATER) {
                        ItemStack drained = FluidContainerRegistry.drainFluidContainer(item);
                        ItemStack filled = FluidContainerRegistry.fillFluidContainer(new FluidStack(SubsistenceFluids.boilingWaterFluid, fluidStack.amount), drained);
                        inventoryPlayer.setInventorySlotContents(0, filled);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onItemToss(ItemTossEvent event) {
        if (event.entityItem.worldObj.provider.dimensionId != -1)
            return;

        final EntityItem entity = event.entityItem;
        final ItemStack item = entity.getEntityItem();

        if (item != null) {
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(item);
            if (fluidStack != null && fluidStack.getFluid() == FluidRegistry.WATER) {
                ItemStack drained = FluidContainerRegistry.drainFluidContainer(item);
                ItemStack filled = FluidContainerRegistry.fillFluidContainer(new FluidStack(SubsistenceFluids.boilingWaterFluid, fluidStack.amount), drained);
                entity.setEntityItemStack(filled);
            }
        }
    }

}
