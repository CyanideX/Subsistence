package subsistence.common.item;

import subsistence.common.core.SubsistenceCreativeTab;
import subsistence.common.item.prefab.SubsistenceMultiItem;

/**
 * @author dmillerw
 */
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
