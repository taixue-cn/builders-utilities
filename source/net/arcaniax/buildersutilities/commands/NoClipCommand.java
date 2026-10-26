/* Local modification of Builders-Utilities. Copyright Arcaniax-Development and contributors.
 * Licensed GPL-3.0-or-later, as the original NoClipCommand.java. */
package net.arcaniax.buildersutilities.commands;
import net.arcaniax.buildersutilities.commands.system.ICommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
public class NoClipCommand implements ICommand {
    @Override public void execute(Player player,String[] args) {
        if(!player.hasPermission("builders.util.noclip") || !player.hasPermission("noclip.use")) {
            player.sendMessage("§7[§bBuilders-Utilities§7]§r §e你暂时没有使用穿墙的权限。§r");return;
        }
        if(!Bukkit.getPluginManager().isPluginEnabled("NoClip")) {
            player.sendMessage("§7[§bBuilders-Utilities§7]§r §e穿墙工具还没准备好，请稍后再试。§r");return;
        }
        player.performCommand("noclip:noclip");
    }
}
