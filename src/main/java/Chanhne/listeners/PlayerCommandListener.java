package Chanhne.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import Chanhne.Main_GiftCode;

public class PlayerCommandListener implements Listener {
    private final Main_GiftCode plugin; 

    public PlayerCommandListener(Main_GiftCode plugin) {
        this.plugin = plugin; 
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String rawCommand = event.getMessage().substring(1).split(" ")[0].toLowerCase();

        if (shouldBlockCommand(player, rawCommand)) {
            event.setCancelled(true);
        }
    }

    private boolean shouldBlockCommand(Player player, String command) {
        return plugin.getCooldownManager().isInCooldown(player)
                && plugin.getBlockedCommands().contains(command)
                && !player.hasPermission("Sunflower.SP.admin");
    }
}