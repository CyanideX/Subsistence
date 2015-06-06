package subsistence.common.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import subsistence.common.core.SubsistenceCreativeTab;
import subsistence.common.item.prefab.SubsistenceMultiItem;
import subsistence.common.lib.MachineType;
import subsistence.common.util.ArrayHelper;

public class ItemBarrelLid extends SubsistenceMultiItem {

    public ItemBarrelLid() {
        super(SubsistenceCreativeTab.ITEMS);

        setMaxStackSize(1);
    }

    @Override
    public String[] getNames() {
        return ArrayHelper.allToString(MachineType.CompostType.values());
    }

    @Override
    public String getIconPrefix() {
        return "";
    }

    @Override
    public void registerIcons(IIconRegister register) {

    }
}
