package dev.projectenhanced.enhancedjda.controller.data.persister.jda;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;
import dev.projectenhanced.enhancedjda.EnhancedBot;
import net.dv8tion.jda.api.entities.Guild;

import java.sql.SQLException;

public class GuildPersister extends StringType {
    private final EnhancedBot bot;

    public GuildPersister(EnhancedBot bot) {
        super(SqlType.STRING, new Class[]{Guild.class});
        this.bot = bot;
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) throws SQLException {
        return ((Guild)javaObject).getId();
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        return bot.getShardManager().getGuildById((String) sqlArg);
    }
}
