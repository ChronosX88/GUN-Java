package io.github.chronosx88.JGUN;

import io.github.chronosx88.JGUN.model.GunGetData;
import io.github.chronosx88.JGUN.storageBackends.MemoryGraph;
import io.github.chronosx88.JGUN.storageBackends.StorageBackend;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;

public class Utils {
    public static Thread setTimeout(Runnable runnable, int delay){
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });
        thread.start();
        return thread;
    }

    public static MemoryGraph getRequest(GunGetData getData, StorageBackend graph) {
        String soul = getData.soul;
        String field = getData.field;
        GunGraphNode node = graph.getNode(soul);
        Object tmp;
        if(node == null) {
            return new MemoryGraph();
        }
        if(field != null) {
            tmp = node.values.get(field);
            if(tmp == null) {
                return new MemoryGraph();
            }
            GunGraphNode node1 = new GunGraphNode(node);
            node = new GunGraphNode();
            node.nodeMetadata = node1.nodeMetadata;
            node.values.put(field, tmp);
            Map<String, Long> tmpStates = node1.nodeMetadata.states;
            node.nodeMetadata.states.put(field, tmpStates.get(field));
        }
        MemoryGraph ack = new MemoryGraph();
        ack.addNode(soul, node);
        return ack;
    }

    public static MemoryGraph prepareDataForPut(JSONObject data) {
        MemoryGraph result = new MemoryGraph();
        for (String objectKey : data.keySet()) {
            Object object = data.get(objectKey);
            if(object instanceof JSONObject) {
                GunGraphNode node = Utils.newNode(objectKey, (JSONObject) object);
                ArrayList<String> path = new ArrayList<>();
                path.add(objectKey);
                prepareNodeForPut(node, result, path);
            }
        }
        return result;
    }

    private static void prepareNodeForPut(GunGraphNode node, MemoryGraph result, ArrayList<String> path) {
        for(String key : new ConcurrentSkipListSet<>(node.values.keySet())) {
            Object value = node.values.get(key);
            if(value instanceof JSONObject) {
                path.add(key);
                String soul = "";
                soul = Utils.join("/", path);
                GunGraphNode tmpNode = Utils.newNode(soul, (JSONObject) value);
                node.values.remove(key);
                node.values.put(key, new JSONObject().put("#", soul));
                prepareNodeForPut(tmpNode, result, new ArrayList<>(path));
                result.addNode(soul, tmpNode);
                path.remove(key);
            }
        }
        result.addNode(node.soul, node);
    }

    public static JSONObject formatGetRequest(String messageID, String key, String field) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("#", messageID);
        JSONObject getParameters = new JSONObject();
        getParameters.put("#", key);
        if(field != null) {
            getParameters.put(".", field);
        }
        jsonObject.put("get", getParameters);
        return jsonObject;
    }

    public static JSONObject formatPutRequest(String messageID, JSONObject data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("#", messageID);
        jsonObject.put("put", data);
        return jsonObject;
    }

    /**
     * This check current nodes for existing IDs in our storage, and if there are existing IDs, it means to replace them.
     * Prevents trailing nodes in storage
     * @param incomingGraph The graph that came to us over the wire.
     * @param graphStorage Graph storage in which the incoming graph will be saved
     * @return Prepared graph for saving
     */
    /*public static MemoryGraph checkIncomingNodesForID(MemoryGraph incomingGraph, StorageBackend graphStorage) {
        for (GunGraphNode node : incomingGraph.nodes()) {
            for(node)
        }
    }*/

    /**
     * Returns a string containing the tokens joined by delimiters.
     *
     * @param delimiter a CharSequence that will be inserted between the tokens. If null, the string
     *     "null" will be used as the delimiter.
     * @param tokens an array objects to be joined. Strings will be formed from the objects by
     *     calling object.toString(). If tokens is null, a NullPointerException will be thrown. If
     *     tokens is empty, an empty string will be returned.
     */
    public static String join(CharSequence delimiter, Iterable tokens) {
        final Iterator<?> it = tokens.iterator();
        if (!it.hasNext()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(it.next());
        while (it.hasNext()) {
            sb.append(delimiter);
            sb.append(it.next());
        }
        return sb.toString();
    }
}
