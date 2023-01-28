package tech.zeroed.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.crashinvaders.vfx.VfxRenderContext;
import com.crashinvaders.vfx.effects.ChainVfxEffect;
import com.crashinvaders.vfx.effects.ShaderVfxEffect;
import com.crashinvaders.vfx.framebuffer.VfxPingPongWrapper;
import com.crashinvaders.vfx.gl.VfxGLUtils;

public class ToneMapper extends ShaderVfxEffect implements ChainVfxEffect {
    private static final String U_TEXTURE0 = "u_texture0";
    private static final String U_TEXTURE1 = "u_texture1";
    private static final String U_RESOLUTION = "u_resolution";

    private int width, height;
    public ToneMapper() {
        super(VfxGLUtils.compileShader(Gdx.files.classpath("gdxvfx/shaders/screenspace.vert"), Gdx.files.internal("Shaders/ToneMapper.frag")));
        rebind();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.width = width;
        this.height = height;
        setUniform(U_RESOLUTION, new Vector2(width, height));
        rebind();
    }

    @Override
    public void rebind() {
        super.rebind();
        program.bind();
        program.setUniformi(U_TEXTURE0, TEXTURE_HANDLE0);
        program.setUniformi(U_TEXTURE1, TEXTURE_HANDLE1);
        program.setUniformf(U_RESOLUTION, width, height);
    }

    @Override
    public void render(VfxRenderContext context, VfxPingPongWrapper buffers) {
        renderShader(context, buffers.getDstBuffer());
    }

    public void render(VfxRenderContext context, VfxPingPongWrapper buffers, Texture bloom, Texture image) {
        image.bind(0);
        bloom.bind(1);
        render(context, buffers);
    }
}
