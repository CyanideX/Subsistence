package subsistence.common.block.machine;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import subsistence.common.block.prefab.SubsistenceTileMultiBlock;
import subsistence.common.item.SubsistenceItems;
import subsistence.common.tile.core.TileCore;
import subsistence.common.tile.machine.TileTable;
import subsistence.common.util.ArrayHelper;

public class BlockTable extends SubsistenceTileMultiBlock {

    public BlockTable() {
        super(Material.wood);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float fx, float fy, float fz) {
        if (!world.isRemote) {
            TileTable tile = (TileTable) world.getTileEntity(x, y, z);

            if (tile != null) {
                if (!player.isSneaking() && side == 1) {
                    ItemStack held = player.getHeldItem();

                    if (tile.stack != null && held == null) {
                        player.setCurrentItemOrArmor(0, tile.stack.copy());
                        tile.setStack(null);
                        return true;
                    }

                    if (held != null && tile.stack == null) {
                        if (tile.attractedFlies) {
                            if (held.getItem() == SubsistenceItems.net && held.getItemDamage() == 0) {
                                held.setItemDamage(1);
                                tile.attractedFlies = false;
                                return true;
                            }
                        }

                        ItemStack stack = held.copy();
                        stack.stackSize = 1;

                        tile.setStack(stack);

                        held.stackSize--;
                        if (held.stackSize <= 0) {
                            player.setCurrentItemOrArmor(0, null);
                        }
                    } else {
                        if (!tile.smash(player)) {
                            if (tile.stack != null && held == null) {
                                player.setCurrentItemOrArmor(0, tile.stack.copy());
                                tile.setStack(null);
                                return true;
                            } else if (tile.stack != null) {
                                if (tile.stack.isItemEqual(held) && (held.stackSize + 1 <= held.getItem().getItemStackLimit(held))) {
                                    held.stackSize += tile.stack.stackSize;

                                    if (held.stackSize > held.getMaxStackSize()) {
                                        held.stackSize = held.getMaxStackSize();
                                        tile.stack.stackSize = held.stackSize - held.getMaxStackSize();
                                        world.markBlockForUpdate(x, y, z);
                                    } else {
                                        tile.setStack(null);
                                    }

                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return !player.isSneaking();
    }


    @Override
    public int[] getSubtypes() {
        return ArrayHelper.getArrayIndexes(TableType.values()); // Forces all aspects of this block to base themselves off the NAMES array
    }

    @Override
    public String getNameForType(int type) {
        return ArrayHelper.safeGetArrayIndex(TableType.values(), type).toString();
    }

    @Override
    public boolean useCustomRender() {
        return true;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return Blocks.planks.getIcon(side, 0);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileTable();
    }
}
