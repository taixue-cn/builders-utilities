/*
 * Builder's Utilities is a collection of a lot of tiny features that help with building.
 * Copyright (C) Arcaniax-Development
 * Copyright (C) Arcaniax team and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package net.arcaniax.buildersutilities.menus;

import net.arcaniax.buildersutilities.NoClipManager;
import net.arcaniax.buildersutilities.listeners.BlockBreakListener;
import net.arcaniax.buildersutilities.listeners.IronTrapdoorListener;
import net.arcaniax.buildersutilities.listeners.PlayerMoveListener;
import net.arcaniax.buildersutilities.listeners.TerracottaInteractListener;
import net.arcaniax.buildersutilities.menus.inv.ClickableItem;
import net.arcaniax.buildersutilities.menus.inv.content.InventoryContents;
import net.arcaniax.buildersutilities.menus.inv.content.InventoryProvider;
import net.arcaniax.buildersutilities.utils.Items;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class UtilitiesMenuProvider implements InventoryProvider {

    private static final ItemStack ENABLED = Items.create(Material.GREEN_STAINED_GLASS_PANE, "&c", "");
    private static final ItemStack DISABLED = Items.create(Material.RED_STAINED_GLASS_PANE, "&c", "");
    private static final ItemStack NO_PERMISSION = Items.create(Material.ORANGE_STAINED_GLASS_PANE, "&c", "");

    private static final String ENABLED_LORE = "&a&l已开启__&7__&e点击切换";
    private static final String DISABLED_LORE = "&c&l已关闭__&7__&e点击切换";

    private static final String IRON_TRAPDOOR_LORE = "__&c__&8&o像木活板门一样，直接点击开关";
    private static final String SLAB_BREAKING_LORE = "__&c__&8&o手持台阶时，可以拆分双层台阶";
    private static final String GLAZED_ROTATING_LORE = "__&c__&8&o空手潜行并右键，调整彩釉陶方向";

    private static final String NIGHT_VISION_LORE = "__&c__&8&o夜间也能清楚查看建筑细节";
    private static final String NO_CLIP_LORE = "__&c__&8&o使用已有穿墙工具";
    private static final String ADVANCED_FLY_LORE = "__&c__&8&o停止飞行时立即刹住，便于精细施工";

    @Override
    public void init(Player player, InventoryContents contents) {
        setIronTrapdoorItem(player, contents);
        setSlabItem(player, contents);
        setTerracottaItem(player, contents);
        setNightVisionItem(player, contents);
        setNoClipItem(player, contents);
        setFlyItem(player, contents);
        contents.set(1,0,ClickableItem.of(Items.create(Material.ARROW,"&b建筑师手册","&7返回工具箱首页"),event ->
            org.bukkit.Bukkit.getScheduler().runTask(net.arcaniax.buildersutilities.BuildersUtilities.getInstance(),()->{player.closeInventory();player.performCommand("bmenu");})));
        contents.set(1,8,ClickableItem.of(Items.create(Material.BARRIER,"&c关闭","&7收起工具箱"),event ->
            org.bukkit.Bukkit.getScheduler().runTask(net.arcaniax.buildersutilities.BuildersUtilities.getInstance(),()->player.closeInventory())));

    }

    private void setIronTrapdoorItem(Player player, InventoryContents contents) {
        if (!player.hasPermission("builders.util.trapdoor")) {
            setNoPermission(1, contents);
            contents.set(1, 1, ClickableItem.empty(
                    Items.create(Material.IRON_TRAPDOOR, "&6铁活板门交互", "&7&l暂时没有权限")));
            return;
        }

        if (!IronTrapdoorListener.ironTrapdoorIds.contains(player.getUniqueId())) {
            setEnabledGlassPanes(1, true, contents);
            contents.set(1, 1, ClickableItem.of(
                    Items.create(Material.IRON_TRAPDOOR,
                            "&6铁活板门交互", ENABLED_LORE + IRON_TRAPDOOR_LORE
                    ),
                    inventoryClickEvent -> {
                        IronTrapdoorListener.ironTrapdoorIds.add(player.getUniqueId());
                        setIronTrapdoorItem(player, contents);
                    }
            ));
        } else {
            setEnabledGlassPanes(1, false, contents);
            contents.set(1, 1, ClickableItem.of(
                    Items.create(Material.IRON_TRAPDOOR,
                            "&6铁活板门交互", DISABLED_LORE + IRON_TRAPDOOR_LORE
                    ),
                    inventoryClickEvent -> {
                        IronTrapdoorListener.ironTrapdoorIds.remove(player.getUniqueId());
                        setIronTrapdoorItem(player, contents);
                    }
            ));
        }
    }

    private void setSlabItem(Player player, InventoryContents contents) {
        if (!player.hasPermission("builders.util.slabs")) {
            setNoPermission(2, contents);
            contents.set(1, 2, ClickableItem.empty(
                    Items.create(Material.STONE_SLAB, "&6双层台阶拆分", "&7&l暂时没有权限")));
            return;
        }

        if (!BlockBreakListener.slabIds.contains(player.getUniqueId())) {
            setEnabledGlassPanes(2, true, contents);
            contents.set(1, 2, ClickableItem.of(
                    Items.create(Material.STONE_SLAB,
                            "&6双层台阶拆分", ENABLED_LORE + SLAB_BREAKING_LORE
                    ),
                    inventoryClickEvent -> {
                        BlockBreakListener.slabIds.add(player.getUniqueId());
                        setSlabItem(player, contents);
                    }
            ));
        } else {
            setEnabledGlassPanes(2, false, contents);
            contents.set(1, 2, ClickableItem.of(
                    Items.create(Material.STONE_SLAB,
                            "&6双层台阶拆分", DISABLED_LORE + SLAB_BREAKING_LORE
                    ),
                    inventoryClickEvent -> {
                        BlockBreakListener.slabIds.remove(player.getUniqueId());
                        setSlabItem(player, contents);
                    }
            ));
        }
    }

    private void setTerracottaItem(Player player, InventoryContents contents) {
        if (!player.hasPermission("builders.util.terracotta")) {
            setNoPermission(3, contents);
            contents.set(1, 3, ClickableItem.empty(
                    Items.create(Material.ORANGE_GLAZED_TERRACOTTA, "&6彩釉陶旋转", "&7&l暂时没有权限")));
            return;
        }

        if (!TerracottaInteractListener.terracottaIds.contains(player.getUniqueId())) {
            setEnabledGlassPanes(3, true, contents);
            contents.set(1, 3, ClickableItem.of(
                    Items.create(Material.ORANGE_GLAZED_TERRACOTTA,
                            "&6彩釉陶旋转", ENABLED_LORE + GLAZED_ROTATING_LORE
                    ),
                    inventoryClickEvent -> {
                        TerracottaInteractListener.terracottaIds.add(player.getUniqueId());
                        setTerracottaItem(player, contents);
                    }
            ));
        } else {
            setEnabledGlassPanes(3, false, contents);
            contents.set(1, 3, ClickableItem.of(
                    Items.create(Material.ORANGE_GLAZED_TERRACOTTA,
                            "&6彩釉陶旋转", DISABLED_LORE + GLAZED_ROTATING_LORE
                    ),
                    inventoryClickEvent -> {
                        TerracottaInteractListener.terracottaIds.remove(player.getUniqueId());
                        setTerracottaItem(player, contents);
                    }
            ));
        }

    }

    private void setNightVisionItem(Player player, InventoryContents contents) {
        if (!player.hasPermission("builders.util.nightvision")) {
            setNoPermission(5, contents);
            contents.set(1, 5, ClickableItem.empty(
                    Items.create(Material.ENDER_EYE, "&6夜视", "&7&l暂时没有权限")));
            return;
        }

        if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            setEnabledGlassPanes(5, true, contents);
            contents.set(1, 5, ClickableItem.of(
                    Items.create(Material.ENDER_EYE,
                            "&6夜视", ENABLED_LORE + NIGHT_VISION_LORE
                    ),
                    inventoryClickEvent -> {
                        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                        setNightVisionItem(player, contents);
                    }
            ));
        } else {
            setEnabledGlassPanes(5, false, contents);
            contents.set(1, 5, ClickableItem.of(
                    Items.create(Material.ENDER_EYE,
                            "&6夜视", DISABLED_LORE + NIGHT_VISION_LORE
                    ),
                    inventoryClickEvent -> {
                        player.addPotionEffect(new PotionEffect(
                                PotionEffectType.NIGHT_VISION,
                                Integer.MAX_VALUE,
                                0,
                                true,
                                false
                        ));
                        setNightVisionItem(player, contents);
                    }
            ));
        }
    }

    private void setNoClipItem(Player player, InventoryContents contents) {
        boolean allowed=player.hasPermission("builders.util.noclip") && player.hasPermission("noclip.use");
        contents.set(0,6,ClickableItem.empty(Items.create(Material.GRAY_STAINED_GLASS_PANE,"&7穿墙由 NoClip 管理","")));
        contents.set(2,6,ClickableItem.empty(Items.create(Material.GRAY_STAINED_GLASS_PANE,"&7点击后查看聊天提示","")));
        contents.set(1,6,allowed ? ClickableItem.of(Items.create(Material.COMPASS,"&6穿墙开关","&7使用现有 NoClip，避免两个开关冲突__&7仅允许从有权限的创造模式开启__&e点击切换，结果会显示在聊天栏"),event ->
            org.bukkit.Bukkit.getScheduler().runTask(net.arcaniax.buildersutilities.BuildersUtilities.getInstance(),()->{
                player.closeInventory();new net.arcaniax.buildersutilities.commands.NoClipCommand().execute(player,new String[0]);
            })) : ClickableItem.empty(Items.create(Material.COMPASS,"&6穿墙开关","&7暂时没有使用权限")));
    }

    private void setFlyItem(Player player, InventoryContents contents) {
        if (!player.hasPermission("builders.util.advancedfly")) {
            setNoPermission(7, contents);
            contents.set(1, 7, ClickableItem.empty(
                    Items.create(Material.FEATHER, "&6飞行急停", "&7&l暂时没有权限")));
            return;
        }

        if (PlayerMoveListener.enabledPlayers.contains(player.getUniqueId())) {
            setEnabledGlassPanes(7, true, contents);
            contents.set(1, 7, ClickableItem.of(
                    Items.create(Material.FEATHER,
                            "&6飞行急停", ENABLED_LORE + ADVANCED_FLY_LORE
                    ),
                    inventoryClickEvent -> {
                        PlayerMoveListener.enabledPlayers.remove(player.getUniqueId());
                        setFlyItem(player, contents);
                    }
            ));
        } else {
            setEnabledGlassPanes(7, false, contents);
            contents.set(1, 7, ClickableItem.of(
                    Items.create(Material.FEATHER,
                            "&6飞行急停", DISABLED_LORE + ADVANCED_FLY_LORE
                    ),
                    inventoryClickEvent -> {
                        PlayerMoveListener.enabledPlayers.add(player.getUniqueId());
                        setFlyItem(player, contents);
                    }
            ));
        }
    }

    /**
     * Sets the glass panes above and below the center row to
     * show if a feature is enabled or disabled.
     * https://i.imgur.com/ETI22Py.png
     *
     * @param col     the column to set the glass panes in
     * @param enabled true if green panes, false if red panes
     */
    private void setEnabledGlassPanes(int col, boolean enabled, InventoryContents contents) {
        contents.set(0, col, ClickableItem.empty(enabled ? ENABLED : DISABLED));
        contents.set(2, col, ClickableItem.empty(enabled ? ENABLED : DISABLED));
    }

    private void setNoPermission(int col, InventoryContents contents) {
        contents.set(0, col, ClickableItem.empty(NO_PERMISSION));
        contents.set(2, col, ClickableItem.empty(NO_PERMISSION));
    }

}
