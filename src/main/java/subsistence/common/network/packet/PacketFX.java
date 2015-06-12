package subsistence.common.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import subsistence.common.lib.client.EnumParticle;
import subsistence.common.network.PacketHandler;
import subsistence.common.util.BlockCoord;
import subsistence.common.util.ItemHelper;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public class PacketFX implements IMessage {

    public static final int MAX_PARTICLE_RANGE = 64;

    public static final int FX_PARTICLE = 0;
    public static final int FX_BLOCK_BREAK = 1;
    public static final int FX_SWING_HAND = 2;

    public double x;
    public double y;
    public double z;

    public int fxType;

    public int[] extraData;

    public PacketFX() {

    }

    public PacketFX(double x, double y, double z, int fxType, int... extraData) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.fxType = fxType;
        this.extraData = extraData;
    }

    public PacketFX(double x, double y, double z, EnumParticle particle) {
        this(x, y, z, FX_PARTICLE, particle.ordinal());
    }

    public PacketFX(double x, double y, double z, ItemStack stack) {
        this(x, y, z, FX_BLOCK_BREAK, ItemHelper.getID(stack), stack.getItemDamage());
    }

    public static void breakFX(int dim, int x, int y, int z, ItemStack stack) {
        PacketFX packet = new PacketFX(x, y, z, stack);
        PacketHandler.INSTANCE.sendToAllAround(packet, new NetworkRegistry.TargetPoint(dim, x, y, z, MAX_PARTICLE_RANGE));
    }
    
    public static void swingArm(EntityPlayer player) {
        PacketFX packet = new PacketFX();
        packet.extraData = new int[] { player.getEntityId() };
        packet.fxType = FX_SWING_HAND;
        BlockCoord pos = new BlockCoord(player);
        PacketHandler.INSTANCE.sendToAllAround(packet, new NetworkRegistry.TargetPoint(player.worldObj.provider.dimensionId, pos.x, pos.y, pos.z, MAX_PARTICLE_RANGE));
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
        buffer.writeInt(fxType);

        buffer.writeInt(extraData.length);
        for (int i : extraData) {
            buffer.writeInt(i);
        }
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        x = buffer.readDouble();
        y = buffer.readDouble();
        z = buffer.readDouble();
        fxType = buffer.readInt();

        extraData = new int[buffer.readInt()];
        for (int i = 0; i < extraData.length; i++) {
            extraData[i] = buffer.readInt();
        }
    }


    public static class Handler implements IMessageHandler<PacketFX, IMessage> {

        @Override
        public IMessage onMessage(PacketFX message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                Minecraft mc = Minecraft.getMinecraft();
                switch (message.fxType) {
                    case FX_PARTICLE: {
                        EnumParticle particle = EnumParticle.values()[message.extraData[0]];
                        particle.display(mc.thePlayer.worldObj, message.x, message.y, message.z, 0, 0, 0);
                        break;
                    }

                    case FX_BLOCK_BREAK: {
                        BlockCoord bc = new BlockCoord(message.x, message.y, message.z);
                        if (message.extraData[0] > 0 && message.extraData[0] < 4096) {
                            mc.theWorld.playAuxSFX(2001, bc.x, bc.y, bc.z, message.extraData[0]);
                        } else {
                            EnumParticle.ITEM_BREAK(new ItemStack(Item.getItemById(message.extraData[0]), message.extraData[1]), mc.thePlayer.worldObj, message.x, message.y, message.z);
                        }
                        break;
                    }
                    
                    case FX_SWING_HAND: {
                        EntityPlayer player = (EntityPlayer) mc.theWorld.getEntityByID(message.extraData[0]);
                        player.swingItem();
                    }
                }
            }
            return null;
        }

    }
}
