package subsistence.common.block.prefab;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author Royalixor
 */
public class SubsistenceWorldDecorBlock extends BlockBush {

    public SubsistenceWorldDecorBlock(Material material) {
        super(material);
    }

    public boolean isValidPosition(World world, int x, int y, int z, int meta) {
        return world.getBlock(x, y - 1, z) != Blocks.air && canPlaceBlockAt(world, x, y, z);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        Block block = world.getBlock(x, y, z);
        this.dropIfCantStay(world, x, y, z, new ItemStack(block, 1, world.getBlockMetadata(x, y, z)));
    }

    public void dropIfCantStay(World world, int x, int y, int z, ItemStack itemStack) {
        if (!this.canReplace(world, x, y, z, 0, itemStack)) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
        }
    }

    @Override
    public boolean canReplace(World world, int x, int y, int z, int side, ItemStack itemStack) {
        return world.getBlock(x, y - 1, z) != Blocks.air && isValidPosition(world, x, y, z, itemStack.getItemDamage());
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        dropIfCantStay(world, x, y, z, new ItemStack(world.getBlock(x, y, z), 1, world.getBlockMetadata(x, y, z)));
    }
}
