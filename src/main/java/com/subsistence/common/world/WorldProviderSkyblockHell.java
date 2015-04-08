package com.subsistence.common.world;

import com.subsistence.common.block.core.BlockSpawnMarker;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldProviderHell;

/**
 * @author dmillerw
 */
public class WorldProviderSkyblockHell extends WorldProviderHell {

    @Override
    public ChunkCoordinates getSpawnPoint() {
        ChunkCoordinates spawn = BlockSpawnMarker.getSpawnPosition(this.worldObj);
        return spawn != null ? spawn : super.getSpawnPoint();
    }

    @Override
    public boolean canRespawnHere() {
        return true;
    }
}
