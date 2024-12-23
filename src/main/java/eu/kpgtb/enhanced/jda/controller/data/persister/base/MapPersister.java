package eu.kpgtb.enhanced.jda.controller.data.persister.base;

import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.LongStringType;
import eu.kpgtb.enhanced.jda.controller.data.GsonAdapterController;
import eu.kpgtb.enhanced.jda.util.CollectionUtil;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MapPersister extends LongStringType {
    private static final MapPersister SINGLETON = new MapPersister();
    private final String NONE_TAG = "NONE";

    public MapPersister() {
        super(SqlType.LONG_STRING, new Class[]{Map.class});
    }

    public static MapPersister getSingleton() {
        return SINGLETON;
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) throws SQLException {
        Map<?,?> map = (Map<?,?>) javaObject;
        String keyClazz = NONE_TAG;
        String valueClazz = NONE_TAG;

        if(!map.isEmpty()) {
            keyClazz = map.keySet().toArray()[0].getClass().getName();
            valueClazz = CollectionUtil.getObjectTypes(
                    map.values().toArray()[0]
            );
        }

        String mapJson = GsonAdapterController.getInstance().getGson()
                .toJson(map);
        return keyClazz + " " + valueClazz + " " + mapJson;
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        String[] data = ((String) sqlArg).split(" ", 3);

        if(data.length < 3) {
            return new HashMap<>();
        }
        if(data[0].equalsIgnoreCase(NONE_TAG) || data[1].equalsIgnoreCase(NONE_TAG)) {
            return new HashMap<>();
        }

        try {
            Type keyType = Class.forName(data[0]);
            Type valueType = CollectionUtil.getTypesFromString(data[1])[0];
            Type mapType = TypeToken.getParameterized(Map.class, keyType, valueType).getType();
            return GsonAdapterController.getInstance().getGson()
                    .fromJson(data[2], mapType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}
