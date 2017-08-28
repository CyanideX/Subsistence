package subsistence.items.testNew.prefab;

import javax.annotation.Nullable;

public interface IItemData {
    /**
     * @return Returns the Item Subname of the ENUM entry
     */
    String getItemName();

    /**
     * @return Gets META of the entry
     */
    int getMeta();

    /**
     * Gives a ITEM_USE_ACTION if it has one
     * this happens when the given object is right clicked;
     */
    @Nullable
    IItemUseAction getItemUseAction();

    // IItemData[] values();
}
