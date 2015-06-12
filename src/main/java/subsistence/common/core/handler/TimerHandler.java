package subsistence.common.core.handler;

import com.google.common.collect.Maps;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import subsistence.common.network.packet.PacketUpdateTimer;

import java.util.Iterator;
import java.util.Map;

/**
 * @author dmillerw
 */
public class TimerHandler {

    public static class Timer {

        public String tag;
        public int duration;

        public int ticks = 0;

        public Callback callback;

        public Timer(String tag, int duration, Callback callback) {
            this.tag = tag;
            this.duration = duration;
            this.callback = callback;
        }

        public boolean tick() {
            ticks++;
            return ticks >= duration;
        }

        @Override
        public String toString() {
            return tag + " : " + duration + " : " + ticks;
        }
    }

    public static abstract class Callback {

        public Object[] args;

        public Callback(Object ... args) {
            this.args = args;
        }

        public abstract void callback(EntityPlayer entityPlayer);
    }

    public static final TimerHandler INSTANCE = new TimerHandler();

    public Map<String, Timer> activeTimers = Maps.newHashMap();

    public boolean startTimer(EntityPlayer entityPlayer, Timer timer) {
        if (activeTimers.containsKey(entityPlayer.getCommandSenderName())) {
            return false;
        }
        activeTimers.put(entityPlayer.getCommandSenderName(), timer);
        PacketUpdateTimer.startTimer(entityPlayer, timer.tag, timer.duration);
        return true;
    }

    public void stopTimer(EntityPlayer entityPlayer) {
        activeTimers.remove(entityPlayer.getCommandSenderName());
        PacketUpdateTimer.stopTimer(entityPlayer);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;

        Iterator<Map.Entry<String, Timer>> iterator = activeTimers.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<String, Timer> entry = iterator.next();
            String player = entry.getKey();
            Timer timer = entry.getValue();

            if (timer.tick()) {
                iterator.remove();
                EntityPlayer entityPlayer = MinecraftServer.getServer().getConfigurationManager().func_152612_a(player);

                PacketUpdateTimer.stopTimer(entityPlayer);
                timer.callback.callback(entityPlayer);
            }
        }
    }
}
