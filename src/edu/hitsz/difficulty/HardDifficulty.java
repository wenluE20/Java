package edu.hitsz.difficulty;

/**
 * 困难难度实现类
 * 根据模板模式实现困难难度的具体配置
 */
public class HardDifficulty extends AbstractDifficultyTemplate {
    private final int baseEnemyHp;        // 基础敌机血量
    private final int baseEnemySpeed;     // 基础敌机速度
    private final int baseEnemyGenerateInterval; // 基础敌机生成周期
    private final int baseBossHp;         // 基础Boss血量
    private int bossGenerationCount = 0;  // Boss生成次数

    public HardDifficulty() {
        // 初始化基础值，用于难度增长计算
        this.baseEnemyHp = 50;
        this.baseEnemySpeed = 5;
        this.baseEnemyGenerateInterval = 400;
        this.baseBossHp = 600;
    }

    @Override
    protected void configureEnemyProperties() {
        // 困难难度：敌机数量多，血量和速度较高
        enemyMaxNumber = 6;
        enemyHp = baseEnemyHp;          // 敌机基础血量
        enemySpeed = baseEnemySpeed;    // 敌机基础速度
    }

    @Override
    protected void configureShootIntervals() {
        // 困难难度：射击频率较高
        heroShootInterval = 200;  // 英雄机射击周期(ms)
        enemyShootInterval = 400; // 敌机射击周期(ms)
    }

    @Override
    protected void configureEnemyGeneration() {
        // 困难难度：敌机生成频率高，精英敌机概率高
        enemyGenerateInterval = baseEnemyGenerateInterval;
        eliteEnemyProbability = 0.4;       // 精英敌机产生概率
        elitePlusGenerateInterval = 4000;  // 超级精英敌机生成周期(ms)
    }

    @Override
    protected void configureBossSettings() {
        // 困难难度：有Boss敌机，每次召唤提升Boss机血量
        hasBoss = true;
        bossScoreThreshold = 200;  // Boss出现阈值（修改为每200分出现一次）
        bossHp = baseBossHp;      // 初始Boss血量
    }

    @Override
    protected void configureDifficultyIncrease() {
        // 困难难度：难度随时间快速增加
        increaseDifficulty = true;
        // 设置背景图片路径
        backgroundImagePath = "images/bg3.jpg";
    }

    @Override
    protected void increaseDifficultyOverTime() {
        // 困难难度：随时间和分数快速增加难度
        // 每20秒增加一次难度
        int difficultyLevel = time / 20000;
        if (difficultyLevel > 0) {
            // 增加敌机血量（每次增加15%）
            enemyHp = baseEnemyHp + (int)(baseEnemyHp * 0.15 * difficultyLevel);
            // 增加敌机速度（每次增加0.8）
            enemySpeed = baseEnemySpeed + (int)(0.8 * difficultyLevel);
            // 缩短敌机生成周期（每次减少10%）
            int newInterval = baseEnemyGenerateInterval - (int)(baseEnemyGenerateInterval * 0.1 * difficultyLevel);
            enemyGenerateInterval = Math.max(newInterval, 200); // 最低不低于200ms
            // 增加精英敌机概率
            eliteEnemyProbability = Math.min(0.4 + 0.08 * difficultyLevel, 0.8); // 最高不超过80%
            // 缩短超级精英敌机生成周期
            elitePlusGenerateInterval = Math.max(4000 - 500 * difficultyLevel, 2000); // 最低不低于2000ms
        }
    }

    /**
     * 增加Boss生成次数，用于计算Boss血量增长
     */
    public void incrementBossGenerationCount() {
        bossGenerationCount++;
        // 每次Boss召唤时，血量增加20%
        bossHp = (int)(baseBossHp * Math.pow(1.2, bossGenerationCount));
    }

    /**
     * 获取Boss生成次数
     * @return Boss生成次数
     */
    public int getBossGenerationCount() {
        return bossGenerationCount;
    }
}