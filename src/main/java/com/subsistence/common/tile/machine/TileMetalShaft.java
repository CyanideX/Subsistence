package com.subsistence.common.tile.machine;

import com.subsistence.common.tile.core.TileCoreMachine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author dmillerw
 */
public class TileMetalShaft extends TileCoreMachine {

    @SideOnly(Side.CLIENT)
    public float angle = 0F;
}
