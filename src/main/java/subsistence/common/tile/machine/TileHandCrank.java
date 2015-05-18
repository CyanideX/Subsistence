package subsistence.common.tile.machine;

import net.minecraft.tileentity.TileEntity;
import subsistence.common.config.MainSettingsStatic;
import subsistence.common.network.nbt.NBTHandler;
import subsistence.common.tile.core.TileCoreMachine;

/**
 * @author dmillerw
 */
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

        if (worldObj.isRemote) {
            TileHammerMill tile = (TileHammerMill) worldObj.getTileEntity(xCoord + orientation.offsetX, yCoord + orientation.offsetY, zCoord + orientation.offsetZ);

            if (tile != null) {
                tile.angle = -spin;
            }
        }
    }

    public void crank() {
        if (spin <= 0) {
            TileHammerMill tile = (TileHammerMill) worldObj.getTileEntity(xCoord + orientation.offsetX, yCoord + orientation.offsetY, zCoord + orientation.offsetZ);
            TileEntity beyond = worldObj.getTileEntity(tile.xCoord + orientation.offsetX, tile.yCoord + orientation.offsetY, tile.zCoord + orientation.offsetZ);

            if (beyond == null || !(beyond instanceof TileHandCrank) && !(beyond instanceof TileKineticCrank) && !(beyond instanceof TileMetalShaft)) {
                tile.charge += MainSettingsStatic.handCrank;

                spin = 360F;

                markForUpdate();
            }
        }
    }
}
