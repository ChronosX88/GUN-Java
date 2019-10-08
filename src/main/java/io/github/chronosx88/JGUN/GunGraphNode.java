package io.github.chronosx88.JGUN;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class GunGraphNode implements Comparable<GunGraphNode>, Serializable {
    @SerializedName("_")
    public GunNodeMetadata nodeMetadata;

    public Map<String, Object> values; // Data

    public GunGraphNode() {}

    public GunGraphNode(GunGraphNode node) {
        this.nodeMetadata = node.nodeMetadata;
        this.values = node.values;
    }

    public GunGraphNode(Map<String, Long> states, String soul, Map<String, Object> values) {
        this.nodeMetadata = new GunNodeMetadata(states, soul);
        this.values = values;
    }

    @Override
    public int compareTo(GunGraphNode other) {
        return nodeMetadata.soul.compareTo(other.nodeMetadata.soul);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (other instanceof String)
            return nodeMetadata.soul.equals(other);
        if (other instanceof GunGraphNode)
            return compareTo((GunGraphNode) other) == 0;
        return false;
    }

    @Override
    public int hashCode() {
        return nodeMetadata.soul.hashCode();
    }
}