package com.subsistence.client.gui;

import com.subsistence.common.inventory.InventoryItem;
import com.subsistence.common.inventory.container.ContainerTray;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * @author dmillerw
 */
public class GuiHandSieve extends GuiContainer {

    public static final ResourceLocation GUI_TRAY = new ResourceLocation("subsistence:textures/gui/single_slot.png");

    public GuiHandSieve(EntityPlayer player, InventoryItem inventory) {
        super(new ContainerTray(player, inventory));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GUI_TRAY);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
}
