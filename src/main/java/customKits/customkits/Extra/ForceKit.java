package customKits.customkits.Extra;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class ForceKit extends Effect {


    private Expression<Player> playerExpr;
    private Expression<String> kitExpr;



    static {
        Skript.registerEffect(ForceKit.class, "force kit give %player% with name %string%");
    }


    @Override
    protected void execute(Event event) {
        Player player = playerExpr.getSingle(event);
        String kit = kitExpr.getSingle(event);

        giveKit.directGiveKit(kit, player);
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean kleenean, SkriptParser.ParseResult parseResult){
        playerExpr = (Expression<Player>) expressions[0];
        if(expressions.length > 1){
            kitExpr = (Expression<String>) expressions[1];
        }
        return true;
    }

    public String toString(org.bukkit.event.Event event, boolean b){
        return "force kit give " + playerExpr.toString() + " with name " + kitExpr.toString();
    }
}
