package subsistence.common.block.machine;

import net.minecraftforge.fluids.FluidContainerRegistry;
import subsistence.client.lib.Model;
import subsistence.client.lib.Texture;

public enum BarrelType {
    WOOD(2, 2, Texture.BARREL_WOOD, Model.BARREL_WOOD),
    STONE(2, 8, Texture.BARREL_STONE, Model.BARREL_STONE),
    NETHER(2, 8, Texture.BARREL_NETHER, Model.BARREL_STONE);

    public final int itemCapacity, fluidCapacity;
    public final Texture texture;
    public final Model model;

    private BarrelType(int itemCapacity, int fluidCapacity, Texture texture, Model model) {
        this.itemCapacity = itemCapacity;
        this.fluidCapacity = fluidCapacity * FluidContainerRegistry.BUCKET_VOLUME;
        this.texture = texture;
        this.model = model;
    }

    public String toString() {
        return name().toLowerCase();
    }
}
