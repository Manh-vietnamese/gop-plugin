# GiftCode Plugin - Hướng dẫn sử dụng

Plugin quản lý GiftCode cho Minecraft server, hỗ trợ tạo code, giới hạn lượt dùng, thêm whitelist, và tích hợp với permission hệ thống.

## 📦 Cài đặt
1. Tải file `.jar` của plugin từ [releases](https://example.com).
2. Đặt file vào thư mục `plugins/` của server.
3. Khởi động lại server để tạo file cấu hình.

## 🔑 Quyền (Permissions)
- **Admin**: `SP.admin` - Truy cập tất cả lệnh quản trị.
- **Người dùng**: `SP.use` - Sử dụng lệnh `/code` để nhập giftcode.

## 📜 Lệnh cơ bản

### **Lệnh Admin (`/sp`)**
| Lệnh | Mô tả | Ví dụ |
|------|-------|-------|
| `/sp create <code> [uses] [time]` | Tạo giftcode mới | `/sp create VIP 5 7d` |
| `/sp create random <số lượng>` | Tạo giftcode ngẫu nhiên | `/sp create random 3` |
| `/sp delete <code>` | Xóa giftcode | `/sp delete VIP` |
| `/sp reload` | Reload cấu hình plugin | `/sp reload` |
| `/sp list` | Xem danh sách giftcode | `/sp list` |
| `/sp enable <code>` | Kích hoạt giftcode | `/sp enable VIP` |
| `/sp disable <code>` | Vô hiệu hóa giftcode | `/sp disable VIP` |
| `/sp whitelist <code> <player>` | Thêm player vào whitelist | `/sp whitelist VIP PlayerA` |
| `/sp permission <code> <player>` | Thêm quyền cho player | `/sp permission VIP PlayerA` |
| `/sp help` | Xem hướng dẫn | `/sp help` |

### **Lệnh Người dùng (`/code`)**
| Lệnh | Mô tả | Ví dụ |
|------|-------|-------|
| `/code <mã>` | Nhập giftcode | `/code VIP123` |

## ⚙️ Cấu hình
- File cấu hình: `plugins/GiftCode/config.yml`
- Tùy chỉnh thông báo trong `messages.yml`.
- Cấu hình giftcode trong `giftcodes.yml`.

## 📌 Ví dụ
### Tạo giftcode:
```bash
/sp create VIP 10 30d  # 10 lượt dùng, hết hạn sau 30 ngày
/sp create random 5    # Tạo 5 code ngẫu nhiên
```
Thêm whitelist:
bash
/sp whitelist VIP PlayerA

## ❓ FAQ
1. Lỗi "No permission": Cấp quyền SP.admin hoặc SP.use.
2. Giftcode không hoạt động: Kiểm tra trạng thái bằng `/sp list` và dùng `/sp enable <code>`.
2. Thời gian hết hạn: Định dạng d (ngày), h (giờ), m (phút). Ví dụ: 7d12h.
## 🌐 Wiki & Hỗ trợ
- Xem chi tiết tại Wiki.

- Báo lỗi: Issues.

## 🛠️ Tác giả
- Plugin được phát triển bởi Chanhne. Liên hệ: contact@example.com.
