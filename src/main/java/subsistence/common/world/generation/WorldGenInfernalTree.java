package subsistence.common.world.generation;

import subsistence.common.block.SubsistenceBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

/**
 * @author dmillerw
 */
public class WorldGenInfernalTree extends WorldGenAbstractTree {

    private final int minTreeHeight;

    public WorldGenInfernalTree(boolean par1) {
        this(par1, 4);
    }

    public WorldGenInfernalTree(boolean par1, int par2) {
        super(par1);
        this.minTreeHeight = par2;
    }

    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
        int l = par2Random.nextInt(3) + this.minTreeHeight;

        if (par4 >= 1 && par4 + l + 1 <= 256) {
            byte b0;
            int k1;
            Block block;

            Block block2 = par1World.getBlock(par3, par4 - 1, par5);

            if (block2 != SubsistenceBlocks.netherGrass) {
                return false;
            }

            if (par4 < 256 - l - 1) {
                b0 = 3;
                byte b1 = 0;
                int l1;
                int i2;
                int j2;
                int i3;

                for (k1 = par4 - b0 + l; k1 <= par4 + l; ++k1) {
                    i3 = k1 - (par4 + l);
                    l1 = b1 + 1 - i3 / 2;

                    for (i2 = par3 - l1; i2 <= par3 + l1; ++i2) {
                        j2 = i2 - par3;

                        for (int k2 = par5 - l1; k2 <= par5 + l1; ++k2) {
                            int l2 = k2 - par5;

                            if (Math.abs(j2) != l1 || Math.abs(l2) != l1 || par2Random.nextInt(2) != 0 && i3 != 0) {
                                Block block1 = par1World.getBlock(i2, k1, k2);

                                if (block1.isAir(par1World, i2, k1, k2) || block1.isLeaves(par1World, i2, k1, k2)) {
                                    this.setBlockAndNotifyAdequately(par1World, i2, k1, k2, SubsistenceBlocks.infernalLeaves, 0);
                                }
                            }
                        }
                    }
                }

                for (k1 = par4 - b0 + l; k1 <= par4 + l; ++k1) {
                    i3 = k1 - (par4 + l);
                    l1 = b1 + 1 - i3 / 2;

                    for (i2 = par3 - l1; i2 <= par3 + l1; ++i2) {
                        j2 = i2 - par3;

                        for (int k2 = par5 - l1; k2 <= par5 + l1; ++k2) {
                            int l2 = k2 - par5;

                            if (Math.abs(j2) != l1 || Math.abs(l2) != l1 || par2Random.nextInt(2) != 0 && i3 != 0) {
                                Block block1 = par1World.getBlock(i2, k1, k2);

                                if (block1.isLeaves(par1World, i2, k1, k2)) {
                                    if (par2Random.nextInt(100) <= 60) {
                                        for (int i = 2; i < 6; i++) {
                                            ForgeDirection side = ForgeDirection.getOrientation(i);

                                            if (par1World.getBlock(i2 + side.offsetX, k1 + side.offsetY, k2 + side.offsetZ) == Blocks.air) {
                                                par1World.setBlock(i2 + side.offsetX, k1 + side.offsetY, k2 + side.offsetZ, Blocks.fire);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                for (k1 = 0; k1 < l; ++k1) {
                    block = par1World.getBlock(par3, par4 + k1, par5);

                    if (block.isAir(par1World, par3, par4 + k1, par5) || block.isLeaves(par1World, par3, par4 + k1, par5)) {
                        this.setBlockAndNotifyAdequately(par1World, par3, par4 + k1, par5, par2Random.nextInt(100) <= 5 ? SubsistenceBlocks.richInfernalLog : SubsistenceBlocks.infernalLog, 0);
                    }
                }

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
