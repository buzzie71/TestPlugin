//Name: buzzie71
//Plugin: TestPlugin
//June 2013

//First plugin, just playing around and seeing what I can do here

package com.gmail.buzziespy.testplugin;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener{

	@Override
	public void onEnable()
	{
		//TODO Insert logic to be performed when plugin is enabled
		getLogger().info("onEnable has been invoked!");
		
		//enable the listener
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable()
	{
		//TODO Insert logic to be performed when plugin is enabled
		getLogger().info("onDisable has been invoked!");
	}
	
	//Section sign for formatting colors
	//§
	
	//Display information on tamed dogs and cats when right-clicked
	@EventHandler
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent e)
	{
		//getLogger().info("Right-clicked!");
		//do stuff if the entity right-clicked is a dog or cat (tamed?)
		if (e.getRightClicked() instanceof Wolf || e.getRightClicked() instanceof Ocelot)
		{
			//getLogger().info("Right-clicked on wolf or ocelot!");
			//mark animal as pet
			Tameable pet = (Tameable)(e.getRightClicked());
			//if the pet is tamed
			if (pet.isTamed())
			{
				//send player info on who owns it
				//if pets belong to player, output "This OCELOT/WOLF belongs to you"
				if (pet.getOwner().getName().equalsIgnoreCase(e.getPlayer().getName()))
				{
					e.getPlayer().sendMessage("§eThis " + e.getRightClicked().getType().toString() + " belongs to you."); 
				}
				else //otherwise "This OCELOT/WOLF belongs to <playername>"
				{
					e.getPlayer().sendMessage("§dThis " + e.getRightClicked().getType().toString() + " belongs to " + pet.getOwner().getName() + "."); 
				}
			}
			
		}
	}
	
	//some commands
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		//a simple text-return only along the lines of /char
		if (cmd.getName().equalsIgnoreCase("buzzie"))
		{
			if (!(sender instanceof Player))
			{
				//if not player who sends command:
				sender.sendMessage("This command can only be run by a player!");
			}
			else
			{
				sender.sendMessage("§1Ohz noes!");
				//do something
			}
			return true;
		}//If this has happened the function will return true.
		//If this hasn't happened then a value of false will be returned.
		
		
		//the fabled /zap command
		else if (cmd.getName().equalsIgnoreCase("zap"))
		{			
			if (!(sender instanceof Player))
			{
				//if console runs command, zap player regardless of whether it is buzzie71
				ConsoleCommandSender zapper = (ConsoleCommandSender)sender;
				Player target = zapper.getServer().getPlayer(args[0]);
				if (target == null)
				{
					getLogger().info(args[0] + " is not online!");
					return true; //stop attempting to run if player is not here
				}
				//notify player of zapping
				getLogger().info("You have zapped " + args[0] + "!");
				target.sendMessage("§eBZZZZZZZZZZZT!");
				//drop lightning on player
				target.getWorld().strikeLightningEffect(target.getLocation());
				//kill player
				target.setHealth(0);
				return true;
				
			}
			else
			{
				//check if player has permissions
				if (sender.hasPermission("zapper"))
				{
					getLogger().info(sender.getName() + " has permission to zap!");
				}
				
				//check that a target has been supplied in args[0]
				if (args.length < 1)
				{
					//sender.sendMessage("Usage: /zap <playername>");
					return false;
				}
				else
				{
					//if argument check has passed, then check if player is online
					Player zapper = (Player)sender;
					Player target = zapper.getServer().getPlayer(args[0]);
					if (target == null)
					{
						//For now, removing Aca checking
						
//						double x = Math.random();
//						if (x < 0.75) //do nothing 75% of the time
//						{
							//IN MOST CASES: Stop command here.
							sender.sendMessage("§e" + args[0] + " is not online!");
							return true; //stop attempting to run if player is not here
//						}
//						else
//						{
							//SOMETIMES: Check if TheAcademician is online and zap her instead
//							target = zapper.getServer().getPlayer("TheAcademician");
//							if (target != null) //if TheAcademician is online
//							{
								//notify player of zapping
//								zapper.sendMessage("You zapped TheAcademician instead...oops.");
//								target.sendMessage("BZZZZZZZZZZZT!");
								//drop lightning on player
//								target.getWorld().strikeLightningEffect(target.getLocation());
								//kill player
//								target.setHealth(0);
//								return true;
//							}
//							else //if not online, pretend this check never happened
//							{
//								sender.sendMessage(args[0] + " is not online!");
//								return true; //stop attempting to run if player is not here
//							}
//						}
						
					}
					
					//check if target is buzzie71
					
					if (target.getName().equals("buzzie71"))
					{
						//if target is buzzie71, drop lighting on player who runs /zap instead:
						//notify player of zapping
						target.sendMessage("§e" + zapper.getName() + " attempted to zap you!");
						zapper.sendMessage("§4You are not allowed to zap buzzie71! >:C");
						//drop lightning on player
						zapper.getWorld().strikeLightningEffect(zapper.getLocation());
						//kill player
						zapper.setHealth(0);
						return true;
					}
					else
					{
						//notify player of zapping
						zapper.sendMessage("§eYou have zapped " + args[0] + "!");
						target.sendMessage("§eBZZZZZZZZZZZT!");
						//drop lightning on player
						target.getWorld().strikeLightningEffect(target.getLocation());
						//kill player
						target.setHealth(0);
						return true;
					}
					
				}
				
				
			
			}
		}
		
		//Spawns a pet tamed to a specific player at the command runner's location
		//(/spawn-tame-mob <ocelot/wolf> <playername>)
		//Techs: Not sure how to work permissions on this one so only modmode mods/admins can run this!  Sorry :S
		else if (cmd.getName().equalsIgnoreCase("spawn-tame-mob"))
		{
			//assuming here that the person executing this command is a logged-in player and not console
			
			//incorrect number of args
			if (args.length != 2)
			{
				sender.sendMessage("Usage: /spawn-tame-mob <ocelot/wolf/cat/dog> <playername>");
				return true;
			}
			
			//for animal spawning, I am assuming that the player whose name is included in the command (ie
			//who the tamed pet should belong to) actually exists.
			
			//if pet requested is an ocelot
			if (args[0].equalsIgnoreCase("Ocelot") || args[0].equalsIgnoreCase("Cat"))
			{
				Player p = (Player)sender;
				
				//Assuming also that the player who the pet should be tamed to is online
				OfflinePlayer owner = (OfflinePlayer)p.getServer().getOfflinePlayer(args[1]);
				
				//spawn in animal at player's location
				Ocelot pet = (Ocelot)p.getWorld().spawnEntity(p.getLocation(), EntityType.OCELOT);
				//set owner accordingly
				pet.setOwner(owner);
				//use vanilla MC randomness to determine ocelot skin
				//unsure how it's determined, can't find online...so assuming equal chance for all three skins
				double x = Math.random();
				if (x < (1.0/3.0))
				{
					//tabby cat
					pet.setCatType(Ocelot.Type.RED_CAT);
				}
				else if (x < (2.0/3.0))
				{
					//tuxedo cat
					pet.setCatType(Ocelot.Type.BLACK_CAT);
				}
				else
				{
					//siamese cat
					pet.setCatType(Ocelot.Type.SIAMESE_CAT);
				}
				
				
				p.sendMessage("Spawned ocelot tamed to " + args[1]);
				return true;
			}
			else if (args[0].equalsIgnoreCase("Wolf") || args[0].equalsIgnoreCase("Dog"))
			{
				Player p = (Player)sender;
				
				//Assuming also that the player who the pet should be tamed to is online
				OfflinePlayer owner = (OfflinePlayer)p.getServer().getOfflinePlayer(args[1]);
				
				//spawn in animal at player's location
				Wolf pet = (Wolf)p.getWorld().spawnEntity(p.getLocation(), EntityType.WOLF);
				//set owner accordingly
				pet.setOwner(owner);
				
				p.sendMessage("Spawned wolf tamed to " + args[1]);
				return true;
			}
		}
		
		return false;
		
	}
}
