package com.acuddlyheadcrab.MCHungerGames.commands;

import java.util.HashSet;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.acuddlyheadcrab.MCHungerGames.HungerGames;
import com.acuddlyheadcrab.MCHungerGames.chests.ChestHandler;
import com.acuddlyheadcrab.util.Perms;
import com.acuddlyheadcrab.util.PluginInfo;
import com.acuddlyheadcrab.util.Util;





public class CornucopiaCommand implements CommandExecutor{
    
    @SuppressWarnings("unused")
    private static HungerGames hungergames;
    public CornucopiaCommand(HungerGames instance){hungergames = instance;}
    
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
                try{
                    int x = Integer.parseInt(args[0]);
                    double dist = Double.parseDouble(args[1]);
                    
                    List<Location> loclist = Util.getSurroundingLocs(player.getLocation(), x, dist);
                    
                    for(Location loc : loclist){
                        loc.getBlock().setTypeIdAndData(35, (byte) 14, false);
                    }
                    
                }catch(IndexOutOfBoundsException e){
                    return false;
                }catch(NumberFormatException e){
                    return false;
                }
            }
        }
        
        return true;
    }
    
    
        
}