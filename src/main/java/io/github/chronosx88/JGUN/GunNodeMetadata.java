package io.github.chronosx88.JGUN;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class GunNodeMetadata {
    @SerializedName(">")
    @Expose(serialize = false)
    public Map<String, Long> states; // Metadata of the node for diff

    @SerializedName("#")
    @Expose(serialize = false)
    public String soul; // i.e. ID of node

    public GunNodeMetadata() {}

    public GunNodeMetadata(Map<String, Long> states, String soul) {
        this.states = states;
        this.soul = soul;
    }
}
