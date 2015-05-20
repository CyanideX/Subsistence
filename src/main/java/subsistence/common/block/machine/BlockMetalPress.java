package subsistence.common.block.machine;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import subsistence.common.block.prefab.SubsistenceTileBlock;
import subsistence.common.recipe.SubsistenceRecipes;
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
                    tile.activate();
                } else {
                    if (player.getHeldItem() == null) { //pull item out
                        if (tile.itemStack != null) {
                            ItemStack current = tile.itemStack.copy();
                            current.stackSize = 1;
                            if (player.inventory.addItemStackToInventory(current.copy())) {
                            } else { //if not added successfully, drop on ground
                                player.func_146097_a(current.copy(), false, false);
                            }
                            tile.itemStack = null;
                        }
                    } else { //put item in
                        if (tile.itemStack == null) { //no item in block
                            ItemStack held = player.getHeldItem().copy();
                            held.stackSize = 1;
                            if (SubsistenceRecipes.METAL_PRESS.isAllowed(held)) {
                                tile.itemStack = held;
                                player.getHeldItem().stackSize--;
                            }
                        }
                    }
                    tile.markForUpdate();
                }
            }
        }
        return true;
    }
}
