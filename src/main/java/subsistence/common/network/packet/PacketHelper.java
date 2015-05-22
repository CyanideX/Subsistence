package subsistence.common.network.packet;

import cpw.mods.fml.server.FMLServerHandler;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketHelper {

    public static void playSound(String sound, TileEntity tileEntity, float volume, float pitch) {
        playSound(sound, tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, volume, pitch);
    }

    public static void playSound(String sound, World world, double x, double y, double z, float volume, float pitch) {
        FMLServerHandler.instance().getServer().getConfigurationManager().sendToAllNear(x, y, z, volume > 1.0F ? (double) (16.0F * volume) : 16.0D, world.provider.dimensionId, new S29PacketSoundEffect(sound, x, y, z, volume, pitch));
    }
}
