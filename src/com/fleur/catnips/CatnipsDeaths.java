package com.fleur.catnips;

import org.bukkit.plugin.Plugin;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CatnipsDeaths {

    private Plugin plugin = Catnips.getPlugin(Catnips.class);   //Get the plugin from the main class
    private HashMap<UUID, Integer> deathCounts = new HashMap<>();   //Init our hashmap

    public CatnipsDeaths(){ //class init
        loadDeaths();    //load in the grace times from config.yml
    }

    public Integer getDeaths(UUID uuid) { //gets the time assigned to this uuid
        return deathCounts.get(uuid); //returns the time assigned to the uuid
    }

    public void setDeaths(UUID uuid, Integer deaths) { //set a time with a uuid
        deathCounts.put(uuid, deaths); //sets a time with the uuid
    }

    public void loadDeaths(){    //load the times from config.yml
        if(plugin.getConfig().isConfigurationSection("Users")) //if the Users section exists
        {
            for(String uuid : plugin.getConfig().getConfigurationSection("Users").getKeys(false)) { //for each uuid in users
                Integer configDeaths = plugin.getConfig().getInt("Users." + uuid + ".Deaths");   //get the grace time as a string
                setDeaths(UUID.fromString(uuid), configDeaths);
            }
        } else {    //if the users section does not exist
            plugin.getLogger().warning("DEATHS: No entries in config.yml"); //warn that there were no entries found
        }
    }

    public void saveDeaths(){    //save the times to the config.yml
        for(Map.Entry<UUID,Integer> entry : deathCounts.entrySet()) { //for each entry in the map
            UUID uuid = entry.getKey(); //get the key
            Integer deaths = entry.getValue(); //get the value
            plugin.getConfig().set("Users." + uuid + ".Deaths", deaths); //set it into the config
            plugin.saveConfig(); //save the config
        }
    }
}
