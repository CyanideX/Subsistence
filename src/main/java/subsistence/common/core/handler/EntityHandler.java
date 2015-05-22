package subsistence.common.core.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import subsistence.common.item.SubsistenceItems;

public class EntityHandler {

    @SubscribeEvent
    public void onItemToss(ItemTossEvent event) {
        final EntityItem entity = event.entityItem;
        final ItemStack item = entity.getEntityItem();

        if (item != null && item.getItem() == Items.water_bucket) {
            entity.setEntityItemStack(new ItemStack(SubsistenceItems.boilingBucket));
        }
    }
}
