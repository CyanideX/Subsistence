package subsistence.client.lib;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import subsistence.Subsistence;

public enum Texture {

    HAND_CRANK("blocks/handCrank"),
    HAMMER_MILL("blocks/hammerMill"),
    KILN("blocks/kiln"),
    KINETIC_CRANK("blocks/kineticCrank"),
    METAL_PRESS("blocks/metalPress"),
    TABLE_SIEVE("blocks/sieveTable"),
    SINTERING_OVEN("blocks/sinteringOven"),
    SPAWN_MARKER("blocks/skull"),
    TABLE_WOOD("blocks/basicTable"),
    TABLE_STONE("blocks/smashingTable"),
    WATER_MILL("blocks/mill"),
    METAL_SHAFT("blocks/shaft"),
    BARREL_WOOD("blocks/barrelWood"),
    BARREL_STONE("blocks/barrelStone"),
    COMPOST_WOOD("blocks/compostWood"),
    COMPOST_STONE("blocks/compostStone");

    protected ResourceLocation path;

    Texture(String path) {
        this.path = new ResourceLocation(Subsistence.RESOURCE_PREFIX + "textures/models/" + path + ".png");
    }

    public void bindTexture() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(path);
    }
}
