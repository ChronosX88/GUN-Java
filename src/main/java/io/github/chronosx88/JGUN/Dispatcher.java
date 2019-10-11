package io.github.chronosx88.JGUN;

import com.google.gson.Gson;
import io.github.chronosx88.JGUN.di.Dagger;
import io.github.chronosx88.JGUN.futures.BaseCompletableFuture;
import io.github.chronosx88.JGUN.futures.FutureGet;
import io.github.chronosx88.JGUN.futures.FuturePut;
import io.github.chronosx88.JGUN.model.GunWireMessage;
import io.github.chronosx88.JGUN.nodes.Peer;
import io.github.chronosx88.JGUN.storageBackends.MemoryGraph;
import io.github.chronosx88.JGUN.storageBackends.StorageBackend;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Dispatcher {
    private final Map<String, BaseCompletableFuture<?>> pendingFutures = new ConcurrentHashMap<>();
    private final Map<String, NodeChangeListener> changeListeners = new ConcurrentHashMap<>();
    private final Map<String, NodeChangeListener.ForEach> forEachListeners = new ConcurrentHashMap<>();
    private final Peer peer;
    private final StorageBackend graphStorage;
    private final Dup dup;
    private final Executor executorService = Executors.newCachedThreadPool();
    @Inject Gson gson;

    public Dispatcher(StorageBackend graphStorage, Peer peer, Dup dup) {
        Dagger.getMainComponent().inject(this);
        this.graphStorage = graphStorage;
        this.peer = peer;
        this.dup = dup;
    }

    public void addPendingFuture(BaseCompletableFuture<?> future) {
        pendingFutures.put(future.getFutureID(), future);
    }

    public void handleIncomingMessage(GunWireMessage message) {
        if(message.putData != null) {
            peer.emit(gson.toJson(handlePut(message)));
        } else if(message.getData != null) {
            peer.emit(gson.toJson(handleGet(message)));
        } else if(message.ackOn != null) {
            handleIncomingAck(message);
        }
        peer.emit(gson.toJson(message));
    }

    private GunWireMessage handleGet(GunWireMessage message) {
        MemoryGraph getResults = Utils.getRequest(message.getData, graphStorage);

        return new GunWireMessage(dup.track(Dup.random()), message.id, getResults, !(getResults.isEmpty()));
    }

    private GunWireMessage handlePut(GunWireMessage message) {
        boolean success = HAM.mix(new MemoryGraph(message.putData), graphStorage, changeListeners, forEachListeners);
        return new GunWireMessage(
                dup.track((Dup.random())),
                message.id,
                success);
    }

    private void handleIncomingAck(GunWireMessage msg) {
        if(msg.putData != null) {
            if(pendingFutures.containsKey(msg.ackOn)) {
                BaseCompletableFuture<?> future = pendingFutures.get(msg.ackOn);
                if(future instanceof FutureGet) {
                    ((FutureGet) future).complete(msg.putData.toUserJSONObject());
                }
            }
        }
        if(msg.getData != null) {
            if(pendingFutures.containsKey(msg.ackOn)) {
                BaseCompletableFuture<?> future = pendingFutures.get(msg.ackOn);
                if(future instanceof FuturePut) {
                    ((FuturePut) future).complete(msg.isOk);
                }
            }
        }
    }

    public void sendPutRequest(String messageID, JSONObject data) {
        executorService.execute(() -> {
            /*MemoryGraph graph = Utils.prepareDataForPut(data);
            peer.emit(Utils.formatPutRequest(messageID, graph.toJSONObject()).toString());*/ // TODO
        });
    }

    public void sendGetRequest(String messageID, String soul, String field) {
        executorService.execute(() -> {
            GunWireMessage message = new GunWireMessage(messageID, soul, field);
            peer.emit(gson.toJson(message));
        });
    }

    public void addChangeListener(String soul, NodeChangeListener listener) {
        changeListeners.put(soul, listener);
    }

    public void addForEachChangeListener(String soul, NodeChangeListener.ForEach listener) {
        forEachListeners.put(soul, listener);
    }
}
