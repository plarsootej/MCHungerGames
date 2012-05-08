package com.acuddlyheadcrab.MCHungerGames.listeners;

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
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.acuddlyheadcrab.MCHungerGames.HungerGames;
import com.acuddlyheadcrab.MCHungerGames.arenas.ArenaUtil;
import com.acuddlyheadcrab.MCHungerGames.arenas.Arenas;
import com.acuddlyheadcrab.MCHungerGames.chat.ChatHandler;
import com.acuddlyheadcrab.MCHungerGames.chests.ChestHandler;
import com.acuddlyheadcrab.util.YMLKeys;
import com.acuddlyheadcrab.util.Util;


public class TributeListener implements Listener {
    public static HungerGames plugin;
    public static EventPriority priority;
    public TributeListener(HungerGames instance) {plugin = instance;}
    
    public static FileConfiguration config;
    // note to self: config is instantiated ONLY once initConfig() is called.
    public static void initConfig(){config = plugin.getConfig();}
    

//  @EventHandler(priority = EventPriority.HIGH)
  public void onPlayerChat(PlayerChatEvent event){
      if(config.getBoolean(YMLKeys.OPS_NEARCHAT_ENABLED.key())){
          event.setCancelled(true);
          Player talkingplayer = event.getPlayer();
          String format = event.getFormat(), msg = event.getMessage();
          Set<Player> recips = event.getRecipients();
          for (Iterator<Player> i=recips.iterator();i.hasNext();) {
              Player recip = i.next();
              ChatHandler.sendChatProxMessage(talkingplayer, recip, format, msg);
          }
          Util.log.info(ChatColor.stripColor(format));
      }
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerDeath(PlayerDeathEvent event){
      Player player = event.getEntity();
      String arenakey = Arenas.getNearbyArena(player.getLocation());
      if(arenakey!=null){
          if(Arenas.isInGame(arenakey)){
              if(Arenas.isTribute(arenakey, player)){
                  Arenas.removeTrib(arenakey, player, true);
                  // replace with broadcast to non-tributes
                  for(Player remainingtrib : Arenas.getOnlineTribNames(arenakey)){
                      Location loc = remainingtrib.getLocation();
                      loc.setY(loc.getY()+10);
                      remainingtrib.getWorld().createExplosion(loc, 0);
                  }
                  Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"There are now "+Arenas.getTribs(arenakey).size()+" tributes left for "+arenakey+"!");
                  int win = 1; //this is here in case I might want to change the rules ;D (like in the story)
                  if(Arenas.getOnlineTribNames(arenakey).size()==win){
                      String suffix = "";
                      int gc = Arenas.getGameCount();
                      switch (gc%10) {
                          case 1: suffix = "st"; break;
                          case 2: suffix = "nd"; break;
                          case 3: suffix = "rd"; break;
                          default: suffix = "th"; break;
                      }
                      String gmcount = gc+""+ChatColor.ITALIC+suffix+ChatColor.RESET+ChatColor.LIGHT_PURPLE;
//                      possible exceptions here. is there a better way to do this?
                      Player pwinner = Arenas.getOnlineTribNames(arenakey).get(0);
                      Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+""+pwinner.getName()+" has won the "+gmcount+" Hunger Games in "+ChatColor.GOLD+player.getWorld().getName()+ChatColor.LIGHT_PURPLE+"!");
                      pwinner.getInventory().clear();
                      pwinner.setHealth(20);
                      pwinner.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000, 2));
                      if(config.getBoolean(YMLKeys.OPS_LOUNGETP_ONWIN.key())){
                          try{
                              pwinner.teleport(Arenas.getLounge(arenakey));
                          }catch(NullPointerException e){}
                      }
                      ArenaUtil.tpAllOnlineTribs(arenakey, false);
                      Arenas.removeTrib(arenakey, pwinner, false);
                      ChestHandler.resetChests();
                      Arenas.setInGame(arenakey, false);
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
              player.sendMessage(ChatColor.LIGHT_PURPLE+"("+from_arena+" is currently in game)");
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
                      Util.repelPlayer(player, to, 5);
                      return;
                  }
              }
              player.sendMessage(ChatColor.LIGHT_PURPLE+"("+from_arena+" is currently in game)");
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
                      Util.repelPlayer(player, to, 5);
                      return;
                  }
              }
              player.sendMessage(ChatColor.LIGHT_PURPLE+"("+to_arena+" is currently in game)");
          }
          player.sendMessage(yellow+"You are now entering "+gold+to_arena);
      }
  }
}
