package com.acuddlyheadcrab.MCHungerGames;

import com.acuddlyheadcrab.MCHungerGames.arenas.Arenas;
import com.acuddlyheadcrab.util.YMLKeys;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


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
    
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onCreatureSpawn(CreatureSpawnEvent event){
        if(config.getBoolean(YMLKeys.OPS_DURGM_NOMOBS.key())){
            String arenakey = Arenas.getNearbyArena(event.getLocation());
            if(arenakey!=null&&Arenas.isInGame(arenakey)) event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e){
        String arenakey = Arenas.getArenaByTrib(e.getPlayer());
        if(arenakey!=null){
            if(Arenas.isInGame(arenakey)||Arenas.isInCountdown(arenakey)){
                e.getPlayer().teleport(Arenas.getCenter(arenakey));
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDisconnect(PlayerQuitEvent e){
        String arenakey = Arenas.getArenaByTrib(e.getPlayer());
        if(arenakey!=null){
            if(Arenas.isInGame(arenakey)){
                if(config.getBoolean(YMLKeys.OPS_DURGM_DISQUALONDISC.key())){
                    Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+""+e.getPlayer().getName()+" has been disqualified from "+arenakey+"!");
                    Arenas.removeTrib(arenakey, e.getPlayer().getName(), true);
                }
            }
        }
    }
    
}