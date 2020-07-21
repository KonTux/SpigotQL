package de.kontux.spigotql.event;

import de.kontux.spigotql.MySQL;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MySQLConnectedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final MySQL mySQL;

    public MySQLConnectedEvent(MySQL mySQL) {
        this.mySQL = mySQL;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
