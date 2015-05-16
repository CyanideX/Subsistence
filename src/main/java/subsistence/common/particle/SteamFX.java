package subsistence.common.particle;

import subsistence.common.particle.core.ParticleCore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.World;


/**
 * Created by Thlayli
 */

@SideOnly(Side.CLIENT)
public class SteamFX extends ParticleCore {

    public SteamFX(World par1World, double par2, double par4, double par6, int indexX, int indexY) {
        super(par1World, par2, par4, par6, indexX, indexY);
        this.motionY = 0.1f;
        this.particleAlpha = 0.2f;
        multipleParticleScaleBy(rand.nextFloat() + 0.5f);
    }
}
