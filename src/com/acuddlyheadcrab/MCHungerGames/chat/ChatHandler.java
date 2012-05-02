package com.acuddlyheadcrab.MCHungerGames.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.acuddlyheadcrab.MCHungerGames.HungerGames;
import com.acuddlyheadcrab.MCHungerGames.arenas.Arenas;
import com.acuddlyheadcrab.util.Util;
import com.acuddlyheadcrab.util.YMLKeys;


public class ChatHandler {
    
    public static HungerGames HungerGamesPlugin;
    public ChatHandler(HungerGames instance) {HungerGamesPlugin = instance;}
    
    public enum ChatProximity{
        SELF(0),
        GLOBAL(0),
        CLEAR(0),
        DISEMBODIED(0),
        GARBLED(0),
        INAUDIBLE(0);
        
        private double distance;
        
        private ChatProximity(double dist) {
            distance = dist;
        }

        public double getDistance() {
            return distance;
        }
        
        public void setDistance(final double newdist){
            this.distance = newdist;
        }
        
        public static ChatProximity withDistance(ChatProximity prox, final double dist){
            prox.setDistance(dist);
            return prox;
        }
    }
    
    public static ChatProximity getChatProximity(Player talkingplayer, Player recip){
        FileConfiguration config = HungerGamesPlugin.getConfig();
        config = HungerGamesPlugin.getConfig();
        String arenakey = Arenas.getArenaByTrib(recip);
        if(talkingplayer==recip) return ChatProximity.SELF;
        if(arenakey!=null){
            if(Arenas.isInGame(arenakey)){
                double 
                    distance = recip.getLocation().distance(talkingplayer.getLocation()),
                    clear = config.getDouble(YMLKeys.OPS_NEARCHAT_DISTS_CLEAR.key()),
                    disemboided = config.getDouble(YMLKeys.OPS_NEARCHAT_DISTS_DISEMBOIDED.key()),
                    garbled = config.getDouble(YMLKeys.OPS_NEARCHAT_DISTS_GARBLED.key())
                ;
                if(distance<=clear) return ChatProximity.withDistance(ChatProximity.CLEAR, distance);
                else if(distance<=disemboided) return ChatProximity.withDistance(ChatProximity.DISEMBODIED, distance);
                else if(distance<=garbled) return ChatProximity.withDistance(ChatProximity.GARBLED, distance);
                else return ChatProximity.withDistance(ChatProximity.INAUDIBLE, distance);
            }
        }
        return ChatProximity.GLOBAL;
    }
    
    public static void sendChatProxMessage(Player talkingplayer, Player recip, String format, String msg){
        switch (getChatProximity(talkingplayer, recip)) {
            case INAUDIBLE : break;
            case SELF: recip.sendMessage(format); break; 
            case CLEAR: recip.sendMessage(format); break;
            case GLOBAL: recip.sendMessage(format); break;
            case DISEMBODIED: recip.sendMessage(format.replaceAll(talkingplayer.getName(), garblifyString(talkingplayer.getName(), ChatColor.GRAY))); break;
            case GARBLED: recip.sendMessage(format.replaceAll(talkingplayer.getName(), garblifyString(talkingplayer.getName(), ChatColor.DARK_GRAY)).replaceAll(msg, garblifyString(msg, ChatColor.GRAY))); break;
            default: break;
        }
    }
    
    public static String garblifyString(String string, ChatColor color){
        Random rand = new Random();
//        I know its... ungainly... but hey, it works
        List<String> letters = new ArrayList<String>();
        for(char chr : string.toCharArray()){
                String 
                    char_s = String.valueOf(chr),
                    newchar = rand.nextBoolean() ? color+""+ChatColor.MAGIC+char_s+ChatColor.RESET : color+char_s;
                ;
//                this gives a 1/10 chance that the letter will just not be added
                if(rand.nextInt(10)!=1){
                    letters.add(newchar);
                }
        }
        return Util.concatList(letters, "");
    }
    
}
