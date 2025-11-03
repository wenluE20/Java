package edu.hitsz.aircraft;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class isDestroyedTest {

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
    void isDestroyed_normalHealth() {
        // 测试正常生命值状态
        assertFalse(heroAircraft.isDestroyed(), "当生命值大于0时，isDestroyed应返回false");
        assertEquals(100, heroAircraft.getHp(), "初始生命值应为100");
    }

    @Test
    void isDestroyed_zeroHealth() {
        // 测试生命值为0的边界情况
        heroAircraft.setHp(0);
        assertTrue(heroAircraft.isDestroyed(), "当生命值为0时，isDestroyed应返回true");
    }

    @Test
    void isDestroyed_negativeHealth() {
        // 测试生命值为负数的情况（黑盒测试 - 异常输入）
        heroAircraft.setHp(-50);
        // 根据代码逻辑，setHp方法会将负数生命值设置为0并调用vanish()
        assertEquals(0, heroAircraft.getHp(), "负数生命值应被设置为0");
        assertTrue(heroAircraft.isDestroyed(), "设置负数生命值后，isDestroyed应返回true");
    }

    @Test
    void isDestroyed_decreaseHpToZero() {
        // 测试通过decreaseHp方法将生命值减少到0
        heroAircraft.decreaseHp(100); // 减少全部生命值
        assertTrue(heroAircraft.isDestroyed(), "生命值被减少到0后，isDestroyed应返回true");
        assertEquals(0, heroAircraft.getHp(), "生命值应为0");
    }

    @Test
    void isDestroyed_decreaseHpBelowZero() {
        // 测试通过decreaseHp方法将生命值减少到负数
        heroAircraft.decreaseHp(200); // 减少超过当前生命值
        assertTrue(heroAircraft.isDestroyed(), "生命值被减少到负数后，isDestroyed应返回true");
        assertEquals(0, heroAircraft.getHp(), "生命值应被限制为0");
    }

    @Test
    void isDestroyed_afterVanish() {
        // 测试vanish方法调用后英雄机的状态
        heroAircraft.vanish(); // 直接调用消失方法
        // 注意：vanish方法只设置isValid为false，不改变hp值
        assertEquals(100, heroAircraft.getHp(), "vanish方法不应改变生命值");
        // 根据isDestroyed的实现，它只检查hp值
        assertFalse(heroAircraft.isDestroyed(), "调用vanish方法后，只要hp>0，isDestroyed应返回false");
        // 但英雄机应该被标记为无效
        assertTrue(heroAircraft.notValid(), "调用vanish方法后，notValid应返回true");
    }
}