package com.acuddlyheadcrab.MCHungerGames;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import com.acuddlyheadcrab.MCHungerGames.FileIO.Configs;
import com.acuddlyheadcrab.MCHungerGames.FileIO.YMLKey;
import com.acuddlyheadcrab.MCHungerGames.arenas.ArenaIO;
import com.acuddlyheadcrab.MCHungerGames.arenas.ArenaUtil;
import com.acuddlyheadcrab.MCHungerGames.arenas.Arenas;
import com.acuddlyheadcrab.MCHungerGames.chat.ChatHandler;
import com.acuddlyheadcrab.MCHungerGames.chests.ChestHandler;
import com.acuddlyheadcrab.MCHungerGames.commands.CornucopiaCommand;
import com.acuddlyheadcrab.MCHungerGames.commands.HGArenaCommand;
import com.acuddlyheadcrab.MCHungerGames.commands.HGArenaEditCommand;
import com.acuddlyheadcrab.MCHungerGames.commands.HGGameCommand;
import com.acuddlyheadcrab.MCHungerGames.commands.HungerGamesCommand;
import com.acuddlyheadcrab.MCHungerGames.inventories.InventoryHandler;
import com.acuddlyheadcrab.MCHungerGames.listeners.BlockListener;
import com.acuddlyheadcrab.MCHungerGames.listeners.HGListener;
import com.acuddlyheadcrab.MCHungerGames.listeners.CraftListener;
import com.acuddlyheadcrab.MCHungerGames.listeners.TributeListener;
import com.acuddlyheadcrab.util.PluginInfo;
import com.acuddlyheadcrab.util.Utility;

public class HGplugin extends JavaPlugin{
    
    public static HGplugin plugin;
    
    public PluginInfo pluginIO = new PluginInfo(this);
    public Utility utility = new Utility(this);
    public ChestHandler chests = new ChestHandler(this);
    public ChatHandler chaths = new ChatHandler(this);
    public Arenas arenas = new Arenas(this);
    public ArenaIO arenaio = new ArenaIO(this);
    public ArenaUtil arenautil = new ArenaUtil(this);
    public InventoryHandler invhandler = new InventoryHandler(this);
    public Configs configs = new Configs(this);
    
    @Override
    public void onEnable() {
        loadConfigs();
        initCommands();
        
        ArenaIO.initArenas(getArenasFile());
        getServer().getPluginManager().registerEvents(new CraftListener(this), this);
        getServer().getPluginManager().registerEvents(new TributeListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        getServer().getPluginManager().registerEvents(new HGListener(this), this);
        
        CraftListener.initConfig();
        ChestHandler.initChestItems();
        
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
    
    public static void loadConfigs(){
        Configs.loadConfig();
        Configs.loadArenasFile();
        Configs.loadChestItemsFile();
    }
    
    public static JavaPlugin getPlugin(){
        return plugin;
    }
    
    public static FileConfiguration getArenasFile() {
        return Configs.getArenasFile();
    }
    
    public static FileConfiguration getChestItemsFile(){
        return Configs.getChestItemsFile();
    }
    
    public static void saveArenasFile(){
        Configs.saveArenas();
    }
    
    public static void saveChestItems(){
        Configs.saveChestItems();
    }
    
    public static void callEvent(Event e){
        Bukkit.getPluginManager().callEvent(e);
    }
    
    public static void callSubEvent(Event superev, Event subev){
        callEvent(subev);
        if(superev instanceof Cancellable && subev instanceof Cancellable){
            ((Cancellable) superev).setCancelled((((Cancellable) subev).isCancelled()));
            if(Configs.getConfig().getBoolean(YMLKey.OPS_DEBUG_ONEVENTS)){
                System.out.println("Sub Is cancelled: "+(((Cancellable) subev).isCancelled()));
                System.out.println("Super Is cancelled: "+(((Cancellable) superev).isCancelled()));
            }
        }
        return;
    }
}
