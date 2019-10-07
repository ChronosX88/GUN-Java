package io.github.chronosx88.JGUN.model;

import com.google.gson.annotations.SerializedName;
import io.github.chronosx88.JGUN.GunGraphNode;

import java.util.Map;

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
    public GunGetRequest getRequest;

    /**
     * Data for db put
     */
    @SerializedName("put")
    public Map<String, GunGraphNode> putRequest;

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
}
