package oldStuff.common.core.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import oldStuff.common.item.SubsistenceItems;

public class WebHandler {

    @SubscribeEvent
    public void harvestDropEvent(BlockEvent.HarvestDropsEvent event) {
        if (event.block == Blocks.web && event.harvester != null) {
            ItemStack stack = event.harvester.getHeldItem();
            if (stack != null && stack.getItem() == SubsistenceItems.net) {
                event.drops.clear();
                event.drops.add(new ItemStack(Item.getItemFromBlock(Blocks.web)));
            }
        }
    }
}
