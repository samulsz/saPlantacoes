package me.samul.Commands;

import me.samul.Rewards.melon;
import me.samul.Rewards.netherwart;
import me.samul.saplantacoes;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;


public class reloadCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0
        && args[0].equalsIgnoreCase("reload")){
            JavaPlugin config = saplantacoes.getInstance();
            saplantacoes.getInstance().reloadConfig();
            melon.setup();
            netherwart.setup();
            sender.sendMessage(config.getConfig().getString("Messages.reload")
                    .replace("&", "§"));
        } else {
            sender.sendMessage("§cUse: /plantacoes reload");
        }
        return false;
    }
}
