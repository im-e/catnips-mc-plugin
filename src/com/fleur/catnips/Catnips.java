package com.fleur.catnips;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Catnips extends JavaPlugin {


    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(new CatnipsEvents(), this);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[Catnips]: Plugin is enabled!");
        loadConfig();

    }

    @Override
    public void onDisable()
    {
        getServer().getConsoleSender().sendMessage(ChatColor.RED +"[Catnips]: Plugin is disabled!");
    }

    public void loadConfig()
    {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
}
