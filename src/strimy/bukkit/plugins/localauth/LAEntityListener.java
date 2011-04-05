package strimy.bukkit.plugins.localauth;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;

public class LAEntityListener extends EntityListener  
{
	LocalAuth plugin;
	
	public LAEntityListener(LocalAuth plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public void onEntityDamage(EntityDamageEvent event) 
	{
		// TODO Auto-generated method stub
		

		boolean unloggedTarget = false;
		if(event.getEntity() instanceof Player)
		{
			Player player = (Player)event.getEntity();
			if(plugin.unloggedPlayers.contains(player))
			{
				event.setDamage(0);
				unloggedTarget = true;
			}
		}
		
		if(event instanceof EntityDamageByEntityEvent)
		{
			EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent)event;
			Entity damager = entityEvent.getDamager();
			
			if(unloggedTarget)
			{
				if(damager instanceof Player)
				{
					((Player)damager).sendMessage("You can't hurt unlogged player.");
				}
				return;
			}
			
			if(damager instanceof Player && entityEvent.getEntity() instanceof LivingEntity)
			{
				Player player = (Player)damager;
				if(plugin.unloggedPlayers.contains(player))
				{
					event.setDamage(0);
					((Player)damager).sendMessage(ChatColor.YELLOW + "You are not logged. You can't hurt people !");
				}
			}
		}
		super.onEntityDamage(event);
	}

}
