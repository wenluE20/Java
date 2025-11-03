package edu.hitsz.difficulty;

/**
 * 普通难度实现类
 * 根据模板模式实现普通难度的具体配置
 */
public class NormalDifficulty extends AbstractDifficultyTemplate {
    private final int baseEnemyHp;        // 基础敌机血量
    private final int baseEnemySpeed;     // 基础敌机速度
    private final int baseEnemyGenerateInterval; // 基础敌机生成周期

    public NormalDifficulty() {
        // 初始化基础值，用于难度增长计算
        this.baseEnemyHp = 40;
        this.baseEnemySpeed = 4;
        this.baseEnemyGenerateInterval = 600;
    }

    @Override
    protected void configureEnemyProperties() {
        // 普通难度：敌机数量适中，血量和速度中等
        enemyMaxNumber = 5;
        enemyHp = baseEnemyHp;          // 敌机基础血量
        enemySpeed = baseEnemySpeed;    // 敌机基础速度
    }

    @Override
    protected void configureShootIntervals() {
        // 普通难度：射击频率适中
        heroShootInterval = 250;  // 英雄机射击周期(ms)
        enemyShootInterval = 600; // 敌机射击周期(ms)
    }

    @Override
    protected void configureEnemyGeneration() {
        // 普通难度：敌机生成频率适中，精英敌机概率中等
        enemyGenerateInterval = baseEnemyGenerateInterval;
        eliteEnemyProbability = 0.3;       // 精英敌机产生概率
        elitePlusGenerateInterval = 6000;  // 超级精英敌机生成周期(ms)
    }

    @Override
    protected void configureBossSettings() {
        // 普通难度：有Boss敌机，每次召唤不改变Boss机血量
        hasBoss = true;
        bossScoreThreshold = 200;  // Boss出现阈值（修改为每200分出现一次）
        bossHp = 500;              // 固定Boss血量
    }

    @Override
    protected void configureDifficultyIncrease() {
        // 普通难度：难度随时间增加
        increaseDifficulty = true;
        // 设置背景图片路径
        backgroundImagePath = "images/bg2.jpg";
    }

    @Override
    protected void increaseDifficultyOverTime() {
        // 普通难度：随时间和分数适度增加难度
        // 每30秒增加一次难度
        int difficultyLevel = time / 30000;
        if (difficultyLevel > 0) {
            // 增加敌机血量（每次增加10%）
            enemyHp = baseEnemyHp + (int)(baseEnemyHp * 0.1 * difficultyLevel);
            // 增加敌机速度（每次增加0.5）
            enemySpeed = baseEnemySpeed + (int)(0.5 * difficultyLevel);
            // 缩短敌机生成周期（每次减少5%）
            int newInterval = baseEnemyGenerateInterval - (int)(baseEnemyGenerateInterval * 0.05 * difficultyLevel);
            enemyGenerateInterval = Math.max(newInterval, 300); // 最低不低于300ms
            // 增加精英敌机概率
            eliteEnemyProbability = Math.min(0.3 + 0.05 * difficultyLevel, 0.6); // 最高不超过60%
        }
    }
}