package customKits.customkits.Extra;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static customKits.customkits.Extra.previewKit.kitInv;


public class backSlot extends Effect {

    private Expression<Integer> slotExpr;
    private Expression<ItemStack> itemExpr;
    private Expression<String> nameExpr;


    static {
        Skript.registerEffect(backSlot.class,"set return slot %integer% of preview to %itemstack% named %string%");
    }

    @Override
    protected void execute(Event event) {
        int slot = slotExpr.getSingle(event);
        ItemStack item = itemExpr.getSingle(event);
        String name = nameExpr.getSingle(event);

        if (item != null) {
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta == null) {
                itemMeta = Bukkit.getItemFactory().getItemMeta(item.getType());
            }
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            item.setItemMeta(itemMeta);
            kitInv.setItem(slot, item);
        }
    }


    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        slotExpr = (Expression<Integer>) expressions[0];
        if(expressions.length > 1){
            itemExpr = (Expression<ItemStack>) expressions[1];
            nameExpr = (Expression<String>) expressions[2];
        }
        return true;
    }

    @Override
    public String toString(org.bukkit.event.Event event, boolean b) {
        return "set return slot " + slotExpr.toString() + " of preview to " + itemExpr.toString() + " named " + nameExpr.toString();
    }


}
