package oldStuff.common.item;

import oldStuff.common.core.SubsistenceCreativeTab;
import oldStuff.common.item.prefab.SubsistenceMultiItem;

public class ItemComponent extends SubsistenceMultiItem {

    private static final String[] NAMES = {"twine", "twine_mesh", "wormwoodLeaves", "sap"};

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
