package de.kontux.spigotql;

import de.kontux.spigotql.event.MySQLConnectedEvent;
import de.kontux.spigotql.statement.resulted.ResultedStatement;
import de.kontux.spigotql.statement.resultless.ResultLessStatement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.sql.*;

public class MySQL {

    private final Plugin plugin;
    private Connection connection;
    private DatabaseMetaData meta;

    public MySQL(Plugin plugin) {
        this.plugin = plugin;
    }

    public void connect(String host, String database, String user, String password, int port, Callback<Boolean> callback) {
        log(ChatColor.YELLOW + "Connecting to your MySQL Database...");

        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", user, password);
            this.meta = connection.getMetaData();
            log(ChatColor.GREEN + "Successfully connected to your MySQL Database.");
            callback.finish(plugin, true);
            Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new MySQLConnectedEvent(this)));
        } catch (SQLException e) {
            logError(e, "Connecting to Database.");
            callback.finish(plugin, false);
        }
    }

    public void update(ResultLessStatement command) {
        if (connection == null) {
            return;
        }

        runAsync(() -> {
            try {
                PreparedStatement statement = connection.prepareStatement(command.constructStatement());
                statement.execute();
                statement.close();
            } catch (SQLException e) {
                logError(e, command.constructStatement());
            }
        });
    }

    public void update(ResultLessStatement command, Callback<Boolean> callback) {
        if (connection == null) {
            callback.finish(plugin, false);
            return;
        }

        runAsync(() -> {
            try {
                PreparedStatement statement = connection.prepareStatement(command.constructStatement());
                statement.execute();
                callback.finish(plugin, true);
                statement.close();
            } catch (SQLException e) {
                logError(e, command.constructStatement());
                callback.finish(plugin, false);
            }
        });
    }

    public void query(ResultedStatement command, Callback<QueryResult> callback) {
        if (connection == null) {
            return;
        }

        runAsync(() -> {
            ResultSet rs = null;

            try {
                PreparedStatement statement = connection.prepareStatement(command.constructStatement());
                rs = statement.executeQuery();
            } catch (SQLException e) {
                logError(e, command.constructStatement());
            }

            callback.finish(plugin, new QueryResult(command, rs));
        });
    }

    public void disconnect() {
        try {
            if (connection == null || connection.isClosed()) {
                return;
            }

            connection.close();
            log(ChatColor.GREEN + "Successfully disconnected from your MySQL Database.");
        } catch (SQLException e) {
            logError(e, "Disconnecting from Database.");
        }
    }

    void logError(SQLException exception, String statement) {
        //Back to the main thread so everything is arranged properly and doesn't intersect with other messages.
        Bukkit.getScheduler().runTask(plugin, () -> {
            log(" ");
            log(ChatColor.RED + "Error while executing a MySQL Statement!");
            log(ChatColor.RED + "Cause: " + exception.getMessage());
            log(ChatColor.RED + "Statement: " + statement);
            log(" ");
        });
    }

    /**
     * Lets you identify whether a column exists in a table.
     *
     * @param table The table name to look up the column in
     * @param name  The column's name
     * @return If the column exists in the given table. Always false if one parameter is null or the table doesn't exist
     */
    public boolean hasColumn(String table, String name) {
        if (name == null || table == null || meta == null) {
            return false;
        }

        try {
            return meta.getColumns(null, null, table, name).next();
        } catch (SQLException e) {
            return false;
        }
    }

    private void runAsync(Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
    }

    public void log(String message) {
        this.plugin.getServer().getConsoleSender().sendMessage(message);
    }

    public DatabaseMetaData getMeta() {
        return meta;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
