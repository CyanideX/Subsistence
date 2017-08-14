package oldStuff.common.tile.core;

import net.minecraftforge.common.util.ForgeDirection;
import oldStuff.common.network.nbt.NBTHandler;

public abstract class TileCoreMachine extends TileCore {

    @NBTHandler.Sync(true)
    public ForgeDirection orientation = ForgeDirection.UNKNOWN;

    public TileCoreMachine() {
        super();

        this.handler.addField(TileCoreMachine.class, "orientation");
    }
}
