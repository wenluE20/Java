package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 * 使用单例模式确保每局游戏只有一架英雄机
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {
    
    // 私有静态实例，确保全局唯一
    private static volatile HeroAircraft instance;

    /**攻击方式 */

    /**
     * 子弹射击方向 (向上发射：-1，向下发射：1)
     */
    private int direction = -1;
    
    /**
     * 默认参数
     */
    private static final int DEFAULT_LOCATION_X = 400;
    private static final int DEFAULT_LOCATION_Y = 500;
    private static final int DEFAULT_SPEED_X = 0;
    private static final int DEFAULT_SPEED_Y = 0;
    public static final int DEFAULT_HP = 10000;

    /**
     * 私有构造函数，防止外部直接实例化
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp    初始生命值
     */
    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        // 调用父类构造函数
        super(locationX, locationY, speedX, speedY, DEFAULT_HP);
        // 显式设置hp为DEFAULT_HP，确保不受传入参数影响
        this.hp = DEFAULT_HP;
        // 确保maxHp为DEFAULT_HP
        this.maxHp = DEFAULT_HP;
    }
    
    /**
     * 公共静态获取实例方法，提供全局访问点
     * 使用双检锁模式确保线程安全
     * @return HeroAircraft的唯一实例
     */
    public static HeroAircraft getInstance() {
        if (instance == null) {
            synchronized (HeroAircraft.class) {
                if (instance == null) {
                    instance = new HeroAircraft(
                        DEFAULT_LOCATION_X,
                        DEFAULT_LOCATION_Y,
                        DEFAULT_SPEED_X,
                        DEFAULT_SPEED_Y,
                        DEFAULT_HP
                    );
                }
            }
        }
        return instance;
    }
    
    /**
     * 带参数的获取实例方法，允许自定义初始参数
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX 英雄机速度x分量
     * @param speedY 英雄机速度y分量
     * @param hp 初始生命值
     * @return HeroAircraft的唯一实例
     */
    public static HeroAircraft getInstance(int locationX, int locationY, int speedX, int speedY, int hp) {
        if (instance == null) {
            synchronized (HeroAircraft.class) {
                if (instance == null) {
                    instance = new HeroAircraft(locationX, locationY, speedX, speedY, hp);
                }
            }
        }
        return instance;
    }
    
    /**
     * 重置单例实例（用于重新开始游戏）
     */
    public static void resetInstance() {
        instance = null;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    @Override
    /**
     * 通过射击产生子弹
     * @return 射击出的子弹List
     */
    public List<BaseBullet> shoot() {
        // 使用策略模式发射子弹
        return bulletContext.shoot(this);
    }
    
    /**
     * 判断英雄机是否被摧毁
     * @return 如果生命值为0，返回true；否则返回false
     */
    public boolean isDestroyed() {
        return this.getHp() <= 0;
    }
    
    /**
     * 设置射击数量
     * @param shootNum 子弹一次发射数量
     */
    public void setShootNum(int shootNum) {
        bulletContext.setBulletNum(shootNum);
    }
    
    /**
     * 设置攻击力
     * @param power 子弹伤害
     */
    public void setPower(int power) {
        bulletContext.setBulletPower(power);
    }

}
