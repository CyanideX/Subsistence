package subsistence.common.block;

import subsistence.Subsistence;
import subsistence.common.block.prefab.SubsistenceBasicBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author dmillerw
 */
public class BlockNetherGrass extends SubsistenceBasicBlock {

    private IIcon side;
    private IIcon top;

    public BlockNetherGrass() {
        super(Material.sand);

        setTickRandomly(true);
        setHardness(0.6F);
        setStepSound(soundTypeGrass);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.isRemote) {
            if (world.provider.dimensionId != -1) {
                if (world.isAirBlock(x, y + 1, z)) {
                    world.setBlock(x, y + 1, z, Blocks.fire);
                }
                world.setBlock(x, y, z, Blocks.soul_sand);
            } else {
                if (world.getBlockLightOpacity(x, y + 1, z) > 2) {
                    world.setBlock(x, y, z, Blocks.soul_sand);
                } else if (world.getBlockLightValue(x, y + 1, z) <= 9) {
                    for (int l = 0; l < 4; ++l) {
                        int i1 = x + random.nextInt(3) - 1;
                        int j1 = y + random.nextInt(5) - 3;
                        int k1 = z + random.nextInt(3) - 1;

                        Block block = world.getBlock(i1, j1 + 1, k1);
                        int meta = world.getBlockMetadata(i1, j1 + 1, k1);

                        if (block == Blocks.soul_sand && meta == 0 && world.getBlockLightValue(i1, j1 + 1, k1) >= 4 && world.getBlockLightOpacity(i1, j1 + 1, k1) <= 2) {
                            world.setBlock(i1, j1, k1, this);
                        }
                    }
                }
            }
        }
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return Item.getItemFromBlock(Blocks.soul_sand);
    }

    @Override
    public boolean useCustomRender() {
        return false;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return side == 1 ? top : this.side;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        side = iconRegister.registerIcon(Subsistence.RESOURCE_PREFIX + "world/netherGrass");
        top = iconRegister.registerIcon(Subsistence.RESOURCE_PREFIX + "world/netherGrass_top");
    }
}
