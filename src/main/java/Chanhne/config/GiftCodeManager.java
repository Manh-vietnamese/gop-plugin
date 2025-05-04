package Chanhne.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;

public class GiftCodeManager {
    private final JavaPlugin plugin;
    private FileConfiguration giftCodesConfig;
    private File configFile;

    public GiftCodeManager(JavaPlugin plugin) {
        this.plugin = plugin;
        setup();
    }

    private void setup() {
        configFile = new File(plugin.getDataFolder(), "giftcodes.yml");
        if (!configFile.exists()) {
            plugin.saveResource("giftcodes.yml", false);
        }
        reload();
    }

    public FileConfiguration getConfig() {
        return giftCodesConfig;
    }

    public void saveConfig() {
        try {
            giftCodesConfig.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Không thể lưu file giftcodes.yml");
        }
    }

    public void reload() {
        giftCodesConfig = YamlConfiguration.loadConfiguration(configFile);
    }
}