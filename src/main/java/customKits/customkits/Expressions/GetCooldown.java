package customKits.customkits.Expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import customKits.customkits.Extra.FormatTime;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import java.text.Format;

import static customKits.customkits.CommandHolder.kitCommand.kitCooldown;

public class GetCooldown extends SimpleExpression<String> {

    private Expression<String> kitExpr;

    public static void register() {
        Skript.registerExpression(GetCooldown.class, String.class, ExpressionType.SIMPLE, "cooldown of kit %string%");
    }


    @Override
    protected String[] get(Event event){
        String kit = kitExpr.getSingle(event);
        Long time = kitCooldown.get(kit);
        return new String[]{FormatTime.formatTime(time)};

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
        return "cooldown of kit " + kitExpr.toString();
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult){
        kitExpr = (Expression<String>) expressions[0];
        return true;
    }
}
