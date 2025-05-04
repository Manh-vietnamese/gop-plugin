package Chanhne.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import Chanhne.config.Config_GiftCode;

import java.util.ArrayList;
import java.util.List;

public class GiftCode_Completer implements TabCompleter {

    private final Config_GiftCode configGiftCode;

    public GiftCode_Completer(Config_GiftCode configGiftCode) {
        this.configGiftCode = configGiftCode;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        // Nếu lệnh không phải /gc thì bỏ qua
        if (!command.getName().equalsIgnoreCase("gc")) {
            return null;
        }

        // Gợi ý subcommand cho arg[0]
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            // Các sub-lệnh có sẵn:
            suggestions.add("create");
            suggestions.add("delete");
            suggestions.add("reload");
            suggestions.add("enable");
            suggestions.add("disable");
            suggestions.add("list");
            suggestions.add("whitelist");
            suggestions.add("permission");
            return partialMatch(suggestions, args[0]);
        }

        // Nếu arg[0] là "delete", "enable", "disable", "whitelist", "permission"
        // thì gợi ý tên giftcode ở arg[1]
        if (args.length == 2) {
            String sub = args[0].toLowerCase();
            if (sub.equals("delete")
                    || sub.equals("enable")
                    || sub.equals("disable")
                    || sub.equals("whitelist")
                    || sub.equals("permission")
            ) {
                List<String> codes = new ArrayList<>(configGiftCode.getGiftCodes().keySet());
                return partialMatch(codes, args[1]);
            }
            // Nếu sub = "create" => gợi ý "random"
            if (sub.equals("create")) {
                List<String> suggestions = new ArrayList<>();
                suggestions.add("random"); // Gợi ý "random"
                // Hoặc bạn có thể để trống => return null
                return partialMatch(suggestions, args[1]);
            }
        }

        // Nếu subcommand = "whitelist" hoặc "permission" => arg[2] gợi ý tên người chơi
        // Ở đây ta để trống, player name tuỳ plugin auto-complete
        // => return null

        return null; // không gợi ý thêm
    }

    // Hàm hỗ trợ lọc kết quả gợi ý theo phần đã nhập
    private List<String> partialMatch(List<String> source, String input) {
        if (input == null || input.isEmpty()) {
            return source;
        }
        List<String> matches = new ArrayList<>();
        String lower = input.toLowerCase();
        for (String s : source) {
            if (s.toLowerCase().startsWith(lower)) {
                matches.add(s);
            }
        }
        return matches;
    }
}
