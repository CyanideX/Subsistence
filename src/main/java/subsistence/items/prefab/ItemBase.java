package subsistence.items.prefab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import subsistence.util.Data;
import subsistence.util.IHasModel;

public abstract class ItemBase extends Item implements IHasModel {
    public ItemBase(String name, CreativeTabs creativeTab) {

        setRegistryName(name);
        setUnlocalizedName(name);

        setCreativeTab(creativeTab);

        Data.ITEMS.add(this);
    }
}
