package Chanhne.config;

import java.io.File;
import java.io.IOException;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MainConfigManager {
    private final JavaPlugin plugin;
    private FileConfiguration config;
    private File configFile;

    public MainConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        setup();
    }

    private void setup() {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        reload();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Không thể lưu file config.yml");
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }
}