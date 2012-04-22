package com.acuddlyheadcrab.MCHungerGames;

import java.util.ArrayList;
import java.util.List;
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
    
    public static void submitNewArena(String name, Location center, double radius, List<String> gms, List<String> tribs, boolean ingame){
    	String arenapath = YMLKeys.ARENAS.key()+name;
    	arenas.set(arenapath, null);
    	arenas.set(getPathType(name, "center.World"), center.getWorld().getName());
    	arenas.set(getPathType(name, "center.x"), center.getX());
    	arenas.set(getPathType(name, "center.y"), center.getY());
    	arenas.set(getPathType(name, "center.z"), center.getZ());
    	arenas.set(getPathType(name, "radius"), radius);
    	arenas.set(getPathType(name, "gms"), gms);
    	arenas.set(getPathType(name, "tribs"), tribs);
    	arenas.set(getPathType(name, "ingame"), ingame);
    	hungergames.saveArenas();
    }
    
    public static void initFiles(){
        arenas = hungergames.getArenasFile();
    }
    
    public static void deleteArena(String arenakey){
    	arenasSet(getPathType(arenakey, "self"), null);
    }
    
    public static Location getCenter(String arenakey){
        String worldstring = arenas.getString(getPathType(arenakey, "center.world"));
        World world = Bukkit.getWorld(worldstring);
        if(world==null) throw new NullPointerException("Could not find world \""+worldstring+"\"");
        double
            x = arenas.getDouble(getPathType(arenakey, "center.x")),
            y = arenas.getDouble(getPathType(arenakey, "center.y")),
            z = arenas.getDouble(getPathType(arenakey, "center.z"))
        ;
        Location center = new Location(world, x, y, z);
        return center;
    }
    
    public static Location getLounge(String arenakey){
        String worldstring = arenas.getString(getPathType(arenakey, "lounge.world"));
        World world = Bukkit.getWorld(worldstring);
        if(world==null) throw new NullPointerException("Could not find world \""+worldstring+"\"");
        double
            x = arenas.getDouble(getPathType(arenakey, "lounge.x")),
            y = arenas.getDouble(getPathType(arenakey, "lounge.y")),
            z = arenas.getDouble(getPathType(arenakey, "lounge.z"))
        ;
        float
            pitch = (float) arenas.getDouble(getPathType(arenakey, "lounge.pitch")),
            yaw = (float) arenas.getDouble(getPathType(arenakey, "lounge.yaw"))
        ;
        Location lounge = new Location(world, x, y, z, yaw, pitch);
        return lounge;
    }
    
    public static double getRadius(String arenakey){
        return arenas.getDouble(getPathType(arenakey, "radius"));
    }
    
    public static List<String> getGMs(String arenakey){
        return arenas.getStringList(getPathType(arenakey, "gms"));
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
    
    public static List<String> getTribs(String arenakey){
        return arenas.getStringList(getPathType(arenakey, "tribs"));
    }
    
    public static List<Player> getOnlineTribs(String arenakey){
        List<Player> onlinetribs = new ArrayList<Player>();
        for(String trib_s : getTribs(arenakey)){
            try{
                Player player = Bukkit.getPlayer(trib_s); 
                if(player!=null) onlinetribs.add(player);
            }catch(NullPointerException e){}
        }
        return onlinetribs;
    }
    
    public static boolean isInGame(String arenakey){
        return arenas.getBoolean(getPathType(arenakey,"ingame"));
    }
    
    public static void renameArena(String arenakey, String renameto){
        submitNewArena(renameto, getCenter(arenakey), getRadius(arenakey), getGMs(arenakey), getTribs(arenakey), isInGame(arenakey));
        deleteArena(arenakey);
        hungergames.saveArenas();
    }
    
    public static void setCenter(String arenakey, Location loc){
        arenasSet(getPathType(arenakey, "center.world"), loc.getWorld().getName());
        arenasSet(getPathType(arenakey, "center.x"), loc.getX());
        arenasSet(getPathType(arenakey, "center.y"), loc.getY());
        arenasSet(getPathType(arenakey, "center.z"), loc.getZ());
    }
    
    
    public static void setLounge(String arenakey, Location loc){
        arenasSet(getPathType(arenakey, "lounge.world"), loc.getWorld().getName());
        arenasSet(getPathType(arenakey, "lounge.x"), loc.getX());
        arenasSet(getPathType(arenakey, "lounge.y"), loc.getY());
        arenasSet(getPathType(arenakey, "lounge.z"), loc.getZ());
        arenasSet(getPathType(arenakey, "lounge.pitch"), loc.getPitch());
        arenasSet(getPathType(arenakey, "lounge.yaw"), loc.getYaw());
    }
    
    public static void setRadius(String arenakey, double radius){
        arenasSet(getPathType(arenakey, "radius"), radius);
    }
    
    public static void setGMs(String arenakey, List<String> gms){
        arenasSet(getPathType(arenakey, "gms"), gms);
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
    
    public static void setTribs(String arenakey, List<String> tribs){
        arenasSet(getPathType(arenakey, "tribs"), tribs);
    }
    
    public static void addTrib(String arenakey, String player){
    	List<String> tribs = getTribs(arenakey);
    	tribs.add(player);
    	setTribs(arenakey, tribs);
    }
    
    public static void removeTrib(String arenakey, String player, boolean from_event){
    	List<String> tribs = getTribs(arenakey);
    	tribs.remove(player);
    	setTribs(arenakey, tribs);
    	if(from_event){
    	    Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+player+" is no longer in battle for "+arenakey+"!");
    	    Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"There are now "+getTribs(arenakey).size()+" tributes left for "+arenakey+"!");
    	}
    }
    
    public static void setInGame(String arenakey, boolean ingame){
        arenasSet(getPathType(arenakey, "ingame"), ingame);
        List<String> currentgames = arenas.getStringList(YMLKeys.CURRENT_GAMES.key());
        if(ingame) currentgames.add(arenakey); else currentgames.remove(arenakey);
        arenasSet(YMLKeys.CURRENT_GAMES.key(), currentgames);
    }
    
    
    public static void arenasSet(String path, Object object){
        arenas.set(path, object);
        hungergames.saveArenas();
    }
    
    public static String getPathType(String arenakey, String type){
        String arenapath = YMLKeys.ARENAS.key()+arenakey;
        if(type.equalsIgnoreCase("center.world")) return arenapath+YMLKeys.ARN_CENTER_WRLD.key();
        if(type.equalsIgnoreCase("center.x")) return arenapath+YMLKeys.ARN_CENTER_X.key();
        if(type.equalsIgnoreCase("center.y")) return arenapath+YMLKeys.ARN_CENTER_Y.key();
        if(type.equalsIgnoreCase("center.z")) return arenapath+YMLKeys.ARN_CENTER_Z.key();
        if(type.equalsIgnoreCase("lounge.world")) return arenapath+YMLKeys.ARN_LOUNGE_WRLD.key();
        if(type.equalsIgnoreCase("lounge.x")) return arenapath+YMLKeys.ARN_LOUNGE_X.key();
        if(type.equalsIgnoreCase("lounge.y")) return arenapath+YMLKeys.ARN_LOUNGE_Y.key();
        if(type.equalsIgnoreCase("lounge.z")) return arenapath+YMLKeys.ARN_LOUNGE_Z.key();
        if(type.equalsIgnoreCase("lounge.pitch")) return arenapath+YMLKeys.ARN_LOUNGE_PITCH.key();
        if(type.equalsIgnoreCase("lounge.yaw")) return arenapath+YMLKeys.ARN_LOUNGE_YAW.key();
        if(type.equalsIgnoreCase("radius"))return arenapath+YMLKeys.ARN_RADIUS.key();
        if(type.equalsIgnoreCase("gms"))return arenapath+YMLKeys.ARN_GMS.key();
        if(type.equalsIgnoreCase("tribs"))return arenapath+YMLKeys.ARN_TRIBS.key();
        if(type.equalsIgnoreCase("ingame"))return arenapath+YMLKeys.ARN_INGAME.key();
        if(type.equalsIgnoreCase("countdown"))return arenapath+YMLKeys.ARN_INCOUNTDOWN.key();
        if(type.equalsIgnoreCase("self")) return arenapath;
        return null;
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
        for(String trib : getTribs(arenakey))
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

    public static void tpAllOnlineTribs(String arenakey, boolean startinggame) {
        for(Player trib : getOnlineTribs(arenakey)){
//                TODO: add backup inventories IF startinggame
            Location 
                center  = getCenter(arenakey), 
                rand = Utility.getRandomChunkLocation(center, 5)
            ;
            trib.teleport(rand);
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
        return arenas.getInt(getPathType(arenakey, "countdown"))>0 &&
            arenas.get(getPathType(arenakey, "countdown"))!=null
        ;
    }
    
    public static void startCountdown(final String arenakey, final int seconds){
//        initialize path
        System.out.println("Setting "+arenakey+" in countdown with "+seconds+" to go!");
        arenas.set(getPathType(arenakey, "countdown"), seconds);
//        cycle through each second
        Bukkit.getScheduler().scheduleSyncRepeatingTask(hungergames, new Runnable() {
            @Override
            public void run() {
                int second = arenas.getInt(getPathType(arenakey, "countdown"));
                Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"[MCHungerGames] "+second);
                if(second==0){
                    Bukkit.getScheduler().cancelTasks(hungergames);
                    arenasSet(getPathType(arenakey, "countdown"), null);
                    setInGame(arenakey, true);
                    Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+arenakey+" is now in game!");
                } else countdown(arenakey);
            }
        }, 20, 20);
//        set arean in game here
    }
    
    public static void countdown(String arenakey){
        String path = getPathType(arenakey, "countdown");
        arenasSet(path, arenas.getInt(path)-1);
    }
}
