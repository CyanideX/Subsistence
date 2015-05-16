package subsistence.common.item.prefab;

import subsistence.Subsistence;
import subsistence.common.core.SubsistenceCreativeTab;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

/**
 * @author dmillerw
 */
public class SubsistenceItem extends Item {

    private IIcon icon;

    public SubsistenceItem(SubsistenceCreativeTab tab) {
        super();

        setCreativeTab(tab.get());
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        return icon;
    }

    @Override
    public void registerIcons(IIconRegister register) {
        if (!getIcon().isEmpty()) {
            icon = register.registerIcon(Subsistence.RESOURCE_PREFIX + getIcon());
        }
    }

    public String getIcon() {
        return "";
    }
}
