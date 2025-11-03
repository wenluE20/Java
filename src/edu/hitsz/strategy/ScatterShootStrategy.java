package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 散射策略实现
 * 子弹分散发射，适用于英雄机获得子弹道具时和超级精英敌机
 */
public class ScatterShootStrategy implements BulletStrategy {
    @Override
    public List<BaseBullet> execute(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY();
        BaseBullet bullet;

        if (aircraft instanceof HeroAircraft) {
            // 英雄机散射实现
            int direction = -1; // 向上发射
            int shootNum = 3; // 散射3颗子弹
            int power = 40; // 增强火力
            y += direction * 2; // 调整发射位置
            int baseSpeedY = direction * 5;

            // 散射实现：中间子弹向上，两侧子弹向斜上方
            for (int i = 0; i < shootNum; i++) {
                int speedX = (i - 1) * 2; // 左右偏移速度
                int speedY = baseSpeedY;
                bullet = new HeroBullet(x + (i * 2 - shootNum + 1) * 15, y, speedX, speedY, power);
                res.add(bullet);
            }
        } else {
            // 超级精英敌机散射实现
            int direction = 1; // 向下发射
            int shootNum = 3; // 散射3颗子弹
            int power = 20; // 增强火力
            y += direction * 2; // 调整发射位置
            int baseSpeedY = aircraft.getSpeedY() + direction * 5;

            // 散射实现：中间子弹向下，两侧子弹向斜下方
            for (int i = 0; i < shootNum; i++) {
                int speedX = (i - 1) * 2; // 左右偏移速度
                int speedY = baseSpeedY;
                bullet = new EnemyBullet(x + (i * 2 - shootNum + 1) * 15, y, speedX, speedY, power);
                res.add(bullet);
            }
        }

        return res;
    }
}