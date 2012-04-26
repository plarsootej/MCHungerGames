package com.acuddlyheadcrab.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.acuddlyheadcrab.MCHungerGames.Arenas;
import com.acuddlyheadcrab.MCHungerGames.HungerGames;



public class Utility {
    
    public static Map<Material, Integer> matwhitelist;
    
    public static HungerGames HungerGamesPlugin;
    public Utility(HungerGames instance) {
        HungerGamesPlugin = instance;
        matwhitelist = new HashMap<Material, Integer>();
        for(Material mat : Material.values()) matwhitelist.put(mat, mat.getMaxStackSize());
        matwhitelist.remove(Material.AIR);
        matwhitelist.remove(Material.BED_BLOCK);
        matwhitelist.remove(Material.BEDROCK);
        matwhitelist.remove(Material.BREWING_STAND);
        matwhitelist.remove(Material.BURNING_FURNACE);
        matwhitelist.remove(Material.CAKE_BLOCK);
        matwhitelist.remove(Material.CAULDRON);
        matwhitelist.remove(Material.DRAGON_EGG);
        matwhitelist.remove(Material.ENDER_PORTAL);
        matwhitelist.remove(Material.ENDER_PORTAL_FRAME);
        matwhitelist.remove(Material.ENDER_STONE);
//        add more later... im tired
    }
    public final static Logger log = Logger.getLogger("Minecraft");
    public static FileConfiguration config;
    
    // done
    public static Map<String, String> getCommandsAndDescs() {
        Map<String, String> map = new HashMap<String, String>();
        List<?> cmds = getKeys(HungerGamesPlugin.getDescription().getCommands());
        List<?> descs =  getValues(HungerGamesPlugin.getDescription().getCommands());
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

    public static List<String> getArenasKeys() {
        FileConfiguration arenas = HungerGamesPlugin.getArenasFile();
        
        Set<String> str_set = arenas.getConfigurationSection("Arenas").getKeys(false);
        String[] keyarr = new String[str_set.size()];
        keyarr = str_set.toArray(keyarr);
        
        return Arrays.asList(keyarr);
    }
    
    public static String toLocKey(Location loc, boolean custom) {
        double x = loc.getX(), y = loc.getY(), z = loc.getZ();
        String key = (String.format("%1$s, %2$s, %3$s", x, y, z));
        if(custom) key = key.concat(", <<custom>>");
        return key;
    }
    
    public static Location parseLocKey(String spawnkey, World world) {
        String[] sarr = spawnkey.split(", ");
        return new Location(world, Double.parseDouble(sarr[0]), Double.parseDouble(sarr[1]), Double.parseDouble(sarr[2]));
    }
    
    public static boolean spawnCCPChest(Block block){
        Block above = block.getRelative(BlockFace.UP);
        BlockState state = null;
        if(block.getType()==Material.CHEST) state = block.getState(); 
        else if(checkChestBlock(above)) {
            above.setType(Material.CHEST);
            state = above.getState();
        }
        else return false;
        
        Chest chest = (Chest) state;
        Inventory inv = chest.getInventory();
        
        Random rand = new Random();
        
        for(int c=0;c<rand.nextInt(inv.getSize());c++){
            inv.setItem(rand.nextInt(inv.getSize()), getRandomItem());
        }
        
        return true;
    }
    
    public static ItemStack getRandomItem(){
        Random rand = new Random();
        Material mat = Material.values()[rand.nextInt(Material.values().length)];
        int max = Math.abs(mat.getMaxDurability());
        System.out.println(max);
        return new ItemStack(
            mat,
            rand.nextInt(mat.getMaxStackSize()),
            (short) Math.abs(rand.nextInt(max))
        );
    }
    
    
    public static boolean checkChestBlock(Block block){
        Block
            nblock = block.getRelative(BlockFace.NORTH),
            eblock = block.getRelative(BlockFace.EAST),
            sblock = block.getRelative(BlockFace.SOUTH),
            wblock = block.getRelative(BlockFace.WEST)
        ;
        boolean
            empty = block.isEmpty(),
            liquid = block.isLiquid(),
            grass = block.getType()==Material.GRASS,
            brownshroom = block.getType()==Material.BROWN_MUSHROOM,
            redshroom = block.getType()==Material.RED_MUSHROOM,
            rose = block.getType()==Material.RED_ROSE,
            flower= block.getType()==Material.YELLOW_FLOWER,
            vine = block.getType()==Material.VINE,
            fire = block.getType()==Material.FIRE,
            transparent =empty||liquid||grass||brownshroom||redshroom||rose||flower||vine||fire,   
            doublechest = isDoubleChest(nblock)||isDoubleChest(eblock)||isDoubleChest(sblock)||isDoubleChest(wblock)
        ;
        
        return transparent&&!doublechest;
    }
    
    public static boolean isDoubleChest(Block block){
        if(block.getType()==Material.CHEST){
            Chest chest = (Chest) block.getState();
            return chest.getInventory().getHolder().getClass().equals(DoubleChest.class);
        } else return false;
    }
    
    public static String getArenaByKey(String arg1) {
        for(String arenas : getArenasKeys()){
            if(arg1.equalsIgnoreCase(arenas)) return arenas;
        }
        return null;
    }
    
    public static boolean isGameMakersArena(CommandSender sender, String arena) {
        FileConfiguration arenas = HungerGamesPlugin.getArenasFile();
        
        if(sender instanceof Player){
            String arenakey = getArenaByKey(arena);
            if(arenakey!=null){
                if(arenas.getStringList(YMLKeys.ARENAS.key()+arenakey+YMLKeys.ARN_GMS.key()).contains(sender.getName())) return true;
            }
        }
        return false;
    }


    public static String concatArray(String[] array, String param) {
        return concatList(Arrays.asList(array), param);
    }


    public static String concatList(List<String> list, String param) {
        String returnable = "";
        for(String i : list)
            returnable = returnable.concat(param+i);
        return returnable;
    }
    
    public static void repelPlayer(Player player, Location repulsive, int force){
        Location ploc = player.getLocation();
        Vector 
            direction = ploc.toVector().subtract(repulsive.toVector()),
            reverse = direction.multiply(force)
        ;
//        bug: if the player is looking away from the boundry, they will be pushed back into the boundry when they hit it.
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
    
    public enum ChatProximity{
        SELF(0),
        GLOBAL(0),
        CLEAR(0),
        DISEMBODIED(0),
        GARBLED(0),
        INAUDIBLE(0);
        
        private double distance;
        
        private ChatProximity(double dist) {
            distance = dist;
        }

        public double getDistance() {
            return distance;
        }
        
        public void setDistance(final double newdist){
            this.distance = newdist;
        }
        
        public static ChatProximity withDistance(ChatProximity prox, final double dist){
            prox.setDistance(dist);
            return prox;
        }
    }
    
    public static ChatProximity getChatProximity(Player talkingplayer, Player recip){
        String arenakey = Arenas.getArenaByTrib(recip);
        if(talkingplayer==recip) return ChatProximity.SELF;
        if(arenakey!=null){
            if(Arenas.isInGame(arenakey)){
                double 
                    distance = recip.getLocation().distance(talkingplayer.getLocation()),
                    clear = config.getDouble(YMLKeys.OPS_NEARCHAT_DISTS_CLEAR.key()),
                    
                    disemboided = config.getDouble(YMLKeys.OPS_NEARCHAT_DISTS_DISEMBOIDED.key()),
                    garbled = config.getDouble(YMLKeys.OPS_NEARCHAT_DISTS_GARBLED.key())
                ;
                if(distance<=clear) return ChatProximity.withDistance(ChatProximity.CLEAR, distance);
                else if(distance<=disemboided) return ChatProximity.withDistance(ChatProximity.DISEMBODIED, distance);
                else if(distance<=garbled) return ChatProximity.withDistance(ChatProximity.GARBLED, distance);
                else return ChatProximity.withDistance(ChatProximity.INAUDIBLE, distance);
            }
        }
        return ChatProximity.GLOBAL;
    }


    public static void sendChatProxMessage(Player recip, Player talkingplayer, String msg, String format) {
        switch (Utility.getChatProximity(talkingplayer, recip)) {
            case SELF: recip.sendMessage(format); break; 
            case CLEAR: recip.sendMessage(format); break;
            case GLOBAL: recip.sendMessage(format); break;
            case DISEMBODIED: recip.sendMessage(format.replaceAll(talkingplayer.getName(), garblifyString(talkingplayer.getName(), ChatColor.GRAY))); break;
            case GARBLED: recip.sendMessage(format.replaceAll(talkingplayer.getName(), garblifyString(talkingplayer.getName(), ChatColor.DARK_GRAY)).replaceAll(msg, garblifyString(msg, ChatColor.GRAY))); break;
            default: break;
        }
    }
    
    public static String garblifyString(String string, ChatColor color){
        Random rand = new Random();
//        I know its... ungainly... but hey, it works
        List<String> letters = new ArrayList<String>();
        for(char chr : string.toCharArray()){
                String 
                    char_s = String.valueOf(chr),
                    newchar = rand.nextBoolean() ? color+""+ChatColor.MAGIC+char_s+ChatColor.RESET : color+char_s;
                ;
//                this gives a 1/10 chance that the letter will just not be added
                if(rand.nextInt(10)!=1){
                    letters.add(newchar);
                }
        }
        return concatList(letters, "");
    }
    
}
