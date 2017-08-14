package oldStuff.common.fluid;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import oldStuff.common.item.ItemWoodenBucket;
import oldStuff.common.item.SubsistenceItems;
import cpw.mods.fml.common.registry.GameRegistry;


public class SubsistenceFluids {

    public static Fluid boilingWaterFluid;

    public static void initializeFluids() {
        boilingWaterFluid = new Fluid("boiling");
        FluidRegistry.registerFluid(boilingWaterFluid);
        boilingWaterFluid.setTemperature(373); // Boiling point of water in Kelvins
    }

    public static void initializeFluidContainers() {
        // Boiling water bucket
        FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluidStack("boiling", FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(SubsistenceItems.boilingBucket), new ItemStack(Items.bucket));

        // Wooden bucket - water
        Fluid fluid = FluidRegistry.WATER;
        ItemWoodenBucket item = new ItemWoodenBucket(fluid, "water");
        item.setUnlocalizedName("wooden_bucket.water");
        GameRegistry.registerItem(item, "wooden_bucket.water");

        ItemWoodenBucket.registerAllContainers(fluid, item);
        
        // Wooden bucket - boiling water
        fluid = SubsistenceFluids.boilingWaterFluid;
        item = new ItemWoodenBucket(fluid, "boiling_water");
        item.setUnlocalizedName("wooden_bucket.boiling_water");
        GameRegistry.registerItem(item, "wooden_bucket.boiling_water");

        ItemWoodenBucket.registerAllContainers(fluid, item);

        // Wooden bucket - lava
        fluid = FluidRegistry.LAVA;
        item = new ItemWoodenBucket(fluid, "lava");
        item.setUnlocalizedName("wooden_bucket.lava");
        GameRegistry.registerItem(item, "wooden_bucket.lava");

        ItemWoodenBucket.registerAllContainers(fluid, item);
    }
}
