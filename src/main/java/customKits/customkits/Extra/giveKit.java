package customKits.customkits.Extra;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import customKits.customkits.language.LanguageManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static customKits.customkits.CommandHolder.kitCommand.*;
import static customKits.customkits.Extra.stopDrag.plugin;

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
        giveKit(kit, player);
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


    public static void giveKit(String kit, Player player){
        UUID playerID = player.getUniqueId();
        Map<UUID, Long> playerCooldown = playerkitCooldown.getOrDefault(kit, new HashMap<>());


        String prefix = LanguageManager.langConfig("Settings.Basics.Prefix");
        String RetakeKit = LanguageManager.langConfig("Settings.Messages.Retake-Kit");


        if (playerCooldown.containsKey(playerID)) {
            if (ReturnTime(player, kit) > 0) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + RetakeKit +  FormatTime.formatTime(ReturnTime(player, kit))));
            } else {
                if (kitHolder.containsKey(kit)) {
                    playerCooldown.put(playerID, System.currentTimeMillis());
                    ArrayList<ItemStack> kitItems = kitHolder.get(kit);
                    PlayerInventory playerInventory = player.getInventory();
                    for (ItemStack item : kitItems) {
                        if (item != null) {
                            playerInventory.addItem(item);
                        }
                    }
                }
            }
        } else {
            if (kitHolder.containsKey(kit)) {
                playerCooldown.put(playerID, System.currentTimeMillis());
                playerkitCooldown.put(kit, playerCooldown);
                ArrayList<ItemStack> kitItems = kitHolder.get(kit);
                PlayerInventory playerInventory = player.getInventory();
                for (ItemStack item : kitItems) {
                    if (item != null) {
                        playerInventory.addItem(item);
                    }
                }
            }
        }

    }

    public static void directGiveKit(String kit, Player player){
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

    public static long ReturnTime(Player player, String kit){
        UUID playerID = player.getUniqueId();
        Map<UUID, Long> playerCooldown = playerkitCooldown.getOrDefault(kit, new HashMap<>());

        long cooldownEndTime = playerCooldown.get(playerID);
        long currentTimeMillis = System.currentTimeMillis();
        long cooldownSeconds = cooldownEndTime / 1000;
        long currentTimeSeconds = currentTimeMillis / 1000;

        long cooldown = kitCooldown.get(kit);
        long timeLeft = (cooldownSeconds + cooldown) - currentTimeSeconds;

        return timeLeft;
    }


}
