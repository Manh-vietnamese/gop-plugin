package Chanhne.core;

import java.util.List;
import java.util.UUID;

public class GiftCode {

    private final String code;                 // Tên hoặc chuỗi mã
    private int maxUses;                       // Số lần sử dụng tối đa
    private long Time_user;                    // Thời gian hết hạn (millis). 0 = không giới hạn
    private boolean active;                    // Trạng thái giftcode: bật/tắt
    private final List<UUID> redeemedPlayers;  // Danh sách người chơi đã sử dụng
    private final List<String> commands;       // Lệnh thực thi khi nhận code
    private final List<UUID> allowedPlayers;   // Danh sách whitelist (nếu rỗng thì ai cũng dùng được)

    public GiftCode(String code, int maxUses, long Time_user, boolean active, List<UUID> redeemedPlayers, List<String> commands, List<UUID> allowedPlayers) {
        this.code = code;
        this.maxUses = maxUses;
        this.active = active;
        this.redeemedPlayers = redeemedPlayers;
        this.commands = commands;
        this.allowedPlayers = allowedPlayers;
    }

    public int getMaxUses() {return maxUses;}

    public long getExpireAt() {return Time_user;}

    public void setMaxUses(int maxUses) {this.maxUses = maxUses;}
    public void setActive(boolean active) {this.active = active;}
    public void setExpireAt(long expireAt) {this.Time_user = expireAt;}

    public String getCode() {return code;}
    public boolean isActive() {return active;}

    public List<UUID> getAllowedPlayers() {return allowedPlayers;}
    public List<UUID> getRedeemedPlayers() {return redeemedPlayers;}
    public List<String> getCommands() {return commands;}

    // Kiểm tra còn hạn và chưa quá số lần sử dụng
    public boolean canUse(UUID playerId) {
        if (!active) return false; // Nếu giftcode đã tắt -> không dùng
        if (Time_user > 0 && System.currentTimeMillis() > Time_user) {
            // Nếu đã hết thời gian
            setActive(false);  // Vô hiệu hóa giftcode
            return false; 
        }
        if (!allowedPlayers.isEmpty() && !allowedPlayers.contains(playerId)) return false; // Kiểm tra whitelist
        if (redeemedPlayers.contains(playerId)) return false; // Mỗi người chơi chỉ được nhận 1 lần
        if (maxUses <= 0) {
            return false; // Kiểm tra số lần sử dụng còn lại
        }

        return true;
    }

    // Người chơi sử dụng giftcode
    public void redeem(UUID playerId) {
        redeemedPlayers.add(playerId);
        maxUses--; // Mỗi lần dùng trừ 1
    }
}
