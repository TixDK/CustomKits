package customKits.customkits.Extra;

import customKits.customkits.language.LanguageManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import java.io.File;

import static customKits.customkits.CustomKits.languageConfig;
import static customKits.customkits.Extra.stopDrag.plugin;


public class FormatTime implements Listener {

    public static String formatTime(long seconds){



        String Days = LanguageManager.langConfig("Settings.Time.Days");
        String Hours = LanguageManager.langConfig("Settings.Time.Hours");
        String Minutes = LanguageManager.langConfig("Settings.Time.Minutes");
        String Seconds = LanguageManager.langConfig("Settings.Time.Seconds");




        long days = seconds / (60 * 60 * 24);
        long hours = (seconds % (60 * 60 * 24)) / (60 * 60);
        long minutes = (seconds % (60 * 60)) / 60;
        long secondsRemain = seconds % 60;

        StringBuilder formattedTime = new StringBuilder();
        if(days > 0) {
            formattedTime.append(days).append(" " + Days + " ");
        }
        if(hours > 0){
            formattedTime.append(hours).append(" " + Hours + " ");
        }
        if(minutes > 0){
            formattedTime.append(minutes).append(" " + Minutes + " ");
        }
        if(secondsRemain > 0){
            formattedTime.append(secondsRemain).append(" " + Seconds + " ");
        }
        return formattedTime.toString();
    }


    public static int parseTime(String timeString) {
        int totalSeconds = 0;
        boolean containsUnit = false;



        String[] parts = timeString.split("[dhmst]");

        for (String part : parts) {
            if (part.length() > 0) {
                int value = Integer.parseInt(part);
                if (timeString.contains("d")) {
                    totalSeconds += value * 24 * 60 * 60;
                    containsUnit = true;
                } else if (timeString.contains("h") || timeString.contains("t")) {
                    totalSeconds += value * 60 * 60;
                    containsUnit = true;
                } else if (timeString.contains("m")) {
                    totalSeconds += value * 60;
                    containsUnit = true;
                } else if (timeString.contains("s")) {
                    totalSeconds += value;
                    containsUnit = true;
                }
            }
        }

        if (!containsUnit) {
            totalSeconds = Integer.parseInt(timeString);
        }
        return totalSeconds;
    }
}
