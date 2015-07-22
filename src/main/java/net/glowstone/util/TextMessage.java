package net.glowstone.util;

import com.google.gson.JsonObject;

/**
 * Simple container for chat message structures until more advanced chat
 * formatting is implemented.
 */
public final class TextMessage {
    private final JsonObject json;

    public TextMessage(JsonObject json) {
        this.json = json;
    }

    public String encode() {
        return json.toString();
    }
}
