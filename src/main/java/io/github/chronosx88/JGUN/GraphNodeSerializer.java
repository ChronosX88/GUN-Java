package io.github.chronosx88.JGUN;

import com.google.gson.*;

import java.lang.reflect.Type;

public class GraphNodeSerializer implements JsonSerializer<GunGraphNode> {
    @Override
    public JsonElement serialize(GunGraphNode src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject metadata = new JsonObject();
        metadata.add("#", new JsonPrimitive(src.soul));
        metadata.add(">", context.serialize(src.states));

        JsonObject jsonObject = context.serialize(src).getAsJsonObject();
        jsonObject.add("_", metadata);
        return jsonObject;
    }
}
