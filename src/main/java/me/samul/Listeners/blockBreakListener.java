package me.samul.Listeners;

import me.samul.saplantacoes;
import me.samul.apis.actionbarAPI;
import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.NetherWarts;
import sun.nio.ch.Net;

import java.util.Random;

public class blockBreakListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onbreak(BlockBreakEvent e) {
        FileConfiguration config = saplantacoes.getInstance().getConfig();
        Player p = e.getPlayer();
        if (e.getBlock().getType().equals(Material.MELON_BLOCK)
                && config.getList("worlds").contains(p.getWorld().getName())) {
            if (!e.getPlayer().hasPermission(config.getString("Plantations.melon.permission"))){
                p.sendMessage(config.getString("Messages.no-permission")
                        .replace("&", "§"));
                e.setCancelled(true);
                return;
            }
            int totalDrops = melondrops(p.getItemInHand());
            e.setCancelled(true);
            e.getBlock().getLocation().getBlock().setType(Material.AIR);
            saplantacoes.getVaultHook().depositPlayer((OfflinePlayer) p, config.getInt("Plantations.melon.price") * totalDrops);
            actionbarAPI.sendActionBarMessage(p, config.getString("Messages.sell")
                    .replace("&", "§")
                    .replace("{price}", String.valueOf(config.getInt("Plantations.melon.price") * totalDrops))
                    .replace("{drops}", String.valueOf(totalDrops)));

        } else if (e.getBlock().getType().equals(Material.NETHER_WARTS)
                && config.getList("worlds").contains(p.getWorld().getName())
                && e.getBlock().getState().getData() instanceof NetherWarts) {

            if (!p.hasPermission(config.getString("Plantations.nether_wart.permission"))){
                p.sendMessage(config.getString("Messages.no-permission")
                        .replace("&", "§"));
                e.setCancelled(true);
                return;
            }
            NetherWarts netherWarts = (NetherWarts) e.getBlock().getState().getData();
            if (netherWarts.getState() == NetherWartsState.RIPE) {
                int totalDrops = wartsdrops(p.getItemInHand());
                e.setCancelled(true);
                e.getBlock().getLocation().getBlock().setType(Material.AIR);
                saplantacoes.getVaultHook().depositPlayer((OfflinePlayer) p, config.getInt("Plantations.nether_wart.price") * totalDrops);
                actionbarAPI.sendActionBarMessage(p, config.getString("Messages.sell")
                        .replace("&", "§")
                        .replace("{price}", String.valueOf(config.getInt("Plantations.nether_wart.price") * totalDrops))
                        .replace("{drops}", String.valueOf(totalDrops)));
            }else e.setCancelled(true);

        }

    }



    private int melondrops(ItemStack itemInHand) {
        int basedrops = new Random().nextInt(6) + 1;
        int fortuneLevel = 0;
        if (itemInHand != null && itemInHand.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
            fortuneLevel = itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        }
        int additionalDrops = 0;
        if (fortuneLevel > 0) {
            additionalDrops = new Random().nextInt(fortuneLevel + 4);
        }
        return basedrops + additionalDrops;
    }

    private int wartsdrops(ItemStack itemInHand) {
        int basedrops = new Random().nextInt(4) + 1;
        int fortuneLevel = 0;
        if (itemInHand != null && itemInHand.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
            fortuneLevel = itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        }
        int additionalDrops = 0;
        if (fortuneLevel > 0) {
            additionalDrops = new Random().nextInt(fortuneLevel + 4);
        }
        return basedrops + additionalDrops;
    }
}
