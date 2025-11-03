package edu.hitsz.prop;

import edu.hitsz.application.Game;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.DirectShootStrategy;
import edu.hitsz.strategy.ScatterShootStrategy;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 火力道具
 * 碰撞后增强英雄机的火力，将发射策略切换为散射策略
 * 效果持续一段时间后恢复
 * 
 * @author hitsz
 */
public class FireProp extends AbstractProp {
    
    // 火力增强效果持续时间（毫秒）
    private static final int FIRE_ENHANCE_DURATION = 5000;
    
    public FireProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }
    
    @Override
    public void active() {
        System.out.println("FireSupply active!");
        // 获取游戏中的英雄机实例
        HeroAircraft heroAircraft = Game.getInstance().getHeroAircraft();
        // 保存当前的火力值，以便恢复
        final int originalPower = heroAircraft.getBulletContext().getBulletPower();
        
        // 将英雄机的发射策略切换为散射策略
        heroAircraft.getBulletContext().setStrategy(new ScatterShootStrategy());
        // 增强子弹火力
        heroAircraft.getBulletContext().setBulletPower(40);
        
        // 设置定时器，一段时间后恢复默认状态
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Fire enhancement effect ends!");
                // 恢复默认直射策略
                heroAircraft.getBulletContext().setStrategy(new DirectShootStrategy());
                // 恢复原始火力
                heroAircraft.getBulletContext().setBulletPower(originalPower);
            }
        }, FIRE_ENHANCE_DURATION);
    }
}