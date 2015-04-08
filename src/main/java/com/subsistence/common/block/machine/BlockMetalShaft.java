package com.subsistence.common.block.machine;

import com.subsistence.common.block.prefab.SubsistenceTileBlock;
import com.subsistence.common.tile.machine.TileMetalShaft;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author dmillerw
 */
public class BlockMetalShaft extends SubsistenceTileBlock {

    public BlockMetalShaft() {
        super(Material.iron);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        TileMetalShaft tile = (TileMetalShaft) world.getTileEntity(x, y, z);

        if (tile != null) {
            switch (tile.orientation) {
                case NORTH:
                case SOUTH:
                    setBlockBounds(0, 0.4F, 0.4F, 1, 0.6F, 0.6F);
                    break;
                case EAST:
                case WEST:
                    setBlockBounds(0.4F, 0.4F, 0, 0.6F, 0.6F, 1);
                    break;
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileMetalShaft();
    }

    @Override
    public boolean useCustomRender() {
        return true;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {

    }
}
