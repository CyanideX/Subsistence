package subsistence.items;

import subsistence.Subsistence;
import subsistence.items.prefab.ItemMetaBase;

import java.util.List;

public class ItemCosmetic extends ItemMetaBase {
    public static final String HEART_CORRUPTION = "heart_corruption";
    public static final String HEART_RIGHTEOUSNESS = "heart_righteousness";

    public ItemCosmetic() {
        super("item_cosmetic", Subsistence.creativeTab);
    }

    @Override
    protected void initItemNames(List<String> subItemNamesList) {
        subItemNamesList.add(ItemCosmetic.HEART_CORRUPTION);
        subItemNamesList.add(ItemCosmetic.HEART_RIGHTEOUSNESS);
    }
}
