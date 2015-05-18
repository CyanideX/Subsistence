package subsistence.common.block.fluid;

import subsistence.Subsistence;
import subsistence.common.block.prefab.SubsistenceBasicFluid;
import subsistence.common.fluid.SubsistenceFluids;
import subsistence.common.lib.client.EnumParticle;
import subsistence.common.particle.SteamFX;
import subsistence.common.util.SubsistenceDamageSource;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

import java.util.Random;

/**
 * Created by Thlayli
 */
public class BlockFluidBoiling extends SubsistenceBasicFluid {

    EnumParticle particleBubble;

    public BlockFluidBoiling(Fluid fluid) {
        super(fluid, Material.water);
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
}
