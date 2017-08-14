package oldStuff.common.item;

import oldStuff.common.core.SubsistenceCreativeTab;
import oldStuff.common.item.prefab.SubsistenceItem;

public class ItemHammer extends SubsistenceItem {

    public static final float[] STRENGTH = new float[]{1F, 2F, 2.5F, 5F, 10F};

    private final String type;

    private final Object component;

    public ItemHammer(int damage, String type, Object component) {
        super(SubsistenceCreativeTab.TOOLS);

        setMaxStackSize(1);
        setMaxDamage(damage);
        setUnlocalizedName("hammer_" + type);
        setFull3D();

        this.type = type;
        this.component = component;
    }

    @Override
    public String getIcon() {
        return "tools/" + type + "Hammer";
    }
}
