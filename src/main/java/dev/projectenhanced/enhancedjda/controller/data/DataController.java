package dev.projectenhanced.enhancedjda.controller.data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DataPersister;
import com.j256.ormlite.field.DataPersisterManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.jdbc.db.MysqlDatabaseType;
import com.j256.ormlite.jdbc.db.SqliteDatabaseType;
import com.j256.ormlite.logger.LogBackendType;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;
import dev.projectenhanced.enhancedjda.EnhancedBot;
import dev.projectenhanced.enhancedjda.logger.EnhancedLogger;
import dev.projectenhanced.enhancedjda.util.ReflectionUtil;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;

public class DataController {
    private @Nullable JdbcPooledConnectionSource connectionSource;
    private final EnhancedBot bot;

    private final HashMap<Class<?>, Dao<?, ?>> daosMap;

    public DataController(EnhancedBot bot) {
        this.bot = bot;
        this.daosMap = new HashMap<>();
    }

    /**
     * Enable database
     */
    public void enable() {
        EnhancedLogger.getLogger().info("Connecting to database...");
        String databaseStr = this.bot.getDotenv().get("DATABASE");

        if(databaseStr.equalsIgnoreCase("SQLITE")) {
            File dbFile = new File("database.db");
            if(!dbFile.exists()) {
                try {
                    dbFile.createNewFile();
                } catch (IOException e) {
                    this.connectionSource = null;
                    EnhancedLogger.getLogger().error("Error when creating SQLITE database file");
                    e.printStackTrace();
                    return;
                }
            }
            String sqliteURL = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            try {
                this.connectionSource = new JdbcPooledConnectionSource(sqliteURL, new SqliteDatabaseType());
            } catch (SQLException e) {
                this.connectionSource = null;
                EnhancedLogger.getLogger().error("Error when connection to SQLITE");
                e.printStackTrace();
                return;
            }
        } else if (databaseStr.startsWith("jdbc:mysql")){
            try {
                this.connectionSource = new JdbcPooledConnectionSource(databaseStr, new MysqlDatabaseType());
            } catch (SQLException e) {
                this.connectionSource = null;
                EnhancedLogger.getLogger().error("Error when connection to MySQL");
                e.printStackTrace();
                return;
            }
        } else {
            this.connectionSource = null;
            EnhancedLogger.getLogger().error("Unknown database type");
            return;
        }

        LoggerFactory.setLogBackendFactory(LogBackendType.NULL);
        EnhancedLogger.getLogger().info("Connected to database");
    }

    /**
     * Register all persisters from specified package
     * @param packageName Name of package with persisters
     */
    public void registerPersisters(String packageName) {
        if(this.connectionSource == null) {
            return;
        }
        var ref = new Object() {
            int registered = 0;
            int found = 0;
            int errors = 0;
        };

        for(Class<?> clazz : ReflectionUtil.getAllClassesInPackage(this.bot.getPackageClass(packageName), packageName)) {
            if(!DataPersister.class.isAssignableFrom(clazz)) {
                continue;
            }
            ref.found++;
            try {
                DataPersister persister = null;
                for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                    int params = constructor.getParameterCount();
                    switch (params) {
                        case 0 -> {
                            persister = (DataPersister) constructor.newInstance();
                        }
                        case 1 -> {
                            if(constructor.getParameterTypes()[0].isAssignableFrom(EnhancedBot.class)) {
                                persister = (DataPersister) constructor.newInstance(this.bot);
                            }
                        }
                    }
                    if(persister != null) break;
                }
                DataPersisterManager.registerDataPersisters(persister);
                ref.registered++;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                ref.errors++;
                e.printStackTrace();
            }
        }

        EnhancedLogger.getLogger().info("Registered {}/{} persisters from {}. Errors: {}", ref.registered,ref.found,packageName,ref.errors);
    }

    /**
     * Register all OrmLite tables from specified package
     * @param packageName Package where are stored all tables
     */
    public void registerTables(String packageName) {
        if(this.connectionSource == null) {
            return;
        }

        var ref = new Object() {
            int registered = 0;
            int found = 0;
            int errors = 0;
        };

        for(Class<?> clazz : ReflectionUtil.getAllClassesInPackage(this.bot.getPackageClass(packageName), packageName)) {
            if(clazz.getDeclaredAnnotation(DatabaseTable.class) == null) {
                continue;
            }
            ref.found++;
            Field idField = null;
            for(Field f : clazz.getDeclaredFields()) {
                DatabaseField databaseField = f.getDeclaredAnnotation(DatabaseField.class);
                if(databaseField == null) {
                    continue;
                }
                if(!databaseField.id() && !databaseField.generatedId() && databaseField.generatedIdSequence().isEmpty()) {
                    continue;
                }
                idField = f;
                break;
            }
            if(idField == null) {
                ref.errors++;
                continue;
            }

            try {
                TableUtils.createTableIfNotExists(this.connectionSource, clazz);
            } catch (SQLException e) {
                ref.errors++;
                e.printStackTrace();
                continue;
            }

            Dao<?,?> dao;
            try {
                dao = DaoManager.createDao(this.connectionSource, clazz);
                dao.setObjectCache(true);
                ref.registered++;
            } catch (SQLException e) {
                ref.errors++;
                e.printStackTrace();
                continue;
            }
            this.daosMap.put(clazz, dao);
        }

        EnhancedLogger.getLogger().info("Registered {}/{} tables from {}. Errors: {}", ref.registered,ref.found,packageName,ref.errors);
    }

    /**
     * Get DAO instance of table
     * @param daoSource Class with table
     * @param idType Class that represents ID
     * @return DAO of this table or null
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public <T,Z> Dao<T,Z> getDao(Class<T> daoSource, Class<Z> idType) {
        if(connectionSource == null) {
            return null;
        }

        Dao<?,?> dao = daosMap.get(daoSource);
        if(dao == null) {
            return null;
        }

        try {
            return (Dao<T, Z>) dao;
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     * Close connection with database
     */
    public void close() {
        if(this.connectionSource == null) {
            return;
        }
        try {
            this.connectionSource.close();
        } catch (Exception ignored) {

        }
    }

    @Nullable
    public JdbcPooledConnectionSource getConnectionSource() {
        return connectionSource;
    }
}
