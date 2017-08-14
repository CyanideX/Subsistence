package oldStuff.common.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import oldStuff.common.block.SubsistenceBlocks;
import oldStuff.common.item.SubsistenceItems;

import java.util.Locale;

public enum SubsistenceCreativeTab {

    BLOCKS,
    ITEMS,
    TOOLS;

    private final CreativeTabs tab;

    SubsistenceCreativeTab() {
        tab = new Tab();
    }

    public CreativeTabs get() {
        return tab;
    }

    private String getLabel() {
        return "subsistence." + name().toLowerCase(Locale.ENGLISH);
    }

    private ItemStack getItem() {
        switch (this) {
            case BLOCKS:
                return new ItemStack(SubsistenceBlocks.table, 1, 0);
            case ITEMS:
                return new ItemStack(SubsistenceItems.resourcePebble, 1, 0);
            case TOOLS:
                return new ItemStack(SubsistenceItems.hammerWood, 1, 0);
            default:
                return null;
        }
    }

    public final class Tab extends CreativeTabs {

        private Tab() {
            super(getLabel());
        }

        @Override
        public ItemStack getIconItemStack() {
            return getItem();
        }

        @Override
        public Item getTabIconItem() {
            return getItem().getItem();
        }
    }
}
