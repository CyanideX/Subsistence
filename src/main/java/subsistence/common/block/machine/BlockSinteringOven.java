package subsistence.common.block.machine;

import subsistence.common.block.prefab.SubsistenceTileBlock;
import subsistence.common.tile.machine.TileSinteringOven;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author Royalixor
 */
public class BlockSinteringOven extends SubsistenceTileBlock {

    public BlockSinteringOven() {
        super(Material.iron);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float fx, float fy, float fz) {
        if (!world.isRemote) {
            if (player.isSneaking()) {
                TileSinteringOven tile = (TileSinteringOven) world.getTileEntity(x, y, z);

                if (tile != null) {
                    tile.open = !tile.open;
                    tile.markForUpdate();
                }
            }
        }

        return player.isSneaking();
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileSinteringOven();
    }

    @Override
    public boolean useCustomRender() {
        return true;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {

    }
}
