package com.grydtech.msstack.servicediscovery;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Member {
    
    private final String name;
    
    private final JsonObject data;
    
    public Member(String name, JsonObject data) {
        
        this.name = name;
        this.data = data;
    }
    
    public String getName() {
        return name;
    }
    
    public String getIp() {
        JsonElement val = data.get("address.ip");
        return String.valueOf(val);
    }
    
    public int getPort() {
        JsonElement val = data.get("address.port");
        return Integer.parseInt(String.valueOf(val));
    }
    
    public String getId() {
        JsonElement val = data.get("id");
        return String.valueOf(val);
    }
    
    public String getGroup() {
        JsonElement val = data.get("group");
        return String.valueOf(val);
    }
    
}
