package Chanhne.listeners;

import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import Chanhne.Main_GiftCode;

public class DamageListener implements Listener {
    private final Main_GiftCode plugin;

    public DamageListener(Main_GiftCode plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageEvent event) {
        // Bỏ qua nếu thực thể không phải Player hoặc sự kiện đã bị hủy
        if (!(event.getEntity() instanceof Player player) || event.isCancelled()) return;
        
        // Kích hoạt cooldown và hiển thị Action Bar
        plugin.getCooldownManager().applyCooldown(player);
        plugin.getCooldownManager().startCooldownDisplay(player);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        
        // Bỏ qua nếu người chơi có quyền admin
        if (player.hasPermission("Sunflower.SP.admin")) return;

        // Trích xuất tên lệnh
        String rawCommand = event.getMessage().substring(1).split(" ")[0].toLowerCase();
        
        // Lấy danh sách lệnh bị chặn từ MainConfigManager
        List<String> blockedCommands = plugin.getMainConfigManager().getConfig().getStringList("blocked-commands");
        
        // Kiểm tra lệnh có bị chặn và đang trong cooldown
        if (blockedCommands.contains(rawCommand) && plugin.getCooldownManager().isInCooldown(player)) {
            event.setCancelled(true);
            player.sendMessage(plugin.getMessager().get("no_command"));
        }
    }
}