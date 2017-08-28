package subsistence;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import subsistence.items.ItemCosmetic;

public class SubsistenceCreativeTab extends CreativeTabs {
    public SubsistenceCreativeTab() {
        super("subsistence");
    }

    @Override
    public ItemStack getTabIconItem() {
        return ModItems.COSMETIC.createItemStack(ItemCosmetic.HEART_CORRUPTION, 1);
    }
}
