package subsistence.common.raytrace;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

public interface IRaytracable {

    List<IndexedAABB> getTargets(World world, int x, int y, int z);

    /**
     * Not pretty, but should only be used to call the super collisionRayTrace method
     */
    MovingObjectPosition raytrace(World world, int x, int y, int z, Vec3 origin, Vec3 direction);

    // These next three methods are to be overridden from Block
    void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity);

    AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z);

    MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 origin, Vec3 direction);
}
