package io.github.chronosx88.JGUN.storageBackends;

import com.google.gson.JsonObject;
import io.github.chronosx88.JGUN.GunGraphNode;
import org.json.JSONObject;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class InMemoryGraph implements StorageBackend {

    private final Map<String, GunGraphNode> nodes = new LinkedHashMap<>();

    public InMemoryGraph(JsonObject source) {
        for (String soul : source.keySet()) {
            GunGraphNode gunGraphNode = new GunGraphNode();
            nodes.put(soul, new GunGraphNode(source.getAsJsonObject(soul)));
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

    @Override
    public String toString() {
        /*JSONObject jsonObject = new JSONObject();
        for(Map.Entry<String, GunGraphNode> entry : nodes.entrySet()) {
            jsonObject.put(entry.getKey(), entry.getValue().toJSONObject());
        }
        return jsonObject.toString();*/ // TODO
        return null;
    }

    public String toPrettyString() {
        /*JSONObject jsonObject = new JSONObject();
        for(Map.Entry<String, GunGraphNode> entry : nodes.entrySet()) {
            jsonObject.put(entry.getKey(), entry.getValue().toJSONObject());
        }
        return jsonObject.toString(2);*/ // TODO
        return null;
    }

    public JSONObject toJSONObject() {
        /*JSONObject jsonObject = new JSONObject();
        for(Map.Entry<String, GunGraphNode> entry : nodes.entrySet()) {
            jsonObject.put(entry.getKey(), entry.getValue().toJSONObject());
        }
        return jsonObject;*/ // TODO
        return null;
    }

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
