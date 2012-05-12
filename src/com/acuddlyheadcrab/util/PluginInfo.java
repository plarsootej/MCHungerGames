package com.acuddlyheadcrab.util;

import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import com.acuddlyheadcrab.MCHungerGames.HGplugin;

public class PluginInfo {
    public final static Logger log = Logger.getLogger("Minecraft");
    
    public static HGplugin plugin;
    public PluginInfo(HGplugin instance){plugin = instance;}
    
    public static final boolean debug = true;
    
    private static ChatColor red = ChatColor.RED;
    private static ChatColor purple = ChatColor.DARK_PURPLE;
    private static ChatColor aqua = ChatColor.AQUA;
    private static ChatColor gray = ChatColor.GRAY;
    
    public static void sendPluginInfo(String message) {
        PluginDescriptionFile plugdes = plugin.getDescription();
        String pluginname = plugdes.getName();
        log.info("[" + pluginname + "] " + message);
    }
    
    public static void debug(int linenum, Class<?> classfile, String msg){
        log.info(String.format("[DEBUG: (%1$s.%2$s)] %3$s", classfile.getName(), linenum, msg));
    }
    
    public static void sendTestMsg(CommandSender sender, String msg) {
        sender.sendMessage("\t    MCHungerGames TEST:");
        sender.sendMessage(msg);
    }

    public static void sendNoPermMsg(CommandSender sender) {
        sender.sendMessage(red + "You don't have permissions to do this!");
    }
    

    public static void sendOnlyPlayerMsg(CommandSender sender) {
        sender.sendMessage(red + "You must be a player to do this!");
    }
    
    public static void sendInGameMsg(CommandSender sender, String arena) {
        sender.sendMessage(ChatColor.GOLD+arena+red+" is currently in game!");
    }
    
    public static void sendInGameWarning(CommandSender sender, String arena){
        sender.sendMessage(ChatColor.LIGHT_PURPLE+""+ChatColor.ITALIC+" (Warning) "+ChatColor.GOLD+arena+ChatColor.LIGHT_PURPLE+" is currently in game!");
    }
    
    public static void sendCommandInfo(CommandSender sender, String cmd, String desc){
        sender.sendMessage(purple+cmd+gray+": "+desc);
    }
    
    
    public static void wrongFormatMsg(CommandSender sender, String msg) {
        sender.sendMessage(red+msg);
    }
    
    public static void sendCommandUsage(MCHGCommandBranch branch, CommandSender sender){
        switch (branch) {
            default:
            case HG:
                String v = plugin.getDescription().getVersion();
                sender.sendMessage(aqua + "    MC Hunger Games v" + v);
                Map<String, String> cmd_map = Utility.getCommandsAndDescs();
                for (String cmd : cmd_map.keySet()) {
                    String desc = cmd_map.get(cmd);
                    sendCommandInfo(sender, cmd, desc);
                }
                sendCommandInfo(sender, "hg reload", "Reloads the config");
                break;
            case HGA:
                PluginInfo.sendCommandInfo(sender, "/hga", "");
                PluginInfo.sendCommandInfo(sender, "    list", "Lists all arenas");
                PluginInfo.sendCommandInfo(sender, "    info", "Gives info on an arena");
                PluginInfo.sendCommandInfo(sender, "    new", "Create a new arena at your location");
                PluginInfo.sendCommandInfo(sender, "    del", "Delete an arena from the config");
                PluginInfo.sendCommandInfo(sender, "    tp", "Teleport to the cornucopia");
                PluginInfo.sendCommandInfo(sender, "    tpall", "Teleport all tributes to the cornucopia");
                PluginInfo.sendCommandInfo(sender, "    rename", "Rename an arena");
                PluginInfo.sendCommandInfo(sender, "    lounge", "Teleport to the arena's lounge");
                PluginInfo.sendCommandInfo(sender, "    join", "Join an arena");
                PluginInfo.sendCommandInfo(sender, "    leave", "Leave an arena");
                PluginInfo.sendCommandInfo(sender, "    tributes", "View a list of all tributes");
                PluginInfo.sendCommandInfo(sender, "    chestreset", "Reset all chests spawned with /spc");
                break;
            case HGAE:
                PluginInfo.sendCommandInfo(sender, "/hgae <arena>", "");
                PluginInfo.sendCommandInfo(sender, "    setcornucopia (setccp)", "Set the center to your location");
                PluginInfo.sendCommandInfo(sender, "    setlounge", "Set the lounge to your location");
                PluginInfo.sendCommandInfo(sender, "    radius", "Create a new arena at your location");
                PluginInfo.sendCommandInfo(sender, "    addgm", "Add a gamemaker");
                PluginInfo.sendCommandInfo(sender, "    addtrib", "Add a tribute");
                PluginInfo.sendCommandInfo(sender, "    removegm", "Remove a gamemaker");
                PluginInfo.sendCommandInfo(sender, "    removetrib", "Remove a tribute");
                PluginInfo.sendCommandInfo(sender, "    addspp", "Add a tribute spawnpoint");
                PluginInfo.sendCommandInfo(sender, "    settribspawn", "Set specific tribute's spawnpoint");
                break;
            case HGG:
                PluginInfo.sendCommandInfo(sender, "/hgg", "");
                PluginInfo.sendCommandInfo(sender, "    start <arena>", "Starts a game");
                PluginInfo.sendCommandInfo(sender, "    stop <arena>", "Stops an ongoing game");
                break;
        }
    }
    
    public enum MCHGCommandBranch{
        HG,
        HGA,
        HGAE,
        HGG;
    }

    public static void printConsoleCommandInfo(CommandSender sender, String cmdlabel, String[] args) {
        if(sender instanceof Player) PluginInfo.sendPluginInfo(sender.getName()+": /"+cmdlabel+" "+Utility.concatArray(args, " "));
    }

}
