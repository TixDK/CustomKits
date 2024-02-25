package customKits.customkits.Expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import customKits.customkits.Extra.FormatTime;
import customKits.customkits.Extra.giveKit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;


public class GetPlayerCooldown extends SimpleExpression<String> {


    private Expression<String> kitExpr;
    private Expression<Player> playerExpr;

    public static void register(){
        Skript.registerExpression(GetPlayerCooldown.class, String.class, ExpressionType.SIMPLE, "cooldown of kit %string% for %player%");
    }

    protected String[] get(Event event){
        String kit = kitExpr.getSingle(event);
        Player player = playerExpr.getSingle(event);
        long time = giveKit.ReturnTime(player, kit);
        if(time > 0){
            return new String[]{FormatTime.formatTime(time)};
        } else {
            return new String[]{};
        }



    }

    @Override
    public boolean isSingle(){
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(Event event, boolean b){
        return "cooldown of kit " + kitExpr.toString() + " for " + playerExpr.toString();
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult){
        kitExpr = (Expression<String>) expressions[0];
        if(expressions.length > 1){
            playerExpr = (Expression<Player>) expressions[1];
        }
        return true;
    }
}
