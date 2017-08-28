package subsistence.proxy;

import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import subsistence.ModBlocks;
import subsistence.ModItems;
import subsistence.blocks.BlockInfernalLeaves;
import subsistence.blocks.BlockInfernalSapling;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomStateMapper(ModBlocks.BLOCK_INFERNAL_LEAVES, new StateMap.Builder().ignore(BlockInfernalLeaves.CHECK_DECAY, BlockInfernalLeaves.DECAYABLE).build());
        ModelLoader.setCustomStateMapper(ModBlocks.BLOCK_INFERNAL_SAPLING, new StateMap.Builder().ignore(BlockInfernalSapling.STAGE).build());

        ModBlocks.initModels(event);
        ModItems.initModels(event);
        // ModFluids.initModels();
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
