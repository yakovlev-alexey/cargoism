package org.cargoism.storage;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public class Storage {
    Gson gson = new Gson();

    public void storeJson(Object[] objects, String path) throws IOException {
        gson.toJson(objects, new FileWriter(path));
    }

    public <T> T loadJson(Type type, String path) {
        return gson.fromJson(path, getClass().getGenericSuperclass());
    }
}
