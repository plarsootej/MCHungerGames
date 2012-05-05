package com.acuddlyheadcrab.MCHungerGames.arenas;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.acuddlyheadcrab.MCHungerGames.HungerGames;
import com.acuddlyheadcrab.util.YMLKeys;


public class ArenaIO {
    
    private static FileConfiguration arenas;
    public static HungerGames hungergames;
    public ArenaIO(HungerGames instance){hungergames = instance;}
    
    public static void initArenas(FileConfiguration arenasFile){
        initFiles(arenasFile);
        Arenas.arenas = arenas;
        Arenas.config = hungergames.getConfig();
        ArenaUtil.arenas = arenas;
        ArenaUtil.initGames();
    }
    
    public static void submitNewArena(String name, Location center, double radius, List<String> gms, List<Map<?, ?>> tribs, boolean ingame){
        arenas.set(YMLKeys.getArenaSubkey(name, YMLKeys.ARN_SELF), null);
        arenas.set(YMLKeys.getArenaSubkey(name, YMLKeys.ARN_CENTER_WRLD), center.getWorld().getName());
        arenas.set(YMLKeys.getArenaSubkey(name, YMLKeys.ARN_CENTER_X), center.getX());
        arenas.set(YMLKeys.getArenaSubkey(name, YMLKeys.ARN_CENTER_Y), center.getY());
        arenas.set(YMLKeys.getArenaSubkey(name, YMLKeys.ARN_CENTER_Z), center.getZ());
        arenas.set(YMLKeys.getArenaSubkey(name, YMLKeys.ARN_RADIUS), radius);
        arenas.set(YMLKeys.getArenaSubkey(name, YMLKeys.ARN_GMS), gms);
        arenas.set(YMLKeys.getArenaSubkey(name, YMLKeys.ARN_TRIBS), tribs);
        arenas.set(YMLKeys.getArenaSubkey(name, YMLKeys.ARN_INGAME), ingame);
        arenas.set(YMLKeys.getArenaSubkey(name, YMLKeys.ARN_INGAME), ingame);
        hungergames.saveArenas();
    }
    
    public static void submitNewArena(String name, Location center, double radius, List<String> gms, List<Map<?, ?>> tribs, boolean ingame, Location lounge){
        submitNewArena(name, center, radius, gms, tribs, ingame);
        arenasSet(YMLKeys.getArenaSubkey(name, YMLKeys.ARN_LOUNGE_WRLD), lounge.getWorld().getName());
        arenasSet(YMLKeys.getArenaSubkey(name, YMLKeys.ARN_LOUNGE_X), lounge.getX());
        arenasSet(YMLKeys.getArenaSubkey(name, YMLKeys.ARN_LOUNGE_Y), lounge.getY());
        arenasSet(YMLKeys.getArenaSubkey(name, YMLKeys.ARN_LOUNGE_Z), lounge.getZ());
        arenasSet(YMLKeys.getArenaSubkey(name, YMLKeys.ARN_LOUNGE_YAW), lounge.getYaw());
        arenasSet(YMLKeys.getArenaSubkey(name, YMLKeys.ARN_LOUNGE_PITCH), lounge.getPitch());
        hungergames.saveArenas();
    }
    
    public static void initFiles(FileConfiguration arenasFile){
        arenas = arenasFile;
    }
    
    public static void deleteArena(String arenakey){
        arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_SELF), null);
    }
    
    public static void renameArena(String arenakey, String renameto){
        ConfigurationSection cs = arenas.getConfigurationSection(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_SELF));
        arenasSet(YMLKeys.getArenaSubkey(renameto, YMLKeys.ARN_SELF), cs);
        deleteArena(arenakey);
        hungergames.saveArenas();
    }
    
    public static void arenasSet(String path, Object object){
        arenas.set(path, object);
        hungergames.saveArenas();
    }
    
    public static String getArenaByKey(String arg1) {
        for(String arenas : getArenasKeys()){
            if(arg1.equalsIgnoreCase(arenas)) return arenas;
        }
        return null;
    }
    
    public static List<String> getArenasKeys() {
        
        Set<String> str_set = arenas.getConfigurationSection("Arenas").getKeys(false);
        String[] keyarr = new String[str_set.size()];
        keyarr = str_set.toArray(keyarr);
        
        return Arrays.asList(keyarr);
    }
}
