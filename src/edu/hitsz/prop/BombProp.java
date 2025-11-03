package edu.hitsz.prop;

import edu.hitsz.application.Game;
import edu.hitsz.application.AudioManager;
import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;

/**
 * 炸弹道具
 * 碰撞后触发炸弹效果，使用观察者模式通知所有敌机和敌机子弹
 * 
 * @author hitsz
 */
public class BombProp extends AbstractProp {
    
    // 使用BombSubject管理观察者
    private BombSubject bombSubject;
    
    public BombProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
        bombSubject = new BombSubject();
    }
    
    @Override
    public void active() {
        System.out.println("BombSupply active!");
        
        // 获取游戏实例
        Game game = Game.getInstance();
        
        // 注册所有敌机和敌机子弹作为观察者
        registerGameObjectsAsObservers(game);
        
        // 触发炸弹爆炸，通知所有观察者
        bombSubject.explode();
        
        // 播放炸弹爆炸音效
        AudioManager.getInstance().playBombExplosionSound();
    }
    
    /**
     * 注册游戏中的所有敌机和敌机子弹作为观察者
     * @param game 游戏实例
     */
    private void registerGameObjectsAsObservers(Game game) {
        // 注册敌机子弹
        for (BaseBullet bullet : game.getEnemyBullets()) {
            if (bullet instanceof BombObserver) {
                bombSubject.addObserver((BombObserver) bullet);
            }
        }
        
        // 注册敌机
        for (AbstractAircraft aircraft : game.getEnemyAircrafts()) {
            if (aircraft instanceof BombObserver) {
                bombSubject.addObserver((BombObserver) aircraft);
            }
        }
    }
}