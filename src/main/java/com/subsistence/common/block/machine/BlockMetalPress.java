package com.subsistence.common.block.machine;

import com.subsistence.Subsistence;
import com.subsistence.common.block.prefab.SubsistenceTileBlock;
import com.subsistence.common.network.PacketSyncContents;
import com.subsistence.common.recipe.SubsistenceRecipes;
import com.subsistence.common.tile.machine.TileCompost;
import com.subsistence.common.tile.machine.TileMetalPress;
import com.subsistence.common.tile.machine.TileSinteringOven;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author dmillerw
 */
public class BlockMetalPress extends SubsistenceTileBlock {

    public BlockMetalPress() {
        super(Material.iron);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileMetalPress();
    }

    @Override
    public boolean useCustomRender() {
        return true;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {

    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileMetalPress tile = (TileMetalPress) world.getTileEntity(x, y, z);
            if (tile != null) {
                if (player.isSneaking()) {
                    tile.sendPoke();
                } else {
                    if (player.getHeldItem() == null) {
                        player.setCurrentItemOrArmor(0, tile.itemStack);
                        tile.itemStack = null;
                    } else {
                        if (tile.itemStack == null) {
                            if (SubsistenceRecipes.METAL_PRESS.isAllowed(player.getHeldItem())) {
                                tile.itemStack = new ItemStack(player.getHeldItem().getItem());
                                Subsistence.network.sendToServer(new PacketSyncContents(tile, tile.itemStack));
                                player.getHeldItem().stackSize--;
                            }
                        }
                    }
                    tile.markForUpdate();
                }
            }
        }

        return player.isSneaking();
    }
}
