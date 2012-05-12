package com.acuddlyheadcrab.apiEvents.parents;

import org.bukkit.event.HandlerList;

import com.acuddlyheadcrab.MCHungerGames.FileIO.Configs;


public class ArenaModifyEvent extends ArenaEvent{
    private String path;
    private Object old_ob;
    private Object new_ob;
    
    public ArenaModifyEvent(String arenakey, String path, Object new_ob){
        super(arenakey);
        setPath(path);
        this.old_ob = Configs.getArenas().get(path);
        setNew(new_ob);
    }
    
    public String getPath(){
        return this.path;
    }
    
    public Object getOld(){
        return this.old_ob;
    }
    
    public Object getNew(){
        return this.new_ob;
    }
    
    public void setNew(Object newob){
        this.new_ob = newob;
    }
    
    public void setPath(String path){
        this.path = path;
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