package subsistence.common.inventory.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import subsistence.common.tile.machine.TileHellfireFurnace;

/**
 * @author dmillerw
 */
public class ContainerInfernalFurnace extends Container {

    private final TileHellfireFurnace tile;

    private int lastCookTime;
    private int lastBurnTime;
    private int lastItemBurnTime;

    public ContainerInfernalFurnace(EntityPlayer player, TileHellfireFurnace tile) {
        this.tile = tile;

        addSlotToContainer(new Slot(tile, 0, 56, 17));
        addSlotToContainer(new Slot(tile, 1, 56, 53));
        addSlotToContainer(new SlotFurnace(player, tile, 2, 116, 35));
        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
        }
    }

    public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0, tile.furnaceCookTime);
        par1ICrafting.sendProgressBarUpdate(this, 1, tile.furnaceBurnTime);
        par1ICrafting.sendProgressBarUpdate(this, 2, tile.currentItemBurnTime);
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (Object crafter : crafters) {
            ICrafting icrafting = (ICrafting) crafter;

            if (lastCookTime != tile.furnaceCookTime) {
                icrafting.sendProgressBarUpdate(this, 0, tile.furnaceCookTime);
            }

            if (lastBurnTime != tile.furnaceBurnTime) {
                icrafting.sendProgressBarUpdate(this, 1, tile.furnaceBurnTime);
            }

            if (lastItemBurnTime != tile.currentItemBurnTime) {
                icrafting.sendProgressBarUpdate(this, 2, tile.currentItemBurnTime);
            }
        }

        lastCookTime = tile.furnaceCookTime;
        lastBurnTime = tile.furnaceBurnTime;
        lastItemBurnTime = tile.currentItemBurnTime;
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        if (par1 == 0) {
            tile.furnaceCookTime = par2;
        }

        if (par1 == 1) {
            tile.furnaceBurnTime = par2;
        }

        if (par1 == 2) {
            tile.currentItemBurnTime = par2;
        }
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
        return tile.isUseableByPlayer(par1EntityPlayer);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack itemstack = null;
        Slot slot = (Slot) inventorySlots.get(par2);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 == 2) {
                if (!mergeItemStack(itemstack1, 3, 39, true)) {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (par2 != 1 && par2 != 0) {
                if (FurnaceRecipes.smelting().getSmeltingResult(itemstack1) != null) {
                    if (!mergeItemStack(itemstack1, 0, 1, false)) {
                        return null;
                    }
                } else if (TileEntityFurnace.isItemFuel(itemstack1)) {
                    if (!mergeItemStack(itemstack1, 1, 2, false)) {
                        return null;
                    }
                } else if (par2 >= 3 && par2 < 30) {
                    if (!mergeItemStack(itemstack1, 30, 39, false)) {
                        return null;
                    }
                } else if (par2 >= 30 && par2 < 39 && !mergeItemStack(itemstack1, 3, 30, false)) {
                    return null;
                }
            } else if (!mergeItemStack(itemstack1, 3, 39, false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        }

        return itemstack;
    }
}
