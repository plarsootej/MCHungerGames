package com.acuddlyheadcrab.util;

import org.bukkit.permissions.Permission;


public enum Perms {
    
    HG_RELOAD(new Permission("HG.reload")),
    
    HGA_LOUNGE(new Permission("HG.arena.lounge")),
    HGA_TP(new Permission("HG.arena.tp")),
    HGA_TPALL(new Permission("HG.arena.tpall")),
    HGA_INFO(new Permission("HG.arena.info")),
    HGA_NEW(new Permission("HG.arena.new")),
    HGA_DEL(new Permission("HG.arena.del")),
    HGA_LIST(new Permission("HG.arena.list")),
    HGA_RENAME(new Permission("HG.arena.rename")),
    HGA_JOIN(new Permission("HG.arena.join")),
    HGA_LEAVE(new Permission("HG.arena.leave")),
    HGA_TRIBUTES(new Permission("HG.arena.tributes")),
    HGA_CHESTRESET(new Permission("HG.arena.chestreset")),
    
    HGAE_SETCCP(new Permission("HG.edit.setcenter")),
    HGAE_SETLOUNGE(new Permission("HG.edit.setlounge")),
    HGAE_LIMIT(new Permission("HG.edit.radius")),
    HGAE_ADDGM(new Permission("HG.edit.addgm")),
    HGAE_ADDTRIB(new Permission("HG.edit.addtrib")),
    HGAE_REMOVEGM(new Permission("HG.edit.removegm")),
    HGAE_REMOVETRIB(new Permission("HG.edit.removetrib")),
    HGAE_SETTRIBSPAWN(new Permission("HG.edit.removetrib")),
    HGAE_ADDSPP(new Permission("HG.edit.addspp")),
    
    HGG_START(new Permission("HG.game.start")),
    HGG_STOP(new Permission("HG.game.stop")),
    
    SPC(new Permission("HG.spawnchest"))
    ;
    
    private Permission permission;
    
    Perms(Permission perm){
        permission = perm;
    }

    public Permission perm() {
        return permission;
    }
}