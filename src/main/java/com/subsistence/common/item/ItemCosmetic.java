package com.subsistence.common.item;

import com.subsistence.common.core.SubsistenceCreativeTab;
import com.subsistence.common.item.prefab.SubsistenceMultiItem;

/**
 * @author dmillerw
 */
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
