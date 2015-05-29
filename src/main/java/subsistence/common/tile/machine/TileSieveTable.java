package subsistence.common.tile.machine;

import com.google.common.collect.Lists;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import subsistence.common.network.nbt.NBTHandler;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.recipe.wrapper.SieveRecipe;
import subsistence.common.tile.core.TileCore;
import subsistence.common.util.InventoryHelper;

import java.util.List;

public class TileSieveTable extends TileCore implements ISidedInventory {

    private static final int INVENTORY_SIZE = 9;

    @NBTHandler.Sync(true)
    public ItemStack[] processing = new ItemStack[INVENTORY_SIZE];
    @NBTHandler.Sync(true)
    public ItemStack[] stuffed;
    @NBTHandler.Sync(true)
    public int maxProcessingTime = 0;
    @NBTHandler.Sync(true)
    public int currentProcessingTime = 0;

    @Override
    public void onBlockBroken() {
        for (ItemStack stack : processing) {
            if (stack != null) {
                InventoryHelper.dropItem(worldObj, xCoord, yCoord, zCoord, ForgeDirection.UNKNOWN, stack, RANDOM);
            }
        }

        if(stuffed != null) {
            for (ItemStack stack : stuffed) {
                if (stack != null) {
                    InventoryHelper.dropItem(worldObj, xCoord, yCoord, zCoord, ForgeDirection.UNKNOWN, stack, RANDOM);
                }
            }
        }
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            // Collect items
            AxisAlignedBB scan = AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 2, 1).offset(xCoord, yCoord, zCoord);
            List entities = worldObj.getEntitiesWithinAABB(EntityItem.class, scan);

            // If the 'stuffed' array is null or empty, normal sieve operations continue
            if (stuffed == null || isEmpty(stuffed)) {
                if (entities != null && entities.size() > 0) {
                    EntityItem item = (EntityItem) entities.get(0);
                    if (item.getEntityItem() != null) {
                        ItemStack drop = item.getEntityItem().copy();
                        drop.stackSize = 1;

                        // Collect everything. items that can't be processed are dealt with later
                        if (TileEntityHopper.func_145889_a(this, drop, 1) == null) {
                            item.getEntityItem().stackSize--;

                            if (item.getEntityItem().stackSize <= 0) {
                                item.setDead();
                            }
                        }
                    }
                }
            }

            // If the 'stuffed' array is null or empty, normal sieve operations continue
            if (stuffed == null || isEmpty(stuffed)) {
                //eject items with no recipes all the time
                for (int i = 0; i < processing.length; i++) {
                    ItemStack inventory = processing[i];
                    if (inventory != null) {
                        SieveRecipe recipe = SubsistenceRecipes.SIEVE.get(inventory, true);
                        if (recipe == null) {
                            ItemStack drop = inventory.copy();
                            drop.stackSize = 1;

                            inventory.stackSize--;
                            if (inventory.stackSize <= 0) {
                                processing[i] = null;
                            }

                            if (dropItemStack(drop) != null) {
                                stuffed = new ItemStack[]{drop};
                            }
                        }
                    }
                }
            }

            // If the 'stuffed' array is null or empty, normal sieve operations continue
            if (stuffed == null || isEmpty(stuffed)) {
                // Set timing if need be
                if (currentProcessingTime == 0 && maxProcessingTime == 0) {
                    for (ItemStack processed : processing) {
                        if (processed != null) {
                            SieveRecipe processingRecipe = SubsistenceRecipes.SIEVE.get(processed, true);

                            if (processingRecipe != null) {
                                maxProcessingTime = processingRecipe.getDurationBlock();
                                break;
                            }
                        }
                    }
                }
            }

            // If the 'stuffed' array is null or empty, normal sieve operations continue
            if (stuffed == null || isEmpty(stuffed)) {
                // Process items
                if (currentProcessingTime == maxProcessingTime && maxProcessingTime != 0) {
                    for (int i = 0; i < processing.length; i++) {
                        ItemStack processed = processing[i];

                        if (processed != null && processed.stackSize > 0) {
                            SieveRecipe processingRecipe = SubsistenceRecipes.SIEVE.get(processed, true);
                            ItemStack[] output = processingRecipe.get(true);

                            List<ItemStack> stuffedList = Lists.newArrayList();


                            if(output != null) {
                                for (ItemStack out : output) {
                                    if(out != null) {
                                        ItemStack drop = out.copy();
                                        drop.stackSize = 1;

                                        if (dropItemStack(drop) == null) {
                                            out.stackSize--;
                                        } else {
                                            stuffedList.add(out);
                                        }
                                    }
                                }
                            }

                            processed.stackSize--;
                            if (processed.stackSize <= 0) {
                                processing[i] = null;
                            }

                            maxProcessingTime = currentProcessingTime = 0;


                            if (!stuffedList.isEmpty()) {
                                this.stuffed = stuffedList.toArray(new ItemStack[stuffedList.size()]);
                            }

                            break;
                        }
                    }
                }

                // Tick process times
                if (maxProcessingTime != 0 && currentProcessingTime < maxProcessingTime) {
                    currentProcessingTime++;
                }
            }

            if (stuffed != null && !isEmpty(stuffed)) {
                // ...we're entirely focused on dumping the stuffed items, and no other processing will take place
                for (int i = 0; i < stuffed.length; i++) {
                    ItemStack stuff = stuffed[i];
                    ItemStack drop = stuff.copy();
                    drop.stackSize = 1;

                    if (dropItemStack(drop) == null) {
                        stuff.stackSize--;
                        if (stuff.stackSize <= 0) {
                            stuffed[i] = null;
                        }
                    }
                }
            }
        }
    }

    /**
     * Attempts to drop a stack either into the inventory below, or on to the ground
     *
     * @return The resulting ItemStack, as in, whatever wouldn't be properly dropped
     */
    private ItemStack dropItemStack(ItemStack itemStack) {
        TileEntity below = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
        if (below instanceof IInventory) {
            return TileEntityHopper.func_145889_a((IInventory) below, itemStack, 1);
        } else {
            // If there's an empty space underneath the block, it's assumed we can simply drop the item, so we'll return null
            if (worldObj.isAirBlock(xCoord, yCoord - 1, zCoord)) {
                InventoryHelper.ejectItem(worldObj, xCoord, yCoord, zCoord, ForgeDirection.DOWN, itemStack, RANDOM);
                return null;
            } else {
                // Otherwise, wah wahhhhh
                return itemStack;
            }
        }
    }

    private boolean isEmpty(Object[] array) {
        if (array == null || array.length == 0) {
            return true;
        } else {
            for (Object object : array) {
                if (object != null) {
                    return false;
                }
            }
            return true;
        }
    }

    /* IINVENTORY / ISIDEDINVENTORY */
    @Override
    public int getSizeInventory() {
        return INVENTORY_SIZE;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return processing[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amt) {
        if (processing[slot] != null) {
            ItemStack itemstack;

            if (processing[slot].stackSize <= amt) {
                itemstack = processing[slot];
                processing[slot] = null;
                return itemstack;
            } else {
                itemstack = processing[slot].splitStack(amt);

                if (processing[slot].stackSize == 0) {
                    processing[slot] = null;
                }

                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if (processing[slot] != null) {
            ItemStack itemstack = processing[slot];
            processing[slot] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        processing[slot] = stack;
    }

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return side == 1 ? new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8} : new int[0];
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return side == 1 && (stuffed == null || isEmpty(stuffed));
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack var2, int side) {
        return false;
    }
}
