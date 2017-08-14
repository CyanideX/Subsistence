package oldStuff.common.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class EntityHelper {

    public static ForgeDirection get2DRotation(EntityLivingBase entity) {
        int l = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0) {
            return ForgeDirection.getOrientation(3);
        } else if (l == 1) {
            return ForgeDirection.getOrientation(4);
        } else if (l == 2) {
            return ForgeDirection.getOrientation(2);
        } else if (l == 3) {
            return ForgeDirection.getOrientation(5);
        } else {
            return ForgeDirection.UNKNOWN;
        }
    }

    public static void copy(EntityLivingBase in, Class<? extends EntityLivingBase> outClass) {
        try {
            EntityLivingBase out = outClass.getConstructor(World.class).newInstance(in.worldObj);
            out.posX = in.posX;
            out.posY = in.posY;
            out.posZ = in.posZ;
            out.rotationYaw = in.rotationYaw;
            out.rotationYawHead = in.rotationYawHead;

            in.setDead();
            out.worldObj.spawnEntityInWorld(out);
        } catch (Exception ignored) {

        }
    }
}
