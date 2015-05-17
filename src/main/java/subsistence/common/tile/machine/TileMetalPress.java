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

    public final float min = 0.6f;
    public final float max = 1f;
    public float currentAngle = 0f;

    @NBTHandler.NBTData
    @NBTHandler.DescriptionData
    public ItemStack itemStack;

    @NBTHandler.NBTData
    @NBTHandler.DescriptionData
    public int amount;

    @NBTHandler.NBTData
    @NBTHandler.DescriptionData
    public boolean closed = false;

    public int switchClosed;

    @Override
    public void onPoked() {
        if (!closed && currentAngle == max) {
            amount++;
            closed = true;
        }
    }


    @Override
    public void updateEntity() {

        if (!worldObj.isRemote) {
            updateLid();
            if (itemStack != null) {
                MetalPressRecipe recipe = SubsistenceRecipes.METAL_PRESS.get(itemStack);
                if (recipe != null) {
                    if (amount >= recipe.getAmount() && currentAngle == min) {
                        itemStack = recipe.getOutputItem();
                        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    }
                }
            }
        }
    }

    private void updateLid() {
        if (closed && currentAngle == min) {
            switchClosed++;
        }

        if (switchClosed >= 10) {
            closed = false;
            switchClosed = 0;
        }

        currentAngle += (closed ? -0.05 : 0.05);

        if (currentAngle <= min) {
            currentAngle = min;
        } else if (currentAngle >= max) {
            currentAngle = max;
        }
    }
}
