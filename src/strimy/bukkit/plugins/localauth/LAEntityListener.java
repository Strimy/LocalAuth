package strimy.bukkit.plugins.localauth;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class LAEntityListener implements Listener  
{
	LocalAuth plugin;
	
	public LAEntityListener(LocalAuth plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) 
	{
		boolean unloggedTarget = false;
		boolean unregisteredTarget = false;
		if(event.getEntity() instanceof Player)
		{
			Player player = (Player)event.getEntity();
			if(plugin.unregisteredPlayers.contains(player))
			{
				unregisteredTarget = true;
				if(!plugin.config.getCanKillUnregistered())
				{
					//plugin.Print("Cancelled as unregistered killing disabled");
					event.setCancelled(true);
					event.setDamage(0);
				}
			}
			else if(plugin.unloggedPlayers.contains(player))
			{
				//plugin.Print("Cancelled because player is unlogged");
				unloggedTarget = true;
				event.setCancelled(true);
				event.setDamage(0);
			}
		}
		
		if(event instanceof EntityDamageByEntityEvent)
		{
			EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent)event;
			Entity damager = entityEvent.getDamager();
			
			if(damager instanceof Player)
			{
				plugin.Print("Origin is player");
				if(unregisteredTarget)
				{
					if(!plugin.config.getCanKillUnregistered())
					{
						((Player)damager).sendMessage("You can't hurt unregistered player.");
						return;
					}
				}
				else if(unloggedTarget)
				{
					((Player)damager).sendMessage("You can't hurt unlogged player.");
					return;
				}
				
				
				if(entityEvent.getEntity() instanceof LivingEntity)
				{
					Player player = (Player)damager;
					if(plugin.unloggedPlayers.contains(player))
					{
						event.setDamage(0);
						event.setCancelled(true);
						((Player)damager).sendMessage(ChatColor.YELLOW + "You are not logged. You can't hurt people !");
					}
				}
			}
			else if(unregisteredTarget || unloggedTarget)
			{
				event.setDamage(0);
				event.setCancelled(true);
			}
		}
		else
		{
			if(unregisteredTarget || unloggedTarget)
			{
				event.setDamage(0);
				event.setCancelled(true);
			}
		}
	}

}
