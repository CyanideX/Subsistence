package oldStuff.common.core.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;

public class SpiderTracker {

    private static final int MOB_SPAWN_LIGHT = 7;
    private static final float WEB_CHANCE = 0.01F;

    private long lastTick = 0L;

    @SubscribeEvent
    public void onSpiderTick(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntitySpider) {
            EntitySpider spider = (EntitySpider) event.entity;
            World world = spider.worldObj;

            if (spider.getAttackTarget() == null && world.rand.nextFloat() <= WEB_CHANCE) {
                float brightness = world.getLightBrightness((int) spider.posX, (int) spider.posY, (int) spider.posZ);

                if (brightness * 16 <= MOB_SPAWN_LIGHT) {
                    if (world.isAirBlock((int) spider.posX, (int) spider.posY, (int) spider.posZ) && !world.canBlockSeeTheSky((int) spider.posX, (int) spider.posY, (int) spider.posZ)) {
                        world.setBlock((int) spider.posX, (int) spider.posY, (int) spider.posZ, Blocks.web);
                    }
                }
            }
        }
    }
}
