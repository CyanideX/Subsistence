package com.subsistence.common.item.resource;

import com.subsistence.common.core.SubsistenceCreativeTab;
import com.subsistence.common.item.prefab.SubsistenceItem;
import com.subsistence.common.lib.SubsistenceProps;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import java.util.List;

/**
 * @author dmillerw
 */
public class ItemResource extends SubsistenceItem {

    public static final String[] NAMES = new String[]{"copper", "gold", "graphite", "iron", "lead", "nickel", "silver", "steel", "tin"};

    protected IIcon[] icons;

    private String type;

    public ItemResource setType(String type) {
        setUnlocalizedName("resource." + type.toLowerCase());
        this.type = type;
        return this;
    }

    public ItemResource() {
        super(SubsistenceCreativeTab.ITEMS);

        setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return String.format(super.getItemStackDisplayName(stack), StatCollector.translateToLocal("ore." + NAMES[stack.getItemDamage()]));
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        return icons[damage];
    }

    @Override
    public void registerIcons(IIconRegister register) {
        icons = new IIcon[NAMES.length];
        if (!getIconPrefix().isEmpty()) {
            for (int i = 0; i < NAMES.length; i++) {
                icons[i] = register.registerIcon(SubsistenceProps.RESOURCE_PREFIX + getIconPrefix() + "/" + NAMES[i] + type);
            }
        }
    }

    @Override
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        for (int meta = 0; meta < NAMES.length; ++meta) {
            if (type.equalsIgnoreCase("ingot") && (meta == 1 || meta == 3)) {
                return;
            }
            list.add(new ItemStack(item, 1, meta));
        }
    }

    public String getIconPrefix() {
        return "resource";
    }
}
