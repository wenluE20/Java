package edu.hitsz.difficulty;

/**
 * 难度工厂类
 * 负责创建不同难度级别的模板实例
 */
public class DifficultyFactory {
    
    /**
     * 根据难度名称创建对应的难度模板实例
     * @param difficulty 难度名称（EASY, NORMAL, HARD）
     * @return 对应的难度模板实例
     */
    public static AbstractDifficultyTemplate createDifficulty(String difficulty) {
        switch (difficulty) {
            case "EASY":
                return new EasyDifficulty();
            case "NORMAL":
                return new NormalDifficulty();
            case "HARD":
                return new HardDifficulty();
            default:
                // 默认返回普通难度
                return new NormalDifficulty();
        }
    }
}