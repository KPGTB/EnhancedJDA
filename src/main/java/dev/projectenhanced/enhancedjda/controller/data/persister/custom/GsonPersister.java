package dev.projectenhanced.enhancedjda.controller.data.persister.custom;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.LongStringType;
import dev.projectenhanced.enhancedjda.controller.data.GsonAdapterController;

import java.sql.SQLException;

public class GsonPersister extends LongStringType {
    private static final GsonPersister SINGLETON = new GsonPersister();
    private final String NONE_TAG = "NONE";

    public GsonPersister() {
        super(SqlType.LONG_STRING, new Class[]{});
    }

    public static GsonPersister getSingleton() {
        return SINGLETON;
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) throws SQLException {
        String clazz = NONE_TAG;
        if(javaObject != null) {
            clazz = javaObject.getClass().getName();
        }
        String json = GsonAdapterController.getInstance().getGson()
                .toJson(javaObject);
        return clazz + " " + json;
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        String[] data = ((String) sqlArg).split(" ", 2);

        if(data.length < 2) {
            return null;
        }
        if(data[0].equalsIgnoreCase(NONE_TAG)) {
            return null;
        }

        try {
            return GsonAdapterController.getInstance().getGson()
                    .fromJson(data[1], Class.forName(data[0]));
        } catch (Exception e) {
            return null;
        }
    }
}
