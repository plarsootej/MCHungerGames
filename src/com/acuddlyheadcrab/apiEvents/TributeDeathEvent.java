package com.acuddlyheadcrab.apiEvents;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;


public class TributeDeathEvent extends PlayerDeathEvent{
    private boolean haskiller = false;
    private LivingEntity killer;
    private String arenakey;
    
    
    public TributeDeathEvent(Player player, List<ItemStack> drops, int droppedExp, String deathMessage, String arenakey) {
        super(player, drops, droppedExp, deathMessage);
        setArenakey(arenakey);
    }
    
    public TributeDeathEvent(Player player, List<ItemStack> drops, int droppedExp, String deathMessage, String arenakey, LivingEntity killer) {
        super(player, drops, droppedExp, deathMessage);
        setArenakey(arenakey);
        setKiller(killer);
        haskiller = true;
    }
    
    public boolean hasKiller(){
        return haskiller;
    }
    
    public LivingEntity getKiller(){
        return killer;
    }
    
    public void setKiller(LivingEntity killer){
        this.killer = killer; 
    }
    
    public String getArenakey(){
        return arenakey;
    }
    
    public void setArenakey(String arenakey){
        this.arenakey = arenakey;
    }
    
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}