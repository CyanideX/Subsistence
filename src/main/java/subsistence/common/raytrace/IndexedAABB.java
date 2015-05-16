package subsistence.common.raytrace;

import net.minecraft.util.AxisAlignedBB;

/**
 * @author dmillerw
 */
public class IndexedAABB {

    public final int id;
    public final int priority;

    public final AxisAlignedBB aabb;

    public IndexedAABB(int id, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this(id, AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ));
    }

    public IndexedAABB(int id, int priority, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this(id, priority, AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ));
    }

    public IndexedAABB(int id, AxisAlignedBB aabb) {
        this(id, 0, aabb);
    }

    public IndexedAABB(int id, int priority, AxisAlignedBB aabb) {
        this.id = id;
        this.priority = priority;
        this.aabb = aabb;
    }
}
