package com.acuddlyheadcrab.MCHungerGames.arenas;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.acuddlyheadcrab.MCHungerGames.HungerGames;
import com.acuddlyheadcrab.util.Util;
import com.acuddlyheadcrab.util.YMLKeys;


public class ArenaUtil {
    
    public static FileConfiguration arenas;
    public static HungerGames hungergames;
    public ArenaUtil(HungerGames instance){
        hungergames = instance;
    }
    
    public static void updateTribLocs(String arenakey){
        // Eww ew ew. This is probably so inefficient, but oh well.
        List<Map<?,?>> 
            alltribs = Arenas.getTribs(arenakey), 
            noncust_tribs = Arenas.getNonCustomTribs(arenakey), 
            maplist = noncust_tribs
        ;
        List<Location> loclist = Util.getSurroundingLocs(Arenas.getCenter(arenakey), maplist.size(), 7);
        for(int i=0;i<maplist.size();i++){
            @SuppressWarnings("unchecked")
            Map<String,String> map = (Map<String, String>) maplist.get(i);
            map.put(Util.getKeys(map).get(0).toString(), Util.toLocKey(loclist.get(i), false));
        }
       alltribs.removeAll(noncust_tribs);
       alltribs.addAll(maplist);
       Arenas.setTribs(arenakey, alltribs);
    }
    
 // TODO redo this
    public static void tpAllOnlineTribs(String arenakey, boolean startinggame) {
        for(Player trib : Arenas.getOnlineTribNames(arenakey)){
//                TODO: add backup inventories IF startinggame
            trib.teleport(Arenas.getTribSpawn(arenakey, trib.getName()));
            if(startinggame){
                trib.getInventory().clear();
                trib.setGameMode(GameMode.SURVIVAL);
            }
            trib.sendMessage(ChatColor.LIGHT_PURPLE+"You have been teleported to "+arenakey);
        }
    }
    
    public static void startGame(final String arenakey, int countdown){
        tpAllOnlineTribs(arenakey, true);
        ArenaIO.arenasSet(YMLKeys.GAME_COUNT.key(), Arenas.getGameCount()+1);
        startCountdown(arenakey, countdown);
    }
    


    public static void initGames() {
        List<String> currentgames = arenas.getStringList(YMLKeys.CURRENT_GAMES.key());
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
        ArenaIO.arenasSet(YMLKeys.CURRENT_GAMES.key(), currentgames);
    }
    

    
    public static void startCountdown(final String arenakey, final int seconds){
//        initialize path
        System.out.println("Setting "+arenakey+" in countdown with "+seconds+" to go!");
        arenas.set(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_INCOUNTDOWN), seconds);
//        cycle through each second
        Bukkit.getScheduler().scheduleSyncRepeatingTask(hungergames, new Runnable() {
            @Override
            public void run() {
                int second = arenas.getInt(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_INCOUNTDOWN));
                Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"[MCHungerGames] "+second);
                if(second==0){
                    Bukkit.getScheduler().cancelTasks(hungergames);
                    ArenaIO.arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_INCOUNTDOWN), null);
                    Arenas.setInGame(arenakey, true);
                    Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+arenakey+" is now in game!");
                } else countdown(arenakey);
            }
        }, 20, 20);
    }
    
    
    public static void countdown(String arenakey){
        String path = YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_INCOUNTDOWN);
        ArenaIO.arenasSet(path, arenas.getInt(path)-1);
    }
    
    
}
