/* Local modification of Builders-Utilities. Copyright Arcaniax-Development and contributors.
 * Licensed GPL-3.0-or-later, as the original SecretBlockMenuProvider.java. */
package net.arcaniax.buildersutilities.menus;

import java.util.*;
import net.arcaniax.buildersutilities.BuildersUtilities;
import net.arcaniax.buildersutilities.menus.inv.ClickableItem;
import net.arcaniax.buildersutilities.menus.inv.content.InventoryContents;
import net.arcaniax.buildersutilities.menus.inv.content.InventoryProvider;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SecretBlockMenuProvider implements InventoryProvider {
    public record Entry(int slot,String spec,String title,String permission,String description) {}
    private static final String BASIC="builders.util.secretblocks", ADMIN="minecraft.command.give";
    public static List<Entry> catalog() {
        List<Entry> items=new ArrayList<>();
        String[][] blocks={
            {"barrier","屏障",BASIC,"隐形碰撞方块；手持屏障可查看位置"},
            {"structure_void","结构空位",BASIC,"结构保存时保留对应位置；不是屏障"},
            {"debug_stick","调试棒","minecraft.debugstick","调整方块朝向、连接等状态；仍遵循使用权限"},
            {"dragon_egg","龙蛋",BASIC,"特殊装饰方块"},
            {"spawner","刷怪笼",BASIC,"沿用原方块工具的获取权限"},
            {"structure_block","结构方块",BASIC,"使用仍受原版操作权限限制"},
            {"jigsaw","拼图方块",ADMIN,"用于结构拼接，操作仍受原版权限限制"},
            {"command_block","命令方块",ADMIN,"管理员工具"},
            {"repeating_command_block","循环命令方块",ADMIN,"管理员工具"},
            {"chain_command_block","连锁命令方块",ADMIN,"管理员工具"},
            {"end_portal_frame","末地传送门框架",BASIC,"可用于装饰；激活会形成传送门"},
            {"budding_amethyst","紫水晶母岩",BASIC,"可用于洞穴和晶体装饰"},
            {"reinforced_deepslate","强化深板岩",BASIC,"可用于大型建筑细节"},
            {"trial_spawner","试炼刷怪笼",ADMIN,"带刷怪行为的技术方块"},
            {"vault","宝库",ADMIN,"带交互和奖励行为的技术方块"},
            {"test_block","测试方块",ADMIN,"原版测试工具，需管理员权限"},
            {"test_instance_block","测试实例方块",ADMIN,"原版测试工具，需管理员权限"}
        };
        for(int i=0;i<blocks.length;i++) {String[] b=blocks[i];items.add(new Entry(i,"minecraft:"+b[0],b[1],b[2],b[3]));}
        items.add(new Entry(18,"minecraft:item_frame[minecraft:entity_data={id:\"minecraft:item_frame\",Invisible:1b}]","隐形物品展示框",BASIC,"放置后隐藏边框，保留展示物品"));
        items.add(new Entry(19,"minecraft:glow_item_frame[minecraft:entity_data={id:\"minecraft:glow_item_frame\",Invisible:1b}]","隐形荧光物品展示框",BASIC,"放置后隐藏边框，展示物品保持明亮"));
        for(int level=0;level<=15;level++)items.add(new Entry(27+level,"minecraft:light[minecraft:block_state={level:\""+level+"\"}]","光源方块 · 亮度 "+level,BASIC,"隐形光源，左键领取一个；数字为发光等级"));
        return List.copyOf(items);
    }
    private static ItemStack label(ItemStack base,String title,String... lore) {
        ItemStack icon=base.clone();var meta=icon.getItemMeta();meta.setDisplayName("§b"+title+"§r");
        meta.setLore(Arrays.stream(lore).map(line->"§7"+line+"§r").toList());icon.setItemMeta(meta);return icon;
    }
    @Override public void init(Player player,InventoryContents contents) {
        for(Entry entry:catalog()) {
            ItemStack payload=Bukkit.getItemFactory().createItemStack(entry.spec());
            boolean allowed=player.hasPermission(BASIC) && player.hasPermission(entry.permission());
            ItemStack icon=label(payload,entry.title(),entry.description(),allowed?"点击领取一个（需要创造模式）":"当前没有领取权限");
            contents.set(entry.slot()/9,entry.slot()%9,ClickableItem.of(icon,event->give(player,entry,payload)));
        }
        contents.set(5,0,ClickableItem.of(label(new ItemStack(Material.ARROW),"返回建筑师手册","回到工具箱首页"),event->navigate(player,"bmenu")));
        contents.set(5,4,ClickableItem.of(label(new ItemStack(Material.ENCHANTED_BOOK),"建筑服菜单","前往全服菜单"),event->navigate(player,"menu")));
        contents.set(5,8,ClickableItem.of(label(new ItemStack(Material.BARRIER),"关闭","收起工具箱"),event->navigate(player,null)));
    }
    public static void give(Player player,Entry entry,ItemStack payload) {
        if(!player.hasPermission(BASIC) || !player.hasPermission(entry.permission())) {player.sendMessage("§7[§bBuilders-Utilities§7]§r §e你暂时没有领取这个物品的权限。§r");return;}
        if(player.getGameMode()!=GameMode.CREATIVE) {player.sendMessage("§7[§bBuilders-Utilities§7]§r §e请先进入你有权限使用的创造模式，再领取建筑工具。§r");return;}
        if(!player.getInventory().addItem(payload.clone()).isEmpty())player.sendMessage("§7[§bBuilders-Utilities§7]§r §e背包满啦，请腾出一个位置后再领取。§r");
    }
    private static void navigate(Player player,String command) {
        Bukkit.getScheduler().runTask(BuildersUtilities.getInstance(),()->{player.closeInventory();if(command!=null)player.performCommand(command);});
    }
}
