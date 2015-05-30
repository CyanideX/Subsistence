package subsistence.common.core.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import subsistence.Subsistence;
import subsistence.common.item.SubsistenceItems;

public class BoilingWaterHandler {

    @SubscribeEvent
    public void onEnterNether(EntityJoinWorldEvent event) {
        if (!event.world.getWorldInfo().getWorldName().equalsIgnoreCase("DIM-1")) {
            return;
        }

        if (event.entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)event.entity;
            InventoryPlayer inventoryPlayer = player.inventory;
            for (int i=0; i < inventoryPlayer.getSizeInventory(); i++) {
                ItemStack item = inventoryPlayer.getStackInSlot(i);
                if (item != null) {
                    if (item.getItem() == Items.water_bucket) {
                        inventoryPlayer.setInventorySlotContents(i, new ItemStack(SubsistenceItems.boilingBucket));
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

        if (item != null && item.getItem() == Items.water_bucket) {
            entity.setEntityItemStack(new ItemStack(SubsistenceItems.boilingBucket));
        }
    }

}
