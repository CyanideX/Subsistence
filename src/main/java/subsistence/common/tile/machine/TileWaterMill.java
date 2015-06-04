package subsistence.common.tile.machine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidBase;
import subsistence.common.block.SubsistenceBlocks;
import subsistence.common.network.nbt.NBTHandler;
import subsistence.common.tile.core.TileCoreMachine;
import subsistence.common.util.SubsistenceReflectionHelper;

public class TileWaterMill extends TileCoreMachine {

    public static final float MAX_SPEED = 11F;

    private float lastSpeed = 0F;

    @NBTHandler.Sync(true)
    public float speed = 0F;

    public float clientSpeed = 0F;

    public float angle = 0F;

    private float sources;

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

                        if (block instanceof BlockLiquid || block instanceof BlockFluidBase) {
                            Vec3 vec;
                            int flow = 0, maxFlow = 8;
                            if (block instanceof BlockLiquid) {
                                vec = SubsistenceReflectionHelper.getFlowVector(worldObj, sx, sy, sz);
                                flow = SubsistenceReflectionHelper.getEffectiveFlowDecay(worldObj, sx, sy, sz);
                            } else {
                                BlockFluidBase fluid = (BlockFluidBase) block;
                                vec = fluid.getFlowVector(worldObj, sx, sy, sz);
                                maxFlow = SubsistenceReflectionHelper.getQuantaPerBlock(fluid);
                                flow = maxFlow - fluid.getQuantaValue(worldObj, sx, sy, sz);
                            }
                            count += getDir(right, vec, flow, maxFlow - 1, Vec3.createVectorHelper(sx, sy, sz));
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

    public double getDir(ForgeDirection dir, Vec3 vec, float flow, float maxFlow, Vec3 block) {

        // get rid of nonsense values
        flow = Math.max(0, flow);
        
        double flowSpeed = ((maxFlow - flow) / maxFlow) * 10;

        // handles water rotation and returns a speed

        // which direction is water coming from
        if (vec.xCoord != 0) {
            sources++;
            if ((dir.offsetX < 0 && vec.xCoord > 0) || (dir.offsetX > 0 && vec.xCoord < 0))
                return -flowSpeed;
            else
                return flowSpeed;
        } else if (vec.zCoord != 0) {
            sources++;
            if ((dir.offsetZ < 0 && vec.zCoord > 0) || (dir.offsetZ > 0 && vec.zCoord < 0))
                return -flowSpeed;
            else
                return flowSpeed;
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
