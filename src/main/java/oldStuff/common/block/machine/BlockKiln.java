package oldStuff.common.block.machine;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import oldStuff.common.block.prefab.SubsistenceTileBlock;
import oldStuff.common.tile.machine.TileKiln;

public class BlockKiln extends SubsistenceTileBlock {

    public BlockKiln() {
        super(Material.iron);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileKiln();
    }

    @Override
    public boolean useCustomRender() {
        return true;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {

    }
}
