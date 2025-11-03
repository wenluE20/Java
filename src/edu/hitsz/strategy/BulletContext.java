package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

/**
 * 子弹发射上下文类
 * 维护对具体策略的引用，提供设置策略和子弹属性的方法
 */
public class BulletContext {
    private BulletStrategy strategy;
    private int bulletPower;
    private int bulletNum;
    private int bulletDirection;
    private int bulletSpeed;

    /**
     * 构造函数，初始化策略
     * @param strategy 初始的子弹发射策略
     */
    public BulletContext(BulletStrategy strategy) {
        this.strategy = strategy;
        this.bulletPower = 30; // 默认火力值
        this.bulletNum = 1;   // 默认子弹数量
        this.bulletDirection = -1; // 默认向上发射
        this.bulletSpeed = 5; // 默认子弹速度
    }

    /**
     * 设置子弹发射策略
     * @param strategy 新的子弹发射策略
     */
    public void setStrategy(BulletStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * 设置子弹火力值
     * @param power 子弹伤害
     */
    public void setBulletPower(int power) {
        this.bulletPower = power;
    }

    /**
     * 设置子弹发射数量
     * @param num 一次发射的子弹数量
     */
    public void setBulletNum(int num) {
        this.bulletNum = num;
    }

    /**
     * 设置子弹发射方向
     * @param direction 方向（向上：-1，向下：1）
     */
    public void setBulletDirection(int direction) {
        this.bulletDirection = direction;
    }

    /**
     * 设置子弹速度
     * @param speed 子弹飞行速度
     */
    public void setBulletSpeed(int speed) {
        this.bulletSpeed = speed;
    }

    /**
     * 执行子弹发射
     * @param aircraft 发射子弹的飞机
     * @return 发射出的子弹列表
     */
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        return strategy.execute(aircraft);
    }

    /**
     * 获取当前火力值
     * @return 子弹伤害
     */
    public int getBulletPower() {
        return bulletPower;
    }

    /**
     * 获取当前子弹数量
     * @return 一次发射的子弹数量
     */
    public int getBulletNum() {
        return bulletNum;
    }

    /**
     * 获取当前子弹发射方向
     * @return 方向（向上：-1，向下：1）
     */
    public int getBulletDirection() {
        return bulletDirection;
    }

    /**
     * 获取当前子弹速度
     * @return 子弹飞行速度
     */
    public int getBulletSpeed() {
        return bulletSpeed;
    }
}