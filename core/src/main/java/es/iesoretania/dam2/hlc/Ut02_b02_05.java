package es.iesoretania.dam2.hlc;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Ut02_b02_05 extends ApplicationAdapter {
	TiledMap map;
	OrthographicCamera camera;
	OrthogonalTiledMapRenderer mapRenderer;
	private int mapWidthInPixels;
	private int mapHeightInPixels;
	private float offsetX, offsetY;

	private BitmapFont texto;
	private Texture image;

	private TextureRegion parado_frente, parado_derecha, parado_izquierda,parado_espalda;
	private TextureRegion espalda1, espalda2;
	private TextureRegion derecha1, derecha2;
	private TextureRegion izquierda1, izquierda2;
	private TextureRegion frente1, frente2;
	boolean pasos;

	Sprite heroe;
	SpriteBatch batch;

	@Override
	public void create() {
		//HEROE
		batch = new SpriteBatch();
		image = new Texture("UT02_B02_Heroe.png");
		texto = new BitmapFont();

		parado_frente = new TextureRegion(image, 24, 64, 24, 32);
		parado_derecha = new TextureRegion(image, 24, 32, 24, 32);
		parado_izquierda = new TextureRegion(image, 24, 96, 24, 32);
		parado_espalda = new TextureRegion(image, 24, 0, 24, 32);

		espalda1 = new TextureRegion(image, 0, 0, 24, 32);
		espalda2 = new TextureRegion(image, 0, 45, 24, 32);

		derecha1 = new TextureRegion(image, 32, 0, 24, 32);
		derecha2 = new TextureRegion(image, 32, 45, 24, 32);

		izquierda1 = new TextureRegion(image, 96, 0, 24, 32);
		izquierda2 = new TextureRegion(image, 96, 45, 24, 32);

		frente1 = new TextureRegion(image, 64, 0, 24, 32);
		frente2 = new TextureRegion(image, 64, 45, 24, 32);

		heroe = new Sprite();
		//heroe.setX(320 - parado_frente.getRegionWidth() / 2.0f);
		//heroe.setY(240 - parado_frente.getRegionHeight() / 2.0f);
		heroe.setPosition(320,240);
		heroe.setSize(24,32);

		//MAPA
		map = new TmxMapLoader().load("mapa_oretania.tmx");
		MapProperties properties = map.getProperties();
		int tileWidth = properties.get("tilewidth", Integer.class);
		int tileHeight = properties.get("tileheight", Integer.class);
		int mapWidthInTiles = properties.get("width", Integer.class);
		int mapHeightInTiles = properties.get("height", Integer.class);
		mapWidthInPixels = mapWidthInTiles * tileWidth;
		mapHeightInPixels = mapHeightInTiles * tileHeight;
		mapRenderer = new OrthogonalTiledMapRenderer(map);
		camera = new OrthographicCamera();
		heroe.setRegion(parado_izquierda);
		offsetX = 0;
		offsetY = 0;
	}
	@Override
	public void render() {
		/*if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) offsetX -= 400 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) offsetX += 400 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) offsetY += 400 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) offsetY -= 400 * Gdx.graphics.getDeltaTime();*/

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			heroe.translateX(-100 * Gdx.graphics.getDeltaTime());
			heroe.setRegion(parado_izquierda);
		}else{
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				heroe.translateX(100 * Gdx.graphics.getDeltaTime());
				heroe.setRegion(parado_derecha);
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			heroe.translateY(100 * Gdx.graphics.getDeltaTime());
			heroe.setRegion(parado_espalda);
		}else{
			if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				heroe.translateY(-100 * Gdx.graphics.getDeltaTime());
				heroe.setRegion(parado_frente);
			}
		}

		if(heroe.getX() > camera.viewportWidth - 100 && offsetX < mapWidthInPixels - camera.viewportWidth){
			offsetX += 100 * Gdx.graphics.getDeltaTime();
			heroe.setX(camera.viewportWidth - 100);
		}
		if(heroe.getX() < 100 && offsetX > 0){
			offsetX -= 100 * Gdx.graphics.getDeltaTime();
			heroe.setX(100);
		}
		if(heroe.getX() < 0) heroe.setX(0);
		if(heroe.getX() > camera.viewportWidth - heroe.getWidth()) heroe.setX(camera.viewportWidth - heroe.getWidth());

		if(heroe.getY() > camera.viewportHeight - 100 && offsetY < 0){
			offsetY += 100 * Gdx.graphics.getDeltaTime();
			heroe.setY(camera.viewportHeight - 100);
		}
		if(heroe.getY() < 100 && offsetY > -mapHeightInPixels + camera.viewportHeight){
			offsetY -= 100 * Gdx.graphics.getDeltaTime();
			heroe.setY(100);
		}
		if(heroe.getY() < 0) heroe.setY(0);
		if (heroe.getY() > camera.viewportHeight - heroe.getRegionHeight()) heroe.setY(camera.viewportHeight - heroe.getHeight());

		if (offsetX < 0) offsetX = 0;
		if (offsetY > 0) offsetY = 0;
		if (offsetX > mapWidthInPixels - camera.viewportWidth) offsetX = mapWidthInPixels - camera.viewportWidth;
		if (offsetY < -mapHeightInPixels + camera.viewportHeight) offsetY = -mapHeightInPixels + camera.viewportHeight;

		camera.position.x = camera.viewportWidth / 2 + offsetX;
		camera.position.y = mapHeightInPixels - camera.viewportHeight / 2 + offsetY;

		int[] capasFondo = {0, 1, 2};
		int[] capasAltas = {3};

		camera.update();
		mapRenderer.setView(camera);
		mapRenderer.render(capasFondo);

		batch.begin();
		//batch.setProjectionMatrix(camera.projection);
		texto.draw(batch,"(" + offsetX + " , " + offsetY + ")",10,15);
		heroe.draw(batch);
		batch.end();
	}
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		camera.setToOrtho(false, width, height);
		camera.position.x = camera.viewportWidth / 2 + offsetX;
		camera.position.y = mapHeightInPixels - camera.viewportHeight / 2 + offsetY;
		camera.update();
	}
	@Override
	public void dispose() {
		batch.dispose();
		image.dispose();
		map.dispose();
		texto.dispose();
	}
}