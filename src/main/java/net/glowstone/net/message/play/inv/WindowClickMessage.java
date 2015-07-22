package net.glowstone.net.message.play.inv;

import com.flowpowered.networking.Message;
import lombok.Data;
import org.spongepowered.api.item.inventory.ItemStack;

@Data
public final class WindowClickMessage implements Message {

    private final int id, slot, button, transaction, mode;
    private final ItemStack item;

}
