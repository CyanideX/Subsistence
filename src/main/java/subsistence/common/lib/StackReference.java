package subsistence.common.lib;

import subsistence.common.block.SubsistenceBlocks;
import subsistence.common.block.BlockComponentGround;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

/**
 * @author dmillerw
 */
public class StackReference {

    public static final ItemStack STONE = new ItemStack(Blocks.stone);
    public static final ItemStack COBBLESTONE = new ItemStack(Blocks.cobblestone);
    public static final ItemStack GRAVEL = new ItemStack(Blocks.gravel);
    public static final ItemStack SAND = new ItemStack(Blocks.sand);
    public static final ItemStack FINE_SAND = new ItemStack(SubsistenceBlocks.componentGround, 1, BlockComponentGround.FINE_SAND);

    public static final ItemStack NETHER_RIND = new ItemStack(SubsistenceBlocks.componentGround, 1, BlockComponentGround.NETHER_RIND);
    public static final ItemStack NETHERRACK = new ItemStack(Blocks.netherrack);
    public static final ItemStack NETHER_GRIT = new ItemStack(SubsistenceBlocks.componentGround, 1, BlockComponentGround.NETHER_GRIT);
    public static final ItemStack SOUL_SAND = new ItemStack(Blocks.soul_sand);
    public static final ItemStack SOUL_DUST = new ItemStack(SubsistenceBlocks.componentGround, 1, BlockComponentGround.SOUL_DUST);

    public static final ItemStack DIRT = new ItemStack(Blocks.dirt);
    public static final ItemStack WEB = new ItemStack(Blocks.web);
    public static final ItemStack MYCELIUM = new ItemStack(Blocks.mycelium);
}
