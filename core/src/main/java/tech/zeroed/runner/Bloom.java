package tech.zeroed.runner;

import com.crashinvaders.vfx.VfxRenderContext;
import com.crashinvaders.vfx.effects.ChainVfxEffect;
import com.crashinvaders.vfx.effects.CompositeVfxEffect;
import com.crashinvaders.vfx.effects.util.CopyEffect;
import com.crashinvaders.vfx.framebuffer.VfxFrameBuffer;
import com.crashinvaders.vfx.framebuffer.VfxPingPongWrapper;

public class Bloom extends CompositeVfxEffect implements ChainVfxEffect {
    private BloomGenerator generator;
    private ColorBooster colorBooster;
    private ToneMapper toneMapper;
    private final CopyEffect copy;
    public Bloom() {
        copy = register(new CopyEffect());
        colorBooster = register(new ColorBooster());
        generator = register(new BloomGenerator());
        toneMapper = register(new ToneMapper());
    }

    @Override
    public void render(VfxRenderContext context, VfxPingPongWrapper buffers) {
        // Apply color changes to the texture
        colorBooster.render(context, buffers);
        buffers.swap();

        // Store a copy of the current buffer
        VfxFrameBuffer origSrc = context.getBufferPool().obtain();
        copy.render(context, buffers.getSrcBuffer(), origSrc);

        // Generate bloom texture
        generator.render(context, buffers);
        buffers.swap();

        // Apply tone mapping between the bloom texture and the saved buffer
        toneMapper.render(context, buffers, buffers.getSrcBuffer().getTexture(), origSrc.getTexture());
    }
}
