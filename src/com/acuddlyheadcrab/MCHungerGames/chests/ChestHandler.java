package com.acuddlyheadcrab.MCHungerGames.chests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.acuddlyheadcrab.MCHungerGames.HGplugin;
import com.acuddlyheadcrab.MCHungerGames.FileIO.Configs;
import com.acuddlyheadcrab.MCHungerGames.FileIO.YMLKey;
import com.acuddlyheadcrab.util.PluginInfo;
import com.acuddlyheadcrab.util.Utility;


public class ChestHandler {
    
public static Map<Material, Integer> matwhitelist;
    
    public static HGplugin HGpluginPlugin;
    public static FileConfiguration chestitems;
    
    public ChestHandler(HGplugin instance) {
        HGpluginPlugin = instance;
        matwhitelist = new HashMap<Material, Integer>();
        matwhitelist.put(Material.ARROW, 12);
        matwhitelist.put(Material.ARROW, 12);
        matwhitelist.put(Material.ARROW, 12);
        matwhitelist.put(Material.ARROW, 12);
        matwhitelist.put(Material.ARROW, 12);
        matwhitelist.put(Material.ARROW, 12);
        matwhitelist.put(Material.ARROW, 12);
        matwhitelist.put(Material.ARROW, 12);
        matwhitelist.put(Material.BOW, 1);
        matwhitelist.put(Material.BOW, 1);
        matwhitelist.put(Material.BOW, 1);
        matwhitelist.put(Material.BOW, 1);
        matwhitelist.put(Material.BOW, 1);
        matwhitelist.put(Material.STONE_SWORD, 1);
        matwhitelist.put(Material.STONE_SWORD, 1);
        matwhitelist.put(Material.STONE_SWORD, 1);
        matwhitelist.put(Material.STONE_SWORD, 1);
        matwhitelist.put(Material.GOLD_SWORD, 1);
        matwhitelist.put(Material.GOLD_SWORD, 1);
        matwhitelist.put(Material.GOLD_SWORD, 1);
        matwhitelist.put(Material.GOLD_SWORD, 1);
        matwhitelist.put(Material.IRON_SWORD, 1);
        matwhitelist.put(Material.LEATHER_HELMET, 1);
        matwhitelist.put(Material.LEATHER_HELMET, 1);
        matwhitelist.put(Material.LEATHER_BOOTS, 1);
        matwhitelist.put(Material.LEATHER_BOOTS, 1);
        matwhitelist.put(Material.LEATHER_LEGGINGS, 1);
        matwhitelist.put(Material.LEATHER_LEGGINGS, 1);
        matwhitelist.put(Material.LEATHER_CHESTPLATE, 1);
        matwhitelist.put(Material.LEATHER_CHESTPLATE, 1);
        matwhitelist.put(Material.IRON_BOOTS, 1);
        matwhitelist.put(Material.IRON_LEGGINGS, 1);
        matwhitelist.put(Material.IRON_CHESTPLATE, 1);
        matwhitelist.put(Material.IRON_HELMET, 1);
        matwhitelist.put(Material.COMPASS, 1);
        matwhitelist.put(Material.COMPASS, 1);
        matwhitelist.put(Material.COMPASS, 1);
        matwhitelist.put(Material.COMPASS, 1);
        matwhitelist.put(Material.BREAD, 2);
        matwhitelist.put(Material.BREAD, 2);
        matwhitelist.put(Material.BREAD, 2);
        matwhitelist.put(Material.MUSHROOM_SOUP, 1);
    }
    
    
    public static void initChestItems(){
        chestitems = HGplugin.getChestItemsFile();
    }
    
    public static boolean spawnCCPChest(Block block, boolean addtochest){
        Block above = block.getRelative(BlockFace.UP);
        BlockState state = null;
        if(block.getType()==Material.CHEST){
            state = block.getState();
            if(!addtochest) ((Chest) state).getInventory().clear();
        } else if(checkChestBlock(above)) {
            above.setType(Material.CHEST);
            state = above.getState();
        } else return false;
        
        Chest chest = (Chest) state;
        Inventory inv = chest.getInventory();
        
        Random rand = new Random();
        
        int slots = Configs.getChestItems().getInt(YMLKey.CHESTITEMS_SLOTS);
        if(slots<=1) slots = 2;
        for(int c=0;c<rand.nextInt(slots-1)+1;c++){
            ItemStack randitem = getRandomItem();
            inv.setItem(rand.nextInt(inv.getSize()), randitem);
        }
        addChestLoc(chest.getLocation());
        return true;
    }
    
    public static ItemStack getRandomItem(){
        Random rand = new Random();
        Material mat = (Material) Utility.getKeys(matwhitelist).get(rand.nextInt(matwhitelist.size()));
        int max = rand.nextInt(matwhitelist.get(mat))+1;
        short dmg = mat.getMaxDurability()==((short) 0) ? (short) 0 : (short) (rand.nextInt(mat.getMaxDurability())+10);
        return new ItemStack(mat,max,dmg);
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
    
    public static void checkChestLocs(){
//        List<Location> loclist = getChestLocs();
//        for(int i=0;i<loclist.size();i++){
//            Location chestloc = loclist.get(i);
//            if(chestloc.getBlock().getState().getType()!=Material.CHEST)
//                System.out.println("Removing "+Utility.toLocKey(chestloc, false, true)+" from the chestloc list");
//                loclist.remove(i);
//        }
//        setChestLocs(loclist);
    }
    
    public static void addChestLoc(Location loc){
        List<Location> loclist = getChestLocs();
        if(!getChestLocs().contains(loc)){
            PluginInfo.sendPluginInfo("Added a chest location to chestitems.yml");
            loclist.add(loc);
        }
        setChestLocs(loclist);
    }
    
    public static void removeChestLoc(Location loc){
        List<Location> loclist = getChestLocs();
        if(getChestLocs().contains(loc)){
            PluginInfo.sendPluginInfo("Removed a chest location from chestitems.yml");
            loclist.remove(loc);
        }
        setChestLocs(loclist);
    }
    
    public static void removeChestLocKey(String lockey){
        List<String> loclist = getChestLocStrings();
        if(getChestLocs().contains(lockey)){
            PluginInfo.sendPluginInfo("Removed a chest location from chestitems.yml");
            loclist.remove(lockey);
        }
        setChestLocKeys(loclist);
    }
    
    public static void setChestLocKeys(List<String> loclist){
        chestitemsset(YMLKey.CHESTITEMS_CHESTLOCS.key(), loclist);
    }
    
    public static void setChestLocs(List<Location> loclist){
        List<String> lockeylist = new ArrayList<String>();
        for(Location loc : loclist)
            lockeylist.add(Utility.toLocKey(loc, false, true));
        setChestLocKeys(lockeylist);
    }
    
    public static void clearChestLocs(){
        setChestLocKeys(new ArrayList<String>());
    }
    
    public static List<Location> getChestLocs(){
        List<Location> loclist = new ArrayList<Location>();
        for(String lockey : getChestLocStrings()){
            loclist.add(Utility.parseLocKey(lockey));
        }
        return loclist;
    }
    
    public static List<String> getChestLocStrings(){
        return chestitems.getStringList(YMLKey.CHESTITEMS_CHESTLOCS.key());
    }
    
    
    public static void chestitemsset(String path, Object object) {
        chestitems.set(path, object);
        HGplugin.saveChestItems();
    }
    
    static int taskID;
    
    public static void resetChests(){
        checkChestLocs();
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(HGpluginPlugin, new Runnable() {
            private int count = 0;
            
            @Override
            public void run() {
                if(count<10){
                    for(Location chest : getChestLocs()){
                        spawnCCPChest(chest.getBlock(), chestitems.getBoolean(YMLKey.CHESTITEMS_ADDTO.key()));
                    }
                    count++;
                } else cancelChestTask();
            }
        }, 1, 1);
    }
    
    static void cancelChestTask(){
        Bukkit.getScheduler().cancelTask(taskID);
    }
}
