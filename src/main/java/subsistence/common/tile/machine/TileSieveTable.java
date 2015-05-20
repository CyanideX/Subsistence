package subsistence.common.tile.machine;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import subsistence.common.network.nbt.NBTHandler;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.recipe.wrapper.SieveRecipe;
import subsistence.common.tile.core.TileCore;
import subsistence.common.util.InventoryHelper;

import java.util.List;
import java.util.Random;

/**
 * @author Royalixor
 */
public class TileSieveTable extends TileCore implements ISidedInventory {

    private static final int INVENTORY_SIZE = 9;

    @NBTHandler.Sync(true)
    public ItemStack[] processing = new ItemStack[INVENTORY_SIZE];

    @NBTHandler.Sync(true)
    public int maxProcessingTime = 0;
    @NBTHandler.Sync(true)
    public int currentProcessingTime = 0;

    @Override
    public void onBlockBroken() {
        Random random = new Random();
        for (ItemStack stack : processing) {
            if (stack != null) {
                InventoryHelper.dropItem(worldObj, xCoord, yCoord, zCoord, ForgeDirection.UNKNOWN, stack, random);
            }
        }
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            // Collect items
            AxisAlignedBB scan = AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 2, 1).offset(xCoord, yCoord, zCoord);
            List entities = worldObj.getEntitiesWithinAABB(EntityItem.class, scan);

            if (entities != null && entities.size() > 0) {
                EntityItem item = (EntityItem) entities.get(0);
                if (item.getEntityItem() != null) {
                    ItemStack drop = item.getEntityItem().copy();
                    drop.stackSize = 1;

                    SieveRecipe recipe = SubsistenceRecipes.SIEVE.get(item.getEntityItem());

                    if (recipe != null) {
                        if (TileEntityHopper.func_145889_a(this, drop, 1) == null) {
                            item.getEntityItem().stackSize--;

                            if (item.getEntityItem().stackSize <= 0) {
                                item.setDead();
                            }
                        }
                    } else {
                        Random random = new Random();
                        if (worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof IInventory && TileEntityHopper.func_145889_a( (IInventory) worldObj.getTileEntity(xCoord,yCoord-1,zCoord), drop, 1) == null) {
                            System.out.println("item inserted, not in recipe, ejecting through bottom");
                        } else {
                            InventoryHelper.ejectItem(worldObj, xCoord, yCoord, zCoord, ForgeDirection.DOWN, drop, random);
                            System.out.println("item inserted, not in recipe, dropping");
                        }
                        item.getEntityItem().stackSize--;
                        if (item.getEntityItem().stackSize <= 0) {
                            item.setDead();
                        }
                    }
                }
            }

            //eject items with no recipes all the time
            for (int i = 0; i < processing.length; i++) {
                ItemStack inventory = processing[i];
                if (inventory != null) {
                    SieveRecipe recipe = SubsistenceRecipes.SIEVE.get(inventory);
                    if (recipe == null) {
                        Random random = new Random();
                        ItemStack drop = inventory.copy();
                        drop.stackSize = 1;
                        if (worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof IInventory && TileEntityHopper.func_145889_a((IInventory) worldObj.getTileEntity(xCoord, yCoord - 1, zCoord), drop, 1) == null) {
                            System.out.println("item not in a recipe, eject out bottom");
                        } else {
                            InventoryHelper.ejectItem(worldObj, xCoord, yCoord, zCoord, ForgeDirection.DOWN, drop, random);
                            System.out.println("item not in a recipe, dropping");
                        }
                        inventory.stackSize--;
                        if (inventory.stackSize <= 0) {
                            processing[i] = null;
                        }
                    }
                }
            }

            // Set timing if need be
            if (currentProcessingTime == 0 && maxProcessingTime == 0) {
                for (ItemStack processed : processing) {
                    if (processed != null) {
                        SieveRecipe processingRecipe = SubsistenceRecipes.SIEVE.get(processed);

                        if (processingRecipe != null) {
                            maxProcessingTime = processingRecipe.getDurationBlock();
                            break;
                        }
                    }
                }
            }

            // Process items
            if (currentProcessingTime == maxProcessingTime && maxProcessingTime != 0) {
                for (int i = 0; i < processing.length; i++) {
                    ItemStack processed = processing[i];

                    if (processed != null && processed.stackSize > 0) {
                        SieveRecipe processingRecipe = SubsistenceRecipes.SIEVE.get(processed);
                        ItemStack[] output = processingRecipe.get(processed, true);
                        Random random = new Random();

                        for (ItemStack out : output) {
                            ItemStack drop = out.copy();
                            drop.stackSize = 1;
                            if (worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof IInventory && TileEntityHopper.func_145889_a( (IInventory) worldObj.getTileEntity(xCoord,yCoord-1,zCoord), drop, 1) == null) {
                                out.stackSize--;
                                System.out.println("recipe finished, ejecting below");
                            } else {
                                InventoryHelper.ejectItem(worldObj, xCoord, yCoord, zCoord, ForgeDirection.DOWN, drop, random);
                                out.stackSize--;
                                System.out.println("recipe finished, dropping");
                             }
                        }

                        processed.stackSize--;
                        if (processed.stackSize <= 0) {
                            processing[i] = null;
                        }

                        maxProcessingTime = currentProcessingTime = 0;

                        break;
                    }
                }
            }

            // Tick process times
            if (maxProcessingTime != 0 && currentProcessingTime < maxProcessingTime) {
                currentProcessingTime++;
            }
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
        return side == 1;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack var2, int side) {
        return false;
    }
}
