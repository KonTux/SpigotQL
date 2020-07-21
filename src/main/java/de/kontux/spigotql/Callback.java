package de.kontux.spigotql;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public interface Callback<T> {

    /**
     * Method called asynchronously to call the accept method on the main thread.
     * @param plugin Plugin to use the scheduler
     * @param result The type of result
     */
    default void finish(Plugin plugin, T result) {
        Bukkit.getScheduler().runTask(plugin, () -> accept(result));
    }

    void accept(T result);
}
