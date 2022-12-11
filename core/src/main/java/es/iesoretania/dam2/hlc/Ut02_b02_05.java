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
	TiledMap mapa;
	OrthographicCamera camera;
	OrthogonalTiledMapRenderer mapRenderer;
	
	private int mapWidthInPixels;
	private int mapHeightInPixels;
	private float offsetX, offsetY;

	private BitmapFont coordenadas;
	private Texture imagen;

	private TextureRegion heroeDelante, heroeDetras, heroeDerecha, heroeIzquierda;

	Sprite heroe;
	SpriteBatch batch;

	@Override
	public void create() {
		//Creamos la textura y el texto
		batch = new SpriteBatch();
		imagen = new Texture(Gdx.files.internal("UT02_B02_Heroe.png"));
		coordenadas = new BitmapFont();

		//Diferentes texturas del heroe
		heroeDelante = new TextureRegion(imagen, 24, 64, 24, 32);
		heroeDerecha = new TextureRegion(imagen, 24, 32, 24, 32);
		heroeIzquierda = new TextureRegion(imagen, 24, 96, 24, 32);
		heroeDetras = new TextureRegion(imagen, 24, 0, 24, 32);

		//Creamos el sprite
		heroe = new Sprite();
		heroe.setPosition(320,240);
		heroe.setSize(24,32);

		//Creamos el mapa
		mapa = new TmxMapLoader().load("tiled.tmx");
		MapProperties properties = mapa.getProperties();
		int tileWidth = properties.get("tilewidth", Integer.class);
		int tileHeight = properties.get("tileheight", Integer.class);
		int mapWidthInTiles = properties.get("width", Integer.class);
		int mapHeightInTiles = properties.get("height", Integer.class);
		mapWidthInPixels = mapWidthInTiles * tileWidth;
		mapHeightInPixels = mapHeightInTiles * tileHeight;
		mapRenderer = new OrthogonalTiledMapRenderer(mapa);
		camera = new OrthographicCamera();
		heroe.setRegion(heroeDelante);
		offsetX = 0;
		offsetY = 0;
	}
	@Override
	public void render() {

		//Indicamos al heroe que tiene que hacer cuando pulsamos alguna de esa tecla
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			heroe.translateX(-100 * Gdx.graphics.getDeltaTime());
			heroe.setRegion(heroeIzquierda);
		}else{
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				heroe.translateX(100 * Gdx.graphics.getDeltaTime());
				heroe.setRegion(heroeDerecha);
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			heroe.translateY(100 * Gdx.graphics.getDeltaTime());
			heroe.setRegion(heroeDetras);
		}else{
			if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				heroe.translateY(-100 * Gdx.graphics.getDeltaTime());
				heroe.setRegion(heroeDelante);
			}
		}

		//Indicamos al heroe que no se salga de los limites de la pantalla en el eje x y en el eje y
		if(heroe.getX() > camera.viewportWidth - 100 && offsetX < mapWidthInPixels - camera.viewportWidth){
			offsetX += 100 * Gdx.graphics.getDeltaTime();
			heroe.setX(camera.viewportWidth - 100);
		}
		if(heroe.getX() < 100 && offsetX > 0){
			offsetX -= 100 * Gdx.graphics.getDeltaTime();
			heroe.setX(100);
		}
		if(heroe.getX() < 0) {
			heroe.setX(0);
		}
		if(heroe.getX() > camera.viewportWidth - heroe.getWidth()) {
			heroe.setX(camera.viewportWidth - heroe.getWidth());
		}

		if(heroe.getY() > camera.viewportHeight - 100 && offsetY < 0){
			offsetY += 100 * Gdx.graphics.getDeltaTime();
			heroe.setY(camera.viewportHeight - 100);
		}
		if(heroe.getY() < 100 && offsetY > -mapHeightInPixels + camera.viewportHeight){
			offsetY -= 100 * Gdx.graphics.getDeltaTime();
			heroe.setY(100);
		}
		if(heroe.getY() < 0) {
			heroe.setY(0);
		}
		if (heroe.getY() > camera.viewportHeight - heroe.getRegionHeight()) {
			heroe.setY(camera.viewportHeight - heroe.getHeight());
		}

		//Indicamos a la camara que no se salga de la pantalla
		if (offsetX < 0) {
			offsetX = 0;
		}
		if (offsetY > 0) {
			offsetY = 0;
		}
		if (offsetX > mapWidthInPixels - camera.viewportWidth) {
			offsetX = mapWidthInPixels - camera.viewportWidth;
		}
		if (offsetY < -mapHeightInPixels + camera.viewportHeight) {
			offsetY = -mapHeightInPixels + camera.viewportHeight;
		}

		camera.position.x = camera.viewportWidth / 2 + offsetX;
		camera.position.y = mapHeightInPixels - camera.viewportHeight / 2 + offsetY;

		//Diferentes capas que tiene el mapa
		int[] capasFondo = {0, 1, 2};
		int[] capasAltas = {3};

		//Se dibujan las diferentes elementos
		camera.update();
		mapRenderer.setView(camera);
		mapRenderer.render(capasFondo);

		batch.begin();
		coordenadas.draw(batch,"(" + offsetX + " , " + offsetY + ")",10,15);
		heroe.draw(batch);
		batch.end();
		mapRenderer.render(capasAltas);
	}

	//Este método se usa para informar del tamaño de la pantalla
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		camera.setToOrtho(false, width, height);
		camera.position.x = camera.viewportWidth / 2 + offsetX;
		camera.position.y = mapHeightInPixels - camera.viewportHeight / 2 + offsetY;
		camera.update();
	}

	//Este método se llama paara liberar los recursos utilizados
	@Override
	public void dispose() {
		batch.dispose();
		imagen.dispose();
		mapa.dispose();
		coordenadas.dispose();
	}
}