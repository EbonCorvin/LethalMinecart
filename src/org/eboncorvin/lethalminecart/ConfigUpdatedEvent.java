package org.eboncorvin.lethalminecart;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ConfigUpdatedEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
    
	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return HANDLER_LIST;
	}
}
