package com.acuddlyheadcrab.MCHungerGames.FileIO;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.acuddlyheadcrab.util.PluginInfo;
import com.acuddlyheadcrab.util.YMLKeys;

public class AcuddlyConfiguration extends FileConfiguration{
    private File file;
    private FileConfiguration fileconfig;
    
    public AcuddlyConfiguration(File file, FileConfiguration fileconfig){
        setFile(file);
        setConfig(fileconfig);
    }
    
    public File getFile(){
        return this.file;
    }
    
    public FileConfiguration getConfig(){
        return this.fileconfig;
    }
    
    public void setFile(File file){
        this.file = file;
    }
    
    public void setConfig(FileConfiguration fileconfig){
        this.fileconfig = fileconfig;
    }
    
    @Override
    public void set(String path, Object object){
        fileconfig.set(path, object);
        save();
    }
    
    public void set(YMLKeys path, Object object){
        set(path.key(), object);
    }
    
    public void save(){
        try {
            fileconfig.save(file);
        } catch (IOException e) {
            PluginInfo.sendPluginInfo("Could not save file!");
            PluginInfo.sendPluginInfo("Local path: "+file.getParent()+"\\"+file.getName());
            e.printStackTrace();
        }
    }

    @Override
    protected String buildHeader() {
        return null;
    }

    @Override
    public void loadFromString(String arg0)
            throws InvalidConfigurationException {
        return;
    }

    @Override
    public String saveToString() {
        return null;
    }
    
    // Modified getters
    
    @Override
    public Object get(String path) {
        return fileconfig.get(path);
    }
    
    @Override
    public Object get(String path, Object def) {
        return fileconfig.get(path, def);
    }
    
    @Override
    public ConfigurationSection createSection(String path) {
        return fileconfig.createSection(path);
    }
    
    @Override
    public ConfigurationSection createSection(String path, Map<?, ?> map) {
        return fileconfig.createSection(path, map);
    }
    
    @Override
    public String getString(String path) {
        return fileconfig.getString(path);
    }
    
    @Override
    public String getString(String path, String def){
        return fileconfig.getString(path, def);
    }
    
    @Override
    public int getInt(String path) {
        return fileconfig.getInt(path);
    }
    
    @Override
    public boolean isInt(String path) {
        return fileconfig.isInt(path);
    }
    
    @Override
    public boolean getBoolean(String path) {
        return fileconfig.getBoolean(path);
    }
    
    @Override
    public double getDouble(String path) {
        return fileconfig.getDouble(path);
    }
    
    @Override
    public long getLong(String path) {
        return fileconfig.getLong(path);
    }
    
    @Override
    public List<?> getList(String path) {
        return fileconfig.getList(path);
    }
    
    @Override
    public List<String> getStringList(String path) {
        return fileconfig.getStringList(path);
    }
    
    @Override
    public List<Integer> getIntegerList(String path) {
        return fileconfig.getIntegerList(path);
    }
    
    @Override
    public List<Boolean> getBooleanList(String path) {
        return fileconfig.getBooleanList(path);
    }
    
    @Override
    public List<Double> getDoubleList(String path) {
        return fileconfig.getDoubleList(path);
    }
    
    @Override
    public List<Float> getFloatList(String path) {
        return fileconfig.getFloatList(path);
    }
    
    @Override
    public List<Long> getLongList(String path) {
        return fileconfig.getLongList(path);
    }
    
    @Override
    public List<Byte> getByteList(String path) {
        return fileconfig.getByteList(path);
    }

    @Override
    public List<Character> getCharacterList(String path) {
        return fileconfig.getCharacterList(path);
    }
    
    @Override
    public List<Short> getShortList(String path) {
        return fileconfig.getShortList(path);
    }
    
    @Override
    public List<Map<?, ?>> getMapList(String path) {
        return fileconfig.getMapList(path);
    }
    
    @Override
    public Vector getVector(String path) {
        return fileconfig.getVector(path);
    }
    
    @Override
    public OfflinePlayer getOfflinePlayer(String path) {
        return fileconfig.getOfflinePlayer(path);
    }

    @Override
    public ItemStack getItemStack(String path) {
        return fileconfig.getItemStack(path);
    }

    @Override
    public ConfigurationSection getConfigurationSection(String path) {
        return fileconfig.getConfigurationSection(path);
    }
    
    public Object get(YMLKeys path) {
        return fileconfig.get(path.key());
    }
    
    public Object get(YMLKeys path, Object def) {
        return fileconfig.get(path.key(), def);
    }
    
    public ConfigurationSection createSection(YMLKeys path) {
        return fileconfig.createSection(path.key());
    }
    
    public ConfigurationSection createSection(YMLKeys path, Map<?, ?> map) {
        return fileconfig.createSection(path.key(), map);
    }
    
    public String getString(YMLKeys path) {
        return fileconfig.getString(path.key());
    }
    
    public String getString(YMLKeys path, String def){
        return fileconfig.getString(path.key(), def);
    }
    
    public int getInt(YMLKeys path) {
        return fileconfig.getInt(path.key());
    }
    
    public boolean isInt(YMLKeys path) {
        return fileconfig.isInt(path.key());
    }
    
    public boolean getBoolean(YMLKeys path) {
        return fileconfig.getBoolean(path.key());
    }
    
    public double getDouble(YMLKeys path) {
        return fileconfig.getDouble(path.key());
    }
    
    public long getLong(YMLKeys path) {
        return fileconfig.getLong(path.key());
    }
    
    public List<?> getList(YMLKeys path) {
        return fileconfig.getList(path.key());
    }
    
    public List<String> getStringList(YMLKeys path) {
        return fileconfig.getStringList(path.key());
    }
    
    public List<Integer> getIntegerList(YMLKeys path) {
        return fileconfig.getIntegerList(path.key());
    }
    
    public List<Boolean> getBooleanList(YMLKeys path) {
        return fileconfig.getBooleanList(path.key());
    }
    
    public List<Double> getDoubleList(YMLKeys path) {
        return fileconfig.getDoubleList(path.key());
    }
    
    public List<Float> getFloatList(YMLKeys path) {
        return fileconfig.getFloatList(path.key());
    }
    
    public List<Long> getLongList(YMLKeys path) {
        return fileconfig.getLongList(path.key());
    }
    
    public List<Byte> getByteList(YMLKeys path) {
        return fileconfig.getByteList(path.key());
    }

    public List<Character> getCharacterList(YMLKeys path) {
        return fileconfig.getCharacterList(path.key());
    }
    
    public List<Short> getShortList(YMLKeys path) {
        return fileconfig.getShortList(path.key());
    }
    
    public List<Map<?, ?>> getMapList(YMLKeys path) {
        return fileconfig.getMapList(path.key());
    }
    
    public Vector getVector(YMLKeys path) {
        return fileconfig.getVector(path.key());
    }
    
    public OfflinePlayer getOfflinePlayer(YMLKeys path) {
        return fileconfig.getOfflinePlayer(path.key());
    }

    public ItemStack getItemStack(YMLKeys path) {
        return fileconfig.getItemStack(path.key());
    }

    public ConfigurationSection getConfigurationSection(YMLKeys path) {
        return fileconfig.getConfigurationSection(path.key());
    }
}
