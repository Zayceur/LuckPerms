package me.lucko.luckperms.commands.user.subcommands;

import me.lucko.luckperms.LuckPermsPlugin;
import me.lucko.luckperms.commands.Predicate;
import me.lucko.luckperms.commands.Sender;
import me.lucko.luckperms.commands.SubCommand;
import me.lucko.luckperms.constants.Message;
import me.lucko.luckperms.constants.Permission;
import me.lucko.luckperms.exceptions.ObjectAlreadyHasException;
import me.lucko.luckperms.users.User;
import me.lucko.luckperms.utils.Patterns;

import java.util.List;

public class UserSetPermission extends SubCommand<User> {
    public UserSetPermission() {
        super("set", "Sets a permission for a user",
                "/%s user <user> set <node> <true|false> [server] [world]", Permission.USER_SETPERMISSION,
                Predicate.notinRange(2, 4));
    }

    @Override
    public void execute(LuckPermsPlugin plugin, Sender sender, User user, List<String> args, String label) {
        String node = args.get(0);
        String bool = args.get(1).toLowerCase();

        if (node.contains("/") || node.contains("$")) {
            sendUsage(sender, label);
            return;
        }

        if (Patterns.GROUP_MATCH.matcher(node).matches()) {
            Message.USER_USE_ADDGROUP.send(sender);
            return;
        }

        if (!bool.equalsIgnoreCase("true") && !bool.equalsIgnoreCase("false")) {
            sendUsage(sender, label);
            return;
        }

        boolean b = Boolean.parseBoolean(bool);

        try {
            if (args.size() >= 3) {
                final String server = args.get(2).toLowerCase();
                if (Patterns.NON_ALPHA_NUMERIC.matcher(server).find()) {
                    Message.SERVER_INVALID_ENTRY.send(sender);
                    return;
                }

                if (args.size() == 3) {
                    user.setPermission(node, b, server);
                    Message.SETPERMISSION_SERVER_SUCCESS.send(sender, node, bool, user.getName(), server);
                } else {
                    final String world = args.get(3).toLowerCase();
                    user.setPermission(node, b, server, world);
                    Message.SETPERMISSION_SERVER_WORLD_SUCCESS.send(sender, node, bool, user.getName(), server, world);
                }

            } else {
                user.setPermission(node, b);
                Message.SETPERMISSION_SUCCESS.send(sender, node, bool, user.getName());
            }

            saveUser(user, sender, plugin);
        } catch (ObjectAlreadyHasException e) {
            Message.ALREADY_HASPERMISSION.send(sender, user.getName());
        }
    }

    @Override
    public List<String> onTabComplete(Sender sender, List<String> args, LuckPermsPlugin plugin) {
        return getBoolTabComplete(args);
    }
}