package com.acuddlyheadcrab.MCHungerGames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import com.acuddlyheadcrab.util.YMLKeys;
import com.acuddlyheadcrab.util.Utility;

public class Arenas {
    
	private static FileConfiguration arenas;
    public static HungerGames hungergames;
    public Arenas(HungerGames instance){
    	hungergames = instance;
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
    
    public static void initFiles(){
        arenas = hungergames.getArenasFile();
    }
    
    public static void deleteArena(String arenakey){
    	arenasSet(YMLKeys.ARENAS+arenakey, null);
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
        if(world==null) throw new NullPointerException("Could not find world \""+worldstring+"\"");
        double
            x = arenas.getDouble(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_LOUNGE_X)),
            y = arenas.getDouble(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_LOUNGE_Y)),
            z = arenas.getDouble(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_LOUNGE_Z))
        ;
        float
            pitch = (float) arenas.getDouble(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_LOUNGE_PITCH)),
            yaw = (float) arenas.getDouble(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_LOUNGE_YAW))
        ;
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
            plist.add(Utility.getKeys(map).get(0).toString());
        }
        return plist;
    }
    
    public static List<Map<?, ?>> getTribs(String arenakey){
        return arenas.getMapList(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_TRIBS));
    }
    
    public static List<Player> getOnlineTribs(String arenakey){
        List<Player> onlinetribs = new ArrayList<Player>();
        for(String trib_s : getTribNames(arenakey)){
            try{
                Player player = Bukkit.getPlayer(trib_s); 
                if(player!=null) onlinetribs.add(player);
            }catch(NullPointerException e){}
        }
        return onlinetribs;
    }
    
    public static Location getTribSpawn(String arenakey, String trib){
        for(Map<?, ?> maps : getTribs(arenakey)){
            if(maps.containsKey(trib.toString())) return Utility.parseLocKey(maps.get(trib.toString()).toString(), getCenter(arenakey).getWorld());
        }
        return null;
    }
    
    public static boolean isInGame(String arenakey){
        return arenas.getBoolean(YMLKeys.getArenaSubkey(arenakey,YMLKeys.ARN_INGAME));
    }
    
    public static void renameArena(String arenakey, String renameto){
        submitNewArena(renameto, getCenter(arenakey), getRadius(arenakey), getGMs(arenakey), getTribs(arenakey), isInGame(arenakey));
        deleteArena(arenakey);
        hungergames.saveArenas();
    }
    
    public static void setCenter(String arenakey, Location loc){
        arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_CENTER_WRLD), loc.getWorld().getName());
        arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_CENTER_X), loc.getX());
        arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_CENTER_Y), loc.getY());
        arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_CENTER_Z), loc.getZ());
    }
    
    
    public static void setLounge(String arenakey, Location loc){
        arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_LOUNGE_WRLD), loc.getWorld().getName());
        arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_LOUNGE_X), loc.getX());
        arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_LOUNGE_Y), loc.getY());
        arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_LOUNGE_Z), loc.getZ());
        arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_LOUNGE_YAW), loc.getPitch());
        arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_LOUNGE_PITCH), loc.getYaw());
    }
    
    public static void setRadius(String arenakey, double radius){
        arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_RADIUS), radius);
    }
    
    public static void setGMs(String arenakey, List<String> gms){
        arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_GMS), gms);
    }
    
    public static void addGM(String arenakey, String player){
    	List<String> gms = getGMs(arenakey);
    	gms.add(player);
    	setGMs(arenakey, gms);
    }
    
    public static void removeGM(String arenakey, String player){
    	List<String> gms = getGMs(arenakey);
    	gms.remove(player);
    	setGMs(arenakey, gms);
    }
    
    public static void setTribs(String arenakey, List<Map<?, ?>> tribs){
        arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_TRIBS), tribs);
    }
    
    public static void setTribLoc(String arenakey, int tribID, String lockey){
        List<Map<?,?>> maplist = getTribs(arenakey);
        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>) maplist.get(tribID);
        map.put(Utility.getKeys(map).get(0).toString(), lockey);
        maplist.remove(tribID);
        maplist.add(tribID, map);
        setTribs(arenakey, maplist);
    }
    
    public static void setTribLoc(String arenakey, int tribID, Location loc){
        setTribLoc(arenakey, tribID, Utility.toLocKey(loc, true));
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
        updateTribLocs(arenakey);
    }
    
    public static void updateTribLocs(String arenakey){
        // Eww ew ew. This is probably so inefficient, but oh well.
        List<Map<?,?>> 
            alltribs = getTribs(arenakey), 
            noncust_tribs = getNonCustomTribs(arenakey), 
            maplist = noncust_tribs
        ;
        List<Location> loclist = Utility.getSurroundingLocs(getCenter(arenakey), maplist.size(), 7);
        for(int i=0;i<maplist.size();i++){
            @SuppressWarnings("unchecked")
            Map<String,String> map = (Map<String, String>) maplist.get(i);
            map.put(Utility.getKeys(map).get(0).toString(), Utility.toLocKey(loclist.get(i), false));
        }
       alltribs.removeAll(noncust_tribs);
       alltribs.addAll(maplist);
       setTribs(arenakey, alltribs);
    }
    
    public static List<Map<?,?>> getNonCustomTribs(String arenakey){
        List<Map<?,?>> maplist = getTribs(arenakey);
        for(int i=0;i<maplist.size();i++){
            Map<?,?> maps = maplist.get(i);
            if(Utility.getValues(maps).get(0).toString().contains("<<custom>>"))
                maplist.remove(i);
        }
        return maplist;
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
        arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_INGAME), ingame);
        List<String> currentgames = arenas.getStringList(YMLKeys.CURRENT_GAMES.key());
        if(ingame) currentgames.add(arenakey); else currentgames.remove(arenakey);
        arenasSet(YMLKeys.CURRENT_GAMES.key(), currentgames);
    }
    
    
    public static void arenasSet(String path, Object object){
        arenas.set(path, object);
        hungergames.saveArenas();
    }
    
    public static boolean isWithinArena(String arenakey, Location loc){
        if(getCenter(arenakey).getWorld()!=loc.getWorld()) return false;
        if(loc.distance(getCenter(arenakey))<=getRadius(arenakey)) return true; else return false; 
    }
    
    public static String getNearbyArena(Location loc){
        for(String arenakey : Utility.getArenasKeys())
            if(isWithinArena(arenakey, loc)) return arenakey;
        return null;
    }
    
    public static boolean isTribute(String arenakey, Player player){
        for(String trib : getTribNames(arenakey))
            if(Bukkit.getPlayer(trib)!=null&&player.equals(Bukkit.getPlayer(trib))) return true;
        return false;
    }
    
    public static String getArenaByTrib(Player player){
        for(String arenakey : Utility.getArenasKeys())
            if(isTribute(arenakey, player)) return arenakey;
        return null;
    }
    
    public static boolean isGM(String arenakey, Player player){
        for(String gm : getGMs(arenakey))
            if(Bukkit.getPlayer(gm)!=null&&player.equals(Bukkit.getPlayer(gm))) return true;
        return false;
    }
    
    public static String getArenaByGM(Player player){
        for(String arenakey : Utility.getArenasKeys())
            if(isGM(arenakey, player)) return arenakey;
        return null;
    }

    // TODO redo this
    public static void tpAllOnlineTribs(String arenakey, boolean startinggame) {
        for(Player trib : getOnlineTribs(arenakey)){
//                TODO: add backup inventories IF startinggame
            trib.teleport(getTribSpawn(arenakey, trib.getName()));
            if(startinggame){
                trib.getInventory().clear();
                trib.setGameMode(GameMode.SURVIVAL);
            }
            trib.sendMessage(ChatColor.LIGHT_PURPLE+"You have been teleported to "+arenakey);
        }
    }
    
    public static void startGame(final String arenakey, int countdown){
        Arenas.tpAllOnlineTribs(arenakey, true);
        arenasSet(YMLKeys.GAME_COUNT.key(), getGameCount()+1);
        startCountdown(arenakey, countdown);
    }

    public static int getGameCount() {
        return arenas.getInt(YMLKeys.GAME_COUNT.key());
    }

    public static void initGames() {
        List<String> currentgames = arenas.getStringList(YMLKeys.CURRENT_GAMES.key());
//        checks for any extra games (aka removes unecessary)
        for(int c=0;c<currentgames.size();c++){
            String game = currentgames.get(c);
            boolean contains = Utility.getArenasKeys().contains(game), ingame = Arenas.isInGame(game);
            if(!contains||!ingame){
                currentgames.remove(c);
            }
        }
//        checks if any arenas are excluded (aka adds not included ones)
        for(String arena : Utility.getArenasKeys()){
            if(currentgames.contains(arena)){
                if(!Arenas.isInGame(arena)) currentgames.remove(arena);
            } else {
                if(Arenas.isInGame(arena)) currentgames.add(arena);
            }
        }
        arenasSet(YMLKeys.CURRENT_GAMES.key(), currentgames);
    }
    
    public static boolean isInCountdown(String arenakey){
        return arenas.getInt(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_INCOUNTDOWN))>0 &&
            arenas.get(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_INCOUNTDOWN))!=null
        ;
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
                    arenasSet(YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_INCOUNTDOWN), null);
                    setInGame(arenakey, true);
                    Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+arenakey+" is now in game!");
                } else countdown(arenakey);
            }
        }, 20, 20);
    }
    
    
    public static void countdown(String arenakey){
        String path = YMLKeys.getArenaSubkey(arenakey, YMLKeys.ARN_INCOUNTDOWN);
        arenasSet(path, arenas.getInt(path)-1);
    }
}
