package me.samul.Rewards;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class Manager {

    private final String key;
    private final String nome;
    private final List<String> comandos;
    private final double chance;

    public Manager(String key, String nome, List<String> comandos, double chance) {
        this.key = key;
        this.nome = nome;
        this.comandos = comandos;
        this.chance = chance;
    }

    public String getKey() {
        return key;
    }

    public String getNome() {
        return nome;
    }

    public List<String> getComandos() {
        return comandos;
    }

    public double getChance() {
        return chance;
    }

    public void executarComandos(Player player) {
        for (String comando : comandos) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), comando.replace("{player}", player.getName()));
        }
    }
}
