package oldStuff.common.item;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import oldStuff.common.core.SubsistenceCreativeTab;
import oldStuff.common.item.prefab.SubsistenceMultiItem;

public class ItemNet extends SubsistenceMultiItem {

    public ItemNet() {
        super(SubsistenceCreativeTab.TOOLS);

        setMaxDamage(0);
        setMaxStackSize(1);
        setHasSubtypes(true);
    }

    @Override
    public float getDigSpeed(ItemStack itemstack, Block block, int metadata) {
        return block == Blocks.web ? 25F : super.getDigSpeed(itemstack, block, metadata);
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack stack) {
        return block == Blocks.web;
    }

    @Override
    public String[] getNames() {
        return new String[]{"normal", "flies"};
    }

    @Override
    public String getIconPrefix() {
        return "tools/net";
    }
}
