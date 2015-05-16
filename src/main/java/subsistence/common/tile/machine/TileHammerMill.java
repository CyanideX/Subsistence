package subsistence.common.tile.machine;

import subsistence.common.block.SubsistenceBlocks;
import subsistence.common.network.PacketFX;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.lib.StackReference;
import subsistence.common.lib.tool.ToolDefinition;
import subsistence.common.network.nbt.NBTHandler;
import subsistence.common.recipe.manager.GeneralManager;
import subsistence.common.tile.core.TileCoreMachine;
import subsistence.common.util.InventoryHelper;
import subsistence.common.util.StackHelper;
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

import java.util.List;
import java.util.Random;

/**
 * @author dmillerw
 */
public class TileHammerMill extends TileCoreMachine implements ISidedInventory {

    private static final int INVENTORY_SIZE = 1;

    public static final byte MAX_STAGE = 4;

    private static final Random random = new Random();

    @NBTHandler.NBTData
    public ItemStack processing;
    @NBTHandler.NBTData
    public ItemStack buffer;

    @NBTHandler.NBTData
    @NBTHandler.DescriptionData
    public byte grindingStage;

    @NBTHandler.NBTData
    public float charge = 0F;

    public float angle = 0F;

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            // Collect items
            AxisAlignedBB scan = AxisAlignedBB.getBoundingBox(0, 1, 0, 1, 2, 1).offset(xCoord, yCoord, zCoord);
            List entities = worldObj.getEntitiesWithinAABB(EntityItem.class, scan);

            if (entities != null && entities.size() > 0) {
                EntityItem item = (EntityItem) entities.get(0);
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
            if (charge >= GeneralManager.processRate && canFunction()) {
                ItemStack output = getOutput(processing);
                PacketFX.breakFX(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, processing);

                if (output != null) {
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
        return getType(stack) != -1;
    }

    private int getType(ItemStack stack) {
        int type = -1; // 0 is vanilla, 1 is nether

        if (stack == null) {
            return type;
        }

        if (
                stack.isItemEqual(StackReference.STONE) ||
                        stack.isItemEqual(StackReference.COBBLESTONE) ||
                        stack.isItemEqual(StackReference.GRAVEL) ||
                        stack.isItemEqual(StackReference.SAND) ||
                        stack.isItemEqual(StackReference.FINE_SAND)) {
            type = 0;
        } else if (
                stack.isItemEqual(StackReference.NETHER_RIND) ||
                        stack.isItemEqual(StackReference.NETHERRACK) ||
                        stack.isItemEqual(StackReference.NETHER_GRIT) ||
                        stack.isItemEqual(StackReference.SOUL_SAND) ||
                        stack.isItemEqual(StackReference.SOUL_DUST)) {
            type = 1;
        }

        return type;
    }

    //TODO Griding stage
    private ItemStack getOutput(ItemStack stack) {
        int type = getType(stack);
        ItemStack output = null;
        switch (grindingStage) {
            case 0: {
                switch (type) {
                    case 1:
                        output = StackReference.NETHERRACK.copy();
                        break;
                    default:
                        output = StackReference.COBBLESTONE.copy();
                        break;
                }
                break;
            }

            case 1: {
                switch (type) {
                    case 1:
                        output = StackReference.NETHER_GRIT.copy();
                        break;
                    default:
                        output = StackReference.GRAVEL.copy();
                        break;
                }
                break;
            }

            case 2: {
                switch (type) {
                    case 1:
                        output = StackReference.SOUL_SAND.copy();
                        break;
                    default:
                        output = StackReference.SAND.copy();
                        break;
                }
                break;
            }

            case 3: {
                switch (type) {
                    case 1:
                        output = StackReference.SOUL_DUST.copy();
                        break;
                    default:
                        output = StackReference.FINE_SAND.copy();
                        break;
                }
                break;
            }
        }
        return output;
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
        return side == 1 && SubsistenceRecipes.TABLE.get(stack, ToolDefinition.HAMMER, false) != null;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return false;
    }
}
