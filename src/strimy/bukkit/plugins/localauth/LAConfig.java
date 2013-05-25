package strimy.bukkit.plugins.localauth;


public class LAConfig
{
	private LocalAuth plugin;
		
	public LAConfig(LocalAuth plugin) 
	{
		this.plugin = plugin;
	}
	
	public void reload()
	{
		plugin.reloadConfig();
	}
	
	public Boolean getCanKillUnregistered() 
	{
		return plugin.getConfig().getBoolean("canKillUnregistered");
	}
	public void setCanKillUnregistered(Boolean canKillUnregistered) 
	{
		plugin.getConfig().set("canKillUnregistered", canKillUnregistered);
	}
	
	public Boolean getCanUnregisteredFly() 
	{
		return plugin.getConfig().getBoolean("canUnregisteredFly");
	}
	public void setCanUnregisteredFly(Boolean canUnregisteredFly) 
	{
		plugin.getConfig().set("canUnregisteredFly", canUnregisteredFly);
	}
	
	public Boolean getCanUnregisteredMove() 
	{
		return plugin.getConfig().getBoolean("canUnregisteredMove");
	}
	public void setCanUnregisteredMove(Boolean canUnregisteredMove) 
	{
		plugin.getConfig().set("canUnregisteredMove", canUnregisteredMove);
	}
	
	
	public Boolean getCanUnloggedMove() 
	{
		return plugin.getConfig().getBoolean("canUnloggedMove");
	}
	public void setCanUnloggedMove(Boolean canUnloggedMove) 
	{
		plugin.getConfig().set("canUnloggedMove", canUnloggedMove);
	}
	
	

}
