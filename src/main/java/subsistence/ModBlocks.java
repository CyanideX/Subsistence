package subsistence;

import net.minecraft.block.Block;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import subsistence.blocks.BlockInfernalLeaves;
import subsistence.blocks.BlockInfernalLog;
import subsistence.blocks.BlockInfernalSapling;
import subsistence.util.Data;
import subsistence.util.IHasModel;

public class ModBlocks {


    public static final BlockInfernalLeaves BLOCK_INFERNAL_LEAVES;
    public static final BlockInfernalLog BLOCK_INFERNAL_LOG;
    public static final BlockInfernalSapling BLOCK_INFERNAL_SAPLING;


    static {
        BLOCK_INFERNAL_LEAVES = new BlockInfernalLeaves();
        BLOCK_INFERNAL_LOG = new BlockInfernalLog();
        BLOCK_INFERNAL_SAPLING = new BlockInfernalSapling();
    }

    public static void registerBlocks(IForgeRegistry<Block> registry) {
        for (Block block : Data.BLOCKS) {
            registry.register(block);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void initModels(ModelRegistryEvent e) {
        for (Block block : Data.BLOCKS) {
            if (block instanceof IHasModel) {
                ((IHasModel) block).initModel(e);
            }
        }
    }
}
