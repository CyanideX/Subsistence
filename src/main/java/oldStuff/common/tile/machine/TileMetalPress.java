package oldStuff.common.tile.machine;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import oldStuff.common.network.nbt.NBTHandler;
import oldStuff.common.recipe.SubsistenceRecipes;
import oldStuff.common.recipe.wrapper.MetalPressRecipe;
import oldStuff.common.tile.core.TileCoreMachine;
import oldStuff.common.util.InventoryHelper;

public class TileMetalPress extends TileCoreMachine {

    public static final float RENDER_MIN = 0.6f;

    public static final int ANIMATE_TICK_MAX = 15;
    public static final int PAUSE_TICK_MAX = 5;

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

    // If set to true, the item will be updated at the end of the next animation cycle
    private boolean checkForUpdate = false;

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
                    if (pauseTicker >= PAUSE_TICK_MAX) {
                        if (!worldObj.isRemote && checkForUpdate) {
                            updateItem();
                            checkForUpdate = false;
                        }

                        state = false;
                    } else {
                        if (pauseTicker <= 0) {
                            worldObj.playSound(xCoord, yCoord, zCoord, "random.anvil_land", 0.25F, 1F, false);
                        }
                        pauseTicker++;
                    }
                }
            } else {
                if (animationTicker > 0)
                    animationTicker--;

                if (pauseTicker > 0)
                    pauseTicker = 0;
            }
        }
    }

    @Override
    public void onBlockBroken() {
        InventoryHelper.dropItem(worldObj, xCoord, yCoord, zCoord, ForgeDirection.UNKNOWN, itemStack, RANDOM);
    }

    public void activate() {
        if (itemStack == null)
            return;

        MetalPressRecipe recipe = SubsistenceRecipes.METAL_PRESS.get(itemStack);
        if (recipe != null && !state && animationTicker == 0) {
            amount++;
            state = true;
            checkForUpdate = true;

            markForUpdate();
        }
    }

    private void updateItem() {
        System.out.println(amount);
        MetalPressRecipe recipe = SubsistenceRecipes.METAL_PRESS.get(itemStack);

        // We send an update every time we activate and the render should update
        markForUpdate();

        if (amount >= recipe.amount) {
            itemStack = recipe.outputItem.copy();
            markForUpdate();
        }
    }
}
