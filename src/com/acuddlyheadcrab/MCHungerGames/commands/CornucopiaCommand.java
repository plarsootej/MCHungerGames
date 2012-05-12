package com.acuddlyheadcrab.MCHungerGames.commands;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.acuddlyheadcrab.MCHungerGames.HGplugin;
import com.acuddlyheadcrab.MCHungerGames.FileIO.YMLKey;
import com.acuddlyheadcrab.MCHungerGames.arenas.ArenaIO;
import com.acuddlyheadcrab.MCHungerGames.arenas.Arenas;
import com.acuddlyheadcrab.MCHungerGames.chests.ChestHandler;
import com.acuddlyheadcrab.MCHungerGames.inventories.InventoryHandler;
import com.acuddlyheadcrab.util.Perms;
import com.acuddlyheadcrab.util.PluginInfo;





public class CornucopiaCommand implements CommandExecutor{
    
    private static HGplugin hungergames;
    public CornucopiaCommand(HGplugin instance){hungergames = instance;}
    
    private static boolean ingame = false;
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
        
        boolean isplayer = sender instanceof Player;
        Player player = isplayer ? (Player) sender : null;
        
        PluginInfo.printConsoleCommandInfo(sender, label, args);
        
        if(cmd.getName().equalsIgnoreCase("spawnccp")){
            if(isplayer){
                if(sender.hasPermission(Perms.SPC.perm())){
                    HashSet<Byte> bset = new HashSet<Byte>();
                    bset.add((byte) 9); bset.add((byte) 8); bset.add((byte) 0);
                    
                    
                    boolean addtochest = false;
                    
                    try{
                        addtochest = args[0].contains("+");
                    }catch(IndexOutOfBoundsException e){}
                    
                    String msg = ChestHandler.spawnCCPChest(player.getTargetBlock(bset, 10), addtochest) ?
                            ChatColor.GREEN+"Spawned a chest" : ChatColor.LIGHT_PURPLE+"Could not spawn chest!";
                    player.sendMessage(msg);
                    
                } else PluginInfo.sendNoPermMsg(sender);
            } else PluginInfo.sendOnlyPlayerMsg(sender);
        }
        
        if(cmd.getName().equalsIgnoreCase("testcmd")){
            if(isplayer){
                if(player.getName().equalsIgnoreCase("acuddlyheadcrab")){
                    try{
                        
                        if(args[0].equalsIgnoreCase("sim")){
                            
//                            try{
//                                if(args[1].equalsIgnoreCase("stop")){}
//                            }catch(IndexOutOfBoundsException e){}
                            if(ingame){}
                            String arenakey = Arenas.getArenaByTrib(player);
                            if(arenakey!=null){
                                startGame(arenakey, player);
                            }
                        }
                        
                        if(args[0].equalsIgnoreCase("save")){
                            InventoryHandler.saveInventory(player);
                            player.sendMessage(ChatColor.GREEN+"Saved your inventory to inventories.yml");
                            return true;
                        }
                        
                        if(args[0].equalsIgnoreCase("get")){
                            InventoryHandler.updateInventory(player);
                            player.sendMessage(ChatColor.GREEN+"Updated your inventory.");
                            return true;
                        }
                        
                    }catch(IndexOutOfBoundsException e){
                        e.printStackTrace();
                        return false;
                    }catch(NumberFormatException e){
                        e.printStackTrace();
                        return false;
                    }
                } else PluginInfo.sendNoPermMsg(sender);
            }
        }
        
        return true;
    }
    

    public static void startGame(final String arenakey, Player trib){
        InventoryHandler.saveInventory(trib);
        trib.getInventory().clear();
        trib.setGameMode(GameMode.SURVIVAL);
        ArenaIO.arenasSet(YMLKey.GAME_COUNT.key(), Arenas.getGameCount()+1);
        startSimCountdown(arenakey, trib);
    }
    
    private static int taskID;
    
//    TODO fix countdown
    public static void startSimCountdown(final String arenakey, final Player player){
        PluginInfo.sendPluginInfo("\tSIMULATION\n\t\tSetting "+arenakey+" in countdown with 10 to go!");
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(hungergames, new Runnable() {
            private int count = 10;
            @Override
            public void run() {
                player.sendMessage(ChatColor.LIGHT_PURPLE+"[MCHungerGames] "+count);
                if(count<=0){
                    ingame = true;
                    player.sendMessage(ChatColor.LIGHT_PURPLE+arenakey+" is now in game!");
                    cancelCountdownTask();
                } else count--;
            }
        }, 20, 20);
    }
    
    static void cancelCountdownTask(){
        Bukkit.getScheduler().cancelTask(taskID);
    }
        
}