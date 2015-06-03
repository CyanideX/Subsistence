package subsistence.common.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.util.IIcon;
import subsistence.Subsistence;
import subsistence.common.core.SubsistenceCreativeTab;

/**
 * @author Thlayli
 */
public class ItemBoilingBucket extends ItemBucket {

    public ItemBoilingBucket(Block block) {
        super(block);
        setCreativeTab(SubsistenceCreativeTab.ITEMS.get());
    }

    @Override
    public void registerIcons(IIconRegister register) {
        itemIcon = register.registerIcon(Subsistence.RESOURCE_PREFIX + "bucketBoilingWater");
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        return itemIcon;
    }
    
    @Override
    public Item setUnlocalizedName(String p_77655_1_) {
        return super.setUnlocalizedName("subsistence." + p_77655_1_);
    }
}
