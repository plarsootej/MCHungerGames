package com.acuddlyheadcrab.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.acuddlyheadcrab.MCHungerGames.HGplugin;



public class Utility {
    
    public static HGplugin HGpluginPlugin;
    public Utility(HGplugin instance) {HGpluginPlugin = instance;}
    
    public final static Logger log = Logger.getLogger("Minecraft");
    public static FileConfiguration config;
    
    // done
    public static Map<String, String> getCommandsAndDescs() {
        Map<String, String> map = new HashMap<String, String>();
        List<?> cmds = getKeys(HGpluginPlugin.getDescription().getCommands());
        List<?> descs =  getValues(HGpluginPlugin.getDescription().getCommands());
        for (int c = 0; c < cmds.size(); c++) {
            String cmd = cmds.get(c).toString();
            String desc = " ";
            List<?> ks = getKeys((Map<?, ?>) descs.get(c));
            List<?> vs = getValues((Map<?, ?>) descs.get(c));
            for (int c2 = 0; c2 < ks.size(); c2++)
                if (ks.get(c2).toString().equalsIgnoreCase("description"))
                    desc = vs.get(c2).toString();
            map.put(cmd, desc);
        }
        return map;
    }
    

    public static List<?> getValues(Map<?, ?> map){
        List<Object> v_list = new ArrayList<Object>();
        Iterator<?> v_itr = map.values().iterator();
        while (v_itr.hasNext()) {
            Object ob = v_itr.next();
            v_list.add(ob);
        }
        return v_list;
    }
    
    public static List<?> getKeys(Map<?, ?> map){
        List<Object> k_list = new ArrayList<Object>();
        Iterator<?> k_itr = map.keySet().iterator();
        while (k_itr.hasNext()) {
            Object ob = k_itr.next();
            k_list.add(ob);
        }
        return k_list;
    }
    
    public static String toLocKey(Location loc, boolean custom, boolean world) {
        double x = loc.getX(), y = loc.getY(), z = loc.getZ();
        String key = (String.format("%1$s, %2$s, %3$s", x, y, z));
        if(custom) key = key.concat(", <<custom>>");
        if(world) key = key.concat(", "+loc.getWorld().getName());
        return key;
    }
    
    public static Location parseLocKey(String spawnkey, World world) {
        String[] sarr = spawnkey.split(", ");
        return new Location(world, Double.parseDouble(sarr[0]), Double.parseDouble(sarr[1]), Double.parseDouble(sarr[2]));
    }
    
    public static Location parseLocKey(String spawnkey) {
        String[] sarr = spawnkey.split(", ");
        World world = Bukkit.getWorlds().get(0);
        try{
            world = Bukkit.getWorld(sarr[3]);
        }catch(NullPointerException e){}
        return new Location(world, Double.parseDouble(sarr[0]), Double.parseDouble(sarr[1]), Double.parseDouble(sarr[2]));
    }


    public static String concatArray(Object[] array, String param) {
        return concatList(Arrays.asList(array), param);
    }
    
    public static String concatList(List<? extends Object> list, String separator){
        String returnable = "";
        for(int i=0;i<=list.size()-1;i++){
            Object object = list.get(i);
            if(i!=list.size()-1){
                returnable += object.toString().concat(separator);
            } else if(i==list.size()-1){
                returnable += object.toString();
            }
        }
        return returnable;
    }
    
    public static void repelPlayer(Player player, Location repulsive, int force){
        Location ploc = player.getLocation();
        Vector 
            direction = ploc.toVector().subtract(repulsive.toVector()),
            reverse = direction.multiply(force)
        ;
        player.setVelocity(reverse);
    }
    
    public static List<Location> getSurroundingLocs(Location origin, int x, double distance){
        List<Location> loclist = new ArrayList<Location>();
        for(int i=0;i<x;i++){
            Location newloc = origin.clone();
            newloc.setYaw((360/x)*i);
            newloc.setPitch(0);
            loclist.add(newloc.add(newloc.getDirection().multiply(distance)));
        }
        return loclist;
    }
    
}
