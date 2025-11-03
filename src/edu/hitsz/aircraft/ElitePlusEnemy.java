package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.prop.BombObserver;
import edu.hitsz.strategy.ScatterShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * 超级精英敌机
 * 实现散射弹道，同时发射3颗子弹，呈扇形
 * 坠毁后随机掉落<=1个道具
 */
public class ElitePlusEnemy extends AbstractAircraft implements BombObserver {
    
    /**攻击方式 */
    
    /**
     * 子弹射击方向 (向上发射：-1，向下发射：1)
     */
    private int direction = 1;

    public ElitePlusEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        // 超级精英敌机默认使用散射策略
        bulletContext.setStrategy(new ScatterShootStrategy());
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT) {
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
        // 炸弹爆炸时超级精英敌机血量减少（减少最大生命值的一半）
        int damage = getMaxHp() / 2;
        decreaseHp(damage);
        System.out.println("超级精英敌机被炸弹击中，血量减少 " + damage);
    }
}