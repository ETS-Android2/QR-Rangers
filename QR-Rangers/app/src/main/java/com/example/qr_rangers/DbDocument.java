package com.example.qr_rangers;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public abstract class DbDocument {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public abstract DbDocument fromMap(Map<String, Object> map);
    public abstract Map<String, Object> toMap();
}
