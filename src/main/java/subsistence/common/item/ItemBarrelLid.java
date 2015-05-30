package subsistence.common.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import subsistence.common.core.SubsistenceCreativeTab;
import subsistence.common.item.prefab.SubsistenceMultiItem;

public class ItemBarrelLid extends SubsistenceMultiItem {

    private static final String[] NAMES = new String[]{"wood", "stone", "nether"};

    public ItemBarrelLid() {
        super(SubsistenceCreativeTab.ITEMS);

        setMaxStackSize(1);
    }

    @Override
    public String[] getNames() {
        return NAMES;
    }

    @Override
    public String getIconPrefix() {
        return "";
    }

    @Override
    public void registerIcons(IIconRegister register) {

    }
}
