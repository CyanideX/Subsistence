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

    private final boolean ignoreNBT;
    public final boolean damageTool;

    public TableRecipe(ItemStack input, ItemStack output, ToolDefinition tool, float speed, boolean ignoreNBT, boolean damageTool) {
        this.input = input;
        this.output = output;
        this.tool = tool;
        this.speed = speed;
        this.ignoreNBT = ignoreNBT;
        this.damageTool = damageTool;
    }

    public boolean isInput(ItemStack stack, ItemStack tool) {
        return isInputStack(stack) && isTool(tool);
    }

    public boolean isInput(ItemStack stack, ToolDefinition tool) {
        return isInputStack(stack) && tool == this.tool;
    }

    private boolean isInputStack(ItemStack stack) {
        return StackHelper.areStacksSimilar(stack, input, ignoreNBT);
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
}