package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 直射策略实现
 * 子弹直线发射，适用于英雄机默认状态和精英敌机
 */
public class DirectShootStrategy implements BulletStrategy {
    @Override
    public List<BaseBullet> execute(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY();
        int speedX = 0;
        int direction;
        int shootNum;
        int power;
        BaseBullet bullet;

        // 根据飞机类型设置不同的发射参数
        if (aircraft instanceof HeroAircraft) {
            direction = -1; // 英雄机向上发射
            shootNum = 1; // 默认1颗子弹
            power = 30;
            y += direction * 2; // 调整发射位置
            speedX = 0;
            int speedY = direction * 5;

            // 直射实现
            for (int i = 0; i < shootNum; i++) {
                bullet = new HeroBullet(x + (i * 2 - shootNum + 1) * 10, y, speedX, speedY, power);
                res.add(bullet);
            }
        } else {
            direction = 1; // 敌机向下发射
            shootNum = 1; // 精英敌机默认1颗子弹
            power = 15;
            y += direction * 2; // 调整发射位置
            int speedY = aircraft.getSpeedY() + direction * 5;

            // 直射实现
            for (int i = 0; i < shootNum; i++) {
                bullet = new EnemyBullet(x + (i * 2 - shootNum + 1) * 10, y, speedX, speedY, power);
                res.add(bullet);
            }
        }

        return res;
    }
}