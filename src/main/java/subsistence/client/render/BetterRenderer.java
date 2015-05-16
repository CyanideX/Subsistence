package subsistence.client.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

/**
 * @author Royalixor
 */
public abstract class BetterRenderer implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        renderInventoryBlock(renderer, block, metadata);
        renderer.clearOverrideBlockTexture();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        renderWorldBlock(world, x, y, z, block, renderer);
        renderer.clearOverrideBlockTexture();
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    public void setCubeBounds(RenderBlocks renderer, float x, float y, float z, float w, float h) {
        renderer.setRenderBounds(x, y, z, x + w, y + h, z + w);
    }

    @Override
    public abstract int getRenderId();

    public abstract void renderInventoryBlock(RenderBlocks renderer, Block block, int meta);

    public abstract void renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, RenderBlocks renderer);
}
