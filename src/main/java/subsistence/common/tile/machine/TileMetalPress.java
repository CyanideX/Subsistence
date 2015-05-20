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

    public static final float MIN = 0.6f;
    public static final float MAX = 1f;

    @NBTHandler.Sync(true)
    public ItemStack itemStack;
    @NBTHandler.Sync(false)
    public int amount;
    @NBTHandler.Sync(false)
    public int pauseCount;

    @Override
    public void updateEntity() {
        //if animations were to go here, they would spam. put them in activate
        if (!worldObj.isRemote) {
            if (itemStack == null) {
                amount = 0;
                pauseCount = 0;
            } else if (pauseCount < 40) {
                pauseCount++;
            }
        }
    }

    public void activate(EntityPlayer entityPlayer) {
        if (itemStack == null)
            return;

        MetalPressRecipe recipe = SubsistenceRecipes.METAL_PRESS.get(itemStack);
        if (recipe != null && pauseCount >= 40) {
            amount++;
            pauseCount = 0;

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
