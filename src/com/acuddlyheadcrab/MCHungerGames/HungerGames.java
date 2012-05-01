package com.acuddlyheadcrab.MCHungerGames;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.acuddlyheadcrab.MCHungerGames.commands.CornucopiaCommand;
import com.acuddlyheadcrab.MCHungerGames.commands.HGArenaCommand;
import com.acuddlyheadcrab.MCHungerGames.commands.HGArenaEditCommand;
import com.acuddlyheadcrab.MCHungerGames.commands.HGGameCommand;
import com.acuddlyheadcrab.MCHungerGames.commands.HungerGamesCommand;
import com.acuddlyheadcrab.util.PluginInfo;
import com.acuddlyheadcrab.util.Util;

public class HungerGames extends JavaPlugin {
    
    public static HungerGames plugin;
    public static FileConfiguration config;
    public static FileConfiguration ArenasFile;
    public static FileConfiguration ChestItemsFile;
    private File fileChestItems;
    private File fileArenas;
    
    public PluginInfo pluginIO = new PluginInfo(this);
    public Util util = new Util(this);
    public Arenas arenas = new Arenas(this);
    
    @Override
    public void onEnable() {
        loadConfig();
        loadArenasFile();
        loadChestItemsFile();
        initCommands();
        Arenas.initFiles();
        Arenas.initGames();
        getServer().getPluginManager().registerEvents(new HungerListener(this), this);
        getServer().getPluginManager().registerEvents(new TributeListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        HungerListener.initConfig();
        
        PluginInfo.log.info("------------ [MCHungerGames] is now enabled ------------");
        PluginInfo.log.info("------------       by acuddlyheadcrab       ------------");
    }
    
    @Override
    public void onDisable() {
        PluginInfo.sendPluginInfo("is now disabled");
    }
    
    public void initCommands(){
        getCommand("hungergames").setExecutor(new HungerGamesCommand(this));
        getCommand("hgarena").setExecutor(new HGArenaCommand(this));
        getCommand("hgaedit").setExecutor(new HGArenaEditCommand(this));
        getCommand("hggame").setExecutor(new HGGameCommand(this));
        getCommand("spawnccp").setExecutor(new CornucopiaCommand(this));
//        remove when in full release
        getCommand("testcmd").setExecutor(new CornucopiaCommand(this));
    }
    
    public void loadConfig() {
        config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();
    }
    
    public void reloadArenas() {
        if (fileArenas == null) {
        fileArenas = new File(getDataFolder(), "Arenas.yml");
        }
        ArenasFile = YamlConfiguration.loadConfiguration(fileArenas);
        
        InputStream defConfigStream = getResource("Arenas.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            ArenasFile.setDefaults(defConfig);
        }
    }
    
    public void loadArenasFile(){
        ArenasFile = getArenasFile();
        ArenasFile.options().copyDefaults(true);
        saveArenas();
    }
    
    public FileConfiguration getArenasFile() {
        if (ArenasFile == null) reloadArenas();
        return ArenasFile;
    }
    
    public void saveArenas() {
        if (ArenasFile == null || fileArenas == null) return;
        try {
            ArenasFile.save(fileArenas);
        } catch (IOException ex) {
            PluginInfo.sendPluginInfo("Could not save config to " + fileArenas.getName() + ex);
        }
    }
    
    public void reloadChestItems() {
        if (fileChestItems == null) {
        fileChestItems = new File(getDataFolder(), "ChestItems.yml");
        }
        ChestItemsFile = YamlConfiguration.loadConfiguration(fileChestItems);
        
        InputStream defConfigStream = getResource("ChestItems.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            ChestItemsFile.setDefaults(defConfig);
        }
    }
    
    public void loadChestItemsFile(){
        ChestItemsFile = getChestItemsFile();
        ChestItemsFile.options().copyDefaults(true);
        saveChestItems();
    }
    
    public FileConfiguration getChestItemsFile() {
        if (ChestItemsFile == null) reloadChestItems();
        return ChestItemsFile;
    }
    
    public void saveChestItems() {
        if (ChestItemsFile == null || fileChestItems == null) return;
        try {
            ChestItemsFile.save(fileChestItems);
        } catch (IOException ex) {
            PluginInfo.sendPluginInfo("Could not save config to " + fileChestItems.getName() + ex);
        }
    }
    
    public static JavaPlugin getPlugin(){
        return plugin;
    }
    
    public static FileConfiguration getInstConfig(){
        /** Only safe AFTER plugin is enabled */
        if(config==null){throw new NullPointerException("CONFIG IS NULL!");} else return config;
    }

}
