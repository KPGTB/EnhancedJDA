package dev.projectenhanced.enhancedjda.controller.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;

import java.lang.reflect.Type;

/**
 * Controller of gson adapters
 */
public class GsonAdapterController {
    private static final GsonAdapterController INSTANCE = new GsonAdapterController();
    private final GsonBuilder gsonBuilder;

    private GsonAdapterController() {
        gsonBuilder = new GsonBuilder();
    }

    /**
     * Register new gson adapter
     * @param clazz Adapted class
     * @param adapter Adapter
     * @return instance
     */
    public GsonAdapterController registerAdapter(Type clazz, TypeAdapter<?> adapter) {
        gsonBuilder.registerTypeAdapter(clazz, adapter);
        return INSTANCE;
    }

    /**
     * @return final gson object
     */
    public Gson getGson() {
        return gsonBuilder.create();
    }

    public static GsonAdapterController getInstance() {
        return INSTANCE;
    }
}
