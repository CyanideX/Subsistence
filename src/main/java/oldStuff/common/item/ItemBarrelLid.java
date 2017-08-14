package oldStuff.common.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import oldStuff.common.core.SubsistenceCreativeTab;
import oldStuff.common.item.prefab.SubsistenceMultiItem;
import oldStuff.common.lib.MachineType;
import oldStuff.common.util.ArrayHelper;

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
