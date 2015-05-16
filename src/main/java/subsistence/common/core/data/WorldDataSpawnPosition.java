package subsistence.common.core.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldSavedData;

/**
 * @author dmillerw
 */
public class WorldDataSpawnPosition extends WorldSavedData {

    public ChunkCoordinates spawn;

    public WorldDataSpawnPosition() {
        super("subsistence:spawn_position");
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("spawn")) {
            NBTTagCompound tag = nbt.getCompoundTag("spawn");
            spawn = new ChunkCoordinates(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        if (spawn != null) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("x", spawn.posX);
            tag.setInteger("y", spawn.posY);
            tag.setInteger("z", spawn.posZ);
            nbt.setTag("spawn", tag);
        }
    }
}
