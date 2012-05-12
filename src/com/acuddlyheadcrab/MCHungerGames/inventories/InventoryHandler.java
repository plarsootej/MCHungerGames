package com.acuddlyheadcrab.MCHungerGames.inventories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.acuddlyheadcrab.MCHungerGames.HGplugin;
import com.acuddlyheadcrab.MCHungerGames.FileIO.AcuddlyConfiguration;
import com.acuddlyheadcrab.MCHungerGames.FileIO.Configs;
import com.acuddlyheadcrab.MCHungerGames.FileIO.YMLKey;


public class InventoryHandler {
    
    public static HGplugin plugin;
    public InventoryHandler(HGplugin instance){plugin = instance;}
    
    private static String separator = ", ";
    
    public static void saveInventory(Player player){
        AcuddlyConfiguration ud = Configs.getUD(player);
        ud.set(YMLKey.UD_INV_CONTENTS, toISkeylist(player.getInventory().getContents()));
        ud.set(YMLKey.UD_INV_ARMOR, toISkeylist(player.getInventory().getArmorContents()));
        ud.save();
    }
    
    public static List<String> toISkeylist(ItemStack[] ISarr){
        return toISkeylist(Arrays.asList(ISarr));
    }
    
    public static List<String> toISkeylist(List<ItemStack> islist){
        List<String> returnable = new ArrayList<String>();
        for(ItemStack is : islist){
            if(is!=null){
                if(is.getType()!=Material.AIR){
                    returnable.add(toISkey(is, islist.indexOf(is)));
                }
            }
        }
        return returnable;
    }
    
    public static String toISkey(ItemStack is, int slot){
        return (String.format("%1$s%6$s%2$s%6$s%3$s%6$s%4$s%6$s%5$s", 
                is.getTypeId(),
                is.getAmount(),
                is.getDurability(),
                is.getData().getData(),
                slot,
                separator
        ));
    }
    
    public static void updateInventory(Player player){
        AcuddlyConfiguration ud = Configs.getUD(player);
        PlayerInventory pinv = player.getInventory();
        for(String iskey : ud.getStringList(YMLKey.UD_INV_CONTENTS)){
            pinv.setItem(parseISkeySlot(iskey), parseISkey(iskey));
        }
        for(String iskey : ud.getStringList(YMLKey.UD_INV_ARMOR.key())){
            switch (parseISkeySlot(iskey)) {
                case 0: pinv.setBoots(parseISkey(iskey)); break;
                case 1: pinv.setLeggings(parseISkey(iskey)); break;
                case 2: pinv.setChestplate(parseISkey(iskey)); break;
                case 3: pinv.setHelmet(parseISkey(iskey)); break;
                default: break;
            }
        }
    }
    
    public static ItemStack parseISkey(String iskey){
        String[] args = iskey.split(separator);
        return new ItemStack(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Short.parseShort(args[2]),
                Byte.parseByte(args[3])
        );
    }
    
    public static int parseISkeySlot(String iskey){
        return Integer.parseInt(iskey.split(separator)[4]);
    }
}
