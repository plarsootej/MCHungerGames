package com.acuddlyheadcrab.MCHungerGames.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.acuddlyheadcrab.MCHungerGames.Arenas;
import com.acuddlyheadcrab.MCHungerGames.HungerGames;
import com.acuddlyheadcrab.util.PluginInfo.MCHGCommandBranch;
import com.acuddlyheadcrab.util.YMLKeys;
import com.acuddlyheadcrab.util.Perms;
import com.acuddlyheadcrab.util.PluginInfo;
import com.acuddlyheadcrab.util.Util;

public class HGArenaEditCommand implements CommandExecutor{
    
	private static HungerGames hungergames;
    public HGArenaEditCommand(HungerGames instance){hungergames = instance;}
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
        
        FileConfiguration config = hungergames.getConfig();
        
        boolean isplayer = sender instanceof Player;
        Player player = isplayer ? (Player) sender : null;
        PluginInfo.printConsoleCommandInfo(sender, label, args);

        if(cmd.getName().equalsIgnoreCase("hgaedit")){
            try{
                String arg1 = args[0];
                
                String arenakey = Util.getArenaByKey(arg1);
                
                if(arenakey!=null){
                    try{
                        String arg2 = args[1];
                        
                        boolean
                            corncp = arg2.equalsIgnoreCase("cornucopia")||arg2.equalsIgnoreCase("corncp")||arg2.equalsIgnoreCase("ccp"),
                            setccp = arg2.equalsIgnoreCase("setcornucopia")||arg2.equalsIgnoreCase("setcorncp")||arg2.equalsIgnoreCase("setccp")||corncp,
                            setlounge = arg2.equalsIgnoreCase("setlounge")||arg2.equalsIgnoreCase("lounge"),
                            radius = arg2.equalsIgnoreCase("radius")||arg2.equalsIgnoreCase("radius"),
                            addgm = arg2.equalsIgnoreCase("addgm"),
                            addtrib = arg2.equalsIgnoreCase("addtrib"),
                            removegm = arg2.equalsIgnoreCase("removegm"),
                            removetrib = arg2.equalsIgnoreCase("removetrib"),
                            settribspawn = arg2.equalsIgnoreCase("settribspawn")
                        ;
                        
                        if(Arenas.isInGame(arenakey)||Arenas.isInCountdown(arenakey)){PluginInfo.sendAlreadyInGameMsg(sender, arenakey); return true;}
                        
                        if(setccp){
                            if(config.getBoolean(YMLKeys.OPS_DEBUG_ONCMD.key())) PluginInfo.sendPluginInfo("Attempted /hgae <arena> setccp command");
                            if(sender.hasPermission(Perms.HGAE_SETCCP.perm())||Util.isGameMakersArena(sender, arenakey)){
                                if(isplayer){
                                    if(Arenas.isInGame(arenakey)){
                                        sender.sendMessage(ChatColor.GOLD+arenakey+ChatColor.RED+" is currently in game!");
                                        return true;
                                    }
                                	Arenas.setCenter(arenakey, player.getLocation());
                                    player.sendMessage(ChatColor.GREEN+"Set your location as the center of "+arenakey);
                                    return true;
                                } else PluginInfo.sendOnlyPlayerMsg(sender); return true;
                            } else PluginInfo.sendNoPermMsg(sender); return true;
                        }
                        
                        if(setlounge){
                            if(config.getBoolean(YMLKeys.OPS_DEBUG_ONCMD.key())) PluginInfo.sendPluginInfo("Attempted /hgae <arena> setlounge command");
                            if(sender.hasPermission(Perms.HGAE_SETLOUNGE.perm())||Util.isGameMakersArena(sender, arenakey)){
                                if(isplayer){
                                    Arenas.setLounge(arenakey, player.getLocation());
                                    if(Arenas.isInGame(arenakey)){
                                        sender.sendMessage(ChatColor.LIGHT_PURPLE+""+ChatColor.ITALIC+"(Warning): "+ChatColor.GOLD+arenakey+ChatColor.LIGHT_PURPLE+" is currently in game");
                                    }
                                    player.sendMessage(ChatColor.GREEN+"Set your location as the lounge of "+arenakey);
                                    return true;
                                } else PluginInfo.sendOnlyPlayerMsg(sender); return true;
                            } else PluginInfo.sendNoPermMsg(sender); return true;
                        }
                        
                        if(radius){
                            if(config.getBoolean(YMLKeys.OPS_DEBUG_ONCMD.key())) PluginInfo.sendPluginInfo("Attempted /hgae <arena> radius command");
                            if(sender.hasPermission(Perms.HGAE_LIMIT.perm())||Util.isGameMakersArena(sender, arenakey)){
                                if(Arenas.isInGame(arenakey)){
                                    sender.sendMessage(ChatColor.GOLD+arenakey+ChatColor.RED+" is currently in game!");
                                    return true;
                                }
                                try{
                                    Arenas.setRadius(arenakey, Double.parseDouble(args[2]));
                                    player.sendMessage(ChatColor.GREEN+"Set "+arenakey+"'s radius to "+args[2]);
                                    return true;
                                }catch(NumberFormatException e){
                                    PluginInfo.wrongFormatMsg(sender, args[2]+" is not a valid number!"); 
                                    return true;
                                }catch(IndexOutOfBoundsException e){
                                    PluginInfo.wrongFormatMsg(sender, "/hgae <arena> radius (number)"); 
                                    return true;
                                }
                            } else PluginInfo.sendNoPermMsg(sender); return true;
                        }
                        
                        if(addgm){
                            if(config.getBoolean(YMLKeys.OPS_DEBUG_ONCMD.key())) PluginInfo.sendPluginInfo("Attempted /hgae <arena> addgm command");
                            if(sender.hasPermission(Perms.HGAE_ADDGM.perm())){
                                try{
                                    String arg3 = args[2];
                                    
                                    try{
                                        if(!Arenas.getGMs(arenakey).contains(arg3)){
                                            if(Arenas.getTribNames(arenakey).contains(arg3)){
                                                sender.sendMessage(ChatColor.LIGHT_PURPLE+""+ChatColor.ITALIC+"Warning: "+arg3+" is already a tribute for "+arenakey+"!");
                                            }
                                            Player gm = Bukkit.getPlayer(arg3);
                                            if(gm!=null){
                                                arg3 = gm.getName();
                                                
                                                if(Arenas.isGM(arenakey, arg3)){
                                                    PluginInfo.wrongFormatMsg(sender, ChatColor.LIGHT_PURPLE+""+ChatColor.ITALIC+arg3+" is already a gamemaker for "+ChatColor.GOLD+arenakey);
                                                    return true;
                                                }
                                                
                                                gm.sendMessage(ChatColor.LIGHT_PURPLE+"You have been added as a gamemaker to the arena: "+arenakey);
                                                
                                                Arenas.addGM(arenakey, arg3);
                                                if(Arenas.isInGame(arenakey))
                                                    sender.sendMessage(ChatColor.LIGHT_PURPLE+""+ChatColor.ITALIC+"(Warning): "+ChatColor.GOLD+arenakey+ChatColor.LIGHT_PURPLE+" is currently in game");
                                                sender.sendMessage(ChatColor.GREEN+"Added "+arg3+" to "+arenakey+"'s gamemakers");
                                                return true;
                                                
                                            } else{
                                                sender.sendMessage(ChatColor.LIGHT_PURPLE+""+ChatColor.ITALIC+"(Warning): Could not find player online \""+arg3+"\"");
                                                return true;
                                            }
                                        } else PluginInfo.wrongFormatMsg(sender, arg3+" is already a gamemaker for "+arenakey+"!"); return true;
                                    }catch(NullPointerException e){
                                        PluginInfo.wrongFormatMsg(sender, "Could not find the player \""+arg3+"\""); return true;
                                    }
                                }catch(IndexOutOfBoundsException e){
                                    PluginInfo.wrongFormatMsg(sender, "/hgae <arena> addgm <player>"); return true;
                                }
                            } else PluginInfo.sendNoPermMsg(sender); return true;
                        }
                        
                        if(addtrib){
                            if(config.getBoolean(YMLKeys.OPS_DEBUG_ONCMD.key())) PluginInfo.sendPluginInfo("Attempted /hgae <arena> addtrib command");
                            if(sender.hasPermission(Perms.HGAE_ADDTRIB.perm())||Util.isGameMakersArena(sender, arenakey)){
                                try{
                                    String arg3 = args[2];
                                    
                                    try{
                                        if(!Arenas.getTribNames(arenakey).contains(arg3)){
                                            if(Arenas.getGMs(arenakey).contains(arg3)){
                                                sender.sendMessage(ChatColor.LIGHT_PURPLE+""+ChatColor.ITALIC+"Warning: "+arg3+" is already a gamemaker for "+arenakey+"!");
                                            }
                                            Player trib = Bukkit.getPlayer(arg3);
                                            if(trib!=null){
                                                arg3 = trib.getName();
                                                trib.sendMessage(ChatColor.LIGHT_PURPLE+"You have been added as a tribute to the arena: "+arenakey);
                                                
                                                if(Arenas.isTribute(arenakey, arg3)){
                                                    PluginInfo.wrongFormatMsg(sender, ChatColor.LIGHT_PURPLE+""+ChatColor.ITALIC+arg3+" is already a tribute for "+ChatColor.GOLD+arenakey);
                                                    return true;
                                                }
                                                Arenas.addTrib(arenakey, arg3);
                                                sender.sendMessage(ChatColor.GREEN+"Added "+arg3+" to "+arenakey+"'s tributes");
                                                return true;
                                            } else{
                                                sender.sendMessage(ChatColor.LIGHT_PURPLE+""+ChatColor.ITALIC+"(Warning): Could not find player online \""+arg3+"\"");
                                            }
                                        } else PluginInfo.wrongFormatMsg(sender, arg3+" is already a tribute for "+arenakey+"!"); return true;
                                    }catch(NullPointerException e){
                                        PluginInfo.wrongFormatMsg(sender, "Could not find the player \""+arg3+"\""); return true;
                                    }
                                }catch(IndexOutOfBoundsException e){
                                    PluginInfo.wrongFormatMsg(sender, "/hgae <arena> addtrib <player>"); return true;
                                }
                            } else PluginInfo.sendNoPermMsg(sender); return true;
                        }
                        
                        if(removegm){
                            if(config.getBoolean(YMLKeys.OPS_DEBUG_ONCMD.key())) PluginInfo.sendPluginInfo("Attempted /hgae <arena> removegm command");
                            if(sender.hasPermission(Perms.HGAE_REMOVEGM.perm())){
                                try{
                                    String arg3 = args[2];
                                    
                                    try{
                                        if(Arenas.getGMs(arenakey).contains(arg3)){
                                            Arenas.removeGM(arenakey, arg3);
                                            sender.sendMessage(ChatColor.GREEN+"Removed "+arg3+" from "+arenakey+"'s gamemakers");
                                             return true;
                                        } else PluginInfo.wrongFormatMsg(sender, arg3+" has already been removed from "+arenakey+"'s gamemakers!"); return true;
                                    }catch(NullPointerException e){
                                        PluginInfo.wrongFormatMsg(sender, "Could not find the player \""+arg3+"\""); return true;
                                    }
                                }catch(IndexOutOfBoundsException e){
                                    PluginInfo.wrongFormatMsg(sender, "/hgae <arena> removegm <player>"); return true;
                                }
                            } else PluginInfo.sendNoPermMsg(sender); return true;
                        }
                        
                        if(removetrib){
                            if(config.getBoolean(YMLKeys.OPS_DEBUG_ONCMD.key())) PluginInfo.sendPluginInfo("Attempted /hgae <arena> removetrib command");
                            if(sender.hasPermission(Perms.HGAE_REMOVETRIB.perm())||Util.isGameMakersArena(sender, arenakey)){
                                try{
                                    String arg3 = args[2];
                                    
                                    try{
                                        if(Arenas.getTribNames(arenakey).contains(arg3)){
                                            Arenas.removeTrib(arenakey, arg3, false);
                                            sender.sendMessage(ChatColor.GREEN+"Removed "+arg3+" from "+arenakey+"'s tributes");
                                             return true;
                                        } else PluginInfo.wrongFormatMsg(sender, arg3+" has already been removed from "+arenakey+"'s tributes!"); return true;
                                    }catch(NullPointerException e){
                                        PluginInfo.wrongFormatMsg(sender, "Could not find the player \""+arg3+"\""); return true;
                                    }
                                }catch(IndexOutOfBoundsException e){
                                    PluginInfo.wrongFormatMsg(sender, "/hgae <arena> removetrib <player>"); return true;
                                }
                            } else PluginInfo.sendNoPermMsg(sender); return true;
                        }
                        
                        if(settribspawn){
                            try{
                                if(!isplayer){
                                    PluginInfo.sendOnlyPlayerMsg(sender);
                                }
                                int index = Integer.parseInt(args[2]);
                                
                                Arenas.setTribLoc(arenakey, index, player.getLocation());
//                                TODO: add check for in the same world
                                player.sendMessage(ChatColor.GREEN+"Set tribute "+Arenas.getTribNames(arenakey).get(index)+"'s spawn point to your location");
                                
                            }catch(IndexOutOfBoundsException e){
                                PluginInfo.wrongFormatMsg(sender, "/hgae <arena> settribspawn <tribID>");
                            }catch(NumberFormatException e){
                                PluginInfo.wrongFormatMsg(sender, "\""+args[2]+"\" is not a valid number!"); 
                            }
                            return true;
                        }
                        
                    }catch(IndexOutOfBoundsException e){}
                } else PluginInfo.wrongFormatMsg(sender, "Could not find the arena \""+arg1+"\""); return true;
            }catch(IndexOutOfBoundsException e){}
            
            if(config.getBoolean(YMLKeys.OPS_DEBUG_ONCMD.key())) PluginInfo.sendPluginInfo("Attempted to show /hgae branch help");
            PluginInfo.sendCommandUsage(MCHGCommandBranch.HGAE, sender);
        } 
        
        return true;
    }
        
}