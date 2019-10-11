package io.github.chronosx88.JGUN.storageBackends;

import io.github.chronosx88.JGUN.GunGraphNode;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface StorageBackend {
    GunGraphNode getNode(String soul);

    void addNode(String soul, GunGraphNode node);

    boolean hasNode(String soul);

    Set<Map.Entry<String, GunGraphNode>> entries();

    Collection<GunGraphNode> nodes();

    boolean isEmpty();
}
