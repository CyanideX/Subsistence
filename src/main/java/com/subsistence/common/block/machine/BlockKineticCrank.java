package com.subsistence.common.block.machine;

import com.subsistence.common.block.prefab.SubsistenceTileBlock;
import com.subsistence.common.tile.machine.TileKineticCrank;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author dmillerw
 */
public class BlockKineticCrank extends SubsistenceTileBlock {

    public BlockKineticCrank() {
        super(Material.iron);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!world.isRemote) {
            TileKineticCrank tile = (TileKineticCrank) world.getTileEntity(x, y, z);

            if (tile != null) {
                tile.updateOrientation();
                tile.markForUpdate();
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileKineticCrank();
    }

    @Override
    public boolean useCustomRender() {
        return true;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {

    }
}
