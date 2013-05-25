package strimy.bukkit.plugins.localauth;

import java.util.Date;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LAPlayerListener implements Listener 
{
	HashMap<String, Date> lastNotif = new HashMap<String, Date>();
	HashMap<Player, Location> startPosition = new HashMap<Player, Location>();
	LocalAuth plugin;
	public LAPlayerListener(LocalAuth plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event) 
	{
		Player player = event.getPlayer();
		Location resetLocation = null;
		resetLocation = player.getLocation();
		String playerName = player.getDisplayName();
		plugin.unloggedPlayers.add(player);
		
		if(!player.hasPlayedBefore())
		{
			plugin.log.info("Reset");
			resetLocation = player.getWorld().getSpawnLocation();
		}
		if(!player.isDead())
		{
			plugin.log.info(resetLocation.toString());
			startPosition.put(player, resetLocation);
			
		}

		
		player.sendMessage(ChatColor.LIGHT_PURPLE + "Ce serveur est protégé par LocalAuth " + plugin.getDescription().getVersion());
		if(plugin.playerManager.listPlayers.containsKey(playerName))
		{
			player.sendMessage(ChatColor.YELLOW + "Veuillez vous connecter en utilisant la commande /login {mot_de_passe}.");
		}
		else
		{
			plugin.unregisteredPlayers.add(player);
			player.sendMessage(ChatColor.RED + "Vous n'êtes pas inscrit sur le serveur. Pour nous rejoindre, inscrivez vous sur le forum.");
			player.sendMessage(ChatColor.RED + "Direction http://kikoo.dyndns.org/forum et présentez vous !");
			if(plugin.config.getCanUnregisteredMove())
			{
				player.sendMessage(ChatColor.GREEN + "Nous vous permettons quand même de vous ballader :)");
			}
			if(plugin.config.getCanUnregisteredFly())
			{
				player.setAllowFlight(true);
			}
		}
	}

	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerMove(PlayerMoveEvent event) 
	{
		Player player = event.getPlayer();
		
		// If the player is logged or is unregistered with the permission to move
		if(		!plugin.unloggedPlayers.contains(player) || 
				plugin.unregisteredPlayers.contains(player) && plugin.config.getCanUnregisteredMove())
		{
			
		}
		else
		{
			if(!startPosition.containsKey(player))
			{
				startPosition.put(player, player.getLocation());
			}
			if(lastNotif.containsKey(player.getDisplayName()))
			{
				Date date = lastNotif.get(player.getDisplayName());
				if((new Date().getTime() - date.getTime()) > 4000)
				{
					event.setFrom(startPosition.get(player));
					event.setTo(startPosition.get(player));
					player.sendMessage(ChatColor.YELLOW + "Tu n'es pas connecté...");
					lastNotif.remove(player.getDisplayName());
					lastNotif.put(player.getDisplayName(), new Date());
					player.teleport(startPosition.get(player));
				}
			}
			else
			{
				lastNotif.put(player.getDisplayName(), new Date());
				Location tpLocation = startPosition.get(player);
				if(tpLocation.getYaw() == 0.0)
				{
					plugin.log.info("TP : "+player.getEyeLocation().toString());
					tpLocation.setYaw(player.getEyeLocation().getYaw());
				}
				player.teleport(startPosition.get(player));
			}
		}
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) 
	{
		Player player = event.getPlayer();
		if(plugin.unloggedPlayers.contains(player))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerCraft(org.bukkit.event.inventory.CraftItemEvent event) 
	{
		Player player = (Player)event.getWhoClicked();
		if(plugin.unloggedPlayers.contains(player))
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerGetItem(org.bukkit.event.player.PlayerPickupItemEvent event) 
	{
		Player player = (Player)event.getPlayer();
		if(plugin.unloggedPlayers.contains(player))
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerInteract(org.bukkit.event.player.PlayerInteractEvent event) 
	{
		plugin.Print("interact");
		HumanEntity human = event.getPlayer();
		if(human instanceof Player)
		{
			Player player = (Player)human;
			if(plugin.unloggedPlayers.contains(player))
			{
				//event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) 
	{

	}

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) 
	{
		if(event.isCancelled())
			return;
		
		String message = event.getMessage();
		if(message.startsWith("/auth"))
		{
			Player player = event.getPlayer();
			event.setCancelled(true);
			String argsStr = message.replaceFirst("\\/auth ", "");
			String[] args = argsStr.split(" ");
			
			if(args.length == 0)
			{
				player.sendMessage(ChatColor.RED + "Command arguments are : password, changepassword, logas");
				return;
			}
			
			if(args[0].equals("password"))
			{
				if(!plugin.unloggedPlayers.contains(player))
				{
					player.sendMessage(ChatColor.LIGHT_PURPLE + "T'es stupide ? T'es déjà connecté...");
				}
				else
				{
					if(args.length != 2)
					{
						player.sendMessage(ChatColor.LIGHT_PURPLE+ "T'as pas oublié le mot de passe ?");
						return;
					}
					String password = args[1];
					if(plugin.playerManager.CheckPassword(player.getDisplayName(), password))
					{
						player.sendMessage(ChatColor.GREEN + "Have fun !");
						plugin.unloggedPlayers.remove(player);
						if(startPosition.containsKey(player))
							startPosition.remove(player);
					}
					else
					{
						player.sendMessage(ChatColor.RED + "Fail !");
					}
				}
			}
			else if(args[0].equals("changepassword"))
			{
				if(plugin.unloggedPlayers.contains(event.getPlayer()))
				{
					player.sendMessage(ChatColor.RED + "You can't change your password. Log in before !");
					return;
				}
				if(args.length != 3)
				{
					player.sendMessage(ChatColor.LIGHT_PURPLE+ "Command usage : /auth changepassword old_password new_password");
					return;
				}
				if(plugin.playerManager.CheckPassword(player.getDisplayName(), args[1]))
				{
					player.sendMessage(ChatColor.GREEN+ "Current password ok");
					plugin.playerManager.changePassword(player, player.getDisplayName(), args[2]);
				}
				else
				{
					player.sendMessage(ChatColor.YELLOW+ "Incorrect current password");
				}
			}
			else
			{
				player.sendMessage(ChatColor.RED + "Bad arguments !");
			}
		}
		else
		{
			if(plugin.unloggedPlayers.contains(event.getPlayer()))
			{
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + "You only have the permission to use /auth command.");
				return;
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) 
	{
		Player p = event.getPlayer();
		Boolean resetPosition = true;
		if(plugin.unregisteredPlayers.contains(p))
		{
			plugin.unregisteredPlayers.remove(p);
			if(plugin.config.getCanUnregisteredMove())
			{
				resetPosition = false;
			}
		}
		if(plugin.unloggedPlayers.contains(p))
		{
			if(resetPosition)
			{
				p.teleport(startPosition.get(p));
			}
			plugin.unloggedPlayers.remove(p);
		}

	}
}
