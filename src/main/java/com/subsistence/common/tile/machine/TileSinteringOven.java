package com.subsistence.common.tile.machine;

import com.subsistence.common.tile.core.TileCoreMachine;
import com.subsistence.common.network.nbt.NBTHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Royalixor
 */
public class TileSinteringOven extends TileCoreMachine {

    public float currentAngle = 0.0F;

    @NBTHandler.NBTData
    @NBTHandler.DescriptionData
    public boolean open = false;

    @Override
    public void onPoked() {
        open = !open;
    }

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
