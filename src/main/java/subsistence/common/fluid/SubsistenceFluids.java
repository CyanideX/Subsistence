package subsistence.common.fluid;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import subsistence.common.block.SubsistenceBlocks;
import subsistence.common.core.handler.BucketHandler;
import subsistence.common.item.SubsistenceItems;

/**
 * Created by Thlayli
 */
public class SubsistenceFluids {

    public static Fluid boilingWaterFluid;

    public static void initializeFluids() {
        boilingWaterFluid = new Fluid("boiling");
        FluidRegistry.registerFluid(boilingWaterFluid);
        boilingWaterFluid.setTemperature(373); // Boiling point of water in Kelvins
    }

    public static void initializeFluidContainers() {
        FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluidStack("boiling", FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(SubsistenceItems.boilingBucket), new ItemStack(Items.bucket));
        BucketHandler.INSTANCE.buckets.put(SubsistenceBlocks.boilingWater, SubsistenceItems.boilingBucket);
    }
}
