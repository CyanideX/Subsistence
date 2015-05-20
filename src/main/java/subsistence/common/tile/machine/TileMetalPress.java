package subsistence.common.tile.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import subsistence.common.network.nbt.NBTHandler;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.recipe.wrapper.MetalPressRecipe;
import subsistence.common.tile.core.TileCoreMachine;

/**
 * @author dmillerw
 */
public class TileMetalPress extends TileCoreMachine {

    public static final float ANGLE_MIN = 0.6f;
    public static final float ANGLE_MAX = 1f;

    public static final int PAUSE_MAX = 40;

    @NBTHandler.Sync(true)
    public ItemStack itemStack;
    @NBTHandler.Sync(false)
    public int amount;
    @NBTHandler.Sync(true)
    public int pauseCount;
    @NBTHandler.Sync(true)
    public boolean state = false; // False = reverting, true = pressing

    @Override
    public void updateEntity() {
        if (itemStack == null) {
            amount = 0;
            pauseCount = 0;
            state = false;
        } else {
            if (state) {
                if (pauseCount < PAUSE_MAX) {
                    pauseCount++;
                } else {
                    state = false;
                }
            } else {
                if (pauseCount > 0)
                    pauseCount--;
            }
        }
    }

    public void activate(EntityPlayer entityPlayer) {
        if (itemStack == null)
            return;

        MetalPressRecipe recipe = SubsistenceRecipes.METAL_PRESS.get(itemStack);
        if (recipe != null && !state && pauseCount == 0) {
            amount++;
            state = true;

            // We send an update every time we activate and the render should update
            markForUpdate();

            //TODO: remove and replace with animations @dmillerw
            entityPlayer.addChatMessage(new ChatComponentText("*clang*"));

            if (amount >= recipe.getAmount()) {
                itemStack = recipe.getOutputItem().copy();
                markForUpdate();

                //TODO: remove and replace with animations @dmillerw
                entityPlayer.addChatMessage(new ChatComponentText("Your hard work has earned you a " + itemStack.getDisplayName()));
            }
        }
    }
}
