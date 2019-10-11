package io.github.chronosx88.JGUN;

import io.github.chronosx88.JGUN.storageBackends.MemoryGraph;
import io.github.chronosx88.JGUN.storageBackends.StorageBackend;
import org.json.JSONObject;

import java.util.Map;

public class HAM {
    static class HAMResult {
        public boolean defer = false; // Defer means that the current state is greater than our computer time, and should only be processed when our computer time reaches this state
        public boolean historical = false; // Historical means the old state. This is usually ignored.
        public boolean converge = false; // Everything is fine, you can do merge
        public boolean incoming = false; // Leave incoming value
        public boolean current = false; // Leave current value
        public boolean state = false;
    }

    public static HAMResult ham(long machineState, long incomingState, long currentState, Object incomingValue, Object currentValue) throws IllegalArgumentException {
        HAMResult result = new HAMResult();

        if(machineState < incomingState) {
            // the incoming value is outside the boundary of the machine's state, it must be reprocessed in another state.
            result.defer = true;
            return result;
        }
        if(incomingState < currentState) {
            // the incoming value is within the boundary of the machine's state, but not within the range.
            result.historical = true;
            return result;
        }
        if(currentState < incomingState) {
            // the incoming value is within both the boundary and the range of the machine's state.
            result.converge = true;
            result.incoming = true;
            return result;
        }
        if(incomingState == currentState) {
            // if incoming state and current state is the same
            if(incomingValue.equals(currentValue)) {
                result.state = true;
                return result;
            }
            if((incomingValue.toString().compareTo(currentValue.toString())) < 0) {
                result.converge = true;
                result.current = true;
                return result;
            }
            if((currentValue.toString().compareTo(incomingValue.toString())) < 0) {
                result.converge = true;
                result.incoming = true;
                return result;
            }
        }
        throw new IllegalArgumentException("Invalid CRDT Data: "+ incomingValue +" to "+ currentValue +" at "+ incomingState +" to "+ currentState +"!");
    }

    public static boolean mix(MemoryGraph change, StorageBackend data, Map<String, NodeChangeListener> changeListeners, Map<String, NodeChangeListener.ForEach> forEachListeners) {
        long machine = System.currentTimeMillis();
        MemoryGraph diff = null;
        for(Map.Entry<String, GunGraphNode> entry : change.entries()) {
            GunGraphNode node = entry.getValue();
            for(String key : node.values.keySet()) {
                Object value = node.values.get(key);
                if ("_".equals(key)) { continue; }
                long state = node.states.get(key);
                long was = -1;
                Object known = null;
                if(data == null) {
                    data = new MemoryGraph();
                }
                if(data.hasNode(node.soul)) {
                    if(data.getNode(node.soul).states.get(key) != null) {
                        was = data.getNode(node.soul).states.get(key);
                    }
                    known = data.getNode(node.soul).values.get(key) == null ? 0 : data.getNode(node.soul).values.get(key);
                }
                HAMResult ham = null;
                try {
                    ham = ham(machine, state, was, value, known);
                } catch (IllegalArgumentException e) {
                    continue;
                }

                if(ham != null) {
                    if(!ham.incoming) {
                        if(ham.defer) {
                            System.out.println("DEFER: " + key + " " + value);
                            // Hack for accessing value in lambda without making the variable final
                            StorageBackend[] graph = new StorageBackend[] {data};
                            Utils.setTimeout(() -> mix(node, graph[0], changeListeners, forEachListeners), (int) (state - machine));
                        }
                        continue;
                    }
                }

                if(diff == null) {
                    diff = new MemoryGraph();
                }

                if(!diff.hasNode(node.soul)) {
                    diff.addNode(node.soul, Utils.newNode(node.soul, new JSONObject()));
                }

                if(!data.hasNode(node.soul)) {
                    data.addNode(node.soul, Utils.newNode(node.soul, new JSONObject()));
                }

                GunGraphNode tmp = data.getNode(node.soul);
                tmp.values.put(key, value);
                tmp.states.put(key, state);
                data.addNode(node.soul, tmp);
                diff.getNode(node.soul).values.put(key, value);
                diff.getNode(node.soul).states.put(key, state);
            }
        }
        if(diff != null) {
            for(Map.Entry<String, GunGraphNode> entry : diff.entries()) {
                if(changeListeners.containsKey(entry.getKey())) {
                    //changeListeners.get(entry.getKey()).onChange(entry.getValue().toUserJSONObject()); // TODO
                }
                if(forEachListeners.containsKey(entry.getKey())) {
                    /*for(Map.Entry<String, Object> jsonEntry : entry.getValue().values.toMap().entrySet()) {
                        forEachListeners.get(entry.getKey()).onChange(jsonEntry.getKey(), jsonEntry.getValue());
                    }*/ // TODO
                }
            }
        }
        return true;
    }

    public static boolean mix(GunGraphNode incomingNode, StorageBackend data, Map<String, NodeChangeListener> changeListeners, Map<String, NodeChangeListener.ForEach> forEachListeners) {
        long machine = System.currentTimeMillis();
        MemoryGraph diff = null;

        for(String key : incomingNode.values.keySet()) {
            Object value = incomingNode.values.get(key);
            if ("_".equals(key)) { continue; }
            //long state = incomingNode.states.getLong(key); // TODO
            long was = -1;
            Object known = null;
            if(data == null) {
                data = new MemoryGraph();
            }
            if(data.hasNode(incomingNode.soul)) {
                /*if(data.getNode(incomingNode.soul).states.opt(key) != null) {
                    was = data.getNode(incomingNode.soul).states.getLong(key);
                }
                known = data.getNode(incomingNode.soul).values.opt(key) == null ? 0 : data.getNode(incomingNode.soul).values.opt(key);*/
                // TODO
            }

            /*HAMResult ham = ham(machine, state, was, value, known);
            if(!ham.incoming) {
                if(ham.defer) {
                    System.out.println("DEFER: " + key + " " + value);
                    // Hack for accessing value in lambda without making the variable final
                    StorageBackend[] graph = new StorageBackend[] {data};
                    Utils.setTimeout(() -> mix(incomingNode, graph[0], changeListeners, forEachListeners), (int) (state - machine));
                }
                continue;
            }*/ // TODO

            if(diff == null) {
                diff = new MemoryGraph();
            }

            if(!diff.hasNode(incomingNode.soul)) {
                diff.addNode(incomingNode.soul, Utils.newNode(incomingNode.soul, new JSONObject()));
            }

            if(!data.hasNode(incomingNode.soul)) {
                data.addNode(incomingNode.soul, Utils.newNode(incomingNode.soul, new JSONObject()));
            }

            GunGraphNode tmp = data.getNode(incomingNode.soul);
            tmp.values.put(key, value);
            //tmp.states.put(key, state); // TODO
            data.addNode(incomingNode.soul, tmp);
            diff.getNode(incomingNode.soul).values.put(key, value);
            //diff.getNode(incomingNode.soul).states.put(key, state); // TODO
        }
        if(diff != null) {
            for(Map.Entry<String, GunGraphNode> entry : diff.entries()) {
                if(changeListeners.containsKey(entry.getKey())) {
                    //changeListeners.get(entry.getKey()).onChange(entry.getValue().toUserJSONObject()); // TODO
                }
                if(forEachListeners.containsKey(entry.getKey())) {
                    /*for(Map.Entry<String, Object> jsonEntry : entry.getValue().values.toMap().entrySet()) {
                        forEachListeners.get(entry.getKey()).onChange(jsonEntry.getKey(), jsonEntry.getValue());
                    }*/ // TODO
                }
            }
        }
        return true;
    }
}
