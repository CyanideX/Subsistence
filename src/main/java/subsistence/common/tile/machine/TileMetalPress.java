package subsistence.common.tile.machine;

import net.minecraft.item.ItemStack;
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

    @Override
    public void updateEntity() {
        if (worldObj.isRemote) {
            //TODO fix animations
//            updateLid();
        } else {
            if (itemStack == null)
                amount = 0;
        }
    }

    public void activate() {
        if (itemStack == null)
            return;

        amount++;

        MetalPressRecipe recipe = SubsistenceRecipes.METAL_PRESS.get(itemStack);
        if (recipe != null) {
            if (amount >= recipe.getAmount()) {
                itemStack = recipe.getOutputItem();
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
        }
    }
}
