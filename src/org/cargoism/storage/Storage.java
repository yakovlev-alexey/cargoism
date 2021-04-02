package org.cargoism.storage;

import com.google.gson.Gson;
import org.cargoism.models.Ship;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public class Storage {
    Gson gson = new Gson();

    public void storeJson(Object[] objects, String path) throws IOException {
        try (var writer = new FileWriter(path)) {
            gson.toJson(objects, writer);
        }
    }

    public <T> T loadJson(Type type, String path) {
        return gson.fromJson(path, getClass().getGenericSuperclass());
    }
}
