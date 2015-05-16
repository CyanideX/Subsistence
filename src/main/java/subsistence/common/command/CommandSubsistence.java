package subsistence.common.command;

import subsistence.CommonProxy;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

/**
 * @author dmillerw
 */
public class CommandSubsistence extends CommandBase {

    @Override
    public String getCommandName() {
        return "subsistence";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "/subsistence reload";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return !(sender instanceof EntityPlayer) || MinecraftServer.getServer().getConfigurationManager().func_152596_g(((EntityPlayer) sender).getGameProfile());
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            CommonProxy.loadJson();
        } else {
            throw new WrongUsageException(getCommandUsage(sender));
        }
    }
}
