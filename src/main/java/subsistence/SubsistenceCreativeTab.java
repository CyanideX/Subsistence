package subsistence;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class SubsistenceCreativeTab extends CreativeTabs {
    public SubsistenceCreativeTab() {
        super("subsistence");
    }

    @Override
    public ItemStack getTabIconItem() {
        // return ItemMaterial.createItemStack(EnumItemMaterial.ATM_STAR, 1);
        return ItemStack.EMPTY;
    }
}
