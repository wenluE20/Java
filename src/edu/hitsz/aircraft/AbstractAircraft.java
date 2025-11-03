package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.strategy.BulletContext;
import edu.hitsz.strategy.DirectShootStrategy;

import java.util.List;

/**
 * 所有种类飞机的抽象父类：
 * 敌机（BOSS, ELITE, MOB），英雄飞机
 *
 * @author hitsz
 */
public abstract class AbstractAircraft extends AbstractFlyingObject {
    /**
     * 生命值
     */
    protected int maxHp;
    protected int hp;

    /**
     * 子弹发射上下文
     */
    protected BulletContext bulletContext;

    public AbstractAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp;
        // 初始化子弹发射上下文，默认为直射策略
        this.bulletContext = new BulletContext(new DirectShootStrategy());
    }

    public void decreaseHp(int decrease){
        hp -= decrease;
        if(hp <= 0){
            hp=0;
            vanish();
        }
    }

    public void increaseHp(int increase) {
        hp += increase;
        if (hp > maxHp) {
            hp = maxHp;
        }
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }
    
    /**
     * 获取X方向速度
     * @return X方向速度
     */
    public int getSpeedX() {
        return speedX;
    }
    
    /**
     * 获取Y方向速度
     * @return Y方向速度
     */
    public int getSpeedY() {
        return speedY;
    }
    
    /**
     * 获取X坐标
     * @return X坐标
     */
    public int getLocationX() {
        return locationX;
    }
    
    /**
     * 获取Y坐标
     * @return Y坐标
     */
    public int getLocationY() {
        return locationY;
    }

    public void setHp(int hp) {
        this.hp = hp;
        if (this.hp > maxHp) {
            this.hp = maxHp;
        }
        if (this.hp <= 0) {
            this.hp = 0;
            vanish();
        }
    }

    /**
     * 设置子弹发射策略
     * @param bulletContext 新的子弹发射上下文
     */
    public void setBulletContext(BulletContext bulletContext) {
        this.bulletContext = bulletContext;
    }

    /**
     * 获取子弹发射上下文
     * @return 子弹发射上下文
     */
    public BulletContext getBulletContext() {
        return bulletContext;
    }


    /**
     * 飞机射击方法，可射击对象必须实现
     * @return
     *  可射击对象需实现，返回子弹
     *  非可射击对象空实现，返回null
     */
    public abstract List<BaseBullet> shoot();

}


