package com.acuddlyheadcrab.MCHungerGames.arenas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.acuddlyheadcrab.MCHungerGames.HungerGames;
import com.acuddlyheadcrab.util.YMLKeys;
import com.acuddlyheadcrab.util.Util;

public class Arenas {
    
	public static FileConfiguration arenas;
    public static HungerGames hungergames;
    public Arenas(HungerGames instance){
    	hungergames = instance;
	}
    
    public static Location getCenter(String arenakey){
        String worldstring = arenas.getString(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_CENTER_WRLD));
        World world = Bukkit.getWorld(worldstring);
        if(world==null) throw new NullPointerException("Could not find world \""+worldstring+"\"");
        double
            x = arenas.getDouble(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_CENTER_X)),
            y = arenas.getDouble(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_CENTER_Y)),
            z = arenas.getDouble(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_CENTER_Z))
        ;
        Location center = new Location(world, x, y, z);
        return center;
    }
    
    public static Location getLounge(String arenakey){
        String worldstring = arenas.getString(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_LOUNGE_WRLD));
        World world = Bukkit.getWorld(worldstring);
        double
            x = arenas.getDouble(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_LOUNGE_X)),
            y = arenas.getDouble(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_LOUNGE_Y)),
            z = arenas.getDouble(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_LOUNGE_Z))
        ;
        float
            pitch = (float) arenas.getDouble(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_LOUNGE_PITCH)),
            yaw = (float) arenas.getDouble(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_LOUNGE_YAW))
        ;
        if(x==0||y==0||z==0) return null;
        if(world==null) throw new NullPointerException("[MCHungerGames] Could not find world \""+worldstring+"\"");
        Location lounge = new Location(world, x, y, z, yaw, pitch);
        return lounge;
    }
    
    public static double getRadius(String arenakey){
        return arenas.getDouble(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_RADIUS));
    }
    
    public static List<String> getGMs(String arenakey){
        return arenas.getStringList(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_GMS));
    }
    
    public static List<Player> getOnlineGMs(String arenakey){
        List<Player> onlinegms = new ArrayList<Player>();
        for(String gm_s : getGMs(arenakey)){
            try{
                Player player = Bukkit.getPlayer(gm_s); 
                if(player!=null) onlinegms.add(player);
            }catch(NullPointerException e){}
        }
        return onlinegms;
    }
    
    public static List<String> getTribNames(String arenakey){
        List<String> plist = new ArrayList<String>();
        for(Map<?,?> map : arenas.getMapList(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_TRIBS))){
            plist.add(Util.getKeys(map).get(0).toString());
        }
        return plist;
    }
    
    public static List<Player> getOnlineTribNames(String arenakey){
        List<Player> onlinetribs = new ArrayList<Player>();
        for(String trib_s : getTribNames(arenakey)){
            try{
                Player player = Bukkit.getPlayer(trib_s); 
                if(player!=null) onlinetribs.add(player);
            }catch(NullPointerException e){}
        }
        return onlinetribs;
    }
    
    public static List<Map<?, ?>> getTribs(String arenakey){
        return arenas.getMapList(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_TRIBS));
    }
    
    public static Location getTribSpawn(String arenakey, String trib){
        for(Map<?, ?> maps : getTribs(arenakey)){
            if(maps.containsKey(trib.toString())) return Util.parseLocKey(maps.get(trib.toString()).toString(), getCenter(arenakey).getWorld());
        }
        return null;
    }
    
    public static boolean isInGame(String arenakey){
        return arenas.getBoolean(YMLKeys.getArenaSubkey(arenakey,YMLKeys.ARN_INGAME));
    }
    
    public static void setCenter(String arenakey, Location loc){
        ArenaIO.arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_CENTER_WRLD), loc.getWorld().getName());
        ArenaIO.arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_CENTER_X), loc.getX());
        ArenaIO.arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_CENTER_Y), loc.getY());
        ArenaIO.arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_CENTER_Z), loc.getZ());
    }
    
    public static void setLounge(String arenakey, Location loc){
        ArenaIO.arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_LOUNGE_WRLD), loc.getWorld().getName());
        ArenaIO.arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_LOUNGE_X), loc.getX());
        ArenaIO.arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_LOUNGE_Y), loc.getY());
        ArenaIO.arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_LOUNGE_Z), loc.getZ());
        ArenaIO.arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_LOUNGE_YAW), loc.getPitch());
        ArenaIO.arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_LOUNGE_PITCH), loc.getYaw());
    }
    
    public static void setRadius(String arenakey, double radius){
        ArenaIO.arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_RADIUS), radius);
    }
    
    public static void setGMs(String arenakey, List<String> gms){
        ArenaIO.arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_GMS), gms);
    }
    
    public static void addGM(String arenakey, String player){
    	List<String> gms = getGMs(arenakey);
    	gms.add(player);
    	setGMs(arenakey, gms);
    }
    
    public static void addGM(String arenakey, Player player){
        addGM(arenakey, player.getName());
    }
    
    public static void removeGM(String arenakey, String player){
    	List<String> gms = getGMs(arenakey);
    	gms.remove(player);
    	setGMs(arenakey, gms);
    }
    
    public static void setTribs(String arenakey, List<Map<?, ?>> tribs){
        ArenaIO.arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_TRIBS), tribs);
    }
    
    public static void setTribLoc(String arenakey, int tribID, String lockey){
        List<Map<?,?>> maplist = getTribs(arenakey);
        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>) maplist.get(tribID);
        map.put(Util.getKeys(map).get(0).toString(), lockey);
        maplist.remove(tribID);
        maplist.add(tribID, map);
        setTribs(arenakey, maplist);
    }
    
    public static void setTribLoc(String arenakey, int tribID, Location loc){
        setTribLoc(arenakey, tribID, Util.toLocKey(loc, true));
    }
    
    public static void addTrib(String arenakey, Map<String, String> entry){
    	List<Map<?, ?>> tribs = getTribs(arenakey);
    	tribs.add(entry);
    	setTribs(arenakey, tribs);
    }
    
    public static void addTrib(String arenakey, String player){
        Map<String, String> map = new HashMap<String, String>();
        map.put(player, "null");
        addTrib(arenakey, map);
        ArenaUtil.updateTribLocs(arenakey);
    }
    
    public static void addTrib(String arenakey, Player player){
        addTrib(arenakey, player.getName());
    }
    
    public static void removeTrib(String arenakey, String player, boolean from_game){
        Map<?, ?> map = null;
        List<Map<?, ?>> tribs = getTribs(arenakey);
        for(Map<?, ?> maps : tribs){
            if(maps.containsKey(player)) map = maps;
        }
        tribs.remove(map);
        setTribs(arenakey, tribs);
        if(from_game){
            Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+player+" is no longer a tribute for "+arenakey);
        }
    }
    
    public static void removeTrib(String arenakey, Player player, boolean from_game){
        removeTrib(arenakey, player.getName(), from_game);
    }
    
    
    public static void setInGame(String arenakey, boolean ingame){
        ArenaIO.arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_INGAME), ingame);
        List<String> currentgames = arenas.getStringList(YMLKeys.CURRENT_GAMES.key());
        if(ingame) currentgames.add(arenakey); else currentgames.remove(arenakey);
        ArenaIO.arenasSet(YMLKeys.CURRENT_GAMES.key(), currentgames);
    }
    
    public static List<Map<?,?>> getNonCustomTribs(String arenakey){
        List<Map<?,?>> maplist = getTribs(arenakey);
        for(int i=0;i<maplist.size();i++){
            Map<?,?> maps = maplist.get(i);
            if(Util.getValues(maps).get(0).toString().contains("<<custom>>"))
                maplist.remove(i);
        }
        return maplist;
    }
    
    public static boolean hasLounge(String arenakey){
        return getLounge(arenakey)!=null;
    }
    
    
    public static boolean isWithinArena(String arenakey, Location loc){
        if(getCenter(arenakey).getWorld()!=loc.getWorld()) return false;
        if(loc.distance(getCenter(arenakey))<=getRadius(arenakey)) return true; else return false; 
    }
    
    public static String getNearbyArena(Location loc){
        for(String arenakey : ArenaIO.getArenasKeys())
            if(isWithinArena(arenakey, loc)) return arenakey;
        return null;
    }
    
    public static boolean isTribute(String arenakey, String player){
        return getTribNames(arenakey).contains(player);
    }
    
    public static boolean isTribute(String arenakey, Player player){
        return isTribute(arenakey, player.getName());
    }
    
    public static String getArenaByTrib(Player player){
        for(String arenakey : ArenaIO.getArenasKeys()){
            if(isTribute(arenakey, player)) return arenakey;
        }
        return null;
    }
    
    public static boolean isGM(String arenakey, String player){
        return getGMs(arenakey).contains(player);
    }
    
    public static boolean isGM(String arenakey, Player player){
        return isGM(arenakey, player.getName());
    }
    
    public static String getArenaByGM(Player player){
        for(String arenakey : ArenaIO.getArenasKeys())
            if(isGM(arenakey, player)) return arenakey;
        return null;
    }

    public static int getGameCount() {
        return arenas.getInt(YMLKeys.GAME_COUNT.key());
    }
    
    public static boolean isInCountdown(String arenakey){
        return arenas.getInt(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_INCOUNTDOWN))>0 &&
            arenas.get(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_INCOUNTDOWN))!=null
        ;
    }
    
    public static boolean isGameMakersArena(CommandSender sender, String arena) {
        FileConfiguration arenas = hungergames.getArenasFile();
        if(sender instanceof Player){
            String arenakey = ArenaIO.getArenaByKey(arena);
            if(arenakey!=null){
                if(arenas.getStringList(YMLKeys.ARENAS.key()+arenakey+YMLKeys.ARN_GMS.key()).contains(sender.getName())) return true;
            }
        }
        return false;
    }
}
