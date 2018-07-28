package com.almasb.dodger;

import com.almasb.fxgl.app.DSLKt;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.extra.entity.components.ExpireCleanComponent;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.time.LocalTimer;
import javafx.util.Duration;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class BallComponent extends Component {

    private LocalTimer timer = FXGL.newLocalTimer();

    private PhysicsComponent physics;

    @Override
    public void onUpdate(double tpf) {

        if (timer.elapsed(Duration.seconds(2))) {
            physics.setLinearVelocity(FXGLMath.randomPoint2D().multiply(350));

            ParticleEmitter emitter = ParticleEmitters.newExplosionEmitter(100);

            Entity e = new Entity();
            e.setPosition(entity.getCenter());
            e.addComponent(new ParticleComponent(emitter));
            e.addComponent(new ExpireCleanComponent(Duration.seconds(1)));

            FXGL.getApp().getGameWorld().addEntity(e);

            timer.capture();
        }
    }
}
