/* Local modification of Builders-Utilities. Copyright Arcaniax-Development and contributors.
 * Licensed GPL-3.0-or-later, as the original UtilsCommand.java. */
package net.arcaniax.buildersutilities.commands;
import net.arcaniax.buildersutilities.commands.system.ICommand;
import net.arcaniax.buildersutilities.menus.Menus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
public class UtilsCommand implements ICommand {
    @Override public void execute(Player player,String[] args) {
        if(!player.hasPermission("builders.util.gui")) {denied(player);return;}
        if(args.length==0) {Menus.TOGGLE_MENU.open(player);return;}
        String target=args[0].toLowerCase(java.util.Locale.ROOT);
        if(!target.equals("gobrush") && !target.equals("gopaint")) {player.sendMessage("§7[§bBuilders-Utilities§7]§r §e没有找到这个工具入口。§r");return;}
        if(!player.hasPermission(target+".use")) {denied(player);return;}
        if(!Bukkit.getPluginManager().isPluginEnabled(target.equals("gobrush")?"goBrush":"goPaint")) {
            player.sendMessage("§7[§bBuilders-Utilities§7]§r §e这个笔刷暂时还没准备好，请稍后再试。§r");return;
        }
        // Open exactly the existing held-tool GUI; editing still runs through the original plugin.
        if(target.equals("gobrush"))player.openInventory(com.arcaniax.gobrush.util.GuiGenerator.generateMainMenu(com.arcaniax.gobrush.Session.getBrushPlayer(player.getUniqueId())));
        else player.openInventory(net.arcaniax.gopaint.GoPaintPlugin.getBrushManager().getPlayerBrush(player).getInventory());
    }
    private static void denied(Player player) {player.sendMessage("§7[§bBuilders-Utilities§7]§r §e你暂时没有使用这个工具的权限。§r");}
}
