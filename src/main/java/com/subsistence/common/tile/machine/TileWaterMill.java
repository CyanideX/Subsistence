package com.subsistence.common.tile.machine;

import com.subsistence.common.block.SubsistenceBlocks;
import com.subsistence.common.network.nbt.NBTHandler;
import com.subsistence.common.tile.core.TileCoreMachine;
import com.subsistence.common.util.SubsistenceReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @author dmillerw
 */
public class TileWaterMill extends TileCoreMachine {

    public static final float MAX_SPEED = 10F;

    private float lastSpeed = 0F;

    @NBTHandler.NBTData
    @NBTHandler.DescriptionData
    public float speed = 0F;

    @SideOnly(Side.CLIENT)
    private float clientSpeed = 0F;

    @SideOnly(Side.CLIENT)
    public float angle = 0F;

    @Override
    public void updateEntity() {
        // Roughly two times per second
        if (!worldObj.isRemote && worldObj.getTotalWorldTime() % 10 == 0) {
            updateSpeed();
        } else if (worldObj.isRemote) {
            if (clientSpeed > speed) {
                clientSpeed--;
            } else if (clientSpeed < speed) {
                clientSpeed++;
            }

            angle += clientSpeed;
            if (angle > 360F) {
                angle = angle - 360F;
            }

            TileKineticCrank crank = (TileKineticCrank) worldObj.getTileEntity(xCoord + orientation.offsetX, yCoord, zCoord + orientation.offsetZ);

            if (crank != null) {
//                if (crank.orientation != orientation) {
//                    crank.orientation = orientation;
//                    crank.markForUpdate();
//                }

                crank.angle = angle;
            }
        }
    }

    public void updateSpeed() {
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
                        int meta = worldObj.getBlockMetadata(sx, sy, sz);

                        if (block.getMaterial() == Material.water) {
                            // Is the block flowing on the ground?
                            Block below = worldObj.getBlock(sx, sy - 1, sz);

                            if (!below.isAir(worldObj, sx, sy, sz) && block.getMaterial() != Material.water) {
                                // It's flowing on something solid
                                count++;
                            } else {
                                Vec3 vec = SubsistenceReflectionHelper.getFlowVector(worldObj, sx, sy, sz);
                                System.out.println(vec.xCoord + ":" + vec.yCoord + ":" + vec.zCoord);
                                count += getDir(right, vec);
                            }
                        } else {
                            if (block != null && block != SubsistenceBlocks.waterMill && !block.isAir(worldObj, sx, sy, sz)) {
                                crank.stopTick = true;
                                count = 0;
                                break;
                            }
                        }
                    }
                }
            }
        }

        speed = MAX_SPEED * ((float) count / (float) 8);
        crank.speed = speed;

        if (lastSpeed != speed) {
            if (lastSpeed > 0 && speed <= 0) {
                crank.stopTick = true;
            }
            lastSpeed = speed;

            markForUpdate();
        }
    }

    public double getDir(ForgeDirection dir, Vec3 vec) {
        if (vec.xCoord != 0) {
            if (dir.offsetX < 0 && vec.xCoord > 0) {
                return -2;
            }
            if (dir.offsetX > 0 && vec.xCoord < 0) {
                return -2;
            }
            return 2;
        }
        if (vec.zCoord != 0) {
            if (dir.offsetZ < 0 && vec.zCoord > 0) {
                return -2;
            }
            if (dir.offsetZ > 0 && vec.zCoord < 0) {
                return -2;
            }
            return 2;
        }

        if (vec.yCoord != 0) {
            return 3;
        }
        return 0;
    }
}
