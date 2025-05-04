package Chanhne;

import Chanhne.commands.GiftCode_Admin;
import Chanhne.commands.GiftCode_Redeem;
import Chanhne.commands.GiftCode_Completer;
import Chanhne.config.Config_GiftCode;
import Chanhne.config.GiftCodeManager;
import Chanhne.config.MainConfigManager;
import Chanhne.listeners.DamageListener;
import Chanhne.listeners.PlayerCommandListener;
import Chanhne.listeners.PlayerDeathListener;
import Chanhne.Messenger.Messager;
import Chanhne.utils.ConfigManager;
import Chanhne.utils.CooldownManager;

import java.io.File;
import java.util.List;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

public class Main_GiftCode extends JavaPlugin {
    private Messager Messager;
    private MainConfigManager mainConfigManager;
    private GiftCodeManager giftCodeManager;
    private Config_GiftCode configGiftCode;
    private CooldownManager cooldownManager;
    private ProtocolManager protocolManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        // Kiểm tra ProtocolLib
        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") == null) {
            getLogger().severe("ProtocolLib không được cài đặt! Plugin sẽ tắt.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        this.protocolManager = ProtocolLibrary.getProtocolManager();

        // Khởi tạo config managers
        this.mainConfigManager = new MainConfigManager(this); // Quản lý config.yml
        this.giftCodeManager = new GiftCodeManager(this);     // Quản lý giftcodes.yml
        this.configGiftCode = new Config_GiftCode(giftCodeManager);

        // Khởi tạo các file cấu hình
        checkAndCreateConfigs();
        saveDefaultMessages();

        // Khởi tạo các thành phần khác
        this.Messager = new Messager(getDataFolder());
        this.configManager = new ConfigManager(this);
        this.cooldownManager = new CooldownManager(this);

        // Đăng ký lệnh
        Objects.requireNonNull(getCommand("gc")).setExecutor(new GiftCode_Redeem(this, configGiftCode));
        Objects.requireNonNull(getCommand("gc")).setTabCompleter(new GiftCode_Completer(configGiftCode));
        Objects.requireNonNull(getCommand("code")).setExecutor(new GiftCode_Admin(this, configGiftCode));

        // Đăng ký listener
        registerListeners();
    }

    // ============ Các phương thức hỗ trợ ============
    private void checkAndCreateConfigs() {
        // Tạo thư mục plugin nếu chưa tồn tại
        File dataFolder = getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            getLogger().warning("Không thể tạo thư mục: " + dataFolder.getAbsolutePath());
        }

        // Khởi tạo config.yml và giftcodes.yml
        mainConfigManager.reload(); // Đảm bảo config.yml được tải
        giftCodeManager.reload();   // Đảm bảo giftcodes.yml được tải
    }

    private void registerListeners() {
        // Truyền chỉ plugin vào DamageListener
        getServer().getPluginManager().registerEvents(new DamageListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerCommandListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
    }

    // ============ Getter methods ============
    public MainConfigManager getMainConfigManager() {
        return mainConfigManager;
    }

    public Config_GiftCode getGiftCodeConfig() {
        return configGiftCode;
    }

    @Override
    public void onDisable() {
        if (cooldownManager != null) {
            cooldownManager.cleanup();
        }
        getLogger().info("GiftCode Plugin disabled!");
    }

    // Phương thức lấy dữ liệu từ config.yml
    public int getCooldownSeconds() {
        return mainConfigManager.getConfig().getInt("cooldown-seconds", 30);
    }

    public List<String> getBlockedCommands() {
        return mainConfigManager.getConfig().getStringList("blocked-commands");
    }
    // Getter methods
    public Messager getMessager() {return Messager;}
    public ConfigManager getConfigManager() {return configManager;}
    public ProtocolManager getProtocolManager() {return protocolManager;}
    public CooldownManager getCooldownManager() {return cooldownManager;}

    // kiểm tra và lưu file,tạo file khi còn thiếu
    private void saveDefaultMessages() {
        File msgFile = new File(getDataFolder(), "messages.yml");
        if (!msgFile.exists()) {
            saveResource("messages.yml", false);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Main_GiftCode plugin = (Main_GiftCode) sender.getServer().getPluginManager().getPlugin("GiftCodePlugin");
        if (plugin == null) {
            sender.sendMessage("§cPlugin not found.");
            return false;
        }
        return false;
    }
    
}
