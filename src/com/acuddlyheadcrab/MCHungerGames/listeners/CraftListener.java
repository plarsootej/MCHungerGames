package com.acuddlyheadcrab.MCHungerGames.listeners;

import com.acuddlyheadcrab.MCHungerGames.HGplugin;
import com.acuddlyheadcrab.MCHungerGames.FileIO.Configs;
import com.acuddlyheadcrab.MCHungerGames.arenas.Arenas;
import com.acuddlyheadcrab.apiEvents.PlayerEnterArenaEvent;
import com.acuddlyheadcrab.apiEvents.PlayerLeaveArenaEvent;
import com.acuddlyheadcrab.apiEvents.PlayerPassArenaReason;
import com.acuddlyheadcrab.apiEvents.TributeDeathEvent;
import com.acuddlyheadcrab.apiEvents.TributeMoveInCountdownEvent;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;


public class CraftListener implements Listener {
    public static HGplugin plugin;
    public static EventPriority priority;
    public CraftListener(HGplugin instance) {plugin = instance;}
    
//    TODO this stuff is now deprecated; using Configs class now
    public static FileConfiguration config;
    public static void initConfig(){
        config = Configs.getConfig();
        BlockListener.initConfig();
        TributeListener.initConfig();
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTP(PlayerTeleportEvent event){
        checkMoveEvent(event);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent e){
        checkMoveEvent(e);
    }
    
    void checkMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo(), from = event.getFrom();
        String
            to_arena = Arenas.getNearbyArena(to),
            from_arena = Arenas.getNearbyArena(from),
            arenakey = Arenas.getArenaByTrib(player)
        ;
        if((from_arena!=null)&&((!from_arena.equals(to_arena))||to_arena==null))
            HGplugin.callSubEvent(event, new PlayerLeaveArenaEvent(player, from, to, from_arena, PlayerPassArenaReason.PLAYERTP));
        if((to_arena!=null)&&((!to_arena.equals(from_arena))||from_arena==null))
            HGplugin.callSubEvent(event, new PlayerEnterArenaEvent(player, from, to, to_arena, PlayerPassArenaReason.PLAYERTP));
        if(arenakey!=null&&Arenas.isInCountdown(arenakey))
            HGplugin.callSubEvent(event, new TributeMoveInCountdownEvent(player, from, to, arenakey));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        String arenakey = Arenas.getArenaByTrib(player);
        if(arenakey!=null){
            if(Arenas.isInGame(arenakey)){
                HGplugin.callSubEvent(event, new TributeDeathEvent(player, event.getDrops(), event.getDroppedExp(), event.getDeathMessage(), arenakey));
            }
        }
    }
}