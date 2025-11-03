package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

/**
 * 子弹发射策略接口
 * 定义了不同子弹发射方式的通用接口
 */
public interface BulletStrategy {
    /**
     * 根据策略执行子弹发射
     * @param aircraft 发射子弹的飞机
     * @return 发射出的子弹列表
     */
    List<BaseBullet> execute(AbstractAircraft aircraft);
}