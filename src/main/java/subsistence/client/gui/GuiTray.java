package subsistence.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import subsistence.common.inventory.InventoryItem;
import subsistence.common.inventory.container.ContainerTray;

/**
 * @author dmillerw
 */
public class GuiTray extends GuiContainer {

    public static final ResourceLocation GUI_TRAY = new ResourceLocation("subsistence:textures/gui/tray.png");

    public GuiTray(EntityPlayer player, InventoryItem inventory) {
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
