package oldStuff.common.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.util.List;

public class CommandTPX extends CommandBase {

    @Override
    public String getCommandName() {
        return "tpx";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "/tpx {player} [id] {x} {y} {z}";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            final World dimension = DimensionManager.getWorld(Integer.parseInt(args[args.length - 1]));
            if (dimension != null) {
                final ChunkCoordinates spawn = dimension.getSpawnPoint();
                teleportPlayer(sender, getCommandSenderAsPlayer(sender), dimension.provider.dimensionId, spawn.posX, spawn.posY, spawn.posZ);
            }
        } else if (args.length == 2) {
            final World dimension = DimensionManager.getWorld(Integer.parseInt(args[args.length - 1]));
            if (dimension != null) {
                final ChunkCoordinates spawn = dimension.getSpawnPoint();
                teleportPlayer(sender, getPlayer(sender, args[args.length - 2]), dimension.provider.dimensionId, spawn.posX, spawn.posY, spawn.posZ);
            }
        } else if (args.length == 4) {
            final EntityPlayerMP player = getCommandSenderAsPlayer(sender);
            if (player.worldObj != null) {
                int i = args.length - 4;
                final int dimension = CommandBase.parseInt(sender, args[i++]);
                final double x = func_110666_a(sender, player.posX, args[i++]);
                final double y = func_110665_a(sender, player.posY, args[i++], 0, 0);
                final double z = func_110666_a(sender, player.posZ, args[i++]);
                teleportPlayer(sender, player, dimension, x, y, z);
            }
        } else if (args.length == 5) {
            final EntityPlayerMP player = getPlayer(sender, args[args.length - 5]);
            if (player.worldObj != null) {
                int i = args.length - 4;
                final int dimension = CommandBase.parseInt(sender, args[i++]);
                final double x = func_110666_a(sender, player.posX, args[i++]);
                final double y = func_110665_a(sender, player.posY, args[i++], 0, 0);
                final double z = func_110666_a(sender, player.posZ, args[i++]);
                teleportPlayer(sender, player, dimension, x, y, z);
            }
        } else {
            throw new WrongUsageException("/tpx {player} [id] {x} {y} {z}");
        }
    }

    public static void teleportPlayer(final ICommandSender sender, final EntityPlayerMP player, final int dimension, final double x, final double y, final double z) {
        player.mountEntity(null);
        if (player.dimension != dimension) {
            MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, dimension);
        }
        player.setPositionAndUpdate(x, y, z);
        player.prevPosX = player.posX = x;
        player.prevPosY = player.posY = y;
        player.prevPosZ = player.posZ = z;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
        return p_71516_2_.length != 1 && p_71516_2_.length != 2 ? null : getListOfStringsMatchingLastWord(p_71516_2_, MinecraftServer.getServer().getAllUsernames());
    }

    @Override

    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
        return p_82358_2_ == 0;
    }
}
