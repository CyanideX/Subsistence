package com.subsistence.common.block.machine;

import com.subsistence.common.tile.machine.TileHammerMill;
import com.subsistence.common.block.prefab.SubsistenceTileBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

/**
 * @author dmillerw
 */
public class BlockHammerMill extends SubsistenceTileBlock {

    public BlockHammerMill() {
        super(Material.iron);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float fx, float fy, float fz) {
        TileHammerMill tile = (TileHammerMill) world.getTileEntity(x, y, z);
        if (side == tile.orientation.ordinal()) {
            if (!world.isRemote) {
                tile.updateStage();
                player.addChatComponentMessage(new ChatComponentText(String.format(StatCollector.translateToLocal("chat.hammer_mill.stage"), tile.grindingStage + 1)));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onBlockEventReceived(World world, int x, int y, int z, int id, int param) {
        TileHammerMill tile = (TileHammerMill) world.getTileEntity(x, y, z);
        if (tile != null) {
            tile.receiveClientEvent(id, param);
            return true;
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileHammerMill();
    }

    @Override
    public boolean useCustomRender() {
        return true;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {

    }
}
