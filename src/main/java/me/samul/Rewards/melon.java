package me.samul.Rewards;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import me.samul.saplantacoes;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Random;


public class melon implements Listener {

    public static HashMap<String, Manager> recompensas = new HashMap<>();

    public static void setup() {

        FileConfiguration config = saplantacoes.getInstance().getConfig();
        for (String key : config.getConfigurationSection("Plantations.melon.rewards.").getKeys(false)) {
            Manager recompensa = new Manager(key,
                    config.getString("Plantations.melon.rewards." + key + ".name"),
                    config.getStringList("Plantations.melon.rewards." + key + ".commands"),
                     config.getDouble("Plantations.melon.rewards." + key + ".chance"));
            Bukkit.getConsoleSender().sendMessage("§b[Melancia] §aRecompensa §a§n" + key + "§a carregada!");
            recompensas.put(key, recompensa);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        FileConfiguration config = saplantacoes.getInstance().getConfig();
        if (e.getBlock().getType().equals(Material.MELON_BLOCK)
            && config.getList("worlds").contains(e.getPlayer().getWorld().getName())){
            if (!e.getPlayer().hasPermission(config.getString("Plantations.melon.permission"))) return;

            for (Manager r : recompensas.values()) {
                if (percentChance(r.getChance())) {
                    r.executarComandos(e.getPlayer());
                    e.getPlayer().sendMessage(config.getString("Messages.give")
                            .replace("{reward-name}", r.getNome())
                            .replace("&", "§"));
                    break;
                }
            }
        }



    }


    protected final boolean percentChance(final double percent) {
        if (percent < 0.0 || percent > 100.0) {
            throw new IllegalArgumentException("A porcentagem nao pode ser maior do que 100 nem menor do que 0");
        }
        final double result = new Random().nextDouble() * 100.0;
        return result <= percent;
    }
}
