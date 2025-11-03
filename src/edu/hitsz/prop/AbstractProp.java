package edu.hitsz.prop;

import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;

/**
 * 道具抽象类
 * 
 * @author hitsz
 */
public abstract class AbstractProp extends AbstractFlyingObject {
    
    public AbstractProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }
    
    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }
    
    /**
     * 道具生效方法
     * 由具体的道具类实现
     */
    public abstract void active();

}