package net.glowstone.text;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.ClickAction;
import org.spongepowered.api.text.action.HoverAction;
import org.spongepowered.api.text.action.ShiftClickAction;
import org.spongepowered.api.text.format.TextColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TextUtils {
    /**
     * Strips all formattings from the given text
     * @param text
     * @return
     */
    public static String strip(Text text) {
        StringBuilder builder = new StringBuilder();
        strip(text, builder);
        return builder.toString();
    }

    private static void strip(Text text, StringBuilder builder) {
        if (text instanceof Text.Literal) {
            Text.Literal literal = (Text.Literal) text;
            builder.append(literal.getContent());
        }
        for (Text child : text.getChildren()) {
            strip(child, builder);
        }
    }

    public static Iterable<Text.Literal> toLiterals(Text text) {
        Collection<Text.Literal> literals = new ArrayList<>();
        toLiterals(text, literals);
        return literals;
    }

    private static void toLiterals(Text text, Collection<Text.Literal> literals) {
        if (text instanceof Text.Literal) {
            literals.add((Text.Literal) text);
        }
        for (Text child : text.getChildren()) {
            toLiterals(child, literals);
        }
    }

    public static Text fromJSONStr(String json) throws JsonSyntaxException {
        JsonParser parser = new JsonParser();
        JsonObject element = (JsonObject) parser.parse(json);
        return fromJSON(element);
    }

    public static Text fromJSON(JsonObject json) {
        Text text = null;
        TextColor color = null; //json.get("color")

        return text;
    }

    public static JsonObject toJSON(Text text) {
        JsonObject jsonObject = new JsonObject();

        if (text instanceof Text.Literal) {
            jsonObject.addProperty("text", ((Text.Literal) text).getContent());
        }

        // Style
        jsonObject.addProperty("color", text.getColor().getId());
        if (text.getStyle().isBold().or(false)) {
            jsonObject.addProperty("bold", true);
        }
        if (text.getStyle().isItalic().or(false)) {
            jsonObject.addProperty("italic", true);
        }
        if (text.getStyle().isObfuscated().or(false)) {
            jsonObject.addProperty("obfuscated", true);
        }
        if (text.getStyle().hasUnderline().or(false)) {
            jsonObject.addProperty("underlined", true);
        }
        if (text.getStyle().hasStrikethrough().or(false)) {
            jsonObject.addProperty("strikethrough", true);
        }

        //Actions
        if (text.getHoverAction().isPresent()) {
            HoverAction action = text.getHoverAction().get();
            JsonObject hoverEvent = new JsonObject();
            if (action instanceof HoverAction.ShowText) {
                hoverEvent.addProperty("action", "show_text");
                hoverEvent.addProperty("text", toJSON((Text) action.getResult()).toString());
            }
            jsonObject.add("hoverEvent", hoverEvent);
        }
        if (text.getClickAction().isPresent()) {
            ClickAction action = text.getClickAction().get();
            JsonObject clickEvent = new JsonObject();
            jsonObject.add("clickEvent", clickEvent);
        }
        if (text.getShiftClickAction().isPresent()) {
            ShiftClickAction action = text.getShiftClickAction().get();
            if (action instanceof ShiftClickAction) {
                jsonObject.addProperty("insertion", toJSON((Text) action.getResult()).toString());
            }
        }

        //Extra
        List<Text> children = text.getChildren();
        if (children.size() > 0) {
            JsonArray extra = new JsonArray();
            for (Text child : children) {
                extra.add(toJSON(child));
            }
            jsonObject.add("extra", extra);
        }


        return jsonObject;
    }
}
