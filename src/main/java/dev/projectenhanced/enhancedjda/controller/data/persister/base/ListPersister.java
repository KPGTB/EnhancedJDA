package dev.projectenhanced.enhancedjda.controller.data.persister.base;

import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.LongStringType;
import dev.projectenhanced.enhancedjda.controller.data.GsonAdapterController;
import dev.projectenhanced.enhancedjda.util.CollectionUtil;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListPersister extends LongStringType {
    private final String NONE_TAG = "NONE";

    public ListPersister() {
        super(SqlType.LONG_STRING, new Class[]{List.class});
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) throws SQLException {
        List<?> list = (List<?>) javaObject;
        String clazz = NONE_TAG;

        if(!list.isEmpty()) {
            clazz = CollectionUtil.getObjectTypes(list.get(0));
        }
        String json = GsonAdapterController.getInstance().getGson()
                .toJson(list);
        return clazz + " " + json;
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        String[] data = ((String) sqlArg).split(" ", 2);

        if(data.length < 2) {
            return new ArrayList<>();
        }
        if(data[0].equalsIgnoreCase(NONE_TAG)) {
            return new ArrayList<>();
        }

        try {
            Type valueType = CollectionUtil.getTypesFromString(data[0])[0];
            Type listType = TypeToken.getParameterized(List.class, valueType).getType();
            return GsonAdapterController.getInstance().getGson()
                    .fromJson(data[1], listType);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
