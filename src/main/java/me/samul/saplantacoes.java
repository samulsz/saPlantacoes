package me.samul;

import me.samul.Commands.reloadCmd;
import me.samul.Listeners.blockBreakListener;
import me.samul.Rewards.melon;
import me.samul.Rewards.netherwart;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class saplantacoes extends JavaPlugin {

    private static Economy econ = null;
    private static JavaPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getConsoleSender().sendMessage("Inicializado com sucesso");
        saveDefaultConfig();
        registerEvents();
        registerCommands();

        if (!setupEconomy() ) {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage("[saPlantacoes] - §cVault ou plugin de economia nao encontrados!");
            Bukkit.getConsoleSender().sendMessage("");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;
        econ = (Economy)rsp.getProvider();
        return (econ != null);
    }

    private void registerEvents() {
        netherwart.setup();
        melon.setup();
        Bukkit.getPluginManager().registerEvents(new melon(), this);
        Bukkit.getPluginManager().registerEvents(new blockBreakListener(), this);
        Bukkit.getPluginManager().registerEvents(new netherwart(), this);
    }

    private void registerCommands(){
        getCommand("plantacoes").setExecutor(new reloadCmd());
    }


    public static Economy getVaultHook() {
        return econ;
    }
    public static JavaPlugin getInstance() {
        return instance;
    }


}
