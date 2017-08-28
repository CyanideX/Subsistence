package subsistence.worldgen;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import subsistence.ModBlocks;
import subsistence.blocks.BlockInfernalLog;

import javax.annotation.Nonnull;

public class WorldGenInfernalTree extends WorldGenAbstractTree
{
    private static final IBlockState LOG_NORMAL = ModBlocks.BLOCK_INFERNAL_LOG.getDefaultState().withProperty(BlockInfernalLog.IS_RICH, false);
    private static final IBlockState LOG_RICH = ModBlocks.BLOCK_INFERNAL_LOG.getDefaultState().withProperty(BlockInfernalLog.IS_RICH, true);
    private static final IBlockState LEAF = ModBlocks.BLOCK_INFERNAL_LEAVES.getDefaultState();
    private final boolean useExtraRandomHeight;

    public WorldGenInfernalTree(boolean notify, boolean useExtraRandomHeightIn)
    {
        super(notify);
        this.useExtraRandomHeight = useExtraRandomHeightIn;
    }

    @Override
    public boolean generate(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos position)
    {
        int i = rand.nextInt(3) + 5;

        if (this.useExtraRandomHeight)
        {
            i += rand.nextInt(7);
        }

        boolean flag = true;

        if (position.getY() >= 1 && position.getY() + i + 1 <= 256)
        {
            for (int j = position.getY(); j <= position.getY() + 1 + i; ++j)
            {
                int k = 1;

                if (j == position.getY())
                {
                    k = 0;
                }

                if (j >= position.getY() + 1 + i - 2)
                {
                    k = 2;
                }

                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l)
                {
                    for (int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1)
                    {
                        if (j >= 0 && j < worldIn.getHeight())
                        {
                            if (!this.isReplaceable(worldIn, blockpos$mutableblockpos.setPos(l, j, i1)))
                            {
                                System.out.println("1: Can't grow because of " + new BlockPos(l, j, i1));
                                flag = false;
                            }
                        }
                        else
                        {
                            System.out.println("2: Can't grow because of " + new BlockPos(l, j, i1));
                            flag = false;
                        }
                    }
                }
            }

            if (!flag)
            {
                return false;
            }
            else
            {
                BlockPos down = position.down();
                IBlockState state = worldIn.getBlockState(down);
                boolean isSoil = state.getBlock().canSustainPlant(state, worldIn, down, net.minecraft.util.EnumFacing.UP, (net.minecraft.block.BlockSapling)Blocks.SAPLING);

                if (isSoil && position.getY() < worldIn.getHeight() - i - 1)
                {
                    state.getBlock().onPlantGrow(state, worldIn, down, position);

                    for (int i2 = position.getY() - 3 + i; i2 <= position.getY() + i; ++i2)
                    {
                        int k2 = i2 - (position.getY() + i);
                        int l2 = 1 - k2 / 2;

                        for (int i3 = position.getX() - l2; i3 <= position.getX() + l2; ++i3)
                        {
                            int j1 = i3 - position.getX();

                            for (int k1 = position.getZ() - l2; k1 <= position.getZ() + l2; ++k1)
                            {
                                int l1 = k1 - position.getZ();

                                if (Math.abs(j1) != l2 || Math.abs(l1) != l2 || rand.nextInt(2) != 0 && k2 != 0)
                                {
                                    BlockPos blockpos = new BlockPos(i3, i2, k1);
                                    IBlockState state2 = worldIn.getBlockState(blockpos);

                                    if (state2.getBlock().isAir(state2, worldIn, blockpos) || state2.getBlock().isAir(state2, worldIn, blockpos))
                                    {
                                        this.setBlockAndNotifyAdequately(worldIn, blockpos, LEAF);
                                    }
                                }
                            }
                        }
                    }

                    for (int j2 = 0; j2 < i; ++j2)
                    {
                        BlockPos upN = position.up(j2);
                        IBlockState state2 = worldIn.getBlockState(upN);

                        if (state2.getBlock().isAir(state2, worldIn, upN) || state2.getBlock().isLeaves(state2, worldIn, upN))
                        {
                            if (rand.nextInt(100) <= 5){
                                this.setBlockAndNotifyAdequately(worldIn, position.up(j2), LOG_RICH);
                            } else {
                                this.setBlockAndNotifyAdequately(worldIn, position.up(j2), LOG_NORMAL);
                            }
                        }
                    }

                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }
    }

    @Override
    protected boolean canGrowInto(Block blockType) {
        return blockType == ModBlocks.BLOCK_INFERNAL_SAPLING || super.canGrowInto(blockType);
    }
}