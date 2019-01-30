package com.almasb.dodger;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.settings.GameSettings;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.Map;

import static com.almasb.fxgl.app.DSLKt.*;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
    //public class som nedarver fra gameapplication og som er nødvendig for at benytte fxgl komponenter.
public class DodgerApp extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {

    }

    @Override       // overskriver metoden "initInput" i superklassen som er importeret
    protected void initInput() {                  // med domain specific language DSLKt.
        onKey(KeyCode.A, "Left", () -> {//initInput håndterer Inputs, såsom bevægelsesdynamik for
            move(Direction.LEFT);                 //objekter/entities med lambda expressions
        });

        onKey(KeyCode.D, "Right", () -> {
            move(Direction.RIGHT);
        });

        onKey(KeyCode.S, "Down", () -> {
            move(Direction.DOWN);
        });

        onKey(KeyCode.W, "Up", () -> {
            move(Direction.UP);
        });
    }

    //metode som indlæser arraylist for bird entities og tilføjer fra BirdComponent klassen
    private void move(Direction direction) {
        for (Entity bird : getGameWorld().getEntitiesByType(EntityType.BIRD)) {
            BirdComponent comp = bird.getComponent(BirdComponent.class);

            comp.move(direction);

            direction = direction.next();
        }
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) { //metode med parametre (string og objekt)
        vars.put("lives", 3);                               // som tilskriver variablerne lives med 3,
        vars.put("score", 0);                               // og score med 0
    }

    //overskriver initGame(), og tilføjer listener til spillet som overvåger variablen "lives"
    // med if-statement, hvis denne når nul.
    @Override
    protected void initGame() { //listener som overvåger variablen "lives" med if-statement, for
        getGameState().<Integer>addListener("lives", (prev, now) -> {
            if (now == 0) {
                getDisplay().showConfirmationBox("Game Over. Continue?", yes -> {
                    if (yes) {
                        startNewGame();
                    } else {
                        exit();
                    }
                });
            }
        });

        getGameScene().setBackgroundColor(Color.BLACK);

        getGameWorld().addEntityFactory(new DodgerFactory());

        respawnEntities();
    }

    //metode som binder spilobjekterne til det såkaldte Stage, med en ramme på 40 pixels fra den ydre
    // ramme/stage, sådan at hele spilobjektet er synligt på skærmen.
    // spilobjektet(entity) "ball" indsættes/spawnes på "scenen" på midten af den horisontale akse,
    // og 30 pixels nede ad den vertikale akse, og spilobjekterne birds placeres med afstand fra
    // hinanden vha. et for-loop
    private void respawnEntities() {
        Entity bounds = Entities.makeScreenBounds(40);
        getGameWorld().addEntity(bounds);

        spawn("ball", getWidth() / 2, 30);

        for (int i = 0; i < 4; i++) {
            double x = random(i*150, i*150 + 150);
            double y = random(400, 600-60);

            spawn("bird", x, y);
        }
    }
    //nedenståede overskriver metoden initPhysics, og fjerner gravitationen, tilføjer kollisionscontrol
    // og afspiiler en lydfil når kollisioner forekommer. Endvidere decrementes variablen "Lives",
    // og alle spilobjekterne (entities) fjernes og genindlæses til at fremstå på de pixels der er
    // anført i metoden ovenfor - respawnEntities() {}
    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 0);

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BIRD, EntityType.BALL) {
            @Override
            protected void onCollisionBegin(Entity bird, Entity ball) {
                play("hit_wall.wav");

                inc("lives", -1);

                getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);

                respawnEntities();
            }
        });
    }

    @Override
    protected void initUI() {   //metoden tilføjer variablerne lives og score til spilskærmen (stage)
        Text textLives = addVarText(10, 20, "lives");
        textLives.setFont(getUIFactory().newFont(26));

        Text textScore = addVarText(700, 20, "score");
        textScore.setFont(getUIFactory().newFont(26));
    }

    @Override // metode som tilskriver variablen tpf med ++(increment) når inc(varName:"score"...) opdateres.
    protected void onUpdate(double tpf) {
        inc("score", +1);
    }

    public static void main(String[] args) { //starter applikationen
        launch(args);
    }
}
