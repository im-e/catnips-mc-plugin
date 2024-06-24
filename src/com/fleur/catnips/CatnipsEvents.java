package com.fleur.catnips;

import org.bukkit.Bukkit;
import org.bukkit.BanList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;


import java.util.Date;
import java.util.UUID;

public class CatnipsEvents implements Listener {

    private CatnipsGrace grace = new CatnipsGrace();  //Init our Grace Class
    private CatnipsDeaths deaths = new CatnipsDeaths(); //Init our Deaths Class

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) { //When a player joins the server

        Player player = event.getPlayer();                   //Get player
        UUID uuid = player.getUniqueId();                    //Get uuid
        Date joinDate = new Date(System.currentTimeMillis()); //Get current date/time

        if(!player.hasPlayedBefore()) { //if user has not played before
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Welcome to Catnips Hardcore Server! :)"); //welcome player
            player.sendMessage(ChatColor.LIGHT_PURPLE + "You will be banned for 12hrs upon death."); //information
            grace.setTimeWithGrace(uuid, joinDate);       //set their grace time
            deaths.setDeaths(uuid, 0);              //set their deaths
            player.sendMessage(ChatColor.GREEN + "Your grace ends at: " + grace.getTime(uuid)); //

        } else { //player has played before
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Welcome back to the Catnips Hardcore Server! :)"); //welcome player
            player.sendMessage(ChatColor.RED + "You have died " + deaths.getDeaths(uuid) + " times.");
            //if player joined within their grace period
            if (grace.isDateInGrace(uuid, joinDate)) player.sendMessage(ChatColor.GREEN + "Your grace ends at: " + grace.getTime(uuid));
        }

        deaths.saveDeaths();
        grace.saveTimes();
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) { //when a player respawns

        Player player = event.getPlayer(); //get player
        UUID uuid = player.getUniqueId();
        Date respawnDate = new Date(System.currentTimeMillis()); //get current date/time

        player.sendMessage(ChatColor.RED + "You have died " + deaths.getDeaths(uuid) + " times.");
        if(grace.isDateInGrace(uuid,respawnDate)) { //If player respawned within their grace period
            player.sendMessage(ChatColor.GREEN + "Your grace ends at: " + grace.getTime(uuid));
        }
        else { //player died outside their grace period
            grace.setTimeWithGrace(uuid,respawnDate); //set them their new grace period
            player.sendMessage(ChatColor.GREEN + "Your new grace ends at: " + grace.getTime(uuid));
        }

        grace.saveTimes();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) { //when a player dies

        Player player = event.getEntity(); //get the player
        UUID uuid = player.getUniqueId();
        Date deathDate = new Date(System.currentTimeMillis()); //get the date of death
        deaths.setDeaths(uuid, deaths.getDeaths(uuid) + 1);

        if(!grace.isDateInGrace(uuid, deathDate)) { //if the player did not die in their grace period
            Date banDate = new Date(deathDate.getTime()+1000*60*60*12); //set the time for the ban - milli->second->minute->hour->12h ban
            player.kickPlayer(ChatColor.RED + "You died! You will be unbanned at: " + banDate.toString()); //kick the player from the server
            Bukkit.getBanList(BanList.Type.NAME).addBan(player.getDisplayName(), "You Died!", banDate, null); //update the ban list
        }

        deaths.saveDeaths();
        grace.saveTimes();
    }



}
