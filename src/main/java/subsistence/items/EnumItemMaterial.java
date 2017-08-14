package subsistence.items;

import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

public enum EnumItemMaterial {
    INVALID_ITEM(Short.MAX_VALUE - 1, "invalid_item", 64),
    ATM_STAR(0, "atm_star", 64);


    private static Int2ObjectMap<EnumItemMaterial> metaMap = new Int2ObjectAVLTreeMap<>();

    static {
        for (EnumItemMaterial enumItemMaterial : EnumItemMaterial.values()) {
            metaMap.put(enumItemMaterial.meta, enumItemMaterial);
        }
    }

    private int meta;
    private String name;
    private int stackSize;


    EnumItemMaterial(int meta, String name, int stackSize) {
        this.meta = meta;
        this.name = name;
        this.stackSize = stackSize;
    }

    public static EnumItemMaterial getFromMeta(int meta) {
        EnumItemMaterial material = metaMap.get(meta);
        return material != null ? material : INVALID_ITEM;
    }

    public int getMeta() {
        return meta;
    }

    public String getName() {
        return name;
    }

    public int getStackSize() {
        return stackSize;
    }
}
