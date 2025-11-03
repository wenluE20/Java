package edu.hitsz.aircraft;

import edu.hitsz.application.Main;

/**
 * Boss敌机工厂类
 * 负责创建Boss敌机实例
 */
public class BossEnemyFactory implements EnemyFactory {
    
    @Override
    public AbstractAircraft createEnemyAircraft() {
        // Boss敌机通常从屏幕上方中央出现
        int locationX = Main.WINDOW_WIDTH / 2;
        // Y坐标固定在屏幕上方，不会太高
        int locationY = 80;
        // 速度设置：左右移动，Y方向缓慢移动
        int speedX = 3; // 左右移动速度
        int speedY = 1; // 上下微调速度
        // 生命值（Boss敌机生命值）
        int hp = 50;
        
        return new BossEnemy(locationX, locationY, speedX, speedY, hp);
    }
}