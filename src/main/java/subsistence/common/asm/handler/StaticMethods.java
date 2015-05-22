package subsistence.common.asm.handler;

import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import subsistence.common.item.SubsistenceItems;

/**
 * @author dmillerw
 */
public class StaticMethods {

    public static EntityPlayer findPlayerToAttack(EntitySpider spider) {
        float brightness = spider.getBrightness(1.0F);
        double distance = 16.0D;

        if (brightness < 0.5F) {
            return spider.worldObj.getClosestVulnerablePlayerToEntity(spider, distance);
        } else {
            EntityPlayer player = spider.worldObj.getClosestPlayerToEntity(spider, distance);
            if (player != null && player.getHeldItem() != null && player.getHeldItem().getItem() == SubsistenceItems.net && player.getHeldItem().getItemDamage() == 1) {
                return player;
            } else {
                return null;
            }
        }
    }
}
