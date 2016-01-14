package nl.fontys.scope.object;

import com.badlogic.gdx.math.Vector3;

import nl.fontys.scope.assets.Assets;
import nl.fontys.scope.core.World;
import nl.fontys.scope.core.controller.LightingController;
import nl.fontys.scope.core.controller.ParticleEffectController;
import nl.fontys.scope.core.controller.RotationController;
import nl.fontys.scope.util.Colors;

/**
 * Creates game objects
 */
public class GameObjectFactory {

    private World world;

    public GameObjectFactory(World world) {
        this.world = world;
    }

    public GameObject createShip(float x, float y, float z) {
        GameObject object = world.createGameObject();
        object.setType(GameObjectType.SHIP);
        object.setCollisionScale(0f);
        object.setScale(1.65f);
        object.getColor().set(0.75f, 0.75f, 0.75f, 1f);
        ParticleEffectController c = new ParticleEffectController(Assets.ParticleEffects.POWER);
        final float X_OFFSET = -3.2f;
        c.setOffset(X_OFFSET, 0f, 0f);
        LightingController lc = new LightingController(world.getLightingManager());
        lc.setStrength(7);
        lc.setOffset(X_OFFSET, 0f, 0f);
        world.addController(object, c);
        world.addController(object, lc);
        return object;
    }

    public GameObject createShot(GameObject ship) {
        GameObject object = world.createGameObject();
        Vector3 pos = new Vector3(ship.getPosition()).add(new Vector3(20f, 0f, 0f).mul(ship.getOrientation()));
        object.setExternalId(ship.getId());
        object.setPosition(pos.x, pos.y, pos.z);
        object.setVelocity(new Vector3(250f, 0f, 0f).mul(ship.getOrientation()));
        object.setOrientation(ship.getOrientation().x, ship.getOrientation().y, ship.getOrientation().z, ship.getOrientation().w);
        object.setScale(14f);
        object.setType(GameObjectType.SHOT);
        object.getColor().set(Colors.SECONDARY);
        object.setPhysics(false);
        return object;
    }

    public GameObject createPlanet(float scale) {
        GameObject object = world.createGameObject();
        object.setType(GameObjectType.PLANET);
        object.getColor().set(Colors.lighten(Colors.BACKGROUND, 2f));
        object.setScale(scale);
        return object;
    }

    public GameObject createSphere(float scale) {
        GameObject sphere = createPlanet(scale);
        sphere.setType(GameObjectType.SPHERE);
        sphere.setCollisionScale(0f);
        world.addController(sphere, new ParticleEffectController(Assets.ParticleEffects.SPHERE));
        return sphere;
    }

    public GameObject createEnergy(float x, float y, float z) {
        GameObject object = world.createGameObject();
        object.getColor().set(Colors.PRIMARY);
        object.setType(GameObjectType.ENERGY);
        object.setPosition(x, y, z);
        object.setCollisionScale(2f);
        object.setScale(2.5f);
        object.setPhysics(false);
        world.addController(object, new ParticleEffectController(Assets.ParticleEffects.ENERGY));
        return object;
    }
}
