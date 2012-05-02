package com.acuddlyheadcrab.MCHungerGames.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import com.acuddlyheadcrab.MCHungerGames.Arenas;
import com.acuddlyheadcrab.MCHungerGames.HungerGames;
import com.acuddlyheadcrab.util.YMLKeys;
import com.acuddlyheadcrab.util.Perms;
import com.acuddlyheadcrab.util.PluginInfo;
import com.acuddlyheadcrab.util.Util;





public class HGGameCommand implements CommandExecutor{
    
    private static HungerGames hungergames;
    public HGGameCommand(HungerGames instance){hungergames = instance;}
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
        
        FileConfiguration config = hungergames.getConfig();
        FileConfiguration arenasfile = hungergames.getArenasFile();
        
        PluginInfo.printConsoleCommandInfo(sender, label, args);
        
        if(cmd.getName().equalsIgnoreCase("hggame")){
            try{
                String arg1 = args[0];
                
                boolean
                    start = arg1.equalsIgnoreCase("start"),
                    stop = arg1.equalsIgnoreCase("stop")
                ;
                
                if(start){
                    if(config.getBoolean(YMLKeys.OPS_DEBUG_ONCMD.key())) PluginInfo.sendPluginInfo("Attempted /hgg start command");
                    try{
                        String arg2 = args[1];
                        
                        final String arenakey = Util.getArenaByKey(arg2);
                        
                        if(arenakey!=null){
                            if(sender.hasPermission(Perms.HGG_START.perm())||Util.isGameMakersArena(sender, arenakey)){

                                
                                try{
                                    final int countdown = Integer.parseInt(args[2]);
                                    final ChatColor color = ChatColor.YELLOW;
                                    
                                    Bukkit.broadcastMessage(color+"Hunger Games are starting in the arena "+arenakey+" in "+countdown+" seconds");
                                    
                                    try{
                                        Arenas.startGame(arenakey, countdown);
                                        return true;
//                                        the following probably wont happen (right now)
                                    }catch(NullPointerException e){
                                        PluginInfo.wrongFormatMsg(sender, arenakey+" does not have enough data to start a game");
                                        e.printStackTrace();
                                        return true;
                                    }
                                }catch(IndexOutOfBoundsException e){
                                    PluginInfo.wrongFormatMsg(sender, "/hgg start <arena> [countdown (seconds]"); return true;
                                }
                            } else PluginInfo.sendNoPermMsg(sender);
                        } else PluginInfo.wrongFormatMsg(sender, "Could not find the arena \""+arg2+"\"");
                    }catch(IndexOutOfBoundsException e){PluginInfo.wrongFormatMsg(sender, "/hgg start <arena> [countdown (seconds]");}
                    return true;
                }
                
                if(stop){
                    if(config.getBoolean(YMLKeys.OPS_DEBUG_ONCMD.key())) PluginInfo.sendPluginInfo("Attempted /hgg stop command");
                    try{
                        String arg2 = args[1];
                        
                        String arenakey = Util.getArenaByKey(arg2);
                        
                        if(arenakey!=null){
                            if(sender.hasPermission(Perms.HGG_STOP.perm())||Util.isGameMakersArena(sender, arenakey)){
                                
                            	Arenas.setInGame(arenakey, false);
                                List<String> games = arenasfile.getStringList(YMLKeys.CURRENT_GAMES.key());
                                games.remove(arenakey);
                                Arenas.arenasSet(YMLKeys.CURRENT_GAMES.key(), games);
                                Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"Force stopped game in "+arenakey);
                            } else PluginInfo.sendNoPermMsg(sender);
                        } else PluginInfo.wrongFormatMsg(sender, "Could not find the arena \""+arg2+"\"");
                    }catch(IndexOutOfBoundsException e){PluginInfo.wrongFormatMsg(sender, "/hgg stop <arena>");}
                    return true;
                }
                
                
                
            }catch(IndexOutOfBoundsException e){
                String cur_games = arenasfile.getString(YMLKeys.CURRENT_GAMES.key());
                cur_games = cur_games.trim();
                if(cur_games==""||cur_games=="[]"){
                    cur_games = ChatColor.DARK_GRAY+"(none)";
                }
                sender.sendMessage(ChatColor.GOLD+"Current Games: "+cur_games);
            }
        }
        
        return true;
    }
        
}