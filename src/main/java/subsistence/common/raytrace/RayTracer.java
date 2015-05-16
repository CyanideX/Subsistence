package subsistence.common.raytrace;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dmillerw
 */
public class RayTracer {

    public static void setBlockBounds(Block block, AxisAlignedBB aabb) {
        block.setBlockBounds((float) aabb.minX, (float) aabb.minY, (float) aabb.minZ, (float) aabb.maxX, (float) aabb.maxY, (float) aabb.maxZ);
    }

    public static class RaytraceResult {

        public final int hitID;

        public final MovingObjectPosition mob;

        public final AxisAlignedBB aabb;

        public final ForgeDirection sideHit;

        public RaytraceResult(int hitID, MovingObjectPosition mob, AxisAlignedBB aabb, ForgeDirection sideHit) {
            this.hitID = hitID;
            this.mob = mob;
            this.aabb = aabb;
            this.sideHit = sideHit;
        }

        @Override
        public String toString() {
            return String.format("Raytrace Result: %s [%s] on side %s", String.valueOf(hitID), aabb.toString(), sideHit.name());
        }
    }

    public static RaytraceResult doRaytrace(World world, int x, int y, int z, EntityPlayer player) {
        double reach = 5; // Client default value...?

        if (player instanceof EntityPlayerMP) {
            reach = ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance();
        }

        double eyeHeight = world.isRemote ? player.getEyeHeight() - player.getDefaultEyeHeight() : player.getEyeHeight();
        Vec3 lookVec = player.getLookVec();
        Vec3 origin = Vec3.createVectorHelper(player.posX, player.posY + eyeHeight, player.posZ);
        Vec3 direction = origin.addVector(lookVec.xCoord * reach, lookVec.yCoord * reach, lookVec.zCoord * reach);

        return doRaytrace(world, x, y, z, origin, direction);
    }

    public static RaytraceResult doRaytrace(World world, int x, int y, int z, Vec3 origin, Vec3 direction) {
        Block block = world.getBlock(x, y, z);

        if (!(block instanceof IRaytracable)) {
            return null;
        }

        IRaytracable raytracable = (IRaytracable) block;
        List<IndexedAABB> targets = raytracable.getTargets(world, x, y, z);

        // If block doesn't provide any targets, just add one based on block bounds
        if (targets == null) {
            targets = new ArrayList<IndexedAABB>();

            block.setBlockBoundsBasedOnState(world, x, y, z);
            targets.add(new IndexedAABB(0, AxisAlignedBB.getBoundingBox(block.getBlockBoundsMinX(), block.getBlockBoundsMinY(), block.getBlockBoundsMinZ(), block.getBlockBoundsMaxX(), block.getBlockBoundsMaxY(), block.getBlockBoundsMaxZ())));
        }

        MovingObjectPosition[] hits = new MovingObjectPosition[targets.size()];
        IndexedAABB[] boxes = targets.toArray(new IndexedAABB[targets.size()]);
        //TODO Side hit

        for (int i = 0; i < boxes.length; i++) {
            IndexedAABB aabb = boxes[i];

            // Set block bounds for easier collision detection
            setBlockBounds(block, aabb.aabb);

            // Ensures we get vanilla collision detection
            hits[i] = raytracable.raytrace(world, x, y, z, origin, direction);
        }

        // Now we get the closest hit
        double minLength = Double.POSITIVE_INFINITY;

        int lastPriority = 0;
        int index = -1;

        for (int i = 0; i < hits.length; i++) {
            MovingObjectPosition hit = hits[i];

            if (hit == null) {
                continue;
            }

            double length = hit.hitVec.squareDistanceTo(origin);
            int priority = boxes[i].priority;

            if (length < minLength || priority > lastPriority) {
                minLength = length;
                lastPriority = priority;
                index = i;
            }
        }

        // Reset block bounds
        block.setBlockBounds(0, 0, 0, 1, 1, 1);

        if (index == -1) {
            return null;
        } else {
            return new RaytraceResult(boxes[index].id, hits[index], boxes[index].aabb, ForgeDirection.UNKNOWN);
        }
    }
}
