package subsistence.common.fluid;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import subsistence.common.item.ItemWoodenBucket;
import subsistence.common.item.SubsistenceItems;


public class SubsistenceFluids {

    public static Fluid boilingWaterFluid;

    public static void initializeFluids() {
        boilingWaterFluid = new Fluid("boiling");
        FluidRegistry.registerFluid(boilingWaterFluid);
        boilingWaterFluid.setTemperature(373); // Boiling point of water in Kelvins
    }

    public static void initializeFluidContainers() {
        FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluidStack("boiling", FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(SubsistenceItems.boilingBucket), new ItemStack(Items.bucket));

        // Wooden bucket - water
        Fluid fluid = FluidRegistry.WATER;
        ItemWoodenBucket item = new ItemWoodenBucket(fluid, "water");
        item.setUnlocalizedName("wooden_bucket.water");

        FluidContainerRegistry.registerFluidContainer(fluid, new ItemStack(item), new ItemStack(SubsistenceItems.woodenBucket));

        GameRegistry.registerItem(item, "wooden_bucket.water");

        // Wooden bucket - boiling water
        fluid = SubsistenceFluids.boilingWaterFluid;
        item = new ItemWoodenBucket(fluid, "boiling_water");
        item.setUnlocalizedName("wooden_bucket.boiling_water");

        FluidContainerRegistry.registerFluidContainer(fluid, new ItemStack(item), new ItemStack(SubsistenceItems.woodenBucket));

        GameRegistry.registerItem(item, "wooden_bucket.boiling_water");
    }
}
