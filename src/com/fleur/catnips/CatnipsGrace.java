package com.fleur.catnips;

import org.bukkit.plugin.Plugin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CatnipsGrace {

    private Plugin plugin = Catnips.getPlugin(Catnips.class);   //Get the plugin from the main class
    private HashMap<UUID, Date> graceTimes = new HashMap<>();   //Init our hashmap

    public CatnipsGrace(){ //class init
        loadTimes();    //load in the grace times from config.yml
    }

    public Date getTime(UUID uuid) { //gets the time assigned to this uuid
        return graceTimes.get(uuid); //returns the time assigned to the uuid
    }

    public void setTime(UUID uuid, Date date) { //set a time with a uuid
        graceTimes.put(uuid, date); //sets a time with the uuid
    }

    public void setTimeWithGrace(UUID uuid, Date date) { //sets the current time as a grace time with uuid
        Date grace = new Date(date.getTime()+1000*60*60); //milli->second->minute->hour (1 hour grace) convert current time to grace time
        graceTimes.put(uuid, grace); //set the time with the uuid
    }

    public Boolean isDateInGrace(UUID uuid, Date date) { //check if date is inside uuid's grace time
        Date grace = getTime(uuid); //get the grace time of player
        int result =  date.compareTo(grace); //check result of two dates
        if (result <= 0) return true;   //if date is inside grace period
        else return false;  //if date is not in grace period
    }

    public void loadTimes(){    //load the times from config.yml
        if(plugin.getConfig().isConfigurationSection("Users")) //if the Users section exists
        {
            SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy"); //set formatting
            for(String uuid : plugin.getConfig().getConfigurationSection("Users").getKeys(false)) { //for each uuid in users
                String configDate = plugin.getConfig().getString("Users." + uuid + ".Grace");   //get the grace time as a string
                try{ //try to convert the string into a date
                    Date date = formatter.parse(configDate); //parse the date from string
                    setTime(UUID.fromString(uuid), date); //set the players grace time
                } catch (ParseException e){ //catch
                    e.printStackTrace(); //trace
                }
            }
        } else {    //if the users section does not exist
            plugin.getLogger().warning("GRACE: No entries in config.yml"); //warn that there were no entries found
        }
    }

    public void saveTimes(){    //save the times to the config.yml
        for(Map.Entry<UUID,Date> entry : graceTimes.entrySet()) { //for each entry in the map
            UUID uuid = entry.getKey(); //get the key
            Date date = entry.getValue(); //get the value
            plugin.getConfig().set("Users." + uuid + ".Grace", date.toString()); //set it into the config
            plugin.saveConfig(); //save the config
        }
    }

}
