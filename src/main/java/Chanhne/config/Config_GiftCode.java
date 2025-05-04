package Chanhne.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import Chanhne.core.GiftCode;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Config_GiftCode {
    private final GiftCodeManager giftCodeManager;
    private final Map<String, GiftCode> giftCodes;

    public Config_GiftCode(GiftCodeManager giftCodeManager) {
        this.giftCodeManager = giftCodeManager;
        this.giftCodes = new HashMap<>();
        loadGiftCodes();
    }

    // Đọc dữ liệu từ giftcodes.yml
    private void loadGiftCodes() {
        FileConfiguration config = giftCodeManager.getConfig();
        
        // Tạo section nếu không tồn tại
        if (!config.isConfigurationSection("giftcodes")) {
            config.createSection("giftcodes");
            saveConfig();
            return;
        }

        for (String codeKey : config.getConfigurationSection("giftcodes").getKeys(false)) {
            int uses = config.getInt("giftcodes." + codeKey + ".uses");
            long createGiftCode = config.getLong("giftcodes." + codeKey + ".createGiftCode");
            boolean active = config.getBoolean("giftcodes." + codeKey + ".active", true);

            List<String> redeemedStr = config.getStringList("giftcodes." + codeKey + ".redeemedPlayers");
            List<UUID> redeemedPlayers = new ArrayList<>();
            for (String s : redeemedStr) {
                redeemedPlayers.add(UUID.fromString(s));
            }

            List<String> commands = config.getStringList("giftcodes." + codeKey + ".commands");

            List<String> allowedStr = config.getStringList("giftcodes." + codeKey + ".allowedPlayers");
            List<UUID> allowedPlayers = new ArrayList<>();
            for (String s : allowedStr) {
                allowedPlayers.add(UUID.fromString(s));
            }

            GiftCode giftCode = new GiftCode(
                    codeKey, uses, createGiftCode, active, redeemedPlayers, commands, allowedPlayers
            );
            giftCodes.put(codeKey, giftCode);
        }
    }

    // Tạo giftcode
    public void createGiftCode(String code, int uses, long createGiftCode) {
        GiftCode giftCode = new GiftCode(
                code,
                uses,
                createGiftCode,
                true,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
        giftCodes.put(code, giftCode);
        
        // Ghi vào giftcodes.yml
        FileConfiguration config = giftCodeManager.getConfig();
        String path = "giftcodes." + code;
        config.set(path + ".uses", uses);
        config.set(path + ".createGiftCode", createGiftCode);
        config.set(path + ".active", true);
        config.set(path + ".redeemedPlayers", new ArrayList<>());
        config.set(path + ".commands", new ArrayList<>());
        config.set(path + ".allowedPlayers", new ArrayList<>());
        saveConfig();
    }

    // Tạo giftcode random
    public List<String> createRandomCodes(int quantity) {
        List<String> generatedCodes = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            String randomCode = generateRandomString();
            createGiftCode(randomCode, 1, 0);
            generatedCodes.add(randomCode);
        }
        return generatedCodes;
    }

    private String generateRandomString() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = ThreadLocalRandom.current().nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    // Xóa giftcode
    public void deleteGiftCode(String code) {
        giftCodes.remove(code);
        giftCodeManager.getConfig().set("giftcodes." + code, null);
        saveConfig();
    }

    // Thêm vào whitelist
    public void addPlayerToWhitelist(String code, UUID playerId) {
        GiftCode giftCode = giftCodes.get(code);
        if (giftCode == null) return;

        List<UUID> allowed = giftCode.getAllowedPlayers();
        if (!allowed.contains(playerId)) {
            allowed.add(playerId);
        }
        
        FileConfiguration config = giftCodeManager.getConfig();
        List<String> allowedStr = new ArrayList<>();
        for (UUID u : allowed) {
            allowedStr.add(u.toString());
        }
        config.set("giftcodes." + code + ".allowedPlayers", allowedStr);
        saveConfig();
    }

    // Bật/tắt giftcode
    public void enableGiftCode(String code) {
        GiftCode giftCode = giftCodes.get(code);
        if (giftCode == null) return;
        giftCode.setActive(true);
        giftCodeManager.getConfig().set("giftcodes." + code + ".active", true);
        saveConfig();
    }

    public void disableGiftCode(String code) {
        GiftCode giftCode = giftCodes.get(code);
        if (giftCode == null) return;
        giftCode.setActive(false);
        giftCodeManager.getConfig().set("giftcodes." + code + ".active", false);
        saveConfig();
    }

    // Xử lý thời gian
    public long parseTime(String timeString) {
        try {
            long now = System.currentTimeMillis();
            if (timeString.endsWith("d")) {
                int days = Integer.parseInt(timeString.replace("d", ""));
                return now + days * 86400000L;
            } else if (timeString.endsWith("h")) {
                int hours = Integer.parseInt(timeString.replace("h", ""));
                return now + hours * 3600000L;
            } else if (timeString.endsWith("m")) {
                int minutes = Integer.parseInt(timeString.replace("m", ""));
                return now + minutes * 60000L;
            }
        } catch (NumberFormatException e) {
            Bukkit.getLogger().warning("Không thể parse thời gian: " + timeString);
        }
        return 0;
    }

    // Lưu config
    private void saveConfig() {
        giftCodeManager.saveConfig();
    }

    public Map<String, GiftCode> getGiftCodes() {
        return giftCodes;
    }

    // Reload chỉ xử lý giftcodes.yml
    public void reload() {
        giftCodeManager.reload();
        giftCodes.clear();
        loadGiftCodes();
    }

    // Cập nhật giftcode
    public void updateGiftCodeInConfig(String code) {
        GiftCode giftCode = giftCodes.get(code);
        if (giftCode == null) return;

        FileConfiguration config = giftCodeManager.getConfig();
        config.set("giftcodes." + code + ".uses", giftCode.getMaxUses());

        List<String> redeemedUUIDs = new ArrayList<>();
        for (UUID uuid : giftCode.getRedeemedPlayers()) {
            redeemedUUIDs.add(uuid.toString());
        }
        config.set("giftcodes." + code + ".redeemedPlayers", redeemedUUIDs);
        saveConfig();
    }
}