package io.github.chronosx88.JGUN.examples;

import io.github.chronosx88.JGUN.nodes.GunSuperPeer;
import io.github.chronosx88.JGUN.storageBackends.MemoryGraph;

public class MainServer {
    public static void main(String[] args) {
        GunSuperPeer gunSuperNode = new GunSuperPeer(5054, new MemoryGraph());
        gunSuperNode.start();
    }
}
