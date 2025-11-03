package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.application.ImageManager;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.prop.BombObserver;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 精英敌机
 * 可以发射子弹
 * 坠毁后随机产生道具
 *
 * @author hitsz
 */
public class EliteEnemy extends AbstractAircraft implements BombObserver {
    
    /**攻击方式 */
    
    /**
     * 子弹射击方向 (向上发射：-1，向下发射：1)
     */
    private int direction = 1;

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    @Override
    public List<BaseBullet> shoot() {
        // 使用策略模式发射子弹
        return bulletContext.shoot(this);
    }

    @Override
    public void update() {
        // 炸弹爆炸时精英敌机被销毁
        vanish();
        System.out.println("精英敌机被炸弹销毁");
    }

}