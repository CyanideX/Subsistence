package subsistence.common.tile.machine;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;
import subsistence.common.block.SubsistenceBlocks;
import subsistence.common.network.nbt.NBTHandler;
import subsistence.common.tile.core.TileCoreMachine;
import subsistence.common.util.SubsistenceReflectionHelper;

public class TileWaterMill extends TileCoreMachine {

    public static final float MAX_SPEED = 10F;

    private float lastSpeed = 0F;

    @NBTHandler.Sync(true)
    public float speed = 0F;

    public float clientSpeed = 0F;

    public float angle = 0F;

    private float sources;

    private boolean spin;

    @Override
    public void updateEntity() {
        // Roughly two times per second
        if (!worldObj.isRemote && worldObj.getTotalWorldTime() % 10 == 0) {
            updateSpeed();
        } else if (worldObj.isRemote) {
            float accel = 0.05f;
            if (clientSpeed > speed) {
                clientSpeed -= accel;
                clientSpeed = Math.max(clientSpeed, speed);
            } else if (clientSpeed < speed) {
                clientSpeed += accel;
                clientSpeed = Math.min(clientSpeed, speed);
            }

            angle += clientSpeed;
            if (angle > 360F) {
                angle = angle - 360F;
            }

            TileEntity crank = worldObj.getTileEntity(xCoord + orientation.offsetX, yCoord, zCoord + orientation.offsetZ);

            if (crank != null && crank instanceof TileKineticCrank) {
                ((TileKineticCrank) crank).angle = angle;
            }
        }
    }

    public void updateSpeed() {
        sources = 0;
        // Basic for now, may change later
        // Basically, we check for the number of FLOWING water blocks in the 3x3 PERIMATER of the mill
        // and use that to determine the current speed

        TileKineticCrank crank = (TileKineticCrank) worldObj.getTileEntity(xCoord + orientation.offsetX, yCoord, zCoord + orientation.offsetZ);
        ForgeDirection right = orientation.getRotation(ForgeDirection.UP).getOpposite();
        boolean xAxis = right.offsetX != 0;
        spin = true;
        int count = 0;
        crank.stopTick = false;
        for (int ix = -1; ix < 2; ix++) {
            for (int iy = -1; iy < 2; iy++) {
                for (int iz = -1; iz < 2; iz++) {
                    int sx = xAxis ? xCoord + ix : xCoord;
                    int sy = yCoord + iy;
                    int sz = xAxis ? zCoord : zCoord + iz;

                    if (sy != 0 && (xAxis ? (iz == -1) : (ix == -1))) {
                        Block block = worldObj.getBlock(sx, sy, sz);

                        if (block.getMaterial() == Material.water) {
                            // Is the block flowing on the ground?
                            Block below = worldObj.getBlock(sx, sy - 1, sz);

                            if (!below.isAir(worldObj, sx, sy, sz) && block.getMaterial() != Material.water) {
                                // It's flowing on something solid
                                spin = false;
                                if (count > 0) {
                                    count--;
                                } else if (count < 0)
                                    count++;
                            } else {
                                if (spin) {
                                    Vec3 vec = SubsistenceReflectionHelper.getFlowVector(worldObj, sx, sy, sz);
                                    int flow = SubsistenceReflectionHelper.getEffectiveFlowDecay(worldObj, sx, sy, sz);
                                    count += getDir(right, vec, flow, Vec3.createVectorHelper(sx, sy, sz));
                                }
                            }
                        } else if (block != null && block != SubsistenceBlocks.waterMill && !block.isAir(worldObj, sx, sy, sz)) {
                            crank.stopTick = true;
                            count = 0;
                            break;
                        }
                    }
                }
            }
        }

        if (sources > 0)
            speed = ((float) count / (sources * 8));
        else speed = 0;
        
        speed = Math.max(0, Math.min(MAX_SPEED, speed));
        
        crank.speed = speed;

        if (lastSpeed != speed) {
            if (lastSpeed > 0 && speed <= 0)
                crank.stopTick = true;

            lastSpeed = speed;

            markForUpdate();
        }
    }

    public double getDir(ForgeDirection dir, Vec3 vec, int flow, Vec3 block) {

        // handles water rotation and returns a speed

        // which direction is water coming from
        if (vec.xCoord != 0) {
            sources++;
            if ((dir.offsetX < 0 && vec.xCoord > 0) || (dir.offsetX > 0 && vec.xCoord < 0))
                return flow - 7;
            else
                return 7 - flow;
        } else if (vec.zCoord != 0) {
            sources++;
            if ((dir.offsetZ < 0 && vec.zCoord > 0) || (dir.offsetZ > 0 && vec.zCoord < 0))
                return flow - 7;
            else
                return 7 - flow;
        } else if (vec.yCoord == -1) {
            sources = 1;
            if (block.xCoord < xCoord)
                return 10;
            else if (block.xCoord > xCoord)
                return -10;
            else if (block.zCoord < zCoord)
                return 10;
            else if (block.zCoord > zCoord)
                return -10;
        }
        return 0;
    }
}
