package me.lucko.luckperms.commands.user.subcommands;

import me.lucko.luckperms.LuckPermsPlugin;
import me.lucko.luckperms.commands.Predicate;
import me.lucko.luckperms.commands.Sender;
import me.lucko.luckperms.commands.SubCommand;
import me.lucko.luckperms.commands.Util;
import me.lucko.luckperms.constants.Message;
import me.lucko.luckperms.constants.Permission;
import me.lucko.luckperms.users.User;
import me.lucko.luckperms.utils.Patterns;

import java.util.List;

public class UserHasPerm extends SubCommand<User> {
    public UserHasPerm() {
        super("haspermission", "Checks to see if a user has a certain permission node",
                "/%s user <user> haspermission <node> [server] [world]", Permission.USER_HASPERMISSION,
                Predicate.notinRange(1, 3));
    }

    @Override
    public void execute(LuckPermsPlugin plugin, Sender sender, User user, List<String> args, String label) {
        if (args.size() >= 2) {
            if (Patterns.NON_ALPHA_NUMERIC.matcher(args.get(1)).find()) {
                Message.SERVER_INVALID_ENTRY.send(sender);
                return;
            }

            if (args.size() == 2) {
                Util.sendBoolean(sender, args.get(0), user.hasPermission(args.get(0), true, args.get(1)));
            } else {
                Util.sendBoolean(sender, args.get(0), user.hasPermission(args.get(0), true, args.get(1), args.get(2)));
            }

        } else {
            Util.sendBoolean(sender, args.get(0), user.hasPermission(args.get(0), true, "global"));
        }
    }
}