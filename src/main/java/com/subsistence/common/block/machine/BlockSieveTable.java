package com.subsistence.common.block.machine;

import com.subsistence.common.block.prefab.SubsistenceTileBlock;
import com.subsistence.common.tile.machine.TileSieveTable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author Royalixor
 */
public class BlockSieveTable extends SubsistenceTileBlock {

    public BlockSieveTable() {
        super(Material.iron);
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
    public void registerBlockIcons(IIconRegister iconRegister) {

    }
}
