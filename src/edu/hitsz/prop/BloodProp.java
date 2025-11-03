package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

/**
 * 加血道具
 * 碰撞后为英雄机恢复生命值
 * 
 * @author hitsz
 */
public class BloodProp extends AbstractProp {
    
    private int recoverHp = 30; // 恢复的生命值
    private HeroAircraft heroAircraft; // 英雄机引用
    
    public BloodProp(int locationX, int locationY, int speedX, int speedY, HeroAircraft heroAircraft) {
        super(locationX, locationY, speedX, speedY);
        this.heroAircraft = heroAircraft;
    }
    
    @Override
    public void active() {
        // 为英雄机恢复生命值，但不超过初始值
        int currentHp = heroAircraft.getHp();
        int maxHp = heroAircraft.getMaxHp();
        
        if (currentHp + recoverHp > maxHp) {
            // 如果恢复后会超过最大值，则只恢复到最大值
            heroAircraft.setHp(maxHp);
        } else {
            // 否则恢复recoverHp点生命值
            heroAircraft.setHp(currentHp + recoverHp);
        }
        
        System.out.println("BloodSupply active! Recover " + recoverHp + " HP.");
    }

}