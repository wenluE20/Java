package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShootTest {

    private HeroAircraft heroAircraft;

    @BeforeEach
    void setUp() {
        // 重置单例实例，避免测试之间的影响
        HeroAircraft.resetInstance();
        // 创建英雄机实例
        heroAircraft = HeroAircraft.getInstance(400, 500, 0, 0, 100);
    }

    @AfterEach
    void tearDown() {
        // 清理资源
        HeroAircraft.resetInstance();
    }

    @Test
    void shoot_defaultShootNum() {
        // 测试默认射击数量（1发子弹）
        List<BaseBullet> bullets = heroAircraft.shoot();
        assertEquals(1, bullets.size(), "默认射击数量应为1");
        // 验证子弹类型和属性
        assertTrue(bullets.get(0) instanceof HeroBullet);
        HeroBullet bullet = (HeroBullet) bullets.get(0);
        assertEquals(30, bullet.getPower(), "默认子弹伤害应为30");
        assertEquals(-5, bullet.getSpeedY(), "英雄机子弹应向上飞行");
    }

    @Test
    void shoot_multipleBullets() {
        // 测试多子弹射击（2发子弹）
        heroAircraft.setShootNum(2);
        List<BaseBullet> bullets = heroAircraft.shoot();
        assertEquals(2, bullets.size(), "设置shootNum为2时应发射2发子弹");
        // 验证子弹横向分布
        assertTrue(bullets.get(0).getLocationX() < heroAircraft.getLocationX(), "第一发子弹应在飞机左侧");
        assertTrue(bullets.get(1).getLocationX() > heroAircraft.getLocationX(), "第二发子弹应在飞机右侧");
    }

    @Test
    void shoot_threeBullets() {
        // 测试三发子弹射击（白盒测试，覆盖循环逻辑）
        heroAircraft.setShootNum(3);
        List<BaseBullet> bullets = heroAircraft.shoot();
        assertEquals(3, bullets.size(), "设置shootNum为3时应发射3发子弹");
        // 验证子弹位置规律 - 中间子弹应与飞机X坐标相近
        assertTrue(Math.abs(bullets.get(1).getLocationX() - heroAircraft.getLocationX()) < 5, "中间子弹应在飞机中心附近");
    }

    @Test
    void setShootNum() {
        // 测试设置射击数量
        heroAircraft.setShootNum(5);
        List<BaseBullet> bullets = heroAircraft.shoot();
        assertEquals(5, bullets.size(), "设置shootNum为5时应发射5发子弹");
        
        // 边界测试：设置为0发子弹
        heroAircraft.setShootNum(0);
        bullets = heroAircraft.shoot();
        assertEquals(0, bullets.size(), "设置shootNum为0时应不发射子弹");
    }
}