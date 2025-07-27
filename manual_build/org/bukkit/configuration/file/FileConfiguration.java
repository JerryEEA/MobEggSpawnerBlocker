package org.bukkit.configuration.file;

public abstract class FileConfiguration {
    public abstract String getString(String path);
    public abstract String getString(String path, String def);
    public abstract boolean getBoolean(String path);
    public abstract boolean getBoolean(String path, boolean def);
    public abstract int getInt(String path, int def);
    public abstract boolean isSet(String path);
    public abstract boolean isString(String path);
    public abstract boolean isBoolean(String path);
    public abstract void set(String path, Object value);
}