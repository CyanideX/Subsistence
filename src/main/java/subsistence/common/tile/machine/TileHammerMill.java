package subsistence.common.tile.machine;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import subsistence.common.block.SubsistenceBlocks;
import subsistence.common.config.CoreSettings;
import subsistence.common.network.nbt.NBTHandler;
import subsistence.common.network.packet.PacketFX;
import subsistence.common.recipe.manager.HammerMillManager;
import subsistence.common.tile.core.TileCoreMachine;
import subsistence.common.util.InventoryHelper;
import subsistence.common.util.StackHelper;

import java.util.List;
import java.util.Random;

public class TileHammerMill extends TileCoreMachine implements ISidedInventory {

    private static final int INVENTORY_SIZE = 1;
    public static final byte MAX_STAGE = 4;

    @NBTHandler.Sync(true)
    public ItemStack processing;
    @NBTHandler.Sync(true)
    public ItemStack buffer;

    @NBTHandler.Sync(true)
    public byte grindingStage;

    @NBTHandler.Sync(true)
    public float charge = 0F;

    public float angle = 0F;

    @SuppressWarnings("unchecked")
    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            // Collect items
            AxisAlignedBB scan = AxisAlignedBB.getBoundingBox(0, 1, 0, 1, 2, 1).offset(xCoord, yCoord, zCoord);
            List<EntityItem> entities = worldObj.getEntitiesWithinAABB(EntityItem.class, scan);

            if (entities != null && entities.size() > 0) {
                EntityItem item = entities.get(0);
                if (item.getEntityItem() != null) {
                    if (canInput(item.getEntityItem())) {
                        ItemStack stack = item.getEntityItem().copy();
                        stack.stackSize = 1;

                        if (TileEntityHopper.func_145889_a(this, stack, 1) == null) {
                            item.getEntityItem().stackSize--;

                            if (item.getEntityItem().stackSize <= 0) {
                                item.setDead();
                            }
                        }
                    }
                }
            }

            // Processing
            if (charge >= CoreSettings.processRate && canFunction()) {
                ItemStack output = getOutput(processing);

                if (output != null) {
                    PacketFX.breakFX(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, processing);
                    worldObj.addBlockEvent(xCoord, yCoord, zCoord, SubsistenceBlocks.hammerMill, Item.getIdFromItem(output.getItem()), output.getItemDamage());
                    IInventory below = getBelowInventory();
                    if (below != null) {
                        ItemStack result = TileEntityHopper.func_145889_a(below, output, ForgeDirection.UP.ordinal());
                        if (result != null) {
                            if (buffer == null) {
                                buffer = result;
                            } else {
                                buffer.stackSize += result.stackSize;
                            }
                        }
                    } else {
                        if (buffer == null) {
                            buffer = output;
                        } else {
                            buffer.stackSize += output.stackSize;
                        }
                    }

                    processing.stackSize--;
                    if (processing.stackSize <= 0) {
                        processing = null;
                    }

                    charge = 0;
                }
            }
        }

        // Empty buffer
        if (buffer != null) {
            ItemStack out = buffer.copy();
            IInventory inventory = getBelowInventory();
            if (inventory != null) {
                buffer = TileEntityHopper.func_145889_a(getBelowInventory(), out, ForgeDirection.UP.ordinal());
            }
        }
    }

    public void updateStage() {
        grindingStage++;
        if (grindingStage >= MAX_STAGE) {
            grindingStage = 0;
        }

        markForUpdate();
    }

    private boolean canInput(ItemStack stack) {
        return HammerMillManager.getOutput(stack, grindingStage) != null;
    }

    private ItemStack getOutput(ItemStack stack) {
        return HammerMillManager.getOutput(stack, grindingStage);
    }

    private IInventory getBelowInventory() {
        TileEntity tile = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
        if (tile != null && tile instanceof IInventory) {
            return (IInventory) tile;
        }
        return null;
    }

    private boolean canFunction() {
        return processing != null && processing.stackSize > 0 && (buffer == null || (StackHelper.areStacksSimilar(getOutput(processing), buffer, true) && buffer.stackSize < buffer.getMaxStackSize()));
    }

    @Override
    public void onBlockBroken() {
        Random random = new Random();
        if (processing != null) {
            InventoryHelper.dropItem(worldObj, xCoord, yCoord, zCoord, ForgeDirection.UNKNOWN, processing, random);
        }
        if (buffer != null) {
            InventoryHelper.dropItem(worldObj, xCoord, yCoord, zCoord, ForgeDirection.UNKNOWN, buffer, random);
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int param) {
//        for (int i = 0; i < 10; i++) {
//            EnumParticle.ITEM_BREAK(new ItemStack(Item.getItemById(id), 1, param), worldObj, xCoord, yCoord, zCoord);
//        }
        return true;
    }

    /* IINVENTORY / ISIDEDINVENTORY */
    @Override
    public int getSizeInventory() {
        return INVENTORY_SIZE;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return processing;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amt) {
        if (processing != null) {
            ItemStack itemstack;

            if (processing.stackSize <= amt) {
                itemstack = processing;
                processing = null;
                return itemstack;
            } else {
                itemstack = processing.splitStack(amt);

                if (processing.stackSize == 0) {
                    processing = null;
                }

                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if (processing != null) {
            ItemStack itemstack = processing;
            processing = null;
            return itemstack;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (processing == null)
            charge = 0;

        processing = stack;
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
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return false;
    }
}
