package com.subsistence.common.inventory.container;

import com.subsistence.common.item.ItemHandSieve;
import com.subsistence.common.inventory.InventoryItem;
import com.subsistence.common.recipe.SubsistenceRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author dmillerw
 */
public class ContainerHandSieve extends ContainerItem {

    private EntityPlayer player;

    public ContainerHandSieve(EntityPlayer player, InventoryItem inventory) {
        super(inventory, player.inventory.currentItem + 27 + inventory.getSizeInventory());

        this.player = player;

        // Init slots
        addSlotToContainer(new Slot(inventory, 0, 80, 35) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return SubsistenceRecipes.SIEVE.get(stack) != null;
            }
        });

        // Init player slots
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        ItemHandSieve.recalculate(inventory.getStack(), player);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
