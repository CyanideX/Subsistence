package oldStuff.common.tile.machine;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import oldStuff.common.config.CoreSettings;
import oldStuff.common.network.nbt.NBTHandler;
import oldStuff.common.tile.core.TileCoreMachine;

public class TileHandCrank extends TileCoreMachine {

    @NBTHandler.Sync(false)
    public boolean reverse = false;
    @NBTHandler.Sync(true)
    public float spin = 0F;

    @Override
    public void updateEntity() {
        if (spin > 0) {
            spin -= 20F;
        }

        if (worldObj.isRemote && isValid()) {
            TileHammerMill tile = (TileHammerMill) worldObj.getTileEntity(xCoord + orientation.offsetX, yCoord + orientation.offsetY, zCoord + orientation.offsetZ);

            if (tile != null) {
                tile.angle = -spin;
            }
        }
    }

    public void crank() {
        if (spin <= 0 && isValid()) {
            TileHammerMill tile = (TileHammerMill) worldObj.getTileEntity(xCoord + orientation.offsetX, yCoord + orientation.offsetY, zCoord + orientation.offsetZ);
            TileEntity beyond = worldObj.getTileEntity(tile.xCoord + orientation.offsetX, tile.yCoord + orientation.offsetY, tile.zCoord + orientation.offsetZ);

            if (beyond == null || !(beyond instanceof TileHandCrank) && !(beyond instanceof TileKineticCrank) && !(beyond instanceof TileMetalShaft)) {
                tile.charge += CoreSettings.handCrank;

                spin = 360F;

                markForUpdate();
            }
        }
    }
    
    private boolean isValid() {
        return orientation != ForgeDirection.UNKNOWN;
    }
}
