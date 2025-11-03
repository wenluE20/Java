package edu.hitsz.prop;

/**
 * 炸弹观察者接口
 * 定义观察者模式中的观察者（Observer）角色
 */
public interface BombObserver {
    /**
     * 炸弹爆炸时的响应方法
     */
    void update();
}