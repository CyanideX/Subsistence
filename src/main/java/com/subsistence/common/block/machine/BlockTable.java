package com.subsistence.common.block.machine;

import com.subsistence.common.block.prefab.SubsistenceTileMultiBlock;
import com.subsistence.common.tile.machine.TileTable;
import com.subsistence.common.util.ArrayHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * @author dmillerw
 */
public class BlockTable extends SubsistenceTileMultiBlock {

    private static final String[] NAMES = new String[]{"wood", "stone"};

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float fx, float fy, float fz) {
        if (!world.isRemote) {
            TileTable tile = (TileTable) world.getTileEntity(x, y, z);

            if (tile != null) {
                if (!player.isSneaking() && side == 1) {
                    ItemStack held = player.getHeldItem();

                    if (held != null && tile.stack == null) {
                        //TODO Check if has to be null
//                        if (tile.attractedFlies) {
//                            if (held.getItem() == SubsistenceItems.net && held.getItemDamage() == 0) {
//                                held.setItemDamage(1);
//                                tile.attractedFlies = false;
//                                return true;
//                            }
//                        }

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
        return ArrayHelper.getArrayIndexes(NAMES); // Forces all aspects of this block to base themselves off the NAMES array
    }

    @Override
    public String getNameForType(int type) {
        return NAMES[type];
    }

    @Override
    public boolean useCustomRender() {
        return true;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return Blocks.planks.getIcon(side, meta);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileTable();
    }
}
