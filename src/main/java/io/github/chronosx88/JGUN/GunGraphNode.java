package io.github.chronosx88.JGUN;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class GunGraphNode implements Comparable<GunGraphNode>, Serializable {
    @SerializedName(">")
    @Expose(serialize = false)
    public Map<String, Long> states; // Metadata of the node for diff

    @SerializedName("#")
    @Expose(serialize = false)
    public String soul; // i.e. ID of node

    public Map<String, Object> values; // Data

    public GunGraphNode() {}

    public GunGraphNode(JsonObject jsonObject) {
        soul = jsonObject.getAsJsonObject("_").get("#").getAsString();
        states = new LinkedHashMap<>();
        for(Map.Entry<String, JsonElement> entry : jsonObject.getAsJsonObject("_").get(">").getAsJsonObject().entrySet()) {
            states.put(entry.getKey(), entry.getValue().getAsLong());
        }
    }

    public GunGraphNode(Map<String, Long> states, String soul, Map<String, Object> values) {
        this.states = states;
        this.soul = soul;
        this.values = values;
    }

    @Override
    public int compareTo(GunGraphNode other) {
        return soul.compareTo(other.soul);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (other instanceof String)
            return soul.equals(other);
        if (other instanceof GunGraphNode)
            return compareTo((GunGraphNode) other) == 0;
        return false;
    }

    @Override
    public int hashCode() {
        return soul.hashCode();
    }
}