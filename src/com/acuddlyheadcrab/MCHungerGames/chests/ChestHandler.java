package com.acuddlyheadcrab.MCHungerGames.chests;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.acuddlyheadcrab.MCHungerGames.HungerGames;
import com.acuddlyheadcrab.util.Util;


public class ChestHandler {
    
public static Map<Material, Integer> matwhitelist;
    
    public static HungerGames HungerGamesPlugin;
    public ChestHandler(HungerGames instance) {
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
    }
    
    
    public static boolean spawnCCPChest(Block block, boolean addtochest){
        Block above = block.getRelative(BlockFace.UP);
        BlockState state = null;
        if(block.getType()==Material.CHEST){
            state = block.getState();
            if(!addtochest) ((Chest) state).getInventory().clear();
        }
        else if(checkChestBlock(above)) {
            above.setType(Material.CHEST);
            state = above.getState();
        }
        else return false;
        
        Chest chest = (Chest) state;
        Inventory inv = chest.getInventory();
        
        Random rand = new Random();
        
        for(int c=0;c<rand.nextInt(inv.getSize());c++){
            ItemStack randitem = getRandomItem();
            inv.setItem(rand.nextInt(inv.getSize()), randitem);
        }
        
        return true;
    }
    
    public static ItemStack getRandomItem(){
        Random rand = new Random();
        Material mat = (Material) Util.getKeys(matwhitelist).get(rand.nextInt(matwhitelist.size()));
        int max = rand.nextInt(mat.getMaxStackSize());
        short dmg = mat.getMaxDurability()==((short) 0) ? (short) 0 : (short) rand.nextInt(mat.getMaxDurability());
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
}
