package com.subsistence.common.item;

import com.subsistence.common.core.SubsistenceCreativeTab;
import com.subsistence.common.item.prefab.SubsistenceMultiItem;

/**
 * @author dmillerw
 */
public class ItemComponent extends SubsistenceMultiItem {

    private static final String[] NAMES = {"twine", "twine_mesh"};

    public ItemComponent() {
        super(SubsistenceCreativeTab.ITEMS);

        setHasSubtypes(true);
    }

    @Override
    public String[] getNames() {
        return NAMES;
    }

    @Override
    public String getIconPrefix() {
        return "component";
    }
}
