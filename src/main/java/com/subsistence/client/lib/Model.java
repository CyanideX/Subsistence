package com.subsistence.client.lib;

import com.subsistence.common.lib.SubsistenceProps;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

/**
 * @author dmillerw
 */
public enum Model {

    BARREL_STONE("blocks/barrelStone.tcn"),
    BARREL_WOOD("blocks/barrelWood.tcn"),
    HAND_CRANK("blocks/handCrank.obj"),
    HAMMER_MILL("blocks/hammerMill.obj"),
    KILN("blocks/kiln.obj"),
    KINETIC_CRANK("blocks/kineticCrank.obj"),
    METAL_PRESS("blocks/metalPress.obj"),
    TABLE_SIEVE("blocks/sieveTable.obj"),
    SINTERING_OVEN("blocks/sinteringOven.obj"),
    SPAWN_MARKER("blocks/skull.tcn"),
    TABLE_WOOD("blocks/basicTable.obj"),
    TABLE_STONE("blocks/smashingTable.obj"),
    WATER_MILL("blocks/mill.obj"),
    METAL_SHAFT("blocks/shaft.tcn"),
    COMPOST("blocks/compost.tcn");

    private IModelCustom model;

    Model(String path) {
        this.model = AdvancedModelLoader.loadModel(new ResourceLocation(SubsistenceProps.RESOURCE_PREFIX + "models/" + path));
    }

    public void renderAll() {
        model.renderAll();
    }

    public void renderOnly(String... groupNames) {
        model.renderOnly(groupNames);
    }

    public void renderPart(String partName) {
        model.renderPart(partName);
    }

    public void renderAllExcept(String... excludedGroupNames) {
        model.renderAllExcept(excludedGroupNames);
    }
}
