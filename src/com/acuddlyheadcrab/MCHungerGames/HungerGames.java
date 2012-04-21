package com.acuddlyheadcrab.MCHungerGames;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.acuddlyheadcrab.MCHungerGames.commands.CornucopiaCommand;
import com.acuddlyheadcrab.MCHungerGames.commands.HGArenaCommand;
import com.acuddlyheadcrab.MCHungerGames.commands.HGArenaEditCommand;
import com.acuddlyheadcrab.MCHungerGames.commands.HGGameCommand;
import com.acuddlyheadcrab.MCHungerGames.commands.HungerGamesCommand;
import com.acuddlyheadcrab.util.PluginInfo;
import com.acuddlyheadcrab.util.Utility;
import com.acuddlyheadcrab.util.YMLKeys;

public class HungerGames extends JavaPlugin {
    
    public static HungerGames plugin;
    public static FileConfiguration config;
    
    public PluginInfo pluginIO = new PluginInfo(this);
    public Utility util = new Utility(this);
    public Arenas arenas = new Arenas(this);
    
    @Override
    public void onEnable() {
        loadConfig();
        initCommands();
        Arenas.initConfig();
        Arenas.initGames();
        getServer().getPluginManager().registerEvents(new HungerListener(this), this);
        getServer().getPluginManager().registerEvents(new TributeListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        HungerListener.initConfig();
        
//       This is just to update configs
        PluginInfo.sendPluginInfo("Updating config...");
        for(String arena : Utility.getArenasKeys()){
            String arenakey = YMLKeys.ARENAS.key()+arena, path = arenakey+".Maxdistance";
            double radius = config.getDouble(path);
            config.set(arenakey+YMLKeys.ARN_RADIUS.key(), radius);
            config.set(path, null);
            PluginInfo.sendPluginInfo("Updated arena "+arena+"...");
            saveConfig();
        }
        PluginInfo.sendPluginInfo("Done!");
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
    }
    
    public void loadConfig() {
        config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();
    }
    
    public static JavaPlugin getPlugin(){
        return plugin;
    }
    
    public static FileConfiguration getInstConfig(){
        /** Only safe AFTER plugin is enabled */
        if(config==null){throw new NullPointerException("CONFIG IS NULL!");} else return config;
    }

}
