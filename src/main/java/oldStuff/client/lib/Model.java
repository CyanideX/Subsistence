package oldStuff.client.lib;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;
import oldStuff.SubsistenceOld;
import oldStuff.common.util.RenderHelper;

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

    private final IModelCustom model;

    private Model(String path) {
        this.model = RenderHelper.loadSubsistenceModel(new ResourceLocation(SubsistenceOld.RESOURCE_PREFIX + "models/" + path));
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
