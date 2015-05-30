package subsistence.common.tile.machine;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.config.CoreSettings;
import subsistence.common.config.HeatSettings;
import subsistence.common.network.nbt.NBTHandler;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.recipe.wrapper.CompostRecipe;
import subsistence.common.tile.core.TileCoreMachine;
import subsistence.common.util.ItemHelper;

import java.util.List;


public class TileCompost extends TileCoreMachine {

    public static final float ANGLE_MAX = 45f;
    public static final float ANGLE_MIN = -21f;

    public static final int ANIMATE_TICK_MAX = 20;

    public static final int VOLUME_WOOD = 8;
    public static final int VOLUME_STONE = 24;

    @NBTHandler.Sync(true)
    public boolean lidOpen = true;
    @NBTHandler.Sync(true)
    public ItemStack[] contents;
    @NBTHandler.Sync(true)
    public FluidStack fluid;

    @NBTHandler.Sync(true)
    public int processingTime;
    @NBTHandler.Sync(true)
    public int maxProcessingTime;

    public CompostRecipe cachedRecipe;

    public int animationTicks = 0;

    public TileCompost() {
        super();
        contents = new ItemStack[0];
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote) {
            if (lidOpen && animationTicks < ANIMATE_TICK_MAX)
                animationTicks++;
            else if (!lidOpen && animationTicks > 0)
                animationTicks--;
        } else {
            if (contents.length > 0 && cachedRecipe == null)
                cachedRecipe = SubsistenceRecipes.COMPOST.get(blockMetadata == 1 ? "stone" : "wood", contents, fluid);

            if (cachedRecipe != null) {
                process();
            }
        }

    }

    public int getVolume() {
        return CoreSettings.STATIC.compostBucketSize * FluidContainerRegistry.BUCKET_VOLUME; //TODO: config value
    }

    private void process() {
        if (maxProcessingTime == 0) {
            maxProcessingTime = getProcessingTime();
        } else {
            if (!lidOpen) {
                if (maxProcessingTime > 0) {
                    processingTime++;

                    if (processingTime >= maxProcessingTime) {
                        contents = new ItemStack[]{cachedRecipe.outputItem.copy()};

                        if (cachedRecipe.requiresCondensate)
                            fluid = null;

                        if (cachedRecipe.requiresHeat() && cachedRecipe.condensates) {
                            fluid = new FluidStack(FluidRegistry.WATER, FluidContainerRegistry.BUCKET_VOLUME);
                        } else {
                            FluidStack output = cachedRecipe.outputLiquid;
                            if (output != null)
                                fluid = output.copy();
                        }

                        reset();
                        markForUpdate();
                    }
                }
            }
        }
    }

    private int getProcessingTime() {
        if (cachedRecipe.requiresHeat()) {
            if (HeatSettings.isTorch(worldObj, xCoord, yCoord - 1, zCoord)) {
                return cachedRecipe.timeTorch;
            } else if (HeatSettings.isLava(worldObj, xCoord, yCoord - 1, zCoord)) {
                return cachedRecipe.timeLava;
            } else if (HeatSettings.isFire(worldObj, xCoord, yCoord - 1, zCoord)) {
                return cachedRecipe.timeFire;
            } else {
                return -1;
            }
        } else {
            return cachedRecipe.time;
        }
    }

    public boolean addItem(ItemStack itemStack) {
        cachedRecipe = null;
        processingTime = 0;
        maxProcessingTime = 0;

        int volume = blockMetadata == 1 ? VOLUME_STONE : VOLUME_WOOD;
        int total = 0;

        List<ItemStack> newContents = Lists.newArrayList();
        for (ItemStack old : contents) {
            if (old != null) {
                newContents.add(old);
                total += old.stackSize;
            }
        }

        if (total >= volume)
            return false;

        newContents.add(itemStack);

        List<ItemStack> finalList = ItemHelper.mergeLikeItems(newContents);
        contents = finalList.toArray(new ItemStack[finalList.size()]);

        return true;
    }

    public ItemStack inventoryPeek() {
        ItemStack last = null;
        for (int i = contents.length - 1; i >= 0; i--) {
            last = contents[i];
            if (last != null) {
                break;
            }
        }

        if (last != null) {
            ItemStack copy = last.copy();
            copy.stackSize = 1;
            return copy;
        } else {
            return null;
        }
    }

    public ItemStack inventoryPop() {
        int index = 0;
        ItemStack last = null;
        for (int i = contents.length - 1; i >= 0; i--) {
            last = contents[i];
            if (last != null) {
                index = i;
                break;
            }
        }

        if (last != null) {
            ItemStack copy = last.copy();
            copy.stackSize = 1;
            last.stackSize--;

            if (last.stackSize <= 0) {
                contents[index] = null;
            }

            return copy;
        } else {
            return null;
        }
    }

    public void updateContents() {
        boolean emptyContents = true;
        emptyContents = (contents == null || contents.length == 0);

        if (!emptyContents) {
            for (ItemStack stack : contents) {
                if (stack != null)
                    emptyContents = false;
            }
        }

        boolean emptyFluid = (fluid == null || fluid.amount == 0);

        if (emptyContents && emptyFluid)
            reset();

        markForUpdate();
    }

    private void reset() {
        cachedRecipe = null;
        processingTime = 0;
        maxProcessingTime = 0;
    }
}