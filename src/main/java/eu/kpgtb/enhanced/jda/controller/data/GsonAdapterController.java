package eu.kpgtb.enhanced.jda.controller.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;

import java.lang.reflect.Type;

public class GsonAdapterController {
    private static final GsonAdapterController INSTANCE = new GsonAdapterController();
    private final GsonBuilder gsonBuilder;

    private GsonAdapterController() {
        gsonBuilder = new GsonBuilder();
    }

    public GsonAdapterController registerAdapter(Type clazz, TypeAdapter<?> adapter) {
        gsonBuilder.registerTypeAdapter(clazz, adapter);
        return INSTANCE;
    }

    public Gson getGson() {
        return gsonBuilder.create();
    }

    public static GsonAdapterController getInstance() {
        return INSTANCE;
    }
}
