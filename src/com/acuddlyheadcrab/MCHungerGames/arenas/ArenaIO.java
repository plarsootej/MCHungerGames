package com.acuddlyheadcrab.MCHungerGames.arenas;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.acuddlyheadcrab.MCHungerGames.HGplugin;
import com.acuddlyheadcrab.MCHungerGames.FileIO.YMLKey;


public class ArenaIO {
    
    private static FileConfiguration arenas;
    public static HGplugin hungergames;
    public ArenaIO(HGplugin instance){hungergames = instance;}
    
    public static void initArenas(FileConfiguration arenasFile){
        initFiles(arenasFile);
        Arenas.arenas = arenas;
        Arenas.config = hungergames.getConfig();
        ArenaUtil.arenas = arenas;
        ArenaUtil.initGames();
    }
    
    public static void submitNewArena(String name, Location center, double radius, List<String> gms, List<Map<?, ?>> tribs, boolean ingame){
        arenas.set(YMLKey.getArenaSubkey(name, YMLKey.ARN_SELF), null);
        arenas.set(YMLKey.getArenaSubkey(name, YMLKey.ARN_CENTER_WRLD), center.getWorld().getName());
        arenas.set(YMLKey.getArenaSubkey(name, YMLKey.ARN_CENTER_X), center.getX());
        arenas.set(YMLKey.getArenaSubkey(name, YMLKey.ARN_CENTER_Y), center.getY());
        arenas.set(YMLKey.getArenaSubkey(name, YMLKey.ARN_CENTER_Z), center.getZ());
        arenas.set(YMLKey.getArenaSubkey(name, YMLKey.ARN_RADIUS), radius);
        arenas.set(YMLKey.getArenaSubkey(name, YMLKey.ARN_GMS), gms);
        arenas.set(YMLKey.getArenaSubkey(name, YMLKey.ARN_TRIBS), tribs);
        arenas.set(YMLKey.getArenaSubkey(name, YMLKey.ARN_INGAME), ingame);
        arenas.set(YMLKey.getArenaSubkey(name, YMLKey.ARN_INGAME), ingame);
        HGplugin.saveArenasFile();
    }
    
    public static void submitNewArena(String name, Location center, double radius, List<String> gms, List<Map<?, ?>> tribs, boolean ingame, Location lounge){
        submitNewArena(name, center, radius, gms, tribs, ingame);
        arenasSet(YMLKey.getArenaSubkey(name, YMLKey.ARN_LOUNGE_WRLD), lounge.getWorld().getName());
        arenasSet(YMLKey.getArenaSubkey(name, YMLKey.ARN_LOUNGE_X), lounge.getX());
        arenasSet(YMLKey.getArenaSubkey(name, YMLKey.ARN_LOUNGE_Y), lounge.getY());
        arenasSet(YMLKey.getArenaSubkey(name, YMLKey.ARN_LOUNGE_Z), lounge.getZ());
        arenasSet(YMLKey.getArenaSubkey(name, YMLKey.ARN_LOUNGE_YAW), lounge.getYaw());
        arenasSet(YMLKey.getArenaSubkey(name, YMLKey.ARN_LOUNGE_PITCH), lounge.getPitch());
        HGplugin.saveArenasFile();
    }
    
    public static void initFiles(FileConfiguration arenasFile){
        arenas = arenasFile;
    }
    
    public static void deleteArena(String arenakey){
        arenasSet(YMLKey.getArenaSubkey(arenakey, YMLKey.ARN_SELF), null);
    }
    
    public static void renameArena(String arenakey, String renameto){
        ConfigurationSection cs = arenas.getConfigurationSection(YMLKey.getArenaSubkey(arenakey, YMLKey.ARN_SELF));
        arenasSet(YMLKey.getArenaSubkey(renameto, YMLKey.ARN_SELF), cs);
        deleteArena(arenakey);
        HGplugin.saveArenasFile();
    }
    
    public static void arenasSet(String path, Object object){
        arenas.set(path, object);
        HGplugin.saveArenasFile();
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
