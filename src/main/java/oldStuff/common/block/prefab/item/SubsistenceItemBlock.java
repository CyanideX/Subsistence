package oldStuff.common.block.prefab.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class SubsistenceItemBlock extends ItemBlock {

    public SubsistenceItemBlock(Block block) {
        super(block);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }
}
