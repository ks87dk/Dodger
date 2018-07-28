package com.almasb.dodger;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.extra.entity.components.KeepOnScreenComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.almasb.fxgl.texture.Texture;
import javafx.util.Duration;

import static com.almasb.fxgl.app.DSLKt.*;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class DodgerFactory implements EntityFactory {

    @Spawns("ball")
    public Entity newBall(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().restitution(1.0f));
        physics.setOnPhysicsInitialized(() -> physics.setLinearVelocity(350, 350));

        return Entities.builder()
                .type(EntityType.BALL)
                .from(data)
                .viewFromNodeWithBBox(texture("ball.png", 40, 40))
                .with(physics)
                .with(new BallComponent())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("bird")
    public Entity newPlayer(SpawnData data) {
        Texture t = texture("bird.png").multiplyColor(FXGLMath.randomColor());

        Entity bird = Entities.builder()
                .type(EntityType.BIRD)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(70, 60)))
                //.viewFromAnimatedTexture("bird.png", 2, Duration.seconds(0.5))
                .with(new BirdComponent())
                .with(new KeepOnScreenComponent(true, true))
                .with(new CollidableComponent(true))
                .build();

        bird.getViewComponent().setAnimatedTexture(t.toAnimatedTexture(2, Duration.seconds(0.5)), true, false);

        return bird;
    }
}
