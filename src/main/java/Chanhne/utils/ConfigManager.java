package Chanhne.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import Chanhne.Main_GiftCode;

public class ConfigManager {
    private String cooldownMessage;
    private Main_GiftCode plugin;;
    private Set<String> blockedCommands = new HashSet<>();
    private Map<String, String> placeholders = new HashMap<>();

    public ConfigManager(Main_GiftCode plugin) {
        this.plugin = plugin;
    }

    public int getCooldownSeconds() {
        return plugin.getMainConfigManager().getConfig().getInt("cooldown-seconds", 30);
    }
    public String getCooldownMessage() {return cooldownMessage;}
    public Set<String> getBlockedCommands() {return blockedCommands;}
    public Map<String, String> getPlaceholders() {return new HashMap<>(placeholders);}
}