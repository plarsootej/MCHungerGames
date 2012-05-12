package com.acuddlyheadcrab.MCHungerGames.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import com.acuddlyheadcrab.MCHungerGames.HGplugin;
import com.acuddlyheadcrab.MCHungerGames.FileIO.YMLKey;
import com.acuddlyheadcrab.util.PluginInfo.MCHGCommandBranch;
import com.acuddlyheadcrab.util.Perms;
import com.acuddlyheadcrab.util.PluginInfo;





public class HungerGamesCommand implements CommandExecutor{
    
    private static HGplugin hungergames;
    public HungerGamesCommand(HGplugin instance){hungergames = instance;}
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
        FileConfiguration config = hungergames.getConfig();
        PluginInfo.printConsoleCommandInfo(sender, label, args);
        
        if(cmd.getName().equalsIgnoreCase("hungergames")){
            try{
                String arg1 = args[0];
                if(arg1.equalsIgnoreCase("reload")){
                    if(config.getBoolean(YMLKey.OPS_DEBUG_ONCMD.key())) PluginInfo.sendPluginInfo("Attempted /hg reload command");
                    if(sender.hasPermission(Perms.HG_RELOAD.perm())){
                        hungergames.saveConfig();
                        PluginInfo.sendPluginInfo("reloaded by "+sender.getName());
                        sender.sendMessage(ChatColor.GREEN+"Reloaded MCHungerGames");
                    } else PluginInfo.sendNoPermMsg(sender);
                }
            }catch(IndexOutOfBoundsException e){
                if(config.getBoolean(YMLKey.OPS_DEBUG_ONCMD.key())) PluginInfo.sendPluginInfo("Attempted to show main cmd tree help");
                PluginInfo.sendCommandUsage(MCHGCommandBranch.HG, sender);
            }
        }
        
        return true;
    }
        
}
