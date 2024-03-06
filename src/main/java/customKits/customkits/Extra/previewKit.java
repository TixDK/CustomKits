package customKits.customkits.Extra;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import customKits.customkits.language.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import static customKits.customkits.CommandHolder.kitCommand.kitHolder;
import static customKits.customkits.CommandHolder.kitCommand.kitMenuHolder;
import static customKits.customkits.Extra.stopDrag.plugin;

public class previewKit extends Effect {

    static Expression<Player> playerExpr;
    static Expression<String> kitExpr;



    static {
        Skript.registerEffect(previewKit.class, "create preview for %player% of kit %string%");
    }

    static Player player;
    static String kit;
    public static Inventory kitInv;
    @Override
    protected void execute(Event event) {
        player = playerExpr.getSingle(event);
        kit = kitExpr.getSingle(event);
        previewKitMenu(kit, player);
    }

    public static void previewKitMenu(String kit, Player player){
        if(!kitHolder.containsKey(kit)){
            return;
        }
        int rows = kitMenuHolder.get(kit);

        String colorCode = LanguageManager.langConfig("Settings.Basics.Preview-ColorCode");
        int num = 0;
        kitInv = Bukkit.createInventory(null, rows * 9, ChatColor.translateAlternateColorCodes('&', "&fPreview " + colorCode + kit));
        if (kitHolder.containsKey(kit)) {
            ArrayList<ItemStack> kitItems = kitHolder.get(kit);
            ItemMeta itemMeta = null;
            for (ItemStack item : kitItems) {
                ItemStack PlaceItem;
                int ItemAmount;
                if (item != null ) {
                    if(item.hasItemMeta()){
                        itemMeta = item.getItemMeta();
                        if (itemMeta != null && itemMeta.hasEnchants()) {
                            Map<Enchantment, Integer> enchantments = itemMeta.getEnchants();
                            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                                Enchantment itemEnchant = entry.getKey();
                                int itemEnchantLevel = entry.getValue();
                                itemMeta.addEnchant(itemEnchant, itemEnchantLevel, true);
                            }
                        }
                        PlaceItem = new ItemStack(item.getType(), item.getAmount());
                        PlaceItem.setItemMeta(itemMeta);
                    } else {
                        ItemAmount = item.getAmount();
                        if (ItemAmount > 1) {
                            PlaceItem = new ItemStack(item.getType(), ItemAmount);
                        } else {
                            PlaceItem = new ItemStack(item.getType(), 1);
                        }
                    }
                    kitInv.setItem(num, PlaceItem);
                    num = num + 1;
                }
            }
            player.openInventory(kitInv);
        }
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        playerExpr = (Expression<Player>) expressions[0];
        if (expressions.length > 1) {
            kitExpr = (Expression<String>) expressions[1];
        }
        return true;
    }

    @Override
    public String toString(org.bukkit.event.Event event, boolean b) {
        return "create preview for " + playerExpr.toString() + " of kit " + kitExpr.toString();
    }


}
