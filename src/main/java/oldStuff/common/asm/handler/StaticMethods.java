package oldStuff.common.asm.handler;

import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MouseHelper;
import org.lwjgl.input.Mouse;
import oldStuff.common.item.SubsistenceItems;

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

    public static int getMinimumLightLevel() {
        return 0;
    }

    public static boolean lockMouse = false;
    public static void updateMouse(MouseHelper helper) {
        if (!lockMouse) {
            helper.deltaX = Mouse.getDX();
            helper.deltaY = Mouse.getDY();
        } else {
            helper.deltaX = 0;
            helper.deltaY = 0;
        }
    }
}
