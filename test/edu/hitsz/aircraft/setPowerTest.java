package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class setPowerTest {

    private HeroAircraft heroAircraft;

    @BeforeEach
    void setUp() {
        // 重置单例实例
        HeroAircraft.resetInstance();
        // 创建英雄机实例
        heroAircraft = HeroAircraft.getInstance();
    }

    @AfterEach
    void tearDown() {
        // 测试结束后重置实例
        HeroAircraft.resetInstance();
    }

    @Test
    void setPower() {
        // 黑盒测试：验证默认攻击力
        List<BaseBullet> defaultBullets = heroAircraft.shoot();
        assertEquals(30, defaultBullets.get(0).getPower(), "默认子弹伤害应该为30");
        
        // 白盒测试：设置正整数攻击力
        heroAircraft.setPower(50);
        List<BaseBullet> bullets50 = heroAircraft.shoot();
        assertEquals(50, bullets50.get(0).getPower(), "设置power=50后，子弹伤害应该为50");
        
        // 黑盒测试：设置不同的攻击力值
        heroAircraft.setPower(10);
        List<BaseBullet> bullets10 = heroAircraft.shoot();
        assertEquals(10, bullets10.get(0).getPower(), "设置power=10后，子弹伤害应该为10");
        
        // 黑盒测试：设置较大的攻击力值
        heroAircraft.setPower(100);
        List<BaseBullet> bullets100 = heroAircraft.shoot();
        assertEquals(100, bullets100.get(0).getPower(), "设置power=100后，子弹伤害应该为100");
        
        // 白盒测试：边界值测试 - 设置攻击力为0
        heroAircraft.setPower(0);
        List<BaseBullet> bullets0 = heroAircraft.shoot();
        assertEquals(0, bullets0.get(0).getPower(), "设置power=0后，子弹伤害应该为0");
        
        // 黑盒测试：连续设置攻击力的一致性
        heroAircraft.setPower(25);
        List<BaseBullet> bullets25_1 = heroAircraft.shoot();
        assertEquals(25, bullets25_1.get(0).getPower(), "第一次设置power=25后，子弹伤害应该为25");
        
        heroAircraft.setPower(25);
        List<BaseBullet> bullets25_2 = heroAircraft.shoot();
        assertEquals(25, bullets25_2.get(0).getPower(), "第二次设置相同的power=25后，子弹伤害应该保持为25");
        
        // 黑盒测试：攻击力变化对多子弹的影响
        heroAircraft.setShootNum(3);
        heroAircraft.setPower(40);
        List<BaseBullet> multiBullets = heroAircraft.shoot();
        assertEquals(3, multiBullets.size(), "应该发射3发子弹");
        
        // 验证所有子弹都具有相同的攻击力
        for (BaseBullet bullet : multiBullets) {
            assertEquals(40, bullet.getPower(), "所有子弹的伤害应该相同且为40");
        }
    }
}