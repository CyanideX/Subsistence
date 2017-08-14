package oldStuff.common.core.handler;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import oldStuff.client.gui.GuiHandSieve;
import oldStuff.client.gui.GuiHellfireFurnace;
import oldStuff.client.gui.GuiTray;
import oldStuff.common.inventory.InventoryItem;
import oldStuff.common.inventory.container.ContainerHandSieve;
import oldStuff.common.inventory.container.ContainerInfernalFurnace;
import oldStuff.common.inventory.container.ContainerTray;
import oldStuff.common.tile.machine.TileHellfireFurnace;

public class GuiHandler implements IGuiHandler {

    public static final int GUI_TRAY = 0;
    public static final int GUI_HAND_SIEVE = 1;
    public static final int GUI_INFERNAL_FURNACE = 2;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case GUI_TRAY:
                return new ContainerTray(player, new InventoryItem(player.getHeldItem(), 1));

            case GUI_HAND_SIEVE:
                return new ContainerHandSieve(player, new InventoryItem(player.getHeldItem(), 1));

            case GUI_INFERNAL_FURNACE:
                return new ContainerInfernalFurnace(player, (TileHellfireFurnace) world.getTileEntity(x, y, z));
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case GUI_TRAY:
                return new GuiTray(player, new InventoryItem(player.getHeldItem(), 1));

            case GUI_HAND_SIEVE:
                return new GuiHandSieve(player, new InventoryItem(player.getHeldItem(), 1));

            case GUI_INFERNAL_FURNACE:
                return new GuiHellfireFurnace(player, (TileHellfireFurnace) world.getTileEntity(x, y, z));
        }

        return null;
    }
}
