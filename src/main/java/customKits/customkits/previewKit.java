package customKits.customkits;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

import static customKits.customkits.kitCommand.kitHolder;
import static customKits.customkits.kitCommand.kitMenuHolder;
import static customKits.customkits.stopDrag.plugin;

public class previewKit extends Effect {

    static Expression<Player> playerExpr;
    static Expression<String> kitExpr;



    static {
        Skript.registerEffect(previewKit.class, "create preview for %player% of kit %string%");
    }

    static Player player;
    static String kit;
    static Inventory kitInv;
    @Override
    protected void execute(Event event) {
        player = playerExpr.getSingle(event);
        kit = kitExpr.getSingle(event);
        previewKitMenu(kit, player);
    }

    public static void previewKitMenu(String kit, Player player){
        int rows = kitMenuHolder.get(kit);
        String colorCode = plugin.getConfig().getString("Settings.Basics.Preview-ColorCode");
        int num = 0;
        kitInv = Bukkit.createInventory(null, rows * 9, ChatColor.translateAlternateColorCodes('&', "&fPreview " + colorCode + kit));
        if (kitHolder.containsKey(kit)) {
            ArrayList<ItemStack> kitItems = kitHolder.get(kit);
            for (ItemStack item : kitItems) {
                if (item != null) {
                    kitInv.setItem(num, item);
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
