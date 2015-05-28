package subsistence.common.tile.machine;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.config.CoreSettings;
import subsistence.common.network.nbt.NBTHandler;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.recipe.wrapper.BarrelStoneRecipe;
import subsistence.common.recipe.wrapper.BarrelWoodRecipe;
import subsistence.common.tile.core.TileCoreMachine;

public final class TileBarrel extends TileCoreMachine {

    public static final int VOLUME_ITEMS = 2;
    public static final int VOLUME_FLUID_WOOD = FluidContainerRegistry.BUCKET_VOLUME * 2;
    public static final int VOLUME_FLUID_STONE = FluidContainerRegistry.BUCKET_VOLUME * 8;

    public static final float DIMENSION_FILL = 0.8F - 0.0625F;

    /* GENERAL */
    @NBTHandler.Sync(true)
    public ItemStack[] itemContents = new ItemStack[VOLUME_ITEMS];
    @NBTHandler.Sync(true)
    public FluidStack fluidContents = null;

    @NBTHandler.Sync(true)
    public boolean hasLid;

    /* WOOD SPECIFIC */
    private int rainDelayTick = 0;
    private int rainDelay = -1;

    private BarrelWoodRecipe cachedWoodRecipe;

    /* STONE SPECIFIC */
    private BarrelStoneRecipe cachedStoneRecipe;

    @Override
    public void updateEntity() {
        if (worldObj.isRemote)
            return;

        if (isWood()) {
            processWoodRecipe();
            collectRainWater();
        } else if (isStone()) {

        }
    }

    /* STATE */
    private boolean isWood() {
        return blockMetadata == 0;
    }

    private boolean isStone() {
        return blockMetadata == 1;
    }

    /* GENERAL HELPerS */
    private int getFluidVolume() {
        return blockMetadata == 1 ? VOLUME_FLUID_STONE : VOLUME_FLUID_WOOD;
    }

    public boolean addFluid(FluidStack fluidStack) {
        if (fluidContents == null || fluidContents.getFluid() == null) {
            fluidContents = fluidStack.copy();

            markForUpdate();

            return true;
        } else {
            if (fluidContents.getFluid() == fluidStack.getFluid()) {
                if (fluidContents.amount + fluidStack.amount <= getFluidVolume()) {
                    fluidContents.amount += fluidStack.amount;

                    markForUpdate();

                    return true;
                }
            } else {
                return false;
            }
        }

        return false;
    }

    public boolean addItem(ItemStack itemStack) {
        if (itemContents == null || itemContents.length != VOLUME_ITEMS) {
            itemContents = new ItemStack[VOLUME_ITEMS];
        }

        for (int i=0; i<itemContents.length; i++) {
            ItemStack contents = itemContents[i];

            if (contents == null) {
                itemContents[i] = itemStack.copy();
                markForUpdate();
                return true;
            }
        }
        return false;
    }

    /* WOOD SPECIFIC */
    private void processWoodRecipe() {
        if (cachedWoodRecipe == null) {
            cachedWoodRecipe = SubsistenceRecipes.BARREL.getWooden(itemContents, fluidContents);
        } else {
            // Wood recipes are immediate
            itemContents = new ItemStack[VOLUME_ITEMS];
            fluidContents = null;

            if (cachedWoodRecipe.outputItem != null)
                itemContents = new ItemStack[] {cachedWoodRecipe.outputItem.copy()};

            if (cachedWoodRecipe.outputLiquid != null)
                fluidContents = cachedWoodRecipe.outputLiquid.copy();

            cachedWoodRecipe = null;

            markForUpdate();
        }
    }

    private void collectRainWater() {
        if (cachedWoodRecipe != null)
            return;

        if (!worldObj.isRaining() || !worldObj.canBlockSeeTheSky(xCoord, yCoord, zCoord))
            return;

        if (rainDelay == -1) {
            rainDelay = RANDOM.nextInt(500);
        } else {
            if (rainDelayTick >= rainDelay) {
                rainDelayTick = 0;
                rainDelay = RANDOM.nextInt(500);

                if (!hasLid) {
                    addFluid(new FluidStack(FluidRegistry.WATER, CoreSettings.STATIC.barrelRain));
                }
            } else {
                rainDelayTick++;
            }
        }
    }

    /* STONE SPECIFIC */
    private void detectStoneRecipe() {
        if (cachedStoneRecipe == null) {
            cachedStoneRecipe = SubsistenceRecipes.BARREL.getStone(itemContents, fluidContents);
        }
    }
}