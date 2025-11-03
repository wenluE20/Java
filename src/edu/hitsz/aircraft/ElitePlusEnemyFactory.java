package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import java.util.Random;

/**
 * 超级精英敌机工厂类
 * 负责创建超级精英敌机实例
 */
public class ElitePlusEnemyFactory implements EnemyFactory {
    private final Random random = new Random();
    
    @Override
    public AbstractAircraft createEnemyAircraft() {
        // 随机生成X坐标，但确保敌机在屏幕范围内
        int locationX = random.nextInt(Main.WINDOW_WIDTH);
        // Y坐标从屏幕顶部出现
        int locationY = 0;
        // 速度设置：X方向随机，Y方向向下（比精英敌机稍慢，但更灵活）
        int speedX = random.nextInt(10) - 5; // -5到4之间的随机速度
        int speedY = 6;
        // 生命值（超级精英敌机生命值更高）
        int hp = 100;
        
        return new ElitePlusEnemy(locationX, locationY, speedX, speedY, hp);
    }
}