package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 环射策略实现
 * 子弹呈环形发射，适用于英雄机获得超级子弹道具时和Boss敌机
 */
public class RingShootStrategy implements BulletStrategy {
    @Override
    public List<BaseBullet> execute(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY();
        BaseBullet bullet;

        if (aircraft instanceof HeroAircraft) {
            // 英雄机环射实现（简化版，发射12颗子弹呈环形）
            int direction = -1;
            int shootNum = 12;
            int power = 50;
            y += direction * 5;
            int bulletBaseSpeed = 4;

            double angleStep = 2 * Math.PI / shootNum;
            for (int i = 0; i < shootNum; i++) {
                double angle = i * angleStep;
                int speedX = (int) (bulletBaseSpeed * Math.sin(angle));
                int speedY = (int) (bulletBaseSpeed * Math.cos(angle)) + direction * 2;
                bullet = new HeroBullet(x, y, speedX, speedY, power);
                res.add(bullet);
            }
        } else {
            // Boss敌机环射实现（发射20颗子弹呈环形）
            int direction = 1;
            int shootNum = 20;
            int power = 15;
            y += direction * 2;
            int bulletBaseSpeed = 5;

            double angleStep = 2 * Math.PI / shootNum;
            for (int i = 0; i < shootNum; i++) {
                double angle = i * angleStep;
                int speedX = (int) (bulletBaseSpeed * Math.sin(angle));
                int speedY = (int) (bulletBaseSpeed * Math.cos(angle)) + direction * 2;
                bullet = new EnemyBullet(x, y, speedX, speedY, power);
                res.add(bullet);
            }
        }

        return res;
    }
}