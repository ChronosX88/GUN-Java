package io.github.chronosx88.JGUN.model;

import com.google.gson.annotations.SerializedName;
import io.github.chronosx88.JGUN.storageBackends.InMemoryGraph;

public class GunWireMessage {
    /**
     * Message ID
     */
    @SerializedName("#")
    public String id;

    /**
     * ID of the message to which this message replies
     */
    @SerializedName("@")
    public String ackOn;

    /**
     * Info about get request
     */
    @SerializedName("get")
    public GunGetData getData;

    /**
     * Data for db put
     */
    @SerializedName("put")
    public InMemoryGraph putData;

    /**
     * Shows the status of the request (is it processed correctly)
     */
    @SerializedName("ok")
    public boolean isOk;

    /**
     * Error text if {@code isOk} is false
     */
    @SerializedName("err")
    public String errorText;

    public GunWireMessage() {}

    /**
     * Construct new GunWireMessage for put ack
     * @param id Message ID
     * @param ackOn ID of the message to which this message replies
     * @param isOk Shows the status of the request (is it processed correctly)
     */
    public GunWireMessage(String id, String ackOn, boolean isOk) {
        this(id, ackOn);
        this.isOk = isOk;
    }

    public GunWireMessage(String id, String ackOn) {
        this.id = id;
        this.ackOn = ackOn;
    }

    /**
     * Construct new GunWireMessage for get ack
     * @param id Message ID
     * @param ackOn ID of the message to which this message replies
     * @param isOk Shows the status of the request (is it processed correctly)
     */
    public GunWireMessage(String id, String ackOn, InMemoryGraph putData, boolean isOk) {
        this(id, ackOn);
        this.putData = putData;
        this.isOk = isOk;
    }

    public GunWireMessage(String id, String desiredSoul, String desiredField) {
        this(id, null);
        this.getData = new GunGetData();
        this.getData.soul = desiredSoul;
        this.getData.field = desiredField;
    }
}
