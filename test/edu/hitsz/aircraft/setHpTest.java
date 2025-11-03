package edu.hitsz.aircraft;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class setHpTest {

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
    void setHp_normalValue() {
        // 测试设置正常生命值（白盒测试 - 正常路径）
        heroAircraft.setHp(50);
        assertEquals(50, heroAircraft.getHp(), "设置生命值为50后，生命值应为50");
        assertFalse(heroAircraft.isDestroyed(), "生命值大于0时不应被销毁");
    }

    @Test
    void setHp_aboveMaxHp() {
        // 测试设置生命值超过最大值（白盒测试 - 分支覆盖）
        heroAircraft.setHp(200); // 大于初始maxHp 100
        assertEquals(100, heroAircraft.getHp(), "设置生命值超过最大值时，应被限制为最大值");
        assertEquals(heroAircraft.getMaxHp(), heroAircraft.getHp(), "生命值不应超过最大值");
    }

    @Test
    void setHp_equalToMaxHp() {
        // 测试设置生命值等于最大值（边界测试）
        heroAircraft.setHp(100);
        assertEquals(100, heroAircraft.getHp(), "设置生命值等于最大值时，应保持不变");
    }

    @Test
    void setHp_zero() {
        // 测试设置生命值为0（白盒测试 - 覆盖hp<=0分支）
        heroAircraft.setHp(0);
        assertEquals(0, heroAircraft.getHp(), "设置生命值为0时，生命值应为0");
        assertTrue(heroAircraft.isDestroyed(), "生命值为0时应被标记为销毁");
        assertTrue(heroAircraft.notValid(), "生命值为0时应被标记为无效");
    }

    @Test
    void setHp_negative() {
        // 测试设置生命值为负数（黑盒测试 - 异常输入）
        heroAircraft.setHp(-50);
        assertEquals(0, heroAircraft.getHp(), "设置负数生命值时，应被设置为0");
        assertTrue(heroAircraft.isDestroyed(), "设置负数生命值后应被标记为销毁");
    }

    @Test
    void setHp_multipleOperations() {
        // 测试多次设置生命值（集成测试）
        // 1. 设置为正常值
        heroAircraft.setHp(30);
        assertEquals(30, heroAircraft.getHp());
        
        // 2. 设置为超过最大值
        heroAircraft.setHp(150);
        assertEquals(100, heroAircraft.getHp());
        
        // 3. 设置为0
        heroAircraft.setHp(0);
        assertEquals(0, heroAircraft.getHp());
        assertTrue(heroAircraft.isDestroyed());
        
        // 4. 尝试在销毁后恢复生命值
        heroAircraft.setHp(80);
        assertEquals(80, heroAircraft.getHp(), "即使之前被销毁，设置正生命值后应恢复生命值");
        assertFalse(heroAircraft.isDestroyed(), "恢复正生命值后，isDestroyed应返回false");
        // 注意：setHp方法不会恢复isValid状态
        assertTrue(heroAircraft.notValid(), "setHp方法不会恢复isValid状态");
    }

    @Test
    void setHp_increaseAfterDecrease() {
        // 测试先减少后增加生命值（白盒路径覆盖）
        // 先减少生命值
        heroAircraft.decreaseHp(40);
        assertEquals(60, heroAircraft.getHp());
        
        // 再设置为较低值
        heroAircraft.setHp(20);
        assertEquals(20, heroAircraft.getHp());
        
        // 再设置为较高值（但不超过最大值）
        heroAircraft.setHp(80);
        assertEquals(80, heroAircraft.getHp());
    }
}