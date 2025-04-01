package com.adamdoesthings.SmelterPlugin;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class SmelterPlugin extends JavaPlugin
{
	// fired when plugin is first enabled
	@Override
	public void onEnable()
	{
		ArrayList<Material> smeltableItems = new ArrayList<Material>();
		
		// add iron items to smelt-able list
		//smeltableItems.add(Material.IRON_AXE);
		smeltableItems.add(Material.IRON_AXE);
		//smeltableItems.add(Material.IRON_BARDING);		name changed in 1.13
		smeltableItems.add(Material.IRON_HORSE_ARMOR);
		smeltableItems.add(Material.IRON_BOOTS);
		smeltableItems.add(Material.IRON_CHESTPLATE);
		smeltableItems.add(Material.IRON_DOOR);
		//smeltableItems.add(Material.IRON_FENCE);			name changed in 1.13
		smeltableItems.add(Material.IRON_BARS);
		smeltableItems.add(Material.IRON_HELMET);
		smeltableItems.add(Material.IRON_HOE);
		smeltableItems.add(Material.IRON_LEGGINGS);
		smeltableItems.add(Material.IRON_PICKAXE);
		//smeltableItems.add(Material.IRON_PLATE);			changed to heavy_weighted_pressure_plate 1.13
		//smeltableItems.add(Material.HEAVY_WEIGHTED_PRESSURE_PLATE); need to fix this to return iron
		smeltableItems.add(Material.IRON_SWORD);
		smeltableItems.add(Material.IRON_TRAPDOOR);
		smeltableItems.add(Material.IRON_NUGGET);
		// add gold items to smelt-able list
		smeltableItems.add(Material.GOLDEN_AXE);
		//smeltableItems.add(Material.GOLD_BARDING);		name changed in 1.13
		smeltableItems.add(Material.GOLDEN_HORSE_ARMOR);//
		smeltableItems.add(Material.GOLDEN_BOOTS);
		smeltableItems.add(Material.GOLDEN_CHESTPLATE);
		smeltableItems.add(Material.GOLDEN_HELMET);
		smeltableItems.add(Material.GOLDEN_HOE);
		smeltableItems.add(Material.GOLDEN_LEGGINGS);
		smeltableItems.add(Material.GOLDEN_PICKAXE);
		//smeltableItems.add(Material.GOLDEN_PLATE);		switched to light_weighted_pressure_plate 1.13
		//smeltableItems.add(Material.LIGHT_WEIGHTED_PRESSURE_PLATE); need to fix this to return gold
		smeltableItems.add(Material.GOLDEN_SWORD);
		// add diamond items to smelt-able list
		smeltableItems.add(Material.DIAMOND_AXE);
		//smeltableItems.add(Material.DIAMOND_BARDING);		name changed in 1.13
		smeltableItems.add(Material.DIAMOND_BOOTS);
		smeltableItems.add(Material.DIAMOND_CHESTPLATE);
		smeltableItems.add(Material.DIAMOND_HELMET);
		smeltableItems.add(Material.DIAMOND_HOE);
		smeltableItems.add(Material.DIAMOND_LEGGINGS);
		smeltableItems.add(Material.DIAMOND_PICKAXE);
		smeltableItems.add(Material.DIAMOND_SWORD);
		// add chainmail items to smelt-able list
		smeltableItems.add(Material.CHAINMAIL_BOOTS);
		smeltableItems.add(Material.CHAINMAIL_CHESTPLATE);
		smeltableItems.add(Material.CHAINMAIL_LEGGINGS);
		smeltableItems.add(Material.CHAINMAIL_HELMET);
		// add other netherite items to smelt-able list
		smeltableItems.add(Material.NETHERITE_AXE);
		smeltableItems.add(Material.NETHERITE_BOOTS);
		smeltableItems.add(Material.NETHERITE_CHESTPLATE);
		smeltableItems.add(Material.NETHERITE_HELMET);
		smeltableItems.add(Material.NETHERITE_HOE);
		smeltableItems.add(Material.NETHERITE_LEGGINGS);
		smeltableItems.add(Material.NETHERITE_PICKAXE);
		smeltableItems.add(Material.NETHERITE_SWORD);
		
		ArrayList<Material> acceptableFuels = new ArrayList<Material>();
		
		// add fuels to acceptable fuels list
		acceptableFuels.add(Material.COAL);
		acceptableFuels.add(Material.CHARCOAL);			// fixed 1.2.1
		acceptableFuels.add(Material.LAVA_BUCKET);
		this.getCommand("smelt").setExecutor(new SmeltCommand(smeltableItems, acceptableFuels));
	}
	
	// fired when plugin is disabled
	@Override
	public void onDisable()
	{
		
	}
	
	public class SmeltCommand implements CommandExecutor
	{
		ArrayList<Material> ValidItems;
		ArrayList<Material> ValidFuels;
		ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
		
		public SmeltCommand(ArrayList<Material> items, ArrayList<Material> fuels)
		{
			ValidItems = items;
			ValidFuels = fuels;
		}
		
		private void SendPlayerErrorMessage(String msg, Player target)
		{
			TextComponent message = new TextComponent(msg);
			message.setColor(ChatColor.RED);
			target.spigot().sendMessage(message);
		}
		
		private void SendPlayerMessage(String msg, Player target)
		{
			TextComponent message = new TextComponent(msg);
			message.setColor(ChatColor.AQUA);
			target.spigot().sendMessage(message);
		}
		
		private void SmeltPlayerItems(Player player, boolean isTest)
		{
			try
			{
				World world = player.getWorld();
				PlayerInventory playerInv = player.getInventory();
				ItemStack heldInMainHand = player.getEquipment().getItemInMainHand();
				ItemStack heldInOffHand = player.getEquipment().getItemInOffHand();
				
				// check inventory to see if it includes furnace
				// check main hand for item made of metal object (gold, iron, diamond, chainmail, netherite)
				// check off hand for fuel (coal, charcoal, lava)
				
				if (heldInMainHand == null || heldInOffHand == null)
				{
					SendPlayerErrorMessage("Hold smeltable item in main hand and fuel in off hand to use smelt command.", player);
				}
				
				if (playerInv.contains(Material.FURNACE))
				{
					if (ValidItems.contains(heldInMainHand.getType()))
						{
							if (heldInMainHand.hasItemMeta() && heldInMainHand.getItemMeta().hasEnchants())
							{
								console.sendMessage(player.getPlayerListName() + " tried smelting enchanted item.");
								SendPlayerErrorMessage("The item's enchantment makes it resistant to smelting.", player);
							}
							else if (ValidFuels.contains(heldInOffHand.getType()))
							{
								// added ,2 in 1.13 to account for material names with more than one "_" like "IRON_HORSE_ARMOR"...
								String[] smeltedItemInfo = heldInMainHand.getType().toString().split("_",2);
								
								if (isTest)
								{
									console.sendMessage("--TEST SMELT--");
								}
								else
								{
									boolean invIsFull = true;
									ItemStack[] contents = playerInv.getContents();
									
									for (ItemStack itemSlot : contents)
									{
										if (itemSlot == null)
										{
											invIsFull = false;
										}
									}
									
									if (invIsFull)
									{
										SendPlayerErrorMessage("You need at least one empty inventory slot to smelt an item.", player);
										console.sendMessage(player.getDisplayName() + " tried smelting with no free inventory slot; cancelled.");
										return;
									}
								}
								console.sendMessage(player.getPlayerListName() + " smelting " + heldInMainHand.getType().toString() + " using " + heldInOffHand.getType().toString() + " as fuel.");
								
								int resultQuantity = 0;		// quantity of resultType to return
								boolean isIronNugget = false; // different result for iron nugget (return flint)
								
								// switch statement to set quantity based on heldInMainHand
								for (int i = 0; i < smeltedItemInfo.length; i++)
								{
									console.sendMessage(smeltedItemInfo[i]);
								}
								
								console.sendMessage("smeltedItemInfo[1] (type of item to smelt): " + smeltedItemInfo[1]);
								
								switch (smeltedItemInfo[1])
								{
									case "AXE": case "PICKAXE": case "HOE": case "SWORD":
										resultQuantity = 1;
										break;
									case "HELMET": case "TRAPDOOR": case "BOOTS": case "PLATE":		// update PLATE to new values
										resultQuantity = 2;
										break;
									case "DOOR": case "BARS": case "LEGGINGS":
										resultQuantity = 3;
										break;
									case "HORSE_ARMOR": case "CHESTPLATE":
										resultQuantity = 4;
										break;
									case "NUGGET":
										resultQuantity = 1;
										isIronNugget = true;
										break;
									default:
										resultQuantity = 0;
								}
								
								// debugging in 1.14.4 - error thrown when smelting
								//console.sendMessage("checking for null values in main hand item object...");
								/*if (heldInMainHand == null)
								{
									throw new Exception("heldInMainHand == null (line 207)");
								}*/
								if (heldInMainHand.getType() == null)
								{
									throw new Exception("heldInMainHand.getType() == null(line 213)");
								}
								//console.sendMessage("heldInMainHand.getType().getMaxDurability(): " + Short.toString(heldInMainHand.getType().getMaxDurability()));
								
								if (heldInMainHand.getType().getMaxDurability() == 0)
								{
									// durability doesn't matter
								}
								else
								{
									// durability does matter, check and adjust quantity accordingly
									
									//console.sendMessage("heldInMainHand.getItemMeta().toString(): " + heldInMainHand.getItemMeta().toString());
									//console.sendMessage("current damage from ((Damageable) heldInMainHand).getDamage(): " + Integer.toString(((Damageable) heldInMainHand.getItemMeta()).getDamage()));
									//Damageable mainHandMeta = (Damageable) heldInMainHand.getItemMeta();
									//if (((Damageable) heldInMainHand.getItemMeta()) == null)
									//{
									//	console.sendMessage("damageable item meta is null, apparently");
									//}
									int currentDurability = ((Damageable) heldInMainHand.getItemMeta()).getDamage();
									int maxDurability = (int) heldInMainHand.getType().getMaxDurability();
									int durabilityRemaining = maxDurability - currentDurability;
									int durabilityDivider = maxDurability / durabilityRemaining;
									
									resultQuantity = resultQuantity / durabilityDivider;
									if (resultQuantity == 0)
									{
										resultQuantity = 1;
									}
									else if (durabilityDivider > 1)
									{
										SendPlayerMessage("The condition of this item has lowered its smeltable material by a factor of " + Integer.toString(durabilityDivider) + ".", player);
										console.sendMessage("Item durability reduced smelting quantity by " + Integer.toString(durabilityDivider) + ".");
									}
								}
								
								ItemStack smeltResultItem = null;
								
								// switch statement for various result types to set item stack material
								
								//console.sendMessage("smeltedItemInfo[0] is '" + smeltedItemInfo[0] + "'");
								switch (smeltedItemInfo[0])
								{
									case "IRON":
										if (!isIronNugget)
										{
											smeltResultItem = new ItemStack(Material.IRON_INGOT);
										}
										else
										{
											smeltResultItem = new ItemStack(Material.FLINT);
										}
										break;
									case "CHAINMAIL":
										smeltResultItem = new ItemStack(Material.IRON_INGOT);
										//console.sendMessage("Chainmail... quantity would be " + Integer.toString(resultQuantity) + "...");
										resultQuantity = resultQuantity / 2;
										//console.sendMessage("Should have divided by 2 - resultQuantitty is now " + Integer.toString(resultQuantity) + "...");
										break;
									case "GOLD":
										smeltResultItem = new ItemStack(Material.GOLD_INGOT);
										break;
									case "GOLDEN":
										smeltResultItem = new ItemStack(Material.GOLD_INGOT);
										break;
									case "DIAMOND":
										smeltResultItem = new ItemStack(Material.DIAMOND);
										break;
									case "NETHERITE":
										smeltResultItem = new ItemStack(Material.NETHERITE_INGOT);
								}
								
								// get item durability and max durability for durability %
								// divide resultQuantity by durability % to set resultQuantity based on item durability
								// for example: pickaxe @ 100% returns 1 bar. at 50%, returns nothing.
								// chestplate @ 100% returns 4 bars. 50% returns 2. 25% returns 1. less than 25% 0.
								
								if (smeltResultItem == null)
								{
									throw new Exception("smeltResultItem is null.");
								}
								
								smeltResultItem.setAmount(resultQuantity);
								
								console.sendMessage("Returning " + Integer.toString(smeltResultItem.getAmount()) + " " + smeltResultItem.getType().toString() + " to " + player.getPlayerListName());
								
								if(heldInMainHand.getAmount() > 1)
								{
									if (!isTest)
									{
										SendPlayerMessage("Using the furnace in your inventory, you heat the " + heldInMainHand.getType().toString(), player);
										SendPlayerMessage("with the " + heldInOffHand.getType().toString() + ".", player);
										if (resultQuantity > 0)
										{
											SendPlayerMessage("You manage to salvage " + Integer.toString(smeltResultItem.getAmount()) + " " + smeltResultItem.getType().toString() + "(s) from the material.", player);
										}
										else
										{
											SendPlayerMessage("However, there was not enough leftover material to be used again after smelting.", player);
										}
										heldInMainHand.setAmount(heldInMainHand.getAmount() - 1);
										heldInOffHand.setAmount(heldInOffHand.getAmount() - 1);
										playerInv.addItem(smeltResultItem);
										
										world.playEffect(player.getLocation(), Effect.SMOKE, 5);
										world.playEffect(player.getLocation(), Effect.EXTINGUISH, 1);
									}
									else
									{
										SendPlayerMessage("The " + heldInMainHand.getType().toString() + " can be smelted with the " + heldInOffHand.getType().toString() + " to create " + Integer.toString(resultQuantity) + " " + smeltResultItem.getType().toString() + "(s).", player);
									}
								}
								else
								{
									if (!isTest)
									{
										SendPlayerMessage("Using the furnace in your inventory, you heat the " + heldInMainHand.getType().toString(), player);
										SendPlayerMessage("with the " + heldInOffHand.getType().toString() + ".", player);
										SendPlayerMessage("You manage to salvage " + Integer.toString(resultQuantity) + " " + smeltResultItem.getType().toString() + "(s) from the material.", player);
										player.getEquipment().setItemInMainHand(smeltResultItem);		// set to smelting result itemstack
										
										if (heldInOffHand.getAmount() >  1)
										{
											heldInOffHand.setAmount(heldInOffHand.getAmount() - 1);
										}
										else
										{
											player.getEquipment().setItemInOffHand(null);
										}
										
										world.playEffect(player.getLocation(), Effect.SMOKE, 5);
										world.playEffect(player.getLocation(), Effect.EXTINGUISH, 1);
									}
									else
									{
										SendPlayerMessage("The " + heldInMainHand.getType().toString() + " can be smelted with the " + heldInOffHand.getType().toString() + "to create " + Integer.toString(resultQuantity) + " " + smeltResultItem.getType().toString() + "(s).", player);
										console.sendMessage("--END TEST SMELT--");
									}
								}
							}
							else
							{
								SendPlayerErrorMessage("Off hand must hold a valid fuel (coal, charcoal, or lava bucket) to smelt.", player);
								console.sendMessage(player.getPlayerListName() + " tried smelting without valid fuel.");
							}
						}
					else
					{
						SendPlayerErrorMessage("Main hand must hold a suitable iron, gold, diamond, or chainmail item to smelt.", player);
						console.sendMessage(player.getPlayerListName() + " tried smelting an invalid item.");
					}
				}
				else
				{
					SendPlayerErrorMessage("You must have a furnace in your inventory to smelt an item.", player);
					console.sendMessage(player.getPlayerListName() + " tried smelting without a furnace in their inventory.");
				}
			}
			catch (Exception ex)
			{
				SendPlayerErrorMessage("Error smelting item(s)", player);
				console.sendMessage(ex.getMessage());
				console.sendMessage(ex.getStackTrace().toString());
			}
		}
		
		// this is the method called when the command is used
		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] cmdArgs)
		{
			/*
			 * BUGS:
			 * 
			 * 
			 * 
			 */
				/* TO DO:
				 * 
				 * add /smelt all to smelt the entire held item stack
				 * 
				 * create separate plugin to remove enchantments from items using XP
				 * this disenchant plugin can capture the enchantment into an unenchanted book if held in off hand
				 * 
				 * make configurable lists of acceptable items/fuels
				 */
				
			if (sender instanceof Player)
			{
				try
				{
					Player player = (Player)sender;
					
					if (cmdArgs.length > 1)
					{
						SendPlayerErrorMessage("Unknown smelt command. Try '/smelt help' for assistance.", player);
						console.sendMessage(player.getDisplayName() + " entered invalid smelt command arguments.");
					}
					else
					{
						if (cmdArgs.length == 0)
						{
							SmeltPlayerItems(player, true);
						}
						else
						{
							switch (cmdArgs[0])
							{
								case "help": case "HELP": case "?":
									SendPlayerMessage("Smelt converts metal & diamond items back into approx. 50% of the primary crafting ingredient quantity, depending on durability & item type. Can also be used to smelt iron nuggets into flint (for use with arrows).", player);
									SendPlayerMessage("Hold item to smelt in main hand and fuel in off hand with furnace in inventory to use.", player);
									SendPlayerMessage("'/smelt' shows what will be returned without actually smelting.", player);
									SendPlayerMessage("'/smelt this' smelts the held item using the held fuel.", player);
									break;
								case "this": case "THIS":
									SmeltPlayerItems(player, false);
									break;
								default:
									SendPlayerMessage("Invalid smelt modifier '" + cmdArgs[0] + "' - use '/smelt help' for assistance.", player);
							}
						}
					}

					// if command worked, return true
					return true;
				}
				catch (Exception ex)
				{
					SendPlayerErrorMessage("Error smelting item(s)", (Player)sender);
					console.sendMessage(ex.getMessage());
					console.sendMessage(ex.getStackTrace().toString());
				}
			}
			else
			{
				console.sendMessage("Invalid sender for 'smelt' command. Sender must be player.");
			}
			return false;
		}
	}
}
