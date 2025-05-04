package Chanhne.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import Chanhne.Main_GiftCode;

public class PlayerDeathListener implements Listener {
    private final Main_GiftCode plugin;

    public PlayerDeathListener(Main_GiftCode plugin) {
        this.plugin = plugin; // Lưu trữ tham chiếu đến plugin chính
    }

    /**
     * Xử lý sự kiện khi người chơi chết:
     * - Xóa cooldown combat hiện tại của người chơi
     * - Dừng hiển thị thanh thời gian cooldown trên Action Bar
     * @param event Sự kiện PlayerDeathEvent được kích hoạt khi người chơi chết
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity(); // Lấy đối tượng người chơi đã chết
        
        // Gọi CooldownManager để xóa trạng thái cooldown
        plugin.getCooldownManager().removeCooldown(player);
        
        // Logic kế thừa: removeCooldown() đã tự động gọi stopCooldownDisplay()
    }
}