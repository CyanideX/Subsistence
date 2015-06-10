package subsistence.common.lib;

import net.minecraftforge.fluids.FluidContainerRegistry;

/**
 * @author dmillerw
 */
public class MachineType {

    public static enum BarrelType {
        WOOD(2, 2),
        STONE(2, 8),
        NETHER(2, 8);

        public final int itemCapacity, fluidCapacity;

        private BarrelType(int itemCapacity, int fluidCapacity) {
            this.itemCapacity = itemCapacity;
            this.fluidCapacity = fluidCapacity * FluidContainerRegistry.BUCKET_VOLUME;
        }

        public String toString() {
            return name().toLowerCase();
        }
    }

    public static enum CompostType {

        WOOD(8, "wood"),
        STONE(24, "stone"),
        NETHER(24, "stone");

        public final int capacity;
        public final String recipeType;

        private CompostType(int capacity, String recipeType) {
            this.capacity = capacity;
            this.recipeType = recipeType;
        }

        public String toString() {
            return name().toLowerCase();
        }
    }
}
