package subsistence.common.lib;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author dmillerw
 */
public class BlockCoordinate {

    public final int x;
    public final int y;
    public final int z;

    public BlockCoordinate(NBTTagCompound tag) {
        x = tag.getInteger("x");
        y = tag.getInteger("y");
        z = tag.getInteger("z");
    }

    public BlockCoordinate(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean isNull(World world) {
        return getBlock(world) == null;
    }

    public Block getBlock(World world) {
        return world.getBlock(x, y, z);
    }

    public int getMetadata(World world) {
        return world.getBlockMetadata(x, y, z);
    }

    public TileEntity getTileEntity(World world) {
        return world.getTileEntity(x, y, z);
    }

    public boolean isAirBlock(World world) {
        return world.isAirBlock(x, y, z);
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger("x", x);
        tag.setInteger("y", y);
        tag.setInteger("z", z);
    }
}
