package subsistence.common.block.machine;

import subsistence.client.lib.Texture;

public enum TableType {
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
