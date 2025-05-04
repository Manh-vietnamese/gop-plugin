package Chanhne.Messenger;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class Messager {

    private static final Logger LOGGER = Logger.getLogger(Messager.class.getName());
    private final File messagesFile;
    private YamlConfiguration messagesConfig;

    public Messager(File dataFolder) {
        this.messagesFile = new File(dataFolder, "messages.yml");
        reload();  // Tải lại cấu hình khi khởi tạo
    }

    // Reload lại file messages.yml
    public void reload() {
        if (!messagesFile.exists()) {
            try {
                if (messagesFile.createNewFile()) {
                    LOGGER.info("Đã tạo file messages.yml mới.");
                } else {
                    LOGGER.info("File messages.yml đã tồn tại.");
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi tạo file messages.yml: " + e.getMessage(), e);
            }
        }
        // Load cấu hình
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    // Lấy tin nhắn theo key, nếu không tìm thấy thì trả về thông báo lỗi
    public String get(String key) {
        return get(key, null);
    }

    // Lấy tin nhắn và thay thế các placeholders nếu có
    public String get(String key, Map<String, String> placeholders) {
        String msg = messagesConfig.getString(key, "&c[Không tìm thấy key: " + key + "]");

        // Thay thế các placeholder (ví dụ %code%)
        if (placeholders != null) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                msg = msg.replace("%" + entry.getKey() + "%", entry.getValue());
            }
        }

        // Chuyển & thành ký tự màu §
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
