package subsistence.common.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import subsistence.common.inventory.InventoryItem;

/**
 * @author dmillerw
 */
public class ContainerTray extends ContainerItem {

    private EntityPlayer player;

    public ContainerTray(EntityPlayer player, InventoryItem inventory) {
        super(inventory, player.inventory.currentItem + 27 + inventory.getSizeInventory());

        this.player = player;

        // Init slots
        addSlotToContainer(new Slot(inventory, 0, 80, 35));

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
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
