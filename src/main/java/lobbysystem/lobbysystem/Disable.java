package lobbysystem.lobbysystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Disable implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p){
            if (args.length == 0){
                p.sendMessage("Please run this command with argument: true or false!");
            } else {
                switch (args[0]) {
                    case "true" -> {
                        LobbySystem.setEnableStatus(true);
                        p.sendMessage("LobbySystem has been Enabled!");
                    }
                    case "false" -> {
                        LobbySystem.setEnableStatus(false);
                        p.sendMessage("LobbySystem has been Disabled!");
                    }
                }
            }
        }
        return false;
    }
}
