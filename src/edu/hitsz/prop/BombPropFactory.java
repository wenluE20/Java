package edu.hitsz.prop;

/**
 * 炸弹道具工厂类
 * 负责创建炸弹道具实例
 */
public class BombPropFactory implements PropFactory {
    
    @Override
    public AbstractProp createProp(int locationX, int locationY) {
        // 道具速度设置：向下移动
        int speedX = 0;
        int speedY = 15;
        
        return new BombProp(locationX, locationY, speedX, speedY);
    }
}