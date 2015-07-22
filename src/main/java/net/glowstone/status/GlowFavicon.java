package net.glowstone.status;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import org.apache.commons.lang3.Validate;
import org.spongepowered.api.status.Favicon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GlowFavicon implements Favicon {
    private final BufferedImage image;

    public GlowFavicon(BufferedImage image) {
        Validate.notNull(image, "Image must not be null");
        Validate.isTrue(image.getWidth() == 64, "Must be 64 pixels wide");
        Validate.isTrue(image.getHeight() == 64, "Must be 64 pixels high");

        this.image = image;
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }

    public String generateData() throws IOException {
        ByteBuf png = Unpooled.buffer();
        ImageIO.write(image, "PNG", new ByteBufOutputStream(png));
        ByteBuf encoded = Base64.encode(png);

        return "data:image/png;base64," + encoded.toString(Charsets.UTF_8);
    }
}
