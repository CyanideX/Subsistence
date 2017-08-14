package oldStuff.common.item;

import oldStuff.common.core.SubsistenceCreativeTab;
import oldStuff.common.item.prefab.SubsistenceMultiItem;

public class ItemCosmetic extends SubsistenceMultiItem {

    public static final String[] NAMES = new String[]{"corruption", "righteousness"};

    public ItemCosmetic() {
        super(SubsistenceCreativeTab.ITEMS);

        setHasSubtypes(true);
        setMaxStackSize(1);
        setMaxDamage(0);
    }

    @Override
    public String[] getNames() {
        return NAMES;
    }

    @Override
    public String getIconPrefix() {
        return "heart";
    }
}
