package org.eboncorvin.lethalminecart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;

public class WorldEventListener implements Listener {
	private static double INT_MAX_DAMAGE = 0;
	private static double INT_FATAL_SPEED = 0;
	private static double DOUBLE_MIN_HARMFUL_SPEED = 0;
	
	private static final HashMap<UUID,Long> tickCheck = new HashMap<UUID,Long>();
	private static final HashMap<UUID,ArrayList<Location>> speedCheck = new HashMap<UUID,ArrayList<Location>>();
	
	private LethalMinecartPlugin plugin;
	
	private final static Object listLock = new Object();
	
	public WorldEventListener(LethalMinecartPlugin plugin) {
		this.plugin = plugin;
	}
	
	public void reloadConfig() {
		plugin.reloadConfig();
		FileConfiguration config = plugin.getConfig();
		INT_MAX_DAMAGE = config.getDouble("max_damage", 20);
		INT_FATAL_SPEED = config.getDouble("fatal_speed", 30);
		DOUBLE_MIN_HARMFUL_SPEED = config.getDouble("harmful_speed", 5);
	}
	
	@EventHandler
	public void onMinecartCollision(VehicleEntityCollisionEvent event) {
		Logger logger = plugin.getLogger();
		Vehicle vehicle = event.getVehicle();
		Entity entity = event.getEntity();
		if(!(entity instanceof Damageable))
			return;
		if(entity.isInsideVehicle())
			return;
		UUID uuid = entity.getUniqueId();
		long currentTick = System.currentTimeMillis();
		if(tickCheck.containsKey(uuid)) {
			long tickCheckInt = tickCheck.get(uuid).longValue();
//			logger.info(Long.toString(currentTick) + " " + (Long.toString(tickCheckInt)));
			if(currentTick - tickCheckInt < 1000) {
//				logger.info("Skip damage because last damage happened less than 20 ticks");
				return;
			} else {
				tickCheck.remove(entity.getUniqueId());
			}
		}

		Block blockUnderRail = vehicle.getLocation().getBlock();
		if(!blockUnderRail.getType().equals(Material.RAIL) && !blockUnderRail.getType().equals(Material.POWERED_RAIL))
			return;
		tickCheck.put(uuid, currentTick);
		Vector velocity = vehicle.getVelocity();
		// double speedPerTick = Math.sqrt(Math.pow(velocity.getX(), 2) + Math.pow(velocity.getZ(), 2));
		double speedPerTick = 0;
		synchronized(listLock) {
			ArrayList<Location> locations = speedCheck.get(vehicle.getUniqueId());
			if(locations==null)
				return;
			speedPerTick = locations.get(1).distance(locations.get(0));
		}
		double speed = speedPerTick*20; // Assume that the server run at 20 ticks a second
		Damageable liveBeing = (Damageable) entity;
		if (speed < DOUBLE_MIN_HARMFUL_SPEED || velocity.isZero()){
			return;
		} else {
			int damage = (int) Math.round(speed / INT_FATAL_SPEED * INT_MAX_DAMAGE);
			if(damage==0)
				return;
			logger.info("Entity that got hit: " + entity.getName());
			logger.info("Minecart velocity: " + velocity.toString());
			logger.info("Minecart speed: " + Double.toString(speed) + " (" + Double.toString(speedPerTick) + " per tick), Damage: " + Integer.toString(damage));
			liveBeing.damage(damage, DamageSource.builder(DamageType.MOB_ATTACK).withDirectEntity(vehicle).withCausingEntity(vehicle).build());
			velocity.setY(0.15);
			entity.setVelocity(velocity);
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		EntityDamageEvent lastDamage = player.getLastDamageCause();
		if(lastDamage!=null) {
			Entity hitBy = lastDamage.getDamageSource().getDirectEntity();
			if(hitBy!=null) {
				if(hitBy instanceof Minecart) {
					event.setDeathMessage(player.getName() + " got launched by a minecart to death");
				}
			}
		}
	}
	
	@EventHandler
	public void onVehicleMove(VehicleMoveEvent event) {
		synchronized(listLock) {
			Vehicle vehicle = event.getVehicle();
			UUID uuid = vehicle.getUniqueId();
			ArrayList<Location> list = new ArrayList<Location>();
			list.add(event.getFrom());
			list.add(event.getTo());
			speedCheck.put(uuid, list);
		}
	}
}
