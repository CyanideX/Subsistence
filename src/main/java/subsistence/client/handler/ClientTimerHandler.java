package subsistence.client.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

/**
 * @author dmillerw
 */
public class ClientTimerHandler {

    public static final ClientTimerHandler INSTANCE = new ClientTimerHandler();

    private final ResourceLocation ICONS = new ResourceLocation("textures/gui/icons.png");

    public String tag;
    public int ticks;
    public int duration;

    public void update(String tag, int ticks, int duration) {
        this.tag = tag;
        this.ticks = ticks;
        this.duration = duration;
    }

    public void stop() {
        tag = null;
        ticks = 0;
        duration = 0;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;

        ticks++;

        if (ticks >= duration + 1)
            stop();
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;

        if (tag == null || tag.isEmpty())
            return;

        final Minecraft mc = Minecraft.getMinecraft();
        final ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();

        final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        fontRenderer.drawStringWithShadow(tag, width / 2 - (fontRenderer.getStringWidth(tag) / 2), height / 2 + 10, 0xFFFFFF);

        Minecraft.getMinecraft().getTextureManager().bindTexture(ICONS);

        final int barWidth = 102;
        drawTexturedModalRect(width / 2 - (barWidth / 2), height / 2 + 25, 0, 64, barWidth, 5);
        drawTexturedModalRect(width / 2 - (barWidth / 2), height / 2 + 25, 0, 69, (int) (((float)ticks / (float)duration) * barWidth), 5);
    }

    public void drawTexturedModalRect(int p_73729_1_, int p_73729_2_, int p_73729_3_, int p_73729_4_, int p_73729_5_, int p_73729_6_) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double) (p_73729_1_ + 0), (double) (p_73729_2_ + p_73729_6_), (double) 0, (double) ((float) (p_73729_3_ + 0) * f), (double) ((float) (p_73729_4_ + p_73729_6_) * f1));
        tessellator.addVertexWithUV((double) (p_73729_1_ + p_73729_5_), (double) (p_73729_2_ + p_73729_6_), (double) 0, (double) ((float) (p_73729_3_ + p_73729_5_) * f), (double) ((float) (p_73729_4_ + p_73729_6_) * f1));
        tessellator.addVertexWithUV((double) (p_73729_1_ + p_73729_5_), (double) (p_73729_2_ + 0), (double) 0, (double) ((float) (p_73729_3_ + p_73729_5_) * f), (double) ((float) (p_73729_4_ + 0) * f1));
        tessellator.addVertexWithUV((double) (p_73729_1_ + 0), (double) (p_73729_2_ + 0), (double) 0, (double) ((float) (p_73729_3_ + 0) * f), (double) ((float) (p_73729_4_ + 0) * f1));
        tessellator.draw();
    }
}
