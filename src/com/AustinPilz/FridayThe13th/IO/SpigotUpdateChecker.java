package com.AustinPilz.FridayThe13th.IO;

/**
 * Created by austinpilz on 8/4/16.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

public class SpigotUpdateChecker {

    private String latestVersion;
    private boolean updateNeeded;

    public boolean checkHigher(String currentVersion, String newVersion) {
       if (currentVersion.equals(newVersion))
       {
           return false;
       }
       else
       {
           return true;
       }
    }

    public void checkUpdate(String currentVersion) throws Exception {
        latestVersion = getVersion("98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4", 43321);

        if (this.checkHigher(currentVersion, latestVersion))
        {
            this.updateNeeded = true;
        }
        else
        {
            this.updateNeeded = false;
        }
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public Boolean isUpdateNeeded() { return updateNeeded; }

    private String getVersion(String key, int resourceId) {
        String version = null;
        try {
            HttpURLConnection con = (HttpURLConnection) new URL("http://www.spigotmc.org/api/general.php").openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream().write(("key=" + key + "&resource=" + resourceId).getBytes("UTF-8"));
            version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return version;
    }

    public String toReadable(String version) {
        String[] split = Pattern.compile(".", Pattern.LITERAL).split(version.replace("v", ""));
        version = "";
        for (String s : split)
            version += String.format("%4s", s);
        return version;
    }
}