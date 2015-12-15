package nl.fontys.scope.core.controller;

import com.badlogic.gdx.math.Vector3;

import java.security.SecureRandom;
import java.util.UUID;

import nl.fontys.scope.core.Arena;
import nl.fontys.scope.object.GameObject;

public class EnergyController implements GameObjectController {

    private static final float MAX_SPEED = 8f;

    private Vector3 velocity = new Vector3();

    private SecureRandom random = new SecureRandom(UUID.randomUUID().toString().getBytes());

    public EnergyController() {
        velocity.x = random.nextFloat() * MAX_SPEED;
        velocity.y = random.nextFloat() * MAX_SPEED;
        if (velocity.len() > MAX_SPEED) {
            velocity.setLength(MAX_SPEED);
        }
    }

    @Override
    public void update(GameObject object, float delta) {
        if (random.nextFloat() > 0.1f) {
            velocity.x += random.nextFloat() < 0.5f ? -random.nextFloat() : random.nextFloat();
        }
        if (random.nextFloat() > 0.1f) {
            velocity.y += random.nextFloat() < 0.5f ? -random.nextFloat() : random.nextFloat();
        }
        if (random.nextFloat() > 0.1f) {
            velocity.z += random.nextFloat() < 0.5f ? -random.nextFloat() : random.nextFloat();
        }
        if (velocity.len() > MAX_SPEED) {
            velocity.setLength(MAX_SPEED);
        }
        object.setVelocity(velocity);
    }
}
