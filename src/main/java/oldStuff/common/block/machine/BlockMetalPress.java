package oldStuff.common.block.machine;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import oldStuff.common.block.prefab.SubsistenceTileBlock;
import oldStuff.common.tile.machine.TileMetalPress;

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
                    tile.activate();
                } else {
                    final ItemStack held = player.getHeldItem();

                    if (held == null) {
                        if (tile.itemStack != null) {
                            player.setCurrentItemOrArmor(0, tile.itemStack.copy());
                            ((EntityPlayerMP) player).updateHeldItem();

                            tile.itemStack = null;
                            tile.markForUpdate();
                        }
                    } else {
                        if (tile.itemStack == null) {
                            final ItemStack copy = held.copy();
                            copy.stackSize = 1; // We never take more than one item, so this is okay
                            held.stackSize--;

                            if (held.stackSize <= 0) {
                                player.setCurrentItemOrArmor(0, null);
                                ((EntityPlayerMP) player).updateHeldItem();
                            }

                            tile.itemStack = copy;
                            tile.state = false;
                            tile.animationTicker = 0;
                            tile.markForUpdate();
                        } else {
                            // Compare the two items and make sure there's room
                            if (held.isItemEqual(tile.itemStack)) {
                                if ((held.stackSize + 1) <= held.getMaxStackSize()) {
                                    held.stackSize++;
                                    ((EntityPlayerMP) player).updateHeldItem();

                                    tile.itemStack = null;
                                    tile.markForUpdate();
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
