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

    public static final float RENDER_MIN = 0.6f;

    public static final int ANIMATE_TICK_MAX = 40;
    public static final int PAUSE_TICK_MAX = 10;

    @NBTHandler.Sync(true)
    public ItemStack itemStack;
    @NBTHandler.Sync(false)
    public int amount;
    @NBTHandler.Sync(true)
    public int animationTicker;
    @NBTHandler.Sync(true)
    public int pauseTicker;
    @NBTHandler.Sync(true)
    public boolean state = false; // False = reverting, true = pressing

    @Override
    public void updateEntity() {
        if (itemStack == null) {
            amount = 0;
            pauseTicker = 0;
            animationTicker = 0;
            state = false;
        } else {
            if (state) {
                if (animationTicker < ANIMATE_TICK_MAX) {
                    animationTicker++;
                } else {
                    if (pauseTicker >= PAUSE_TICK_MAX)
                        state = false;
                    else
                        pauseTicker++;
                }
            } else {
                if (animationTicker > 0)
                    animationTicker--;

                if (pauseTicker > 0)
                    pauseTicker = 0;
            }
        }
    }

    public void activate(EntityPlayer entityPlayer) {
        if (itemStack == null)
            return;

        MetalPressRecipe recipe = SubsistenceRecipes.METAL_PRESS.get(itemStack);
        if (recipe != null && !state && animationTicker == 0) {
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
