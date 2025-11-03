package edu.hitsz.prop;

/**
 * 道具工厂接口
 * 定义创建道具的抽象方法
 */
public interface PropFactory {
    /**
     * 创建道具实例
     * @param locationX 道具的X坐标
     * @param locationY 道具的Y坐标
     * @return 创建的道具实例
     */
    AbstractProp createProp(int locationX, int locationY);
}