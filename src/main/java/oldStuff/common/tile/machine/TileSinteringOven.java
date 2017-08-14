package oldStuff.common.tile.machine;

import oldStuff.common.network.nbt.NBTHandler;
import oldStuff.common.tile.core.TileCoreMachine;

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
