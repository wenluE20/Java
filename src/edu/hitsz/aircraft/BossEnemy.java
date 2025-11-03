package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.prop.BombObserver;
import edu.hitsz.strategy.RingShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * Boss敌机
 * 实现环射弹道，同时发射20颗子弹，呈环形
 * 坠毁后随机掉落<=3个道具
 * 悬停于界面上方左右移动
 */
public class BossEnemy extends AbstractAircraft implements BombObserver {
    
    /**攻击方式 */
    
    /**
     * 子弹射击方向 (向上发射：-1，向下发射：1)
     */
    private int direction = 1;
    
    /**
     * 悬停区域的Y坐标范围（保持在屏幕上方）
     */
    private final int hoverYRange = 100;
    
    /**
     * 左右移动的边界
     */
    private final int leftBound = 50;
    private final int rightBound;

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.rightBound = Main.WINDOW_WIDTH - 50;
        // Boss敌机默认使用环射策略
        bulletContext.setStrategy(new RingShootStrategy());
    }

    @Override
    public void forward() {
        // 悬停于界面上方左右移动
        // 首先更新X坐标
        this.setLocationX(this.getLocationX() + this.getSpeedX());
        
        // 边界检测，到达边界后反向
        if (this.getLocationX() <= leftBound || this.getLocationX() >= rightBound) {
            // 改变X方向速度
            this.setSpeedX(-this.getSpeedX());
            // 确保不会出界
            if (this.getLocationX() <= leftBound) {
                this.setLocationX(leftBound);
            } else {
                this.setLocationX(rightBound);
            }
        }
        
        // Y坐标保持在悬停区域内，只做微调
        int newY = this.getLocationY() + this.getSpeedY();
        if (newY < 50 || newY > hoverYRange) {
            this.setSpeedY(-this.getSpeedY());
        } else {
            this.setLocationY(newY);
        }
    }

    @Override
    public List<BaseBullet> shoot() {
        // 使用策略模式发射子弹
        return bulletContext.shoot(this);
    }
    
    /**
     * 设置X方向速度
     */
    private void setSpeedX(int speedX) {
        this.speedX = speedX;
    }
    
    /**
     * 设置Y方向速度
     */
    private void setSpeedY(int speedY) {
        this.speedY = speedY;
    }
    
    /**
     * 设置X坐标
     */
    private void setLocationX(int locationX) {
        this.locationX = locationX;
    }
    
    /**
     * 设置Y坐标
     */
    private void setLocationY(int locationY) {
        this.locationY = locationY;
    }
    
    @Override
    public void update() {
        // 根据要求，Boss敌机不受炸弹影响
        System.out.println("Boss敌机不受炸弹影响");
        // 不执行任何操作
    }
}