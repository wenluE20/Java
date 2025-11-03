package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

/**
 * 加血道具工厂类
 * 负责创建加血道具实例
 */
public class BloodPropFactory implements PropFactory {
    
    @Override
    public AbstractProp createProp(int locationX, int locationY) {
        // 道具速度设置：向下移动
        int speedX = 0;
        int speedY = 15;
        
        // 获取英雄机单例实例
        HeroAircraft heroAircraft = HeroAircraft.getInstance();
        
        return new BloodProp(locationX, locationY, speedX, speedY, heroAircraft);
    }
}