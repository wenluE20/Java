package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import java.util.Random;

/**
 * 普通敌机工厂类
 * 负责创建普通敌机实例
 */
public class MobEnemyFactory implements EnemyFactory {
    private final Random random = new Random();
    
    @Override
    public AbstractAircraft createEnemyAircraft() {
        // 随机生成X坐标，但确保敌机在屏幕范围内
        int locationX = random.nextInt(Main.WINDOW_WIDTH);
        // Y坐标从屏幕顶部出现
        int locationY = 0;
        // 速度设置：X方向随机，Y方向向下
        int speedX = random.nextInt(10) - 5; // -5到4之间的随机速度
        int speedY = 10;
        // 生命值
        int hp = 30;
        
        return new MobEnemy(locationX, locationY, speedX, speedY, hp);
    }
}