package subsistence.blocks;

import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import subsistence.Subsistence;
import subsistence.util.Data;
import subsistence.util.IHasModel;

import javax.annotation.Nonnull;

public class BlockInfernalLog extends BlockLog implements IHasModel {

    public static final PropertyBool IS_RICH = PropertyBool.create("is_rich");

    public BlockInfernalLog() {
        super();

        setCreativeTab(Subsistence.creativeTab);
        setRegistryName("block_infernal_log");
        setUnlocalizedName("block_infernal_log");

        Data.BLOCKS.add(this);

        setDefaultState(blockState.getBaseState().withProperty(IS_RICH, false).withProperty(LOG_AXIS, EnumAxis.Y));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, IS_RICH, LOG_AXIS);
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


    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = this.getDefaultState().withProperty(IS_RICH, (meta & 1) == 1);

        switch (meta & 0b110)
        {
            case 0b000:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
                break;
            case 0b010:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
                break;
            case 0b110:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
                break;
            default:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
        }

        return iblockstate;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @SuppressWarnings("incomplete-switch")
    public int getMetaFromState(IBlockState state)
    {
        int i = state.getValue(IS_RICH) ? 1 : 0;

        switch (state.getValue(LOG_AXIS))
        {
            case X:
                i |= 0b010;
                break;
            case Y:
                i |= 0b000;
                break;
            case Z:
                i |= 0b110;
                break;
            case NONE:
                i |= 0b100;
        }

        return i;
    }
}
