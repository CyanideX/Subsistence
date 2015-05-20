package subsistence.common.tile.machine;

import subsistence.common.tile.core.TileCoreMachine;
import subsistence.common.network.nbt.NBTHandler;

/**
 * @author Royalixor
 */
public class TileSinteringOven extends TileCoreMachine {

    public float currentAngle = 0.0F;

    @NBTHandler.Sync(true)
    public boolean open = false;

    @Override
    public void updateEntity() {
        if (worldObj.isRemote) {
            currentAngle += (open ? 7.5F : -7.5F);

            if (currentAngle <= 0F) {
                currentAngle = 0F;
            } else if (currentAngle >= 90F) {
                currentAngle = 90F;
            }
        }
    }
}
