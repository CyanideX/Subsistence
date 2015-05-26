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
    public ItemStack[] itemContents = new ItemStack[0];
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
                    if (fluidContents == null || fluidContents.getFluid() == null) {
                        fluidContents = new FluidStack(FluidRegistry.WATER, CoreSettings.STATIC.barrelRain);

                        markForUpdate();
                    } else {
                        if (fluidContents.getFluid() == FluidRegistry.WATER) {
                            if (fluidContents.amount + CoreSettings.STATIC.barrelRain <= getFluidVolume()) {
                                fluidContents.amount += CoreSettings.STATIC.barrelRain;

                                markForUpdate();
                            }
                        }
                    }
                }
            } else {
                rainDelayTick++;
            }
        }
    }
}