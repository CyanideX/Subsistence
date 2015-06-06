package subsistence.common.lib;

import net.minecraftforge.fluids.FluidContainerRegistry;
import subsistence.client.lib.Model;
import subsistence.client.lib.Texture;

/**
 * @author dmillerw
 */
public class MachineType {

    public static enum BarrelType {
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

    public static enum CompostType {

        WOOD(8, "wood", Texture.COMPOST_WOOD),
        STONE(24, "stone", Texture.COMPOST_STONE),
        NETHER(24, "stone", Texture.COMPOST_NETHER);

        public final int capacity;
        public final String recipeType;
        public final Texture texture;

        private CompostType(int capacity, String recipeType, Texture texture) {
            this.capacity = capacity;
            this.recipeType = recipeType;
            this.texture = texture;
        }

        public String toString() {
            return name().toLowerCase();
        }
    }

    public static enum TableType {
        WOOD(Texture.TABLE_WOOD),
        STONE(Texture.TABLE_COBBLESTONE),
        NETHER(Texture.TABLE_NETHER);

        public final Texture texture;

        private TableType(Texture texture) {
            this.texture = texture;
        }

        public String toString() {
            return name().toLowerCase();
        }

        public boolean isWood() {
            return this == WOOD;
        }
    }
}
