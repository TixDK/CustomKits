package customKits.customkits.Extra;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;

import static customKits.customkits.CommandHolder.kitCommand.kitHolder;

public class giveKit extends Effect {

    private Expression<Player> playerExpr;
    private Expression<String> kitExpr;


    static {
        Skript.registerEffect(giveKit.class, "kit give %player% with name %string%");
    }

    @Override
    protected void execute(Event event) {
        Player player = playerExpr.getSingle(event);
        String kit = kitExpr.getSingle(event);

        if (kitHolder.containsKey(kit)) {
            ArrayList<ItemStack> kitItems = kitHolder.get(kit);
            PlayerInventory playerInventory = player.getInventory();
            for (ItemStack item : kitItems) {
                if (item != null) {
                    playerInventory.addItem(item);
                }
            }
        }
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean kleenean, SkriptParser.ParseResult parseResult){
        playerExpr = (Expression<Player>) expressions[0];
        if(expressions.length > 1){
            kitExpr = (Expression<String>) expressions[1];
        }
        return true;
    }

    @Override
    public String toString(org.bukkit.event.Event event, boolean b){
        return "kit give " + playerExpr.toString() + " with name " + kitExpr.toString();
    }
}
