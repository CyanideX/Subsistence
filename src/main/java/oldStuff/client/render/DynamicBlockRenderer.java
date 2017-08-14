package oldStuff.client.render;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public class DynamicBlockRenderer {

    private static final DynamicBlockRenderer INSTANCE = new DynamicBlockRenderer();

    private static final float LIGHT_BOTTOM = 0.5F;
    private static final float LIGHT_TOP = 1.0F;
    private static final float LIGHT_EAST_WEST = 0.8F;
    private static final float LIGHT_NORTH_SOUTH = 0.6F;

    private static int lastDimension = Integer.MIN_VALUE;

    public static DynamicBlockRenderer getInstance() {
        Minecraft mc = FMLClientHandler.instance().getClient();
        WorldClient worldClient = mc.theWorld;

        if (worldClient != null) {
            int currentDimension = worldClient.provider.dimensionId;

            if (lastDimension != currentDimension) {
                INSTANCE.renderBlocks = new RenderBlocks(worldClient);
            }
        }

        return INSTANCE;
    }

    protected RenderBlocks renderBlocks;

    private boolean initialized = false;

    private double offsetX = 0;
    private double offsetY = 0;
    private double offsetZ = 0;

    public double minX = 0F;
    public double minY = 0F;
    public double minZ = 0F;
    public double maxX = 1F;
    public double maxY = 1F;
    public double maxZ = 1F;

    private boolean tessellate = true;
    private boolean calculateLight = true;

    public void initialize(TileEntity tile) {
        if (initialized) {
            throw new IllegalStateException("Already initialized!");
        }

        offsetX = (double) tile.xCoord - TileEntityRendererDispatcher.staticPlayerX;
        offsetY = (double) tile.yCoord - TileEntityRendererDispatcher.staticPlayerY;
        offsetZ = (double) tile.zCoord - TileEntityRendererDispatcher.staticPlayerZ;

        initialized = true;
    }

    public void cleanup() {
        if (!initialized) {
            throw new IllegalStateException("Already reset!");
        }

        offsetX = 0;
        offsetY = 0;
        offsetZ = 0;

        minX = 0F;
        minY = 0F;
        minZ = 0F;
        maxX = 1F;
        maxY = 1F;
        maxZ = 1F;

        tessellate = true;
        calculateLight = true;

        initialized = false;
    }

    public DynamicBlockRenderer tessellate(boolean value) {
        this.tessellate = value;
        return this;
    }

    public DynamicBlockRenderer calculateLight(boolean value) {
        this.calculateLight = value;
        return this;
    }

    public DynamicBlockRenderer setBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        return this;
    }

    public void render(IBlockAccess world, int x, int y, int z, Block block) {
        Tessellator tessellator = Tessellator.instance;

        boolean realDoLight = calculateLight;

        if (world == null) {
            realDoLight = false;
        }

        if (tessellate) {
            tessellator.startDrawingQuads();
            tessellator.setTranslation(offsetX, offsetY, offsetZ);
        }

        float light = -1F;
        if (realDoLight) {
            light = block.getMixedBrightnessForBlock(world, x, y, z);
            tessellator.setBrightness((int) light);
            light = light + ((1.0f - light) * 0.4f);
            tessellator.setColorOpaque_F(LIGHT_BOTTOM * light, LIGHT_BOTTOM * light, LIGHT_BOTTOM * light);
        }

        renderBlocks.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);

        renderBlocks.renderFaceYNeg(block, x, y, z, block.getBlockTextureFromSide(0));

        if (realDoLight) {
            tessellator.setColorOpaque_F(LIGHT_TOP * light, LIGHT_TOP * light, LIGHT_TOP * light);
        }

        renderBlocks.renderFaceYPos(block, x, y, z, block.getBlockTextureFromSide(1));

        if (realDoLight) {
            tessellator.setColorOpaque_F(LIGHT_EAST_WEST * light, LIGHT_EAST_WEST * light, LIGHT_EAST_WEST * light);
        }

        renderBlocks.renderFaceZNeg(block, x, y, z, block.getBlockTextureFromSide(2));

        if (realDoLight) {
            tessellator.setColorOpaque_F(LIGHT_EAST_WEST * light, LIGHT_EAST_WEST * light, LIGHT_EAST_WEST * light);
        }

        renderBlocks.renderFaceZPos(block, x, y, z, block.getBlockTextureFromSide(3));

        if (realDoLight) {
            tessellator.setColorOpaque_F(LIGHT_NORTH_SOUTH * light, LIGHT_NORTH_SOUTH * light, LIGHT_NORTH_SOUTH * light);
        }

        renderBlocks.renderFaceXNeg(block, x, y, z, block.getBlockTextureFromSide(4));

        if (realDoLight) {
            tessellator.setColorOpaque_F(LIGHT_NORTH_SOUTH * light, LIGHT_NORTH_SOUTH * light, LIGHT_NORTH_SOUTH * light);
        }

        renderBlocks.renderFaceXPos(block, x, y, z, block.getBlockTextureFromSide(5));

        if (tessellate) {
            tessellator.setTranslation(0, 0, 0);
            tessellator.draw();
        }
    }
}
