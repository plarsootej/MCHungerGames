package com.acuddlyheadcrab.MCHungerGames.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import com.acuddlyheadcrab.MCHungerGames.HungerGames;
import com.acuddlyheadcrab.MCHungerGames.arenas.ArenaIO;
import com.acuddlyheadcrab.MCHungerGames.arenas.ArenaUtil;
import com.acuddlyheadcrab.MCHungerGames.arenas.Arenas;
import com.acuddlyheadcrab.MCHungerGames.chests.ChestHandler;
import com.acuddlyheadcrab.util.PluginInfo.MCHGCommandBranch;
import com.acuddlyheadcrab.util.YMLKeys;
import com.acuddlyheadcrab.util.Perms;
import com.acuddlyheadcrab.util.PluginInfo;
import com.acuddlyheadcrab.util.Util;

public class HGArenaCommand implements CommandExecutor{
    
	private static HungerGames hungergames;
    public HGArenaCommand(HungerGames instance){hungergames = instance;}
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args){
        
        FileConfiguration config = hungergames.getConfig();
        FileConfiguration arenasfile = HungerGames.getArenasFile();
        
        boolean isplayer = sender instanceof Player;
        Player player = isplayer ? (Player) sender : null;
        PluginInfo.printConsoleCommandInfo(sender, label, args);
        
        if(cmd.getName().equalsIgnoreCase("hgarena")){
            try{
                String arg1 = args[0];
                
                boolean
                    hga_info = arg1.equalsIgnoreCase("info"),
                    hga_new = arg1.equalsIgnoreCase("new"),
                    hga_del = arg1.equalsIgnoreCase("del"),
                    hga_list = arg1.equalsIgnoreCase("list"),
                    hga_lounge = arg1.equalsIgnoreCase("lounge"),
                    hga_tp = arg1.equalsIgnoreCase("tp"),
                    hga_tpall = arg1.equalsIgnoreCase("tpall")||arg1.equalsIgnoreCase("tpa"),
                    hga_rename = arg1.equalsIgnoreCase("rename"),
                    hga_join = arg1.equalsIgnoreCase("join"),
                    hga_leave = arg1.equalsIgnoreCase("leave"),
                    hga_chestreset = arg1.equalsIgnoreCase("chests")||arg1.equalsIgnoreCase("chestreset"),
                    hga_tributes = arg1.equalsIgnoreCase("tributes")
                ;
                
                if(hga_tributes){
                    if(config.getBoolean(YMLKeys.OPS_DEBUG_ONCMD.key())) PluginInfo.sendPluginInfo("Attempted /hga join command");
                    if(sender.hasPermission(Perms.HGA_TRIBUTES.perm())){
                        for(String arenakey : ArenaIO.getArenasKeys()){
                            List<String> tribs = new ArrayList<String>();
                            for(String trib : Arenas.getTribNames(arenakey)){
                                String tribname = Bukkit.getPlayerExact(trib)!=null ? ChatColor.DARK_GREEN+trib : ChatColor.DARK_GRAY+""+ChatColor.STRIKETHROUGH+trib;
                                tribs.add(tribname);
                            }
                            
                            String arena = Arenas.isInGame(arenakey) ? ChatColor.GREEN+arenakey : ChatColor.BLUE+arenakey;
                            String triblist = Util.concatList(tribs, ", "+ChatColor.RESET);
                            if(triblist.length()==0) triblist = ChatColor.DARK_GRAY+"(none)";
                            sender.sendMessage(arena+":");
                            sender.sendMessage("    "+triblist);
                        }
                        return true;
                    } else PluginInfo.sendNoPermMsg(sender);
                }
                
                if(hga_join){
                    if(config.getBoolean(YMLKeys.OPS_DEBUG_ONCMD.key())) PluginInfo.sendPluginInfo("Attempted /hga join command");
                    if(sender.hasPermission(Perms.HGA_JOIN.perm())){
                        if(isplayer){
                            try{
                                String arenakey = ArenaIO.getArenaByKey(args[1]);
                                
                                if(arenakey!=null){
                                    if(Arenas.isInGame(arenakey)||Arenas.isInCountdown(arenakey)) {
                                        PluginInfo.wrongFormatMsg(sender, arenakey+" is currently in game!"); return true;
                                    }
                                    if(Arenas.isTribute(arenakey, player)){
                                        PluginInfo.wrongFormatMsg(sender, "You're already a tribute for "+arenakey+"!");
                                        return true;
                                    }
                                    Arenas.addTrib(arenakey, player.getName());
                                    player.sendMessage(ChatColor.LIGHT_PURPLE+"You have added yourself as a tribute to "+arenakey); return true;
                                } else PluginInfo.wrongFormatMsg(sender, "Could not find \""+args[1]+"\"!"); return true; 
                            }catch(IndexOutOfBoundsException e){
                                PluginInfo.wrongFormatMsg(sender, "/hga join <arena>"); return true;
                            }
                        } else PluginInfo.sendOnlyPlayerMsg(sender); return true;
                    } else PluginInfo.sendNoPermMsg(sender); return true;
                }
                
                if(hga_leave){
                    if(config.getBoolean(YMLKeys.OPS_DEBUG_ONCMD.key())) PluginInfo.sendPluginInfo("Attempted /hga join command");
                    if(sender.hasPermission(Perms.HGA_LEAVE.perm())){
                        if(isplayer){
                            try{
                                String arenakey = ArenaIO.getArenaByKey(args[1]);
                                
                                if(arenakey!=null){
                                    if(Arenas.isInGame(arenakey)||Arenas.isInCountdown(arenakey)) {
                                        PluginInfo.wrongFormatMsg(sender, "Fool! You can't leave while "+arenakey+" is in game!"); return true;
                                    }
                                    if(!Arenas.isTribute(arenakey, player)){
                                        PluginInfo.wrongFormatMsg(sender, "You're already not a tribute for "+arenakey+"!");
                                        return true;
                                    }
                                    Arenas.removeTrib(arenakey, player, false);
                                    player.sendMessage(ChatColor.LIGHT_PURPLE+"You have left "+arenakey); return true;
                                } else PluginInfo.wrongFormatMsg(sender, "Could not find \""+args[1]+"\"!"); return true; 
                            }catch(IndexOutOfBoundsException e){
                                PluginInfo.wrongFormatMsg(sender, "/hga leave <arena>"); return true;
                            }
                        } else PluginInfo.sendOnlyPlayerMsg(sender); return true;
                    } else PluginInfo.sendNoPermMsg(sender); return true;
                }
                
                if(hga_info){
                    if(config.getBoolean(YMLKeys.OPS_DEBUG_ONCMD.key())) PluginInfo.sendPluginInfo("Attempted /hga info command");
                    if(sender.hasPermission(Perms.HGA_INFO.perm())){
                        try{
                            String arg2 = args[1];
                            
                            String 
                                arenakey = ArenaIO.getArenaByKey(arg2),
                                arenapath = YMLKeys.ARENAS.key()+arenakey
                            ;
                            
                            if(arenakey!=null){
                                Location corn = null;
                                try{
                                    corn = Arenas.getCenter(arenakey);
                                }catch(NullPointerException e){
                                    System.out.println("Message: "+e.getMessage());
                                    sender.sendMessage("Could not find world!");
                                }
                                
                                if(corn==null){
                                    PluginInfo.wrongFormatMsg(sender, "There was an error parsing the location key!");
                                    return true;
                                }
                                
                                double
                                    size = arenasfile.getDouble(arenapath+YMLKeys.ARN_RADIUS.key()),
                                    x = corn.getBlockX(),
                                    y = corn.getBlockY(),
                                    z = corn.getBlockZ()
                                ;
                                
                                boolean arena_ingame = Arenas.isInGame(arenakey);
                                
                                ChatColor
                                    main = arena_ingame ? ChatColor.GREEN : ChatColor.BLUE,
                                    underline = ChatColor.UNDERLINE,
                                    onlineplayer = ChatColor.DARK_GREEN,
                                    offlineplayer = ChatColor.DARK_GRAY,
                                    offlineformat = ChatColor.STRIKETHROUGH,
                                    reset = ChatColor.RESET
                                ;
                                
                                String
                                    ingame = arena_ingame ? ChatColor.LIGHT_PURPLE+""+ChatColor.ITALIC+"IN GAME" : ChatColor.DARK_GRAY+""+ChatColor.ITALIC+"NOT IN GAME",
                                    gms = "",
                                    tribs = "",
                                    value = ChatColor.RESET+""+ChatColor.GRAY
                                ;
                                
                                for(String gm : Arenas.getGMs(arenakey)){
                                    String status = ChatColor.GRAY+"";
                                    Player gm_player = Bukkit.getPlayerExact(gm);
                                    status = gm_player==null ? offlineplayer+""+offlineformat : reset+""+onlineplayer;
                                    gms = gms.concat(status+gm+" ");
                                }
                                
                                for(String trib : Arenas.getTribNames(arenakey)){
                                    String status = ChatColor.GRAY+"";
                                    Player trib_player = Bukkit.getPlayerExact(trib);
                                    status = trib_player==null ? offlineplayer+""+offlineformat : reset+""+onlineplayer;
                                    int index = Arenas.getTribNames(arenakey).indexOf(trib);
                                    tribs = tribs.concat(ChatColor.GRAY+""+index+". "+status+trib+" ");
                                }
                                
                                sender.sendMessage(main+""+"Arena name: "+value+arenakey+"        ".concat(ingame));
                                sender.sendMessage(main+"    Cornucopia: "+value+x+", "+y+", "+z+"    ");
                                sender.sendMessage(main+"    World: "+value+corn.getWorld().getName());
                                sender.sendMessage(main+"    Radius: "+value+size);
                                if(isplayer){
                                    int dist = player.getWorld()==corn.getWorld() ? (int) player.getLocation().distance(corn) : -1;
                                    String distmsg = dist==-1 ? value+"Not in same world" : value+dist;
                                    player.sendMessage(main+"    Current distance from center: "+distmsg);
                                }
                                sender.sendMessage(main+"    Gamemakers:"+ChatColor.RESET+ChatColor.ITALIC+onlineplayer+"    online"+offlineplayer+"    offline");
                                sender.sendMessage(gms);
                                sender.sendMessage(main+"    "+underline+"Tributes:");
                                sender.sendMessage(tribs);
                                return true;
                            } else PluginInfo.wrongFormatMsg(sender, "Could not find the arena \""+arg2+"\"");
                        }catch(IndexOutOfBoundsException e){
                            PluginInfo.wrongFormatMsg(sender, "/hga info <arena>"); return true;
                        }
                    } else PluginInfo.sendNoPermMsg(sender); return true;
                } 
                
                if(hga_new){
                    if(config.getBoolean(YMLKeys.OPS_DEBUG_ONCMD.key())) PluginInfo.sendPluginInfo("Attempted /hga new command");
                    if(sender.hasPermission(Perms.HGA_NEW.perm())){
                        if(isplayer){
                            try{
                                String name = args[1];
                                double size = Double.parseDouble(args[2]);
                                
                                if(ArenaIO.getArenasKeys().contains(name)){
                                    PluginInfo.wrongFormatMsg(sender, "Arena \""+name+"\" has already been set!"); return true;
                                }
                                
                                ArenaIO.submitNewArena(name, player.getLocation(), size, null, null, false);
                                sender.sendMessage(ChatColor.GREEN+"Sucessfully made new arena \""+ChatColor.GOLD+name+ChatColor.GREEN+"\" at your location");
                                return true;
                            }catch(IndexOutOfBoundsException e){
                                PluginInfo.wrongFormatMsg(sender, "/hga new <name> <maxdistance>");
                            }catch(NumberFormatException e){
                                PluginInfo.wrongFormatMsg(sender, "Could not parse \""+args[2]+"\" as a number");
                            }
                        } else PluginInfo.sendOnlyPlayerMsg(sender); return true;
                    } else PluginInfo.sendNoPermMsg(sender); return true;
                }
                
                if(hga_del){
                    if(config.getBoolean(YMLKeys.OPS_DEBUG_ONCMD.key())) PluginInfo.sendPluginInfo("Attempted /hga del command");
                    if(sender.hasPermission(Perms.HGA_DEL.perm())){
                        try{
                            String name = args[1];
                            
                            if(ArenaIO.getArenasKeys().contains(name)){
                                ArenaIO.deleteArena(name);
                                sender.sendMessage(ChatColor.GREEN+"Deleted \""+ChatColor.GOLD+name+ChatColor.GREEN+"\" from the arenas!");
                                return true;
                            } else PluginInfo.wrongFormatMsg(sender, "Could not find \""+name+"\""); return true;
                        }catch(IndexOutOfBoundsException e){
                            PluginInfo.wrongFormatMsg(sender, "/hga del <arena name>"); return true;
                        }
                    }
                }
                
                if(hga_list){
                    if(config.getBoolean(YMLKeys.OPS_DEBUG_ONCMD.key())) PluginInfo.sendPluginInfo("Attempted /hga list command");
                    if(sender.hasPermission(Perms.HGA_LIST.perm())){
                        String arenas = Util.concatList(ArenaIO.getArenasKeys(), ChatColor.GRAY+", "+ChatColor.GOLD);
                        if(arenas.length()==0){
                            arenas = ChatColor.DARK_GRAY+"(none)";
                        }
                        
                        sender.sendMessage(ChatColor.AQUA+"Arenas: "+ChatColor.GOLD+arenas);
                        return true;
                    }
                }
                
                if(hga_lounge){
                    if(config.getBoolean(YMLKeys.OPS_DEBUG_ONCMD.key())) PluginInfo.sendPluginInfo("Attempted /hga tp command");
                    if(isplayer){
                        if(sender.hasPermission(Perms.HGA_LOUNGE.perm())){
                            try{
                                String arg2 = args[1];
                                String arenakey = ArenaIO.getArenaByKey(arg2);
                                
                                if(arenakey!=null){
                                    Location loc = null;
                                    try{
                                        loc = Arenas.getLounge(arenakey);
                                    }catch(NullPointerException e){
                                        player.sendMessage(ChatColor.RED+"Arena "+ChatColor.GOLD+arenakey+ChatColor.RED+" does not have a lounge set!"); return true;
                                    }
                                    player.teleport(loc);
                                    player.sendMessage(ChatColor.GREEN+"Teleported you to the lounge of "+arenakey);
                                } else PluginInfo.wrongFormatMsg(sender, "Could not find the arena \""+arg2+"\""); return true;
                            }catch(IndexOutOfBoundsException e){
                                PluginInfo.wrongFormatMsg(sender, "/hga lounge <arena>"); return true;
                            }
                        } else PluginInfo.sendNoPermMsg(sender);
                    } else PluginInfo.sendOnlyPlayerMsg(sender);
                }
                
                if(hga_tp){
                    if(config.getBoolean(YMLKeys.OPS_DEBUG_ONCMD.key())) PluginInfo.sendPluginInfo("Attempted /hga tp command");
                    if(isplayer){
                        if(sender.hasPermission(Perms.HGA_TP.perm())){
                            try{
                                String arg2 = args[1];
                                
                                String arenakey = ArenaIO.getArenaByKey(arg2);
                                
                                if(arenakey!=null){
                                    try{
                                        player = Bukkit.getPlayer(args[2]);
                                        if(player==null){
                                            PluginInfo.wrongFormatMsg(sender, "Could not find player \""+args[2]+"\"");
                                            return true;
                                        }
                                    }catch(IndexOutOfBoundsException e){}
                                    
                                    player.teleport(Arenas.getCenter(arenakey));
                                    player.sendMessage(ChatColor.GREEN+"Teleported you to the center of "+arenakey);
                                } else PluginInfo.wrongFormatMsg(sender, "Could not find the arena \""+arg2+"\""); return true;
                            }catch(IndexOutOfBoundsException e){
                                PluginInfo.wrongFormatMsg(sender, "/hga tp <arena>"); return true;
                            }
                        } else PluginInfo.sendNoPermMsg(sender);
                    } else PluginInfo.sendOnlyPlayerMsg(sender);
                }
                
                if(hga_tpall){
                    if(config.getBoolean(YMLKeys.OPS_DEBUG_ONCMD.key())) PluginInfo.sendPluginInfo("Attempted /hga tp command");
                    if(sender.hasPermission(Perms.HGA_TPALL.perm())){
                        try{
                            String arenakey = ArenaIO.getArenaByKey(args[1]);
                            
                            if(arenakey!=null){
                                ArenaUtil.tpAllOnlineTribs(arenakey, false);
                                sender.sendMessage("Teleported all online tributes for "+arenakey);
                                return true;
                            } else PluginInfo.wrongFormatMsg(sender, "Could not find the arena \""+args[1]+"\""); return true;
                        }catch(IndexOutOfBoundsException e){
                            PluginInfo.wrongFormatMsg(sender, "/hga tpall <arena name>");
                            return true;
                        }
                    } else PluginInfo.sendNoPermMsg(sender);
                }
                
                if(hga_rename){
                    if(config.getBoolean(YMLKeys.OPS_DEBUG_ONCMD.key())) PluginInfo.sendPluginInfo("Attempted /hga tp command");
                    if(sender.hasPermission(Perms.HGA_RENAME.perm())){
                        try{
                            String 
                                arenakey = ArenaIO.getArenaByKey(args[1]),
                                renameto = args[2]
                            ;
                            if(arenakey!=null){
                                List<String> cur_games = arenasfile.getStringList(YMLKeys.CURRENT_GAMES.key());
                                //renames current games
                                for(String game : cur_games){
                                    if(game.equalsIgnoreCase(renameto)){
                                        cur_games.remove(game);
                                        cur_games.add(renameto);
                                    }
                                }
                                ArenaIO.arenasSet(YMLKeys.CURRENT_GAMES.key(), cur_games);
                                ArenaIO.renameArena(arenakey, renameto);
                                
                                sender.sendMessage(ChatColor.GREEN+"Renamed \""+arenakey+"\" to \""+renameto+"\"");
                                return true;
                            } else PluginInfo.wrongFormatMsg(sender, "Could not find the arena \""+args[1]+"\""); return true;
                        }catch(IndexOutOfBoundsException e){
                            PluginInfo.wrongFormatMsg(sender, "/hga rename <arena name> <rename to>");
                            return true;
                        }
                    } else PluginInfo.sendNoPermMsg(sender); return true;
                }
                
                if(hga_chestreset){
                    if(sender.hasPermission(Perms.HGA_CHESTRESET.perm())){
                        try{
                            if(args[1].equalsIgnoreCase("clear")){
                                ChestHandler.clearChestLocs();
                                sender.sendMessage(ChatColor.GREEN+"Cleared all chests from chestitems.yml");
                                return true;
                            }
                        }catch(IndexOutOfBoundsException e){}
                        ChestHandler.resetChests();
                        sender.sendMessage(ChatColor.GREEN+"Reset all chests");
                        return true;
                    } else PluginInfo.sendNoPermMsg(sender);
                }
                
            }catch(IndexOutOfBoundsException e){}
            if(config.getBoolean(YMLKeys.OPS_DEBUG_ONCMD.key())) PluginInfo.sendPluginInfo("Attempted to show /hga branch help");
                PluginInfo.sendCommandUsage(MCHGCommandBranch.HGA, sender);
        }
        
        
        return true;
    }
        
}
