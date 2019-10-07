package io.github.chronosx88.JGUN;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.github.chronosx88.JGUN.storageBackends.InMemoryGraph;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Map;

public class GunGraphNode implements Comparable<GunGraphNode>, Serializable {
    @SerializedName(">")
    @Expose(serialize = false)
    public Map<String, Long> states; // Metadata of the node for diff

    @SerializedName("#")
    @Expose(serialize = false)
    public transient String soul; // i.e. ID of node

    public Map<String, Object> values; // Data

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