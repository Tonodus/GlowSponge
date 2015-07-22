package net.glowstone.world;

import org.spongepowered.api.world.weather.Weather;

public class GlowWeather implements Weather {
    private final String id, name;

    public GlowWeather(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
