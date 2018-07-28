package com.almasb.dodger;

import com.almasb.fxgl.entity.component.Component;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class BirdComponent extends Component {

    public void move(Direction direction) {
        entity.translate(direction.vector.multiply(5));
    }
}
