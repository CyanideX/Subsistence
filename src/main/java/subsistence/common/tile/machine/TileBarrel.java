package subsistence.common.tile.machine;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.config.CoreSettings;
import subsistence.common.network.nbt.NBTHandler;
import subsistence.common.tile.core.TileCoreMachine;

public final class TileBarrel extends TileCoreMachine {

    public static final int VOLUME_ITEMS = 2;
    public static final int VOLUME_FLUID_WOOD = FluidContainerRegistry.BUCKET_VOLUME * 2;
    public static final int VOLUME_FLUID_STONE = FluidContainerRegistry.BUCKET_VOLUME * 8;

    public static final float DIMENSION_FILL = 0.8F - 0.0625F;

    public static enum RecipeMode {
        NONE,
        MIXING,
        MELTING
    }

    @NBTHandler.Sync(true)
    public ItemStack[] itemContents = new ItemStack[VOLUME_ITEMS];
    @NBTHandler.Sync(true)
    public FluidStack fluidContents = null;

    @NBTHandler.Sync(true)
    public RecipeMode recipeMode = RecipeMode.NONE;

    @NBTHandler.Sync(true)
    public boolean hasLid;

    private int rainDelayTick = 0;
    private int rainDelay = -1;

    @Override
    public void updateEntity() {
        if (worldObj.isRemote)
            return;

        collectRainWater();
    }

    private int getFluidVolume() {
        return blockMetadata == 1 ? VOLUME_FLUID_STONE : VOLUME_FLUID_WOOD;
    }

    private void collectRainWater() {
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
}