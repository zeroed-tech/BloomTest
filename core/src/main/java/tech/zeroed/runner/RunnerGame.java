package tech.zeroed.runner;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.WaterDistortionEffect;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.VisUI.SkinScale;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class RunnerGame extends ApplicationAdapter {

    private VfxManager vfxManager;
    private WaterDistortionEffect waterEffect;
    private Bloom bloomEffect;


    private TextureRegion shapeTexture;

    SpriteBatch batch;

    @Override
    public void create () {
        VisUI.setSkipGdxVersionCheck(true);
        VisUI.load(SkinScale.X1);
        ShaderProgram.pedantic = false;

        vfxManager = new VfxManager(Pixmap.Format.RGBA8888);
        bloomEffect = new Bloom();
        vfxManager.addEffect(bloomEffect);

        batch = new SpriteBatch();
        createShapeTexture();
        //shapeTexture = new TextureRegion(new Texture(Gdx.files.internal("img.png")));
    }

    private void createShapeTexture(){
        // Create the initial shape buffer
        FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888, 300, 300, true);
        fbo.begin();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        PixmapIO.writePNG(Gdx.files.local("Test.png"), Pixmap.createFromFrameBuffer(0, 0, fbo.getWidth(), fbo.getHeight()));
        fbo.end();

        shapeTexture = new TextureRegion(fbo.getColorBufferTexture());
        shapeTexture.flip(false, true);
    }

    @Override
    public void resize (int width, int height) {
        //stage.getViewport().update(width, height, true);
        vfxManager.resize(width, height);
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //bloomEffect.setBloomIntensity(100);
        float x = Gdx.graphics.getWidth()/2f - shapeTexture.getRegionWidth() / 2f;
        float y = Gdx.graphics.getHeight()/2f  - shapeTexture.getRegionHeight() / 2f;

        vfxManager.update(Gdx.graphics.getDeltaTime());
        vfxManager.cleanUpBuffers();
        vfxManager.beginInputCapture();
        batch.begin();
        batch.draw(shapeTexture, x, y);
        batch.end();
        vfxManager.endInputCapture();
        vfxManager.applyEffects();
        vfxManager.renderToScreen();
    }

    @Override
    public void dispose () {
        VisUI.dispose();

        batch.dispose();
        bloomEffect.dispose();
        vfxManager.dispose();
    }
}
