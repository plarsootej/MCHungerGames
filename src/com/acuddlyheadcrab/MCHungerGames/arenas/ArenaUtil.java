package com.acuddlyheadcrab.MCHungerGames.arenas;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.acuddlyheadcrab.MCHungerGames.HGplugin;
import com.acuddlyheadcrab.MCHungerGames.FileIO.YMLKey;
import com.acuddlyheadcrab.MCHungerGames.inventories.InventoryHandler;
import com.acuddlyheadcrab.util.PluginInfo;
import com.acuddlyheadcrab.util.Utility;


public class ArenaUtil {
    
    public static FileConfiguration arenas;
    public static HGplugin hungergames;
    public ArenaUtil(HGplugin instance){
        hungergames = instance;
    }
    
    public static void updateTribLocs(String arenakey, boolean deafaultspawnpoints){
        // Eww ew ew. This is probably so inefficient, but oh well.
        List<Map<?,?>> 
            alltribs = Arenas.getTribs(arenakey), 
            noncust_tribs = Arenas.getNonCustomTribs(arenakey), 
            maplist = noncust_tribs
        ;
        List<Location> loclist = Utility.getSurroundingLocs(Arenas.getCenter(arenakey), maplist.size(), 7);
        for(int i=0;i<maplist.size();i++){
            @SuppressWarnings("unchecked")
            Map<String,String> map = (Map<String, String>) maplist.get(i);
            String lockey = Utility.toLocKey(loclist.get(i), false, false);
            try{
                if(deafaultspawnpoints){
                    lockey = Arenas.getSpawnPointKeys(arenakey).get(i);
                }
            }catch(IndexOutOfBoundsException e){}
            map.put(Utility.getKeys(map).get(0).toString(), lockey);
        }
       alltribs.removeAll(noncust_tribs);
       alltribs.addAll(maplist);
       Arenas.setTribs(arenakey, alltribs);
    }
    
    public static void tpAllOnlineTribs(String arenakey, boolean startinggame) {
        for(Player trib : Arenas.getOnlineTribNames(arenakey)){
            trib.teleport(Arenas.getTribSpawn(arenakey, trib.getName()));
            trib.sendMessage(ChatColor.LIGHT_PURPLE+"You have been teleported to "+arenakey);
        }
    }
    
    public static void startGame(final String arenakey, int countdown){
//        HGplugin.callSubEvent(Arena, null)
        for(Player trib : Arenas.getOnlineTribNames(arenakey)){
            PluginInfo.debug(62, ArenaUtil.class, "Saving "+trib.getName()+"'s inventory...");
            InventoryHandler.saveInventory(trib);
            PluginInfo.debug(64, ArenaUtil.class, "Saved "+trib.getName()+"'s inv!");
            trib.getInventory().clear();
            trib.setGameMode(GameMode.SURVIVAL);
        }
        ArenaUtil.tpAllOnlineTribs(arenakey, true);
        ArenaIO.arenasSet(YMLKey.GAME_COUNT.key(), Arenas.getGameCount()+1);
        startCountdown(arenakey, countdown);
    }


    public static void initGames() {
        List<String> currentgames = arenas.getStringList(YMLKey.CURRENT_GAMES.key());
//        checks for any extra games (aka removes unecessary)
        for(int c=0;c<currentgames.size();c++){
            String game = currentgames.get(c);
            boolean contains = ArenaIO.getArenasKeys().contains(game), ingame = Arenas.isInGame(game);
            if(!contains||!ingame){
                currentgames.remove(c);
            }
        }
//        checks if any arenas are excluded (aka adds not included ones)
        for(String arena : ArenaIO.getArenasKeys()){
            if(currentgames.contains(arena)){
                if(!Arenas.isInGame(arena)) currentgames.remove(arena);
            } else {
                if(Arenas.isInGame(arena)) currentgames.add(arena);
            }
        }
        ArenaIO.arenasSet(YMLKey.CURRENT_GAMES.key(), currentgames);
    }
    
    
    private static int taskID;
    
//    TODO fix countdown
    public static void startCountdown(final String arenakey, final int seconds){
        PluginInfo.sendPluginInfo("Setting "+arenakey+" in countdown with "+seconds+" to go!");
        Bukkit.broadcastMessage(ChatColor.YELLOW+"Hunger Games are starting in the arena "+arenakey+" in "+seconds+" seconds");
        
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(hungergames, new Runnable() {
            private int count = seconds;
            @Override
            public void run() {
                Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"[MCHungerGames] "+count);
                if(count<=0){
                    Arenas.setInGame(arenakey, true);
                    Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+arenakey+" is now in game!");
                    cancelCountdownTask();
                } else count--;
            }
        }, 20, 20);
    }
    
    static void cancelCountdownTask(){
        Bukkit.getScheduler().cancelTask(taskID);
    }
    
}
