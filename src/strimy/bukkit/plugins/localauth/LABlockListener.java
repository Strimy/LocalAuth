package strimy.bukkit.plugins.localauth;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class LABlockListener implements Listener 
{
	LocalAuth plugin;
	
	public LABlockListener(LocalAuth plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void onBlockDamage(BlockDamageEvent event) {
		Player player = event.getPlayer();
		if(plugin.unloggedPlayers.contains(player))
		{
			event.setCancelled(true);
		}
	}


	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if(plugin.unloggedPlayers.contains(player))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if(plugin.unloggedPlayers.contains(player))
		{
			event.setCancelled(true);
		}
	}

}
