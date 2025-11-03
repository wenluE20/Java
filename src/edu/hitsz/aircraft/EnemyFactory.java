package edu.hitsz.aircraft;

/**
 * 敌机工厂接口
 * 定义创建敌机的抽象方法
 */
public interface EnemyFactory {
    /**
     * 创建敌机实例
     * @return 创建的敌机实例
     */
    AbstractAircraft createEnemyAircraft();
}