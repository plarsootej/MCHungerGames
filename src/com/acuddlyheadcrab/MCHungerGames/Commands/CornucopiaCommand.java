package com.acuddlyheadcrab.MCHungerGames.commands;

import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.acuddlyheadcrab.MCHungerGames.HungerGames;
import com.acuddlyheadcrab.util.Perms;
import com.acuddlyheadcrab.util.PluginInfo;
import com.acuddlyheadcrab.util.Utility;





public class CornucopiaCommand implements CommandExecutor{
    
    @SuppressWarnings("unused")
    private static HungerGames hungergames;
    public CornucopiaCommand(HungerGames instance){hungergames = instance;}
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
        
        boolean isplayer = sender instanceof Player;
        Player player = isplayer ? (Player) sender : null;
        
        if(isplayer) PluginInfo.sendPluginInfo(sender.getName()+": /"+label+Utility.concatArray(args, " "));
        
        if(cmd.getName().equalsIgnoreCase("spawnccp")){
            if(isplayer){
                if(sender.hasPermission(Perms.SPC.perm())){
                    
                    HashSet<Byte> bset = new HashSet<Byte>();
//                    adds water to transparent blocks
                    bset.add((byte) 9);
                    bset.add((byte) 8);
//                    and dont forget air
                    bset.add((byte) 0);
                    
                    String msg = Utility.spawnCCPChest(player.getTargetBlock(bset, 10)) ?
                            ChatColor.GREEN+"Spawned a chest" : ChatColor.LIGHT_PURPLE+"Cannot spawn chests next to double chests!";
                    player.sendMessage(msg);
                    
                } else PluginInfo.sendNoPermMsg(sender);
            } else PluginInfo.sendOnlyPlayerMsg(sender);
        }
        
        return true;
    }
        
}