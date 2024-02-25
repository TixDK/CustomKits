package customKits.customkits.Extra;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static customKits.customkits.CommandHolder.kitCommand.kitHolder;
import static customKits.customkits.CommandHolder.kitCommand.playerkitCooldown;

public class ResetCooldown extends Effect {


    private Expression<String> kitExpr;
    private Expression<Player> playerExpr;

    static {
        Skript.registerEffect(ResetCooldown.class, "reset cooldown of kit %string% for %player%");
    }


    @Override
    protected void execute(Event event) {
        String kit = kitExpr.getSingle(event);
        Player player = playerExpr.getSingle(event);
        if(!kitHolder.containsKey(kit)){
            Bukkit.getLogger().info("[CustomKits] Error: Kit database doesn't contain kit value - " + kit);
            return;
        }
        Map<UUID, Long> playerCooldown = playerkitCooldown.getOrDefault(kit, new HashMap<>());
        assert player != null;
        if(playerCooldown.containsKey(player.getUniqueId())){
            playerkitCooldown.remove(kit, playerCooldown);
        }
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean kleenean, SkriptParser.ParseResult parseResult){
        kitExpr = (Expression<String>) expressions[0];
        if(expressions.length > 1){
            playerExpr = (Expression<Player>) expressions[1];
        }
        return true;
    }

    @Override
    public String toString(org.bukkit.event.Event event, boolean b){
        return "reset cooldown of kit " + kitExpr.toString() + " for " + playerExpr.toString();
    }

}
