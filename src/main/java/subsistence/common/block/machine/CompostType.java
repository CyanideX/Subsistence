package subsistence.common.block.machine;

import subsistence.client.lib.Texture;

public enum CompostType {

    WOOD(8, Texture.COMPOST_WOOD),
    STONE(24, Texture.COMPOST_STONE),
    NETHER(24, Texture.COMPOST_NETHER);

    public final int capacity;
    public final Texture texture;

    private CompostType(int capacity, Texture texture) {
        this.capacity = capacity;
        this.texture = texture;
    }

    public String toString() {
        return name().toLowerCase();
    }
}
