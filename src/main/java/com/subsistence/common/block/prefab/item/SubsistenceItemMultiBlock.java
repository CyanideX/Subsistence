package com.subsistence.common.block.prefab.item;

import com.subsistence.common.block.prefab.SubsistenceMultiBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * @author dmillerw
 */
public class SubsistenceItemMultiBlock extends ItemBlock {

    public SubsistenceItemMultiBlock(Block block) {
        super(block);

        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "." + ((SubsistenceMultiBlock) this.field_150939_a).getNameForType(stack.getItemDamage());
    }
}
