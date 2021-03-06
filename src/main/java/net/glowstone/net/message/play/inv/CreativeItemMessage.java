package net.glowstone.net.message.play.inv;

import com.flowpowered.networking.Message;
import lombok.Data;
import org.spongepowered.api.item.inventory.ItemStack;

@Data
public final class CreativeItemMessage implements Message {

    private final int slot;
    private final ItemStack item;

}
