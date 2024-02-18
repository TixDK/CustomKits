package customKits.customkits.manager;



import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


public class UpdateManager {

    private static final String apiUrl = "https://api.github.com/repos/TixDK/CustomKits/releases/latest";
    public static String nyVersion;

    public static boolean isNewUpdateAvailable() {
        try {
            String currentVersion = Bukkit.getPluginManager().getPlugin("CustomKits").getDescription().getVersion();

            String token = "--------------------------";
            if (token == null || token.isEmpty()) {
                Bukkit.getLogger().warning("GitHub token is missing. Update check will not be performed.");
                return false;
            }

            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JsonObject json = new Gson().fromJson(response.toString(), JsonObject.class);
                String latestVersion = json.get("tag_name").getAsString();
                Bukkit.getLogger().info("Version: " + currentVersion + " Ny version " + latestVersion);
                nyVersion = latestVersion;
                return isNewerVersion(latestVersion, currentVersion);
            } else {
                Bukkit.getLogger().warning("Failed to retrieve latest version information from GitHub API. Response code: " + responseCode);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isNewerVersion(String version1, String version2) {
        boolean isNewer = version1.compareTo(version2) > 0;
        return isNewer;
    }



}
