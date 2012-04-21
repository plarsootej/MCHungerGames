package com.acuddlyheadcrab.MCHungerGames;

import com.acuddlyheadcrab.util.YMLKeys;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;


public class HungerListener implements Listener {
    public static HungerGames plugin;
    public static EventPriority priority;
    public HungerListener(HungerGames instance) {plugin = instance;}
    
    public static FileConfiguration config;
    // note to self: config is instantiated ONLY once initConfig() is called.
    public static void initConfig(){
        config = plugin.getConfig();
        BlockListener.initConfig();
        TributeListener.initConfig();
    }
    
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent event){
        if(config.getBoolean(YMLKeys.OPTS_DURGM_NOMOBS.key())){
            String arenakey = Arenas.getNearbyArena(event.getLocation());
            if(arenakey!=null&&Arenas.isInGame(arenakey)) event.setCancelled(true);
        }
    }
    
    
}