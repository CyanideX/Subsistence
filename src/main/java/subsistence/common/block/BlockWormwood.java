package subsistence.common.block;

import subsistence.common.item.SubsistenceItems;
import subsistence.common.recipe.manager.GeneralManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author lclc98
 */
public class BlockWormwood extends BlockBush implements IGrowable {
    private IIcon[] textures;

    int tickDry = 0;

    protected BlockWormwood() {
        this.setTickRandomly(true);
        this.setBlockBounds(0, 0.0F, 0, 1, 0.25F, 1);
        this.setHardness(0.0F);
        this.setStepSound(soundTypeGrass);
        this.disableStats();
    }

    @Override
    public boolean canPlaceBlockOn(Block p_149854_1_) {
        return p_149854_1_ == Blocks.grass || p_149854_1_ == Blocks.dirt || p_149854_1_ == SubsistenceBlocks.netherGrass;
    }

    @Override
    public int tickRate(World world) {
        return 1;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random p_149674_5_) {
        if (world.getWorldTime() % 10 == 0) {
            if (world.getBlockLightValue(x, y + 1, z) >= 9) {
                int l = world.getBlockMetadata(x, y, z);

                if (l < 7) {
                    float f = this.func_149864_n(world, x, y, z);

                    if (p_149674_5_.nextInt((int) (25.0F / f) + 1) == 0) {
                        ++l;
                        world.setBlockMetadataWithNotify(x, y, z, l, 2);
                    }
                }
            }
        }
        if (world.getBlockMetadata(x, y, z) > 7) {
            tickDry++;
            if (tickDry >= GeneralManager.wormwoodDry) {
                if (world.getBlockLightValue(x, y + 1, z) >= 9) {
                    int l = world.getBlockMetadata(x, y, z);

                    if (l < 9) {
                        float f = this.func_149864_n(world, x, y, z);

                        if (p_149674_5_.nextInt((int) (25.0F / f) + 1) == 0) {
                            ++l;
                            world.setBlockMetadataWithNotify(x, y, z, l, 2);
                        }
                    }
                }
            }
        }
    }

    private float func_149864_n(World world, int x, int y, int z) {
        float f = 1.0F;
        Block block = world.getBlock(x, y, z - 1);
        Block block1 = world.getBlock(x, y, z + 1);
        Block block2 = world.getBlock(x - 1, y, z);
        Block block3 = world.getBlock(x + 1, y, z);
        Block block4 = world.getBlock(x - 1, y, z - 1);
        Block block5 = world.getBlock(x + 1, y, z - 1);
        Block block6 = world.getBlock(x + 1, y, z + 1);
        Block block7 = world.getBlock(x - 1, y, z + 1);
        boolean flag = block2 == this || block3 == this;
        boolean flag1 = block == this || block1 == this;
        boolean flag2 = block4 == this || block5 == this || block6 == this || block7 == this;

        for (int l = x - 1; l <= x + 1; ++l) {
            for (int i1 = z - 1; i1 <= z + 1; ++i1) {
                float f1 = 0.0F;

                if (canPlaceBlockOn(world.getBlock(l, y - 1, i1))) {
                    f1 = 1.0F;
                }

                if (l != x || i1 != z) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        if (flag2 || flag && flag1) {
            f /= 2.0F;
        }

        return f;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta < 0 || meta > 9) {
            meta = 9;
        }

        return this.textures[meta];
    }

    @Override
    public int getRenderType() {
        return 6;
    }

    @Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int metadata, float p_149690_6_, int p_149690_7_) {
        super.dropBlockAsItemWithChance(world, x, y, z, metadata, p_149690_6_, 0);
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return null;
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }

    @Override
    public boolean func_149851_a(World world, int x, int y, int z, boolean p_149851_5_) {
        return world.getBlockMetadata(x, y, z) != 7;
    }

    @Override
    public boolean func_149852_a(World world, Random p_149852_2_, int x, int y, int z) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        this.textures = new IIcon[10];

        for (int i = 0; i < this.textures.length; i++) {

            if (i == 8)
                this.textures[i] = p_149651_1_.registerIcon("subsistence:plants/crops_7_dry_1");
            else if (i == 9)
                this.textures[i] = p_149651_1_.registerIcon("subsistence:plants/crops_7_dry_2");
            else
                this.textures[i] = p_149651_1_.registerIcon("subsistence:plants/crops_" + i);
        }
    }

    @Override
    public void func_149853_b(World world, Random random, int x, int y, int z) {
        int l = world.getBlockMetadata(x, y, z) + MathHelper.getRandomIntegerInRange(random, 2, 5);

        if (l > 7) {
            l = 7;
        }

        world.setBlockMetadataWithNotify(x, y, z, l, 2);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        if (world.rand.nextInt(20) != 0) {
            ret.add(new ItemStack(SubsistenceItems.seeds, 1, 3));
        }
        if (metadata >= 7) {
            if (world.rand.nextInt(20) != 0) {
                ret.add(new ItemStack(SubsistenceItems.component, 1, 3));
            }
            if (world.rand.nextInt(1) == 0) {
                ret.add(new ItemStack(SubsistenceItems.component, 1, 0));
            }
        }
        if (metadata >= 9) {
            ret.add(new ItemStack(SubsistenceItems.component, 1, 3));
        }

        return ret;
    }
}
