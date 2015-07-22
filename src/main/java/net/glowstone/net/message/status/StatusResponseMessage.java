package net.glowstone.net.message.status;

import com.flowpowered.networking.Message;
import com.google.gson.JsonObject;
import lombok.Data;

@Data
public final class StatusResponseMessage implements Message {

    private final String json;

    public StatusResponseMessage(JsonObject json) {
        this.json = json.toString();
    }

}
