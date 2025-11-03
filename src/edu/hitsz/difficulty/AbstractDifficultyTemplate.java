package edu.hitsz.difficulty;

/**
 * 游戏难度模板抽象类
 * 使用模板模式定义游戏难度配置的算法框架
 */
public abstract class AbstractDifficultyTemplate {
    // 游戏难度相关属性
    protected int enemyMaxNumber;        // 敌机最大数量
    protected int enemyHp;               // 敌机血量
    protected int enemySpeed;            // 敌机速度
    protected int heroShootInterval;     // 英雄机射击周期
    protected int enemyShootInterval;    // 敌机射击周期
    protected double eliteEnemyProbability; // 精英敌机产生概率
    protected int enemyGenerateInterval; // 敌机产生周期
    protected int elitePlusGenerateInterval; // 超级精英敌机产生周期
    protected int bossScoreThreshold;    // Boss敌机产生的分数阈值
    protected int bossHp;                // Boss敌机的血量
    protected boolean hasBoss;           // 是否有Boss敌机
    protected boolean increaseDifficulty; // 难度是否随时间增加
    protected int time = 0;              // 当前时间
    protected int score = 0;             // 当前分数
    protected String backgroundImagePath; // 背景图片路径

    /**
     * 模板方法：应用难度设置
     * 定义了难度配置的算法骨架，具体步骤由子类实现
     */
    public final void applyDifficultySettings() {
        // 配置敌机基本属性
        configureEnemyProperties();
        // 配置射击周期
        configureShootIntervals();
        // 配置敌机生成参数
        configureEnemyGeneration();
        // 配置Boss相关参数
        configureBossSettings();
        // 配置难度增长特性
        configureDifficultyIncrease();
    }

    /**
     * 更新游戏难度（随时间和分数）
     * @param currentTime 当前游戏时间
     * @param currentScore 当前游戏分数
     */
    public void updateDifficulty(int currentTime, int currentScore) {
        this.time = currentTime;
        this.score = currentScore;
        if (increaseDifficulty) {
            increaseDifficultyOverTime();
        }
    }

    /**
     * 配置敌机基本属性（抽象方法）
     */
    protected abstract void configureEnemyProperties();

    /**
     * 配置射击周期（抽象方法）
     */
    protected abstract void configureShootIntervals();

    /**
     * 配置敌机生成参数（抽象方法）
     */
    protected abstract void configureEnemyGeneration();

    /**
     * 配置Boss相关参数（抽象方法）
     */
    protected abstract void configureBossSettings();

    /**
     * 配置难度增长特性（抽象方法）
     */
    protected abstract void configureDifficultyIncrease();

    /**
     * 随时间增加难度（抽象方法）
     */
    protected abstract void increaseDifficultyOverTime();

    // Getter方法
    public int getEnemyMaxNumber() {
        return enemyMaxNumber;
    }

    public int getEnemyHp() {
        return enemyHp;
    }

    public int getEnemySpeed() {
        return enemySpeed;
    }

    public int getHeroShootInterval() {
        return heroShootInterval;
    }

    public int getEnemyShootInterval() {
        return enemyShootInterval;
    }

    public double getEliteEnemyProbability() {
        return eliteEnemyProbability;
    }

    public int getEnemyGenerateInterval() {
        return enemyGenerateInterval;
    }

    public int getElitePlusGenerateInterval() {
        return elitePlusGenerateInterval;
    }

    public int getBossScoreThreshold() {
        return bossScoreThreshold;
    }

    public int getBossHp() {
        return bossHp;
    }

    public boolean hasBoss() {
        return hasBoss;
    }

    public boolean shouldIncreaseDifficulty() {
        return increaseDifficulty;
    }
    
    /**
     * 获取背景图片路径
     * @return 背景图片路径
     */
    public String getBackgroundImagePath() {
        return backgroundImagePath;
    }
    
    /**
     * 设置背景图片路径
     * @param backgroundImagePath 背景图片路径
     */
    public void setBackgroundImagePath(String backgroundImagePath) {
        this.backgroundImagePath = backgroundImagePath;
    }
}