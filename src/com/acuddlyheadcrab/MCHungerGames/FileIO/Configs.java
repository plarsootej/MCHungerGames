package com.acuddlyheadcrab.MCHungerGames.FileIO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.acuddlyheadcrab.MCHungerGames.HungerGames;
import com.acuddlyheadcrab.util.PluginInfo;


public class Configs {
    
    private static HungerGames plugin;
    public Configs(HungerGames instance){plugin = instance;}
    
    public static FileConfiguration config;
    public static FileConfiguration ArenasFile;
    public static FileConfiguration ChestItemsFile;
    private static File fileChestItems;
    private static File fileArenas;
    
    public static void saveUD(AcuddlyConfiguration acuconfig){
        try{
            acuconfig.save(acuconfig.getFile());
        }catch(IOException e){
            PluginInfo.sendPluginInfo("Could not save config! "+e);
        }
    }
    
    public static AcuddlyConfiguration getUD(File file){
        if(!file.exists()||!file.isFile()){try {
            file.createNewFile();
        } catch (IOException e) {
            PluginInfo.sendPluginInfo("Error creating new file!");
            PluginInfo.sendPluginInfo("Local Path: "+file.getPath()+"\\"+file.getName());
            e.printStackTrace();
        }}
        FileConfiguration UDfile = YamlConfiguration.loadConfiguration(file);
        InputStream defConfigStream = plugin.getResource(file.getName());
        if(defConfigStream != null){
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            UDfile.setDefaults(defConfig);
        }
        return new AcuddlyConfiguration(file, UDfile);
    }
    
    public static AcuddlyConfiguration getUD(String player){
        File invdir = new File(plugin.getDataFolder()+"\\Inventories\\");
        if(!(invdir.isDirectory())||invdir==null) invdir.mkdir();
        return getUD(new File(invdir.getAbsoluteFile()+"\\"+player+".yml"));
    }
    
    public static AcuddlyConfiguration getUD(Player player){
        return getUD(player.getName());
    }
    
    public static void loadUD(File file){
        saveUD(getUD(file));
    }
    
    public static void loadConfig() {
        config = plugin.getConfig();
        config.options().copyDefaults(true);
        plugin.saveConfig();
    }
    
    public static void saveConfig(){
        plugin.saveConfig();
    }
    
    public static void reloadArenas() {
        if (fileArenas == null) {
            fileArenas = new File(plugin.getDataFolder(), "Arenas.yml");
        }
        ArenasFile = YamlConfiguration.loadConfiguration(fileArenas);
        InputStream defConfigStream = plugin.getResource("Arenas.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            ArenasFile.setDefaults(defConfig);
        }
    }
    
    public static void loadArenasFile(){
        ArenasFile = getArenasFile();
        ArenasFile.options().copyDefaults(true);
        saveArenas();
    }
    
    public static FileConfiguration getArenasFile() {
        if (ArenasFile == null) reloadArenas();
        return ArenasFile;
    }
    
    public static void saveArenas() {
        if (ArenasFile == null || fileArenas == null) return;
        try {
            ArenasFile.save(fileArenas);
        } catch (IOException ex) {
            PluginInfo.sendPluginInfo("Could not save config to " + fileArenas.getName() + ex);
        }
    }
    
    public static void reloadChestItems() {
        if (fileChestItems == null) {
        fileChestItems = new File(plugin.getDataFolder(), "ChestItems.yml");
        }
        ChestItemsFile = YamlConfiguration.loadConfiguration(fileChestItems);
        InputStream defConfigStream = plugin.getResource("ChestItems.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            ChestItemsFile.setDefaults(defConfig);
        }
    }
    
    public static void loadChestItemsFile(){
        ChestItemsFile = getChestItemsFile();
        ChestItemsFile.options().copyDefaults(true);
        saveChestItems();
    }
    
    public static FileConfiguration getChestItemsFile() {
        if (ChestItemsFile == null) reloadChestItems();
        return ChestItemsFile;
    }
    
    public static void saveChestItems() {
        if (ChestItemsFile == null || fileChestItems == null) return;
        try {
            ChestItemsFile.save(fileChestItems);
        } catch (IOException ex) {
            PluginInfo.sendPluginInfo("Could not save config to " + fileChestItems.getName() + ex);
        }
    }
    
}
