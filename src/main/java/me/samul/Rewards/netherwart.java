package me.samul.Rewards;

import me.samul.saplantacoes;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.NetherWarts;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Random;


public class netherwart implements Listener {

    public static HashMap<String, Manager> recompensas = new HashMap<>();

    public static void setup() {

        FileConfiguration config = saplantacoes.getInstance().getConfig();
        for (String key : config.getConfigurationSection("Plantations.nether_wart.rewards.").getKeys(false)) {
            Manager recompensa = new Manager(key,
                    config.getString("Plantations.nether_wart.rewards." + key + ".name"),
                    config.getStringList("Plantations.nether_wart.rewards." + key + ".commands"),
                     config.getDouble("Plantations.nether_wart.rewards." + key + ".chance"));
            Bukkit.getConsoleSender().sendMessage("§b[Fungo] §aRecompensa §a§n" + key + "§a carregada!");
            recompensas.put(key, recompensa);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent e) {
        FileConfiguration config = saplantacoes.getInstance().getConfig();
        if (e.getBlock().getType().equals(Material.NETHER_WARTS)
                && (e.getBlock().getState().getData() instanceof NetherWarts)
                && config.getList("worlds").contains(e.getPlayer().getWorld().getName())){
            if (!e.getPlayer().hasPermission(config.getString("Plantations.nether_wart.permission"))) return;
            NetherWarts netherWarts = (NetherWarts) e.getBlock().getState().getData();
            if (netherWarts.getState() == NetherWartsState.RIPE){
                Player player = e.getPlayer();
                if (config.getBoolean("Plantations.nether_wart.replant.activated")){
                    replant(player, e.getBlock());
                }

                for (Manager r : recompensas.values()) {
                    if (percentChance(r.getChance())) {
                        r.executarComandos(player);
                        player.sendMessage(config.getString("Messages.give")
                                .replace("{reward-name}", r.getNome())
                                .replace("&", "§"));
                        break;
                    }
                }
            }
        }
    }

    protected final void replant(Player p, Block bloco){
        FileConfiguration config = saplantacoes.getInstance().getConfig();
        if (config.getBoolean("Plantations.nether_wart.replant.verify_inventory")
        && p.getInventory().contains(Material.NETHER_STALK)){
            p.getInventory().removeItem(new ItemStack(Material.NETHER_STALK, 1));
            new BukkitRunnable(){
                @Override
                public void run() {
                    bloco.setType(Material.NETHER_WARTS);
                }
            }.runTaskLater(saplantacoes.getInstance(), 10L);
        }
        if (!config.getBoolean("Plantations.nether_wart.replant.verify_inventory")){
            new BukkitRunnable(){
                @Override
                public void run() {
                    bloco.setType(Material.NETHER_WARTS);
                }
            }.runTaskLater(saplantacoes.getInstance(), 10L);
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
