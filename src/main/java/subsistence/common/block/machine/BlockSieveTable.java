package subsistence.common.block.machine;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import subsistence.common.block.prefab.SubsistenceTileBlock;
import subsistence.common.tile.machine.TileSieveTable;

public class BlockSieveTable extends SubsistenceTileBlock {

    public BlockSieveTable() {
        super(Material.wood);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileSieveTable();
    }

    @Override
    public boolean useCustomRender() {
        return true;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return false;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {

    }
}
