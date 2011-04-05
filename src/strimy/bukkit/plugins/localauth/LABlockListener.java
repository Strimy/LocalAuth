package strimy.bukkit.plugins.localauth;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class LABlockListener extends BlockListener 
{
	LocalAuth plugin;
	
	public LABlockListener(LocalAuth plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public void onBlockDamage(BlockDamageEvent event) {
		Player player = event.getPlayer();
		if(plugin.unloggedPlayers.contains(player))
		{
			event.setCancelled(true);
		}
		super.onBlockDamage(event);
	}


	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if(plugin.unloggedPlayers.contains(player))
		{
			event.setCancelled(true);
		}
		super.onBlockPlace(event);
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if(plugin.unloggedPlayers.contains(player))
		{
			event.setCancelled(true);
		}
		super.onBlockBreak(event);
	}

}
