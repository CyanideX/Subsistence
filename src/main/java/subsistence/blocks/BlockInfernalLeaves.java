package subsistence.blocks;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import subsistence.ModBlocks;
import subsistence.Subsistence;
import subsistence.util.Data;
import subsistence.util.IHasModel;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class BlockInfernalLeaves extends BlockLeaves implements IHasModel {
    public BlockInfernalLeaves() {
        super();
        setCreativeTab(Subsistence.creativeTab);

        setRegistryName("block_infernal_leaves");
        setUnlocalizedName("block_infernal_leaves");

        setDefaultState(blockState.getBaseState().withProperty(CHECK_DECAY, false).withProperty(DECAYABLE, true));

        Data.BLOCKS.add(this);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(ModBlocks.BLOCK_INFERNAL_SAPLING);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return true;
    }

    @Override
    public BlockPlanks.EnumType getWoodType(int meta) {
        return BlockPlanks.EnumType.DARK_OAK;
    }

    @Override
    public boolean shouldSideBeRendered(@Nonnull IBlockState blockState,@Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
        return true;
    }

    @Override
    public boolean isFlammable(@Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing face) {
        return true;
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 0;
    }

    @Override
    public boolean isFireSource(@Nonnull World world, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public List<ItemStack> onSheared(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        return NonNullList.withSize(1, new ItemStack(Item.getItemFromBlock(this)));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(DECAYABLE, (meta & 1) == 0).withProperty(CHECK_DECAY, (meta & 4) > 0);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        if (!state.getValue(DECAYABLE))
        {
            i |= 1;
        }

        if (state.getValue(CHECK_DECAY))
        {
            i |= 4;
        }

        return i;
    }
}
