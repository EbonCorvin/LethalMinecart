package org.eboncorvin.lethalminecart;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.util.Vector;

public class WorldEventListener implements Listener {
	private static double INT_MAX_DAMAGE = 0;
	private static double INT_FATAL_SPEED = 0;
	private static double DOUBLE_MIN_HARMFUL_SPEED = 0;
	
	private static final HashMap<UUID,Long> tickCheck = new HashMap<UUID,Long>();
	
	private FileConfiguration config;
	
	public WorldEventListener(FileConfiguration config) {
		this.config = config;
	}
	
	public void reloadConfig() {
		INT_MAX_DAMAGE = config.getDouble("max_damage", 20);
		INT_FATAL_SPEED = config.getDouble("fatal_speed", 30);
		DOUBLE_MIN_HARMFUL_SPEED = config.getDouble("harmful_speed", 5);
	}
	
	@EventHandler
	public void CheckMinecartCollision(VehicleEntityCollisionEvent event) {
		Logger logger = Bukkit.getLogger();
		Vehicle vehicle = event.getVehicle();
		Entity entity = event.getEntity();
		if(!(entity instanceof Damageable))
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
		Vector velocity = vehicle.getVelocity();
		double speed = Math.sqrt(Math.pow(velocity.getX(), 2) + Math.pow(velocity.getZ(), 2));
		speed*=20; // Assume that the server run at 20 ticks a second
		Damageable liveBeing = (Damageable) entity;
		if(speed > INT_FATAL_SPEED) {
			liveBeing.damage(100);
		}else {
			int damage = (int) Math.round(Math.max(0,speed - DOUBLE_MIN_HARMFUL_SPEED) / INT_FATAL_SPEED * INT_MAX_DAMAGE);
			if(damage==0)
				return;
			logger.info("Entity that got hit: " + entity.getName());
			logger.info("Minecart speed: " + Double.toString(speed) + ", Damage: " + Integer.toString(damage));
			liveBeing.damage(damage, DamageSource.builder(DamageType.MOB_ATTACK).build());
		}
		tickCheck.put(uuid, currentTick);
	}
}
