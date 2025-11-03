package edu.hitsz.prop;

import java.util.List;
import java.util.ArrayList;

/**
 * 炸弹主题类
 * 定义观察者模式中的主题（Subject）角色
 */
public class BombSubject {
    //观察者列表
    private List<BombObserver> observers = new ArrayList<>();
    
    //增加观察者
    public void addObserver(BombObserver observer) {
        observers.add(observer);
    }
    
    //删除观察者
    public void removeObserver(BombObserver observer) {
        observers.remove(observer);
    }
    
    //通知所有观察者
    public void notifyAllObservers() {
        for (BombObserver observer : observers) {
            observer.update();
        }
    }
    
    //炸弹爆炸
    public void explode() {
        notifyAllObservers();
    }
}