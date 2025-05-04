package Chanhne.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Chanhne.Main_GiftCode;
import Chanhne.config.Config_GiftCode;
import Chanhne.core.GiftCode;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GiftCode_Admin implements CommandExecutor {
    private final Main_GiftCode plugin;
    private final Config_GiftCode configGiftCode;

    public GiftCode_Admin(Main_GiftCode plugin, Config_GiftCode configGiftCode) {
        this.plugin = plugin;
        this.configGiftCode = configGiftCode;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Kiểm tra xem người gọi lệnh có phải là người chơi?
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessager().get("no_permission"));
            return true;
        }
        Player player = (Player) sender;

        // Kiểm tra cú pháp /code <mã_code>
        if (args.length < 1) {
            player.sendMessage(plugin.getMessager().get("code_user"));
            return true;
        }
        String codeName = args[0];

        // Lấy GiftCode
        GiftCode giftCode = configGiftCode.getGiftCodes().get(codeName);
        if (giftCode == null) {
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("code", codeName); // Thay thế %code% bằng codeName

            player.sendMessage(plugin.getMessager().get("giftcode_not_found", placeholders));
            return true;
        }

        // Kiểm tra người chơi có thể sử dụng
        UUID playerId = player.getUniqueId();
        if (!giftCode.canUse(playerId)) {
            player.sendMessage(plugin.getMessager().get("no_giftcode"));
            return true;
        }

        // Thực thi commands (nếu còn lượt)
        for (String cmd : giftCode.getCommands()) {
            String finalCmd = cmd.replace("{player}", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCmd);
        }

        // Gọi redeem => lưu playerId, giảm uses
        giftCode.redeem(playerId);

        // **Cập nhật config** (để lưu lại uses đã bị giảm & danh sách player)
        configGiftCode.updateGiftCodeInConfig(codeName);
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("code", codeName); // Thay thế %code% bằng codeName

        // Thông báo
        player.sendMessage(plugin.getMessager().get("giftcode_redeemed", placeholders));

        // Nếu uses = 0 => xóa giftcode
        if (giftCode.getMaxUses() <= 0) {
            configGiftCode.deleteGiftCode(codeName);
            return true;  // Dừng, không chạy commands
        }

        return true;
    }
}
