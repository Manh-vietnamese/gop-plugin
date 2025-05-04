package Chanhne.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Chanhne.Main_GiftCode;
import Chanhne.config.Config_GiftCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Redeem implements CommandExecutor {
    private final Main_GiftCode plugin;
    private final Config_GiftCode configGiftCode;

    public Redeem(Main_GiftCode plugin, Config_GiftCode configGiftCode) {
        this.configGiftCode = configGiftCode;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Kiểm tra quyền
        if (!sender.hasPermission("SP.admin")) {
            sender.sendMessage(plugin.getMessager().get("no_permission"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(plugin.getMessager().get("plugin_user"));
            return true;
        }

        switch (args[0].toLowerCase()) {
            
            case "help":
            case "?":
                sendHelpMessage(sender);
                break;

            case "create":
                handleCreate(sender, args);
                break;

            case "delete":
                if (args.length < 2) {
                    sender.sendMessage(plugin.getMessager().get("delete_user"));
                    return true;
                }
                configGiftCode.deleteGiftCode(args[1]);

                Map<String, String> deleteGiftCode = new HashMap<>();
                deleteGiftCode.put("code", args[1]); // Thay thế %code% bằng giá trị thực tế

                sender.sendMessage(plugin.getMessager().get("giftcode_deleted", deleteGiftCode));

                break;

            case "reload":
                plugin.reloadAllConfigs();
                sender.sendMessage(plugin.getMessager().get("reload_plugin"));
                break;

            case "enable":
                if (args.length < 2) {
                    sender.sendMessage(plugin.getMessager().get("enable_user"));
                    return true;
                }
                configGiftCode.enableGiftCode(args[1]);

                Map<String, String> enableGiftCode = new HashMap<>();
                enableGiftCode.put("code", args[1]); // Thay thế %code% bằng giá trị thực tế

                sender.sendMessage(plugin.getMessager().get("giftcode_enableGiftCode", enableGiftCode));
                break;

            case "disable":
                if (args.length < 2) {
                    sender.sendMessage(plugin.getMessager().get("disable_user"));
                    return true;
                }
                configGiftCode.disableGiftCode(args[1]);

                Map<String, String> disableGiftCode = new HashMap<>();
                disableGiftCode.put("code", args[1]); // Thay thế %code% bằng giá trị thực tế

                sender.sendMessage(plugin.getMessager().get("giftcode_disableGiftCode", disableGiftCode));
                break;

            case "list":
                sender.sendMessage(plugin.getMessager().get("list_giftcode"));
                for (String code : configGiftCode.getGiftCodes().keySet()) {
                    sender.sendMessage("- " + code);
                }
                break;

            case "whitelist":
            case "permission":
                if (args.length < 3) {
                    Map<String, String> placeholders = new HashMap<>();
                    placeholders.put("command", args[0]); // Thay thế %command% bằng tên lệnh thực tế

                    sender.sendMessage(plugin.getMessager().get("usage_sp", placeholders));
                    return true;
                }
                String code = args[1];
                String playerName = args[2];

                // Thay thế getOfflinePlayer bằng getPlayerExact
                Player targetPlayer = Bukkit.getPlayerExact(playerName);
                if (targetPlayer == null) {
                    Map<String, String> placeholders = new HashMap<>();
                    placeholders.put("player", playerName); // Thay thế %player% bằng tên thực tế

                    sender.sendMessage(plugin.getMessager().get("player_not_found", placeholders));
                    return true;
                }

                UUID playerId = targetPlayer.getUniqueId();
                configGiftCode.addPlayerToWhitelist(code, playerId);

                // Tạo placeholders để thay thế trong tin nhắn
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("player", playerName);
                placeholders.put("code", code);

                // Gửi tin nhắn từ messages.yml
                sender.sendMessage(plugin.getMessager().get("whitelist_added", placeholders));
                break;
        }

        return true;
    }

    private void handleCreate(CommandSender sender, String[] args) {
        // /sp create random <num>
        if (args.length >= 3 && args[1].equalsIgnoreCase("random")) {
            int num = 1;
            try {
                num = Integer.parseInt(args[2]);
            } catch (NumberFormatException ignored) {}
            // Tạo giftcode random, mỗi code dùng 1 lần
            List<String> codes = configGiftCode.createRandomCodes(num);

            // Tạo placeholders để thay thế trong tin nhắn
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("num", String.valueOf(num)); // Thay %num% bằng số thực tế

            // Gửi tin nhắn từ messages.yml
            sender.sendMessage(plugin.getMessager().get("giftcode_random_created", placeholders));

            for (String c : codes) {
                sender.sendMessage(" - " + c);
            }
            return;
        }

        // /sp create <code> [number] [time]
        // code => tên giftcode
        // number => số lần sử dụng
        // time => dạng d/h/m => parse sang millis
        String codeName = args[1];
        int uses = 1;          // mặc định 1
        long expireAt = 0;     // 0 = không hạn

        if (args.length >= 3) {
            try {
                uses = Integer.parseInt(args[2]);
            } catch (NumberFormatException ignored) {}
        }

        if (args.length >= 4) {
            expireAt = configGiftCode.parseTime(args[3]);
        }

        configGiftCode.createGiftCode(codeName, uses, expireAt);

        // Tạo placeholders để thay thế trong tin nhắn
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("code", codeName);
        placeholders.put("uses", String.valueOf(uses));

        // Gửi tin nhắn tạo thành công
        sender.sendMessage(plugin.getMessager().get("giftcode_created", placeholders));
        sender.sendMessage(plugin.getMessager().get("giftcode_details", placeholders));

        // Kiểm tra thời gian hết hạn
        if (expireAt == 0) {
            sender.sendMessage(plugin.getMessager().get("giftcode_no_expiry"));
        } else {
            placeholders.put("expiry", String.valueOf(expireAt)); // Thay thế %expiry%
            sender.sendMessage(plugin.getMessager().get("giftcode_expiry", placeholders));
        }

    }

    private void sendHelpMessage(CommandSender sender) {
        List<String> helpMessages = plugin.getMessager().getList("help_message");
        if (helpMessages == null || helpMessages.isEmpty()) {
            // Tin nhắn mặc định nếu không tìm thấy trong messages.yml
            sender.sendMessage("§6===== &eGiftCode Help &6=====");
            sender.sendMessage("§a/sp create <code> [uses] [time] §7- Tạo giftcode");
            sender.sendMessage("§a/sp create random <số lượng> §7- Tạo giftcode ngẫu nhiên");
            sender.sendMessage("§a/sp delete <code> §7- Xóa giftcode");
            sender.sendMessage("§a/sp reload §7- Reload toàn bộ plugin");
            sender.sendMessage("§a/sp list §7- Xem danh sách giftcode");
            sender.sendMessage("§a/sp enable/disable <code> §7- Bật/tắt giftcode");
            sender.sendMessage("§a/sp whitelist/permission <code> <player> §7- Thêm player vào whitelist");
        } else {
            for (String msg : helpMessages) {
                sender.sendMessage(msg);
            }
        }
    }
}
