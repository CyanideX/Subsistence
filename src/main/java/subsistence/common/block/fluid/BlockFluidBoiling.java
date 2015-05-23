package subsistence.common.block.fluid;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import subsistence.Subsistence;
import subsistence.common.block.SubsistenceBlocks;
import subsistence.common.block.prefab.SubsistenceBasicFluid;
import subsistence.common.fluid.SubsistenceFluids;
import subsistence.common.lib.client.EnumParticle;
import subsistence.common.particle.SteamFX;
import subsistence.common.util.SubsistenceDamageSource;

import java.util.Random;


public class BlockFluidBoiling extends SubsistenceBasicFluid {

    private static final Material water_boiling = (new MaterialLiquid(MapColor.waterColor));

    EnumParticle particleBubble;

    public BlockFluidBoiling(Fluid fluid) {
        super(fluid, BlockFluidBoiling.water_boiling);
        setQuantaPerBlock(4);
        particleBubble = EnumParticle.BUBBLE;
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        stillIcon = register.registerIcon(Subsistence.RESOURCE_PREFIX + "fluid/boilingWater_still");
        flowingIcon = register.registerIcon(Subsistence.RESOURCE_PREFIX + "fluid/boilingWater_flow");
        SubsistenceFluids.boilingWaterFluid.setFlowingIcon(flowingIcon);
        SubsistenceFluids.boilingWaterFluid.setStillIcon(stillIcon);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        super.randomDisplayTick(world, x, y, z, rand);
        particleBubble.display(world, x + rand.nextDouble(), y, z + rand.nextDouble(), 0, 0.5, 0);
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(new SteamFX(world, x + rand.nextDouble(), y + 1, z + rand.nextDouble(), rand.nextInt(2), 0));
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (entity.ticksExisted % 20 == 0) {
            entity.attackEntityFrom(SubsistenceDamageSource.boilingWater, 1);
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (!world.isRemote) {
            super.updateTick(world, x, y, z, rand);

            final int range = 3;
            if (!this.isSourceBlock(world, x, y, z) && !checkSurroundingBlocksForSource(world, x, y, z, range)) {

                Vec3 b = getSourceBlock(world, x, y, z);
                if (b != null) {
                    world.setBlockToAir((int) b.xCoord, (int) b.yCoord, (int) b.zCoord);
                    double randSound = rand.nextDouble();
                    if (randSound > 0.8D) {
                        world.playSoundEffect((int) b.xCoord, (int) b.yCoord, (int) b.zCoord, "random.fizz", 0.05f, 0.0f);
                    }
                }
            }
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        tryTransform(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        super.onNeighborBlockChange(world, x, y, z, block);
        tryTransform(world, x, y, z);
    }

    private void tryTransform(World world, int x, int y, int z) {
        if (world.getBlock(x, y, z) == this) {
            boolean flag = false;

            if (flag || world.getBlock(x, y, z - 1).getMaterial() == Material.lava) {
                flag = true;
            }

            if (flag || world.getBlock(x, y, z + 1).getMaterial() == Material.lava) {
                flag = true;
            }

            if (flag || world.getBlock(x - 1, y, z).getMaterial() == Material.lava) {
                flag = true;
            }

            if (flag || world.getBlock(x + 1, y, z).getMaterial() == Material.lava) {
                flag = true;
            }

            if (flag || world.getBlock(x, y + 1, z).getMaterial() == Material.lava) {
                flag = true;
            }

            if (flag) {
                if (world.provider.dimensionId == -1) {
                    world.setBlock(x, y, z, SubsistenceBlocks.componentGround, 2, 2);
                } else {
                    int metadata = world.getBlockMetadata(x, y, z);

                    if (metadata == 0) {
                        world.setBlock(x, y, z, Blocks.obsidian);
                    } else if (metadata <= 4) {
                        world.setBlock(x, y, z, Blocks.cobblestone);
                    }
                }

                world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
                for (int l = 0; l < 8; ++l) {
                    world.spawnParticle("largesmoke", (double) x + Math.random(), (double) y + 1.2D, (double) z + Math.random(), 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }
}
