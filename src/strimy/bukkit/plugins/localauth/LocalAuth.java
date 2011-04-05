package strimy.bukkit.plugins.localauth;

import java.util.HashSet;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LocalAuth extends JavaPlugin 
{
	Logger log;
	HashSet<Player> unloggedPlayers;
	public PlayerManagement playerManager;

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnable() 
	{
		log = getServer().getLogger();
		Print("["+getDescription().getName()+" "+ getDescription().getVersion()+"] Plugin loaded !");
		unloggedPlayers = new HashSet<Player>();
		Player[] currentPlayers = getServer().getOnlinePlayers();
		for(Player player : currentPlayers)
		{
			unloggedPlayers.add(player);
			player.sendMessage(ChatColor.AQUA + "Sorry, the plugin has been reloaded.");
			player.sendMessage(ChatColor.GOLD + "You must loggin again (/auth password your_password).");
		}
		
		LAEntityListener entityListener = new LAEntityListener(this);
		LAPlayerListener playerListener = new LAPlayerListener(this);
		LABlockListener blockListener = new LABlockListener(this);
		
		playerManager = new PlayerManagement(this);
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener , Priority.Normal, this);
		
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
		
		pm.registerEvent(Event.Type.BLOCK_DAMAGE, blockListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Priority.Normal, this);
		
		getCommand("user").setExecutor(playerManager);
	}
	
	public void Print(String message)
	{
		log.info(message);
	}

}
