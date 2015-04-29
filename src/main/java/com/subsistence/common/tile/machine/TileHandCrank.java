package com.subsistence.common.tile.machine;

import com.subsistence.common.network.nbt.NBTHandler;
import com.subsistence.common.recipe.manager.GeneralManager;
import com.subsistence.common.tile.core.TileCoreMachine;
import net.minecraft.tileentity.TileEntity;

/**
 * @author dmillerw
 */
public class TileHandCrank extends TileCoreMachine {

    @NBTHandler.NBTData
    public boolean reverse = false;
    @NBTHandler.NBTData
    @NBTHandler.DescriptionData
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
                tile.charge += GeneralManager.handCrank;

                spin = 360F;

                markForUpdate();
            }
        }
    }
}
