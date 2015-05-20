package subsistence.common.block.machine;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import subsistence.common.block.prefab.SubsistenceTileBlock;
import subsistence.common.tile.machine.TileMetalPress;

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
                    tile.activate(player);
                } else {
                    final ItemStack held = player.getHeldItem();

                    //TODO Stack merging
                    if (held == null) {
                        if (tile.itemStack != null) {
                            player.setCurrentItemOrArmor(0, tile.itemStack.copy());
                            ((EntityPlayerMP)player).updateHeldItem();

                            tile.itemStack = null;
                            tile.markForUpdate();
                        }
                    } else {
                        if (tile.itemStack == null) {
                            final ItemStack copy = held.copy();
                            copy.stackSize = 1;
                            held.stackSize--;

                            if (held.stackSize <= 0) {
                                player.setCurrentItemOrArmor(0, null);
                                ((EntityPlayerMP)player).updateHeldItem();
                            }

                            tile.itemStack = copy;
                            tile.markForUpdate();
                        }
                    }
                }
            }
        }
        return true;
    }
}
