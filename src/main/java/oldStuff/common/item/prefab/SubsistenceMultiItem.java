package oldStuff.common.item.prefab;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import oldStuff.SubsistenceOld;
import oldStuff.common.core.SubsistenceCreativeTab;

import java.util.List;

public abstract class SubsistenceMultiItem extends SubsistenceItem {

    protected IIcon[] icons;

    public SubsistenceMultiItem(SubsistenceCreativeTab tab) {
        super(tab);

        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        int meta = itemStack.getItemDamage();

        if (meta < 0 || meta >= getNames().length) {
            meta = 0;
        }
        return super.getUnlocalizedName() + "." + getNames()[meta];
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        if (damage < 0 || damage >= getNames().length) {
            damage = 0;
        }

        return icons[damage];
    }

    @Override
    public void registerIcons(IIconRegister register) {
        icons = new IIcon[getNames().length];
        if (!getIconPrefix().isEmpty()) {
            for (int i = 0; i < getNames().length; i++) {
                icons[i] = register.registerIcon(SubsistenceOld.RESOURCE_PREFIX + getIconPrefix() + "/" + getNames()[i]);
            }
        }
    }

    @Override
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        for (int meta = 0; meta < getNames().length; ++meta) {
            list.add(new ItemStack(item, 1, meta));
        }
    }

    public abstract String[] getNames();

    public abstract String getIconPrefix();
}
