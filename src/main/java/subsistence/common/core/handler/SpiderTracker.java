package subsistence.common.core.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.init.Blocks;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

/**
 * @author dmillerw
 */
public class SpiderTracker {

    private static final int MOB_SPAWN_LIGHT = 7;

    private static final float WEB_CHANCE = 0.01F;

    private long lastTick = 0L;

    @SubscribeEvent
    public void onSpiderTick(TickEvent.ServerTickEvent event) {
        // Runs once a second (roughly)
        long nano = System.nanoTime();
        if (nano > lastTick) {
            lastTick = nano + 1000000000L;
        } else {
            return;
        }

        for (WorldServer world : DimensionManager.getWorlds()) {
            for (Object obj : world.loadedEntityList) {
                if (obj instanceof EntitySpider) {
                    EntitySpider spider = (EntitySpider) obj;

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
    }
}
