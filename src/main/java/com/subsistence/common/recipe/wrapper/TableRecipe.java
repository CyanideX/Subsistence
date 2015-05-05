package com.subsistence.common.recipe.wrapper;

import com.subsistence.common.lib.tool.ToolDefinition;
import com.subsistence.common.util.StackHelper;
import net.minecraft.item.ItemStack;

/**
 * @author dmillerw
 */
public class TableRecipe {

    private final ItemStack input;
    private final ItemStack output;

    private final ToolDefinition tool;
    private final float speed;

    private final boolean table;
    private final boolean hammerMill;

    private final boolean ignoreNBT;
    public final boolean damageTool;


    public TableRecipe(ItemStack input, ItemStack output, ToolDefinition tool, float speed, boolean table, boolean hammerMill, boolean ignoreNBT, boolean damageTool) {
        this.input = input;
        this.output = output;
        this.tool = tool;
        this.speed = speed;
        this.ignoreNBT = ignoreNBT;
        this.damageTool = damageTool;
        this.table = table;
        this.hammerMill = hammerMill;
    }

    public boolean isInput(ItemStack stack, ItemStack tool, boolean isTable) {

        return StackHelper.areStacksSimilar(stack, input, ignoreNBT) && isTool(tool) && ((isTable && this.table) || (!isTable && this.hammerMill));
    }

    public boolean isInput(ItemStack stack, ToolDefinition tool, boolean isTable) {
        return StackHelper.areStacksSimilar(stack, input, ignoreNBT) && tool == this.tool && ((isTable && this.table) || (!isTable && this.hammerMill));
    }

    private boolean isTool(ItemStack stack) {
        return ToolDefinition.isType(stack, tool);
    }

    public ItemStack getOutput(boolean equivalentSize) {
        ItemStack out = output.copy();
        if (equivalentSize) {
            out.stackSize = input.stackSize;
        }
        return out;
    }

    public float getSpeed() {
        return speed;
    }

    public ItemStack getInputItem() {
        return input;
    }

    public boolean isTable() {
        return table;
    }
}