package customKits.customkits.Extra;

import org.bukkit.event.Listener;


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


    public static int parseTime(String timeString) {
        int totalSeconds = 0;
        boolean containsUnit = false;

        String[] parts = timeString.split("[dhms]");

        for (String part : parts) {
            if (part.length() > 0) {
                int value = Integer.parseInt(part);
                if (timeString.contains("d")) {
                    totalSeconds += value * 24 * 60 * 60;
                    containsUnit = true;
                } else if (timeString.contains("h")) {
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
