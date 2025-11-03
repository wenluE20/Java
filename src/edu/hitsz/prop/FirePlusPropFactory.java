package edu.hitsz.prop;

/**
 * 子弹加强道具工厂类
 * 负责创建子弹加强道具实例
 */
public class FirePlusPropFactory implements PropFactory {
    
    @Override
    public AbstractProp createProp(int locationX, int locationY) {
        // 道具速度设置：向下移动
        int speedX = 0;
        int speedY = 15;
        
        return new FirePlusProp(locationX, locationY, speedX, speedY);
    }
}