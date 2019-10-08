package io.github.chronosx88.JGUN.storageBackends;

import io.github.chronosx88.JGUN.GunGraphNode;
import org.json.JSONObject;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class InMemoryGraph implements StorageBackend {

    private final Map<String, GunGraphNode> nodes = new LinkedHashMap<>();

    public InMemoryGraph(InMemoryGraph source) {
        for (Map.Entry<String, GunGraphNode> entry : source.entries()) {
            nodes.put(entry.getKey(), new GunGraphNode(entry.getValue()));
        }
    }

    public InMemoryGraph() {}

    public GunGraphNode getNode(String soul) {
        return nodes.get(soul);
    }

    public void addNode(String soul, GunGraphNode incomingNode) {
        nodes.put(soul, incomingNode);
    }

    public boolean hasNode(String soul) {
        return nodes.containsKey(soul);
    }

    public Set<Map.Entry<String, GunGraphNode>> entries() {
        return nodes.entrySet();
    }

    public Collection<GunGraphNode> nodes() { return nodes.values(); }

    public JSONObject toUserJSONObject() {
        /*JSONObject jsonObject = new JSONObject();
        for(Map.Entry<String, GunGraphNode> entry : nodes.entrySet()) {
            jsonObject.put(entry.getKey(), entry.getValue().toUserJSONObject());
        }
        return jsonObject;*/ // TODO
        return null;
    }

    public boolean isEmpty() {
        return nodes.isEmpty();
    }
}
