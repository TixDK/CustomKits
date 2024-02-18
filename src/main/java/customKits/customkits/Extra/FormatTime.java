package customKits.customkits.Extra;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Map;

import static customKits.customkits.CommandHolder.kitCommand.kitCooldown;
import static customKits.customkits.CommandHolder.kitCommand.playerkitCooldown;

public class FormatTime implements Listener {

    public static String formatTime(long seconds){
        long days = seconds / (60 * 60 * 24);
        long hours = (seconds % (60 * 60 * 24)) / (60 * 60);
        long minutes = (seconds % (60 * 60)) / 60;
        long secondsRemain = seconds % 60;

        StringBuilder formattedTime = new StringBuilder();
        if(days > 0) {
            formattedTime.append(days).append(" dage ");
        }
        if(hours > 0){
            formattedTime.append(hours).append(" timer ");
        }
        if(minutes > 0){
            formattedTime.append(minutes).append(" minutter ");
        }
        if(secondsRemain > 0){
            formattedTime.append(secondsRemain).append(" sekunder ");
        }
        return formattedTime.toString();
    }
}
