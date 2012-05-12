package com.acuddlyheadcrab.MCHungerGames.listeners;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.acuddlyheadcrab.MCHungerGames.HGplugin;
import com.acuddlyheadcrab.MCHungerGames.FileIO.Configs;
import com.acuddlyheadcrab.MCHungerGames.FileIO.YMLKey;
import com.acuddlyheadcrab.MCHungerGames.arenas.ArenaUtil;
import com.acuddlyheadcrab.MCHungerGames.arenas.Arenas;
import com.acuddlyheadcrab.MCHungerGames.chests.ChestHandler;
import com.acuddlyheadcrab.MCHungerGames.inventories.InventoryHandler;
import com.acuddlyheadcrab.apiEvents.ArenaTribAddEvent;
import com.acuddlyheadcrab.apiEvents.GameStartEvent;
import com.acuddlyheadcrab.apiEvents.PlayerEnterArenaEvent;
import com.acuddlyheadcrab.apiEvents.PlayerLeaveArenaEvent;
import com.acuddlyheadcrab.apiEvents.PlayerPassArenaReason;
import com.acuddlyheadcrab.apiEvents.TributeDeathEvent;
import com.acuddlyheadcrab.apiEvents.TributeWinEvent;
import com.acuddlyheadcrab.util.Utility;


public class HGListener implements Listener {
    public static HGplugin plugin;
    public static EventPriority priority;
    public HGListener(HGplugin instance) {plugin = instance;}
    
    public static FileConfiguration config;
    // note to self: config is instantiated ONLY once initConfig() is called.
    public static void initConfig(){config = plugin.getConfig();}
    
    
    @EventHandler
    public void onGameStart(GameStartEvent e){
    }
    
    private static int taskID;
    
    static void cancelTask(){
        Bukkit.getScheduler().cancelTask(taskID);
    }
    
    @EventHandler
    public void onTribAdd(ArenaTribAddEvent e){
        final String arenakey = e.getArenakey();
        if(config.getBoolean(YMLKey.AG_ENABLED.key())){
            int limit = config.getInt(YMLKey.AG_STARTWHEN_PLAYERCOUNT.key());
            if(Arenas.getOnlineTribNames(arenakey).size()>=limit){
                final int mins = Configs.getConfig().getInt(YMLKey.AG_DELAYMINS);
                Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"Starting a game in "+arenakey+" in "+mins+" minutes!");
                taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                    private int count = mins;
                    @Override
                    public void run() {
                        if(count<=0){
                            ArenaUtil.startGame(arenakey, config.getInt(YMLKey.AG_COUNTDOWN.key()));
                            cancelTask();
                        }
                        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"[MCHungerGames] "+count+" minute(s) remaining...");
                        count--;
                    }
                }, 1L, 1200L);
            }
        }
    }
    
    
    @EventHandler
    public void onPlayerLeaveArena(PlayerLeaveArenaEvent e){
        String arenakey = e.getArenakey();
        Player player = e.getPlayer();
        if(Arenas.isInGame(arenakey)){
            if(!Arenas.isGM(arenakey, player)){
                if(Arenas.isTribute(arenakey, player)){
                    if(e.getReason()==PlayerPassArenaReason.PLAYERMOVE){
                        player.getWorld().playEffect(e.getTo(), Effect.EXTINGUISH, 1);
                        player.damage(3);
                        player.setFireTicks(5*20);
                        player.teleport(e.getFrom());
                        Utility.repelPlayer(player, e.getTo(), 5);
                    }
                    player.sendMessage(ChatColor.RED+"You are not allowed to leave "+arenakey+"!");
                    e.setCancelled(true);
                    return;
                }
            }
            player.sendMessage(ChatColor.LIGHT_PURPLE+"("+arenakey+" is currently in game)");
        }
        player.sendMessage(ChatColor.YELLOW+"You are now leaving "+ChatColor.GOLD+arenakey);
    }
    
    @EventHandler
    public void onPlayerEnterArena(PlayerEnterArenaEvent e){
        String arenakey = e.getArenakey();
        Player player = e.getPlayer();
        if(Arenas.isInGame(arenakey)){
            if(!Arenas.isGM(arenakey, player)){
                String istrib = Arenas.isTribute(arenakey, player) ? " is" : " is not";
                System.out.println(player.getName()+istrib+" a tribute for "+arenakey);
                if(Arenas.isTribute(arenakey, player)){
                    if(e.getReason()==PlayerPassArenaReason.PLAYERMOVE){
                        player.sendMessage(ChatColor.RED+"You are not allowed to enter "+arenakey+"!");
                        e.setCancelled(true);
                        player.teleport(e.getFrom());
                        Utility.repelPlayer(player, e.getTo(), 5);
                    }
                    return;
                }
            }
            player.sendMessage(ChatColor.LIGHT_PURPLE+"("+arenakey+" is currently in game)");
        }
        player.sendMessage(ChatColor.YELLOW+"You are now entering "+ChatColor.GOLD+arenakey);
    }
    
    private HashMap<Player, String> waiting_respawn = new HashMap<Player, String>();
    
    @EventHandler
    public void onTributeDeath(TributeDeathEvent e){
        String arenakey = e.getArenakey();
        Player whodied = e.getEntity();
        
        Arenas.removeTrib(arenakey, e.getEntity());
        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+whodied.getName()+" is no longer a tribute for "+arenakey);
        
        for(Player remainingtrib : Arenas.getOnlineTribNames(arenakey)){
            Location loc = remainingtrib.getLocation();
            loc.setY(loc.getY()+10);
            remainingtrib.getWorld().createExplosion(loc, 0);
        }
        
//        TODO: this *should* only broadcast to non-tributes, but meh
        
        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"There are now "+Arenas.getTribs(arenakey).size()+" tributes left for "+arenakey+"!");
        
        if(Arenas.getOnlineTribNames(arenakey).size()==1){
            Player pwinner = Arenas.getOnlineTribNames(arenakey).get(0);
            HGplugin.callSubEvent(e, new TributeWinEvent(pwinner, arenakey));
        }
        waiting_respawn.put(whodied, arenakey);
    }
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e){
        Player player = e.getPlayer();
        if(waiting_respawn.containsKey(player)){
            InventoryHandler.updateInventory(player);
            try{
                if(Configs.getConfig().getBoolean(YMLKey.OPS_LOUNGETP_ONDEATH)) player.teleport(Arenas.getLounge(waiting_respawn.get(player)));
            }catch(NullPointerException ex){}
        }
    }
    
    @EventHandler
    public void onTributeWin(TributeWinEvent e){
        Player winner = e.getPlayer();
        String suffix = "", arenakey = e.getArenakey();
        int gc = Arenas.getGameCount();
        switch (gc%10) {
            case 1: suffix = "st"; break;
            case 2: suffix = "nd"; break;
            case 3: suffix = "rd"; break;
            default: suffix = "th"; break;
        }
        String gmcount = gc+""+ChatColor.ITALIC+suffix+ChatColor.RESET+ChatColor.LIGHT_PURPLE;
//        possible exceptions here. is there a better way to do this?
        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+""+winner.getName()+" has won the "+gmcount+" Hunger Games in "+ChatColor.GOLD+winner.getWorld().getName()+ChatColor.LIGHT_PURPLE+"!");
        winner.getInventory().clear();
        winner.setHealth(20);
        winner.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000, 2));
        if(Configs.getConfig().getBoolean(YMLKey.OPS_LOUNGETP_ONWIN)){
            try{
                winner.teleport(Arenas.getLounge(arenakey));
            }catch(NullPointerException ex){}
        } else {
            winner.teleport(Arenas.getCenter(arenakey));
        }
        Arenas.removeTrib(arenakey, winner);
        ChestHandler.resetChests();
        Arenas.setInGame(arenakey, false);
    }
    
}
