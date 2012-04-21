package com.acuddlyheadcrab.MCHungerGames;

import java.util.Iterator;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.acuddlyheadcrab.util.ConfigKeys;
import com.acuddlyheadcrab.util.Utility;


public class TributeListener implements Listener {
    public static HungerGames plugin;
    public static EventPriority priority;
    public TributeListener(HungerGames instance) {plugin = instance;}
    
    public static FileConfiguration config;
    // note to self: config is instantiated ONLY once initConfig() is called.
    public static void initConfig(){config = plugin.getConfig();}
    
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDisconnect(PlayerQuitEvent e){
        String arenakey = Arenas.getArenaByTrib(e.getPlayer());
        if(arenakey!=null) 
            if(Arenas.isInGame(arenakey)) 
                if(config.getBoolean(ConfigKeys.OPTS_DURGM_KICKONDISC.key()))
                    Arenas.removeTrib(arenakey, e.getPlayer().getName());
    }
    

//  @EventHandler(priority = EventPriority.HIGHEST) //Disabled for now... 
  public void onPlayerChat(PlayerChatEvent event){
      event.setCancelled(true);
      
      Player talkingplayer = event.getPlayer();
      String format = event.getFormat();
      
      System.out.println("Talking player: "+talkingplayer.getName());
      Set<Player> recips = event.getRecipients();
      for (Iterator<Player> i=recips.iterator();i.hasNext();) {
          Player recip = i.next();
          System.out.println("     "+recip.getName()+": "+Utility.getChatProximity(talkingplayer, recip));
          Utility.sendChatProxMessage(talkingplayer, recip, format);
      }
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerDeath(PlayerDeathEvent event){
      Player player = event.getEntity();
      String arena = Arenas.getNearbyArena(player.getLocation());
      if(arena!=null){
          if(Arenas.isInGame(arena)){
              if(Arenas.isTribute(arena, player)){
                  Arenas.removeTrib(arena, player.getName());
                  // replace with broadcast to non-tributes
                  for(Player remainingtrib : Arenas.getOnlineTribs(arena)){
                      Location loc = remainingtrib.getLocation();
                      loc.setY(loc.getY()+10);
                      remainingtrib.getWorld().createExplosion(loc, 0);
                  }
                  Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"Tribute "+player.getName()+" has died!");
                  int winner = 1; //this is here in case I might want to change the rules ;D (like in the story)
                  if(Arenas.getOnlineTribs(arena).size()==winner){
                      String suffix = "";
                      int gc = Arenas.getGameCount();
                      switch (gc%10) {
                          case 1: suffix = "st"; break;
                          case 2: suffix = "nd"; break;
                          case 3: suffix = "rd"; break;
                          default: suffix = "th"; break;
                      }
                      String gmcount = gc+""+ChatColor.ITALIC+suffix+ChatColor.RESET+ChatColor.LIGHT_PURPLE;
                      Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+""+Arenas.getOnlineTribs(arena).get(winner-1).getName()+" has won the "+gmcount+" Hunger Games in "+ChatColor.GOLD+player.getWorld().getName()+ChatColor.LIGHT_PURPLE+"!");
                      Arenas.tpAllOnlineTribs(arena, false); //im too lazy to use player.teleport(), okay?
                      Arenas.setInGame(arena, false);
                  }
              }
          }
      }
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerTP(PlayerTeleportEvent event){
      Player player = event.getPlayer();
      Location 
          to = event.getTo(),
          from = event.getFrom()
      ;
      String
          to_arena = Arenas.getNearbyArena(to),
          from_arena = Arenas.getNearbyArena(from)
      ;
      
      ChatColor
          gold = ChatColor.GOLD,
          yellow = ChatColor.YELLOW,
          red = ChatColor.RED
      ;  
      boolean
          leaving = (from_arena!=null)&&((!from_arena.equals(to_arena))||to_arena==null),
          entering = (to_arena!=null)&&((!to_arena.equals(from_arena))||from_arena==null)
      ;
      
      if(leaving){
          if(Arenas.isInGame(from_arena)){
              if(!Arenas.isGM(from_arena, player)){
                  player.sendMessage(red+"You are not allowed to leave "+from_arena+"!");
                  event.setCancelled(true);
                  return;
              }
              player.sendMessage(ChatColor.LIGHT_PURPLE+"("+to_arena+" is currently in game)");
          }
          player.sendMessage(yellow+"You are now leaving "+gold+from_arena);
      }
      
      if(entering){
          if(Arenas.isInGame(to_arena)){
              if(!Arenas.isGM(to_arena, player)){
                  if(!Arenas.isTribute(to_arena, player)){
                      player.sendMessage(red+"You are not allowed to enter "+to_arena+"!");
                      event.setCancelled(true);
                      return;
                  }
              }
              player.sendMessage(ChatColor.LIGHT_PURPLE+"("+to_arena+" is currently in game)");
          }
          player.sendMessage(yellow+"You are now entering "+gold+to_arena);
      }
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerMove(PlayerMoveEvent event){
      Player player = event.getPlayer();
      Location 
          to = event.getTo(),
          from = event.getFrom()
      ;
      String
          to_arena = Arenas.getNearbyArena(to),
          from_arena = Arenas.getNearbyArena(from),
          arenakey = Arenas.getArenaByTrib(player)
      ;
      
      ChatColor
          gold = ChatColor.GOLD,
          yellow = ChatColor.YELLOW,
          red = ChatColor.RED
      ;  
      boolean
          leaving = (from_arena!=null)&&((!from_arena.equals(to_arena))||to_arena==null),
          entering = (to_arena!=null)&&((!to_arena.equals(from_arena))||from_arena==null)
      ;
      
      
      if(arenakey!=null&&Arenas.isInCountdown(arenakey)){
          event.setCancelled(true);
          player.setVelocity(player.getVelocity().multiply(0));
          player.teleport(from);
      }
      
      if(leaving){
          if(Arenas.isInGame(from_arena)){
              if(!Arenas.isGM(from_arena, player)){
                  if(Arenas.isTribute(from_arena, player)){
                      player.getWorld().playEffect(to, Effect.EXTINGUISH, 1);
                      player.damage(3);
                      player.setFireTicks(5*20);
                      player.sendMessage(red+"You are not allowed to leave "+from_arena+"!");
                      event.setCancelled(true);
                      player.teleport(from);
                      Utility.repelPlayer(player, to, 5);
                      return;
                  }
              }
              player.sendMessage(ChatColor.LIGHT_PURPLE+"("+to_arena+" is currently in game)");
          }
          player.sendMessage(yellow+"You are now leaving "+gold+from_arena);
      }
      
      if(entering){
          if(Arenas.isInGame(to_arena)){
              if(!Arenas.isGM(to_arena, player)){
                  String istrib = Arenas.isTribute(to_arena, player) ? " is" : " is not";
                  System.out.println(player.getName()+istrib+" a tribute for "+to_arena);
                  if(Arenas.isTribute(to_arena, player)){
                      player.sendMessage(red+"You are not allowed to enter "+to_arena+"!");
                      event.setCancelled(true);
                      player.teleport(from);
                      Utility.repelPlayer(player, to, 5);
                      return;
                  }
              }
              player.sendMessage(ChatColor.LIGHT_PURPLE+"("+to_arena+" is currently in game)");
          }
          player.sendMessage(yellow+"You are now entering "+gold+to_arena);
      }
  }
}
