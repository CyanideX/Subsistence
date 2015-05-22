package subsistence.common.tile.machine;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import subsistence.common.config.CoreSettings;
import subsistence.common.tile.core.TileCoreMachine;

import java.util.ArrayList;
import java.util.List;

public class TileKineticCrank extends TileCoreMachine {

    private static final int MAX_LENGTH = 8;

    public float angle = 0F;

    public float speed = 0F;

    public boolean stopTick = false;

    private List<TileEntity> connectedTilesCache;

    @Override
    public void updateEntity() {
        if (connectedTilesCache == null) {
            connectedTilesCache = getConnectedTiles();
        } else {
            if (worldObj.getTotalWorldTime() % 20 == 0) {
                connectedTilesCache.clear();
                connectedTilesCache.addAll(getConnectedTiles());
            }
        }

        if (!worldObj.isRemote) {
            for (TileEntity tile : connectedTilesCache) {
                if (tile != null && tile instanceof TileHammerMill && (MAX_LENGTH - speed) != 0) {
                    ((TileHammerMill) tile).charge += CoreSettings.STATIC.waterMill;
                }
            }
        } else {
            for (TileEntity tile : connectedTilesCache) {
                if (tile != null) {
                    if (tile instanceof TileHammerMill) {
                        if (((TileHammerMill) tile).orientation.getRotation(ForgeDirection.UP) != this.orientation)
                            ((TileHammerMill) tile).angle = angle;
                        else ((TileHammerMill) tile).angle = -angle;
                    } else if (tile instanceof TileMetalShaft) {
                        if (((TileMetalShaft) tile).orientation != this.orientation)
                            ((TileMetalShaft) tile).angle = angle;
                        else ((TileMetalShaft) tile).angle = -angle;
                    }
                }
            }
        }
    }

    /**
     * Updates orientation so "forward" is facing away from the water mill
     */
    public void updateOrientation() {
        ForgeDirection forward = orientation;
        ForgeDirection back = forward.getOpposite();

        TileEntity forwardTile = worldObj.getTileEntity(xCoord + forward.offsetX, yCoord + forward.offsetY, zCoord + forward.offsetZ);
        TileEntity backwardTile = worldObj.getTileEntity(xCoord + back.offsetX, yCoord + back.offsetY, zCoord + back.offsetZ);

        if (forwardTile != null && forwardTile instanceof TileWaterMill) {
            orientation = forward;
        } else if (backwardTile != null && backwardTile instanceof TileWaterMill) {
            orientation = back;
        }
    }

    private List<TileEntity> getConnectedTiles() {
        List<TileEntity> list = new ArrayList<TileEntity>();

        int x = xCoord;
        int y = yCoord;
        int z = zCoord;

        int length = 0;

        boolean foundEnd = false;

        while (!foundEnd) {
            if (length > MAX_LENGTH) {
                foundEnd = true;
                break;
            }

            length++;
            x += orientation.getOpposite().offsetX;
            y += orientation.getOpposite().offsetY;
            z += orientation.getOpposite().offsetZ;

            TileEntity tile = worldObj.getTileEntity(x, y, z);
            TileEntity beyond = worldObj.getTileEntity(x + orientation.getOpposite().offsetX, y + orientation.getOpposite().offsetY, z + orientation.getOpposite().offsetZ);

            //     System.out.println(FMLCommonHandler.instance().getEffectiveSide() + " " + tile);

            if (tile instanceof TileHammerMill) {
                // Don't let hammer mills get strung together
                if (list.size() > 0 && list.get(list.size() - 1) instanceof TileHammerMill) {
                    foundEnd = true;
                    break;
                }

                if (beyond == null || !(beyond instanceof TileHandCrank) && !(beyond instanceof TileKineticCrank)) {
                    if (((TileHammerMill) tile).orientation == orientation.getOpposite().getRotation(ForgeDirection.UP) || ((TileHammerMill) tile).orientation == orientation.getRotation(ForgeDirection.UP))
                        list.add(tile);
                }
            } else if (tile instanceof TileMetalShaft) {
                if (((TileMetalShaft) tile).orientation != orientation.getOpposite().getRotation(ForgeDirection.UP)) {
                    ((TileMetalShaft) tile).orientation = orientation.getOpposite().getRotation(ForgeDirection.UP);
                    ((TileMetalShaft) tile).markForUpdate();
                }

                list.add(tile);
            } else {
                foundEnd = true;
                break;
            }
        }

        return list;
    }
}
