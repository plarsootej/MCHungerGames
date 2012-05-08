package com.acuddlyheadcrab.MCHungerGames.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;

import com.acuddlyheadcrab.MCHungerGames.HungerGames;
import com.acuddlyheadcrab.MCHungerGames.arenas.Arenas;
import com.acuddlyheadcrab.MCHungerGames.chests.ChestHandler;
import com.acuddlyheadcrab.util.Util;
import com.acuddlyheadcrab.util.YMLKeys;
import com.acuddlyheadcrab.util.PluginInfo;


public class BlockListener implements Listener {
    public static HungerGames plugin;
    public static EventPriority priority;
    public BlockListener(HungerGames instance) {plugin = instance;}
    
    public static FileConfiguration config;
    // note to self: config is instantiated ONLY once initConfig() is called.
    public static void initConfig(){config = plugin.getConfig();}
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent e){
        Location loc = e.getBlock().getLocation();
        
        String lockey = Util.toLocKey(loc, false, true);
        if(ChestHandler.getChestLocStrings().contains(lockey)){
            ChestHandler.removeChestLocKey(lockey);
        }
        if(ChestHandler.getChestLocs().contains(loc)){
            ChestHandler.removeChestLoc(loc);
        }
        
//        I don't really know the 'rules' for the mushroom-leaf style games. Sorry? Deal wid it.
        if(config.getBoolean(YMLKeys.OPS_BP_MUSHLEAF.key())){
            String arenakey = Arenas.getNearbyArena(loc);
            if(arenakey!=null){
                Block block = e.getBlock();
                boolean
                    redmush = block.getType()==Material.RED_MUSHROOM,
                    brownmush =block.getType()==Material.BROWN_MUSHROOM,
                    mush = redmush||brownmush,
                    leaves = block.getType()==Material.LEAVES
                ;
                
                if(!(mush||leaves)){
                    if(config.getBoolean(YMLKeys.OPS_BP_SHOWWARN.key()))
                        e.getPlayer().sendMessage(ChatColor.RED+"You can only break mushrooms or leaves!");
                    e.setCancelled(true);
                }
            }
        }
        
        if(!config.getBoolean(YMLKeys.OPS_BP_RULES_BREAK.key())){
            String arenakey = Arenas.getNearbyArena(loc);
            if(arenakey!=null){
                if(config.getBoolean(YMLKeys.OPS_BP_CONDIT_INARENA.key())){
                    boolean onlyingame = config.getBoolean(YMLKeys.OPS_BP_CONDIT_INGAME.key());
                    if(onlyingame){
                        if(!Arenas.isInGame(arenakey)) 
//                            temp debug
                            if(config.getBoolean(YMLKeys.OPS_DEBUG_ONBLOCKCHANGE.key())) 
                                PluginInfo.sendPluginInfo("Returning, because arena "+arenakey+" is not in game");
                            return;
                    }
                    
                    if(config.getBoolean(YMLKeys.OPS_DEBUG_ONBLOCKCHANGE.key())) 
                        PluginInfo.sendPluginInfo("Cancelled BREAK "+e.getBlock().getType().toString()+" inside "+arenakey+" ("+e.getPlayer().getName()+")");
                    String 
                        suffix = onlyingame ? "while "+arenakey+" is in game!" : "inside "+arenakey+"!",
                        msg = ChatColor.RED+"You cannot break blocks "+suffix
                    ;
                    if(config.getBoolean(YMLKeys.OPS_BP_SHOWWARN.key()))
                        e.getPlayer().sendMessage(msg);
                    e.setCancelled(!config.getBoolean(YMLKeys.OPS_BP_RULES_BREAK.key()));
                    return;
                }
            } else {
                if(config.getBoolean(YMLKeys.OPS_BP_CONDIT_OUTARENA.key())){
                    if(config.getBoolean(YMLKeys.OPS_DEBUG_ONBLOCKCHANGE.key())) 
                        PluginInfo.sendPluginInfo("Cancelled BREAK "+e.getBlock().getType().toString()+" outside arenas ("+e.getPlayer().getName()+")");
                    String msg = ChatColor.RED+"You cannot break blocks outside of arenas!";
                    if(config.getBoolean(YMLKeys.OPS_BP_SHOWWARN.key()))
                        e.getPlayer().sendMessage(msg);
                    e.setCancelled(!config.getBoolean(YMLKeys.OPS_BP_RULES_BREAK.key()));
                    return;
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockCanBuild(BlockPlaceEvent e){
        Location loc = e.getBlock().getLocation();
        
        if(!config.getBoolean(YMLKeys.OPS_BP_RULES_BUILD.key())){
            String arenakey = Arenas.getNearbyArena(loc);
            if(arenakey!=null){
                if(config.getBoolean(YMLKeys.OPS_BP_CONDIT_INARENA.key())){
                    boolean onlyingame = config.getBoolean(YMLKeys.OPS_BP_CONDIT_INGAME.key());
                    if(onlyingame){
                        if(!Arenas.isInGame(arenakey)) 
                            if(config.getBoolean(YMLKeys.OPS_DEBUG_ONBLOCKCHANGE.key())) 
                                PluginInfo.sendPluginInfo("Returning, because arena "+arenakey+" is not in game");
                            return;
                    }
                    
                    if(config.getBoolean(YMLKeys.OPS_DEBUG_ONBLOCKCHANGE.key())) 
                        PluginInfo.sendPluginInfo("Cancelled BUILD "+e.getBlock().getType().toString()+" inside "+arenakey+" ("+e.getPlayer().getName()+")");
                    String 
                        suffix = onlyingame ? "while "+arenakey+" is in game!" : "inside "+arenakey+"!",
                        msg = ChatColor.RED+"You cannot place blocks "+suffix
                    ;
                    if(config.getBoolean(YMLKeys.OPS_BP_SHOWWARN.key()))
                        e.getPlayer().sendMessage(msg);
                    e.setCancelled(!config.getBoolean(YMLKeys.OPS_BP_RULES_BUILD.key()));
                    return;
                }
            } else {
                if(config.getBoolean(YMLKeys.OPS_BP_CONDIT_OUTARENA.key())){
                    if(config.getBoolean(YMLKeys.OPS_DEBUG_ONBLOCKCHANGE.key())) 
                        PluginInfo.sendPluginInfo("Cancelled BUILD "+e.getBlock().getType().toString()+" outside arenas ("+e.getPlayer().getName()+")");
                    String msg = ChatColor.RED+"You cannot place blocks outside of arenas!";
                    if(config.getBoolean(YMLKeys.OPS_BP_SHOWWARN.key()))    
                        e.getPlayer().sendMessage(msg);
                    e.setCancelled(!config.getBoolean(YMLKeys.OPS_BP_RULES_BUILD.key()));
                    return;
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCraft(CraftItemEvent e){
        Location loc = e.getWhoClicked().getLocation();
        
        if(!config.getBoolean(YMLKeys.OPS_BP_RULES_CRAFT.key())){
            String arenakey = Arenas.getNearbyArena(loc);
            if(arenakey!=null){
                if(config.getBoolean(YMLKeys.OPS_BP_CONDIT_INARENA.key())){
                    boolean onlyingame = config.getBoolean(YMLKeys.OPS_BP_CONDIT_INGAME.key());
                    if(onlyingame){
                        if(!Arenas.isInGame(arenakey)) 
                            if(config.getBoolean(YMLKeys.OPS_DEBUG_ONBLOCKCHANGE.key())) 
                                PluginInfo.sendPluginInfo("Returning, because arena "+arenakey+" is not in game");
                            return;
                    }
                    
                    if(config.getBoolean(YMLKeys.OPS_DEBUG_ONBLOCKCHANGE.key())) 
                        PluginInfo.sendPluginInfo("Cancelled CRAFT inside "+arenakey+" ("+e.getWhoClicked().getName()+")");
                    String 
                        suffix = onlyingame ? "while "+arenakey+" is in game!" : "inside "+arenakey+"!",
                        msg = ChatColor.RED+"You cannot craft "+suffix
                    ;
                    if(e.getWhoClicked() instanceof Player) ((Player) e.getWhoClicked()).sendMessage(msg);
                    e.setCancelled(!config.getBoolean(YMLKeys.OPS_BP_RULES_CRAFT.key()));
                    return;
                }
            } else {
                if(config.getBoolean(YMLKeys.OPS_BP_CONDIT_OUTARENA.key())){
                    if(config.getBoolean(YMLKeys.OPS_DEBUG_ONBLOCKCHANGE.key())) 
                        PluginInfo.sendPluginInfo("Cancelled CRAFT outside arenas ("+e.getWhoClicked().getName()+")");
                    String msg = ChatColor.RED+"You cannot craft outside of an arena!";
                    if(e.getWhoClicked() instanceof Player) ((Player) e.getWhoClicked()).sendMessage(msg);
                    e.setCancelled(!config.getBoolean(YMLKeys.OPS_BP_RULES_CRAFT.key()));
                    return;
                }
            }
        }
    }
}
