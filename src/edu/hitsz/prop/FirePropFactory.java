package edu.hitsz.prop;

/**
 * 火力道具工厂类
 * 负责创建火力道具实例
 */
public class FirePropFactory implements PropFactory {
    
    @Override
    public AbstractProp createProp(int locationX, int locationY) {
        // 道具速度设置：向下移动
        int speedX = 0;
        int speedY = 15;
        
        return new FireProp(locationX, locationY, speedX, speedY);
    }
}