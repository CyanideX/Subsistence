package subsistence.common.block.example;

import subsistence.common.raytrace.RayTracer;
import subsistence.common.raytrace.IRaytracable;
import subsistence.common.raytrace.IndexedAABB;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dmillerw
 *         <p/>
 *         NOT TO BE USED IN GAME! SIMPLY FOR THE PURPOSES OF DEMONSTRATION
 */
public class BlockRaytraceExample extends Block implements IRaytracable {

    public BlockRaytraceExample() {
        super(Material.rock);

        setCreativeTab(CreativeTabs.tabBrewing);
    }

    @Override
    public List<IndexedAABB> getTargets(World world, int x, int y, int z) {
        // List of all possible targets for the raytracer to choose from
        List<IndexedAABB> targets = new ArrayList<IndexedAABB>();

        // For this example, we split the block into two halves, top and bottom
        // Each target gets assigned a unique ID, which can later be used to determine
        // which part was hit
        targets.add(new IndexedAABB(0, AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 0.5, 1)));
        targets.add(new IndexedAABB(1, AxisAlignedBB.getBoundingBox(0, 0.5, 0, 1, 1, 1)));
        targets.add(new IndexedAABB(2, AxisAlignedBB.getBoundingBox(0, 1, 0, 1, 1.5, 1)));

        return targets;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float fx, float fy, float fz) {
        if (!world.isRemote && !player.isSneaking()) {
            RayTracer.RaytraceResult result = RayTracer.doRaytrace(world, x, y, z, player);
            player.addChatComponentMessage(result != null ? new ChatComponentText("Hit Part " + result.hitID) : new ChatComponentText("No part hit"));
            return true;
        }

        return !player.isSneaking();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity) {
        // For this example, just sets the bounds to full. This can change
        setBlockBounds(0, 0, 0, 1, 1, 1);
        super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
    }

    @SideOnly(Side.CLIENT)
    @Override
    /**
     * Used to return the selection bounding box
     */
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        RayTracer.RaytraceResult result = RayTracer.doRaytrace(world, x, y, z, Minecraft.getMinecraft().thePlayer);

        if (result != null && result.aabb != null) {
            // Returns the resulting bounding box, offset to match the block coordinates
            return result.aabb.offset(x, y, z);
        } else {
            return super.getSelectedBoundingBoxFromPool(world, x, y, z);
        }
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 origin, Vec3 direction) {
        RayTracer.RaytraceResult result = RayTracer.doRaytrace(world, x, y, z, origin, direction);

        if (result == null) {
            return null;
        } else {
            return result.mob;
        }
    }

    @Override
    public MovingObjectPosition raytrace(World world, int x, int y, int z, Vec3 origin, Vec3 direction) {
        return super.collisionRayTrace(world, x, y, z, origin, direction);
    }
}
