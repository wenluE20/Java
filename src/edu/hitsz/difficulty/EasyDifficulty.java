package edu.hitsz.difficulty;

/**
 * 简单难度实现类
 * 根据模板模式实现简单难度的具体配置
 */
public class EasyDifficulty extends AbstractDifficultyTemplate {

    @Override
    protected void configureEnemyProperties() {
        // 简单难度：敌机数量少，血量和速度较低
        enemyMaxNumber = 4;
        enemyHp = 30;       // 敌机基础血量
        enemySpeed = 3;     // 敌机基础速度
    }

    @Override
    protected void configureShootIntervals() {
        // 简单难度：射击频率适中
        heroShootInterval = 300;  // 英雄机射击周期(ms)
        enemyShootInterval = 800; // 敌机射击周期(ms)
    }

    @Override
    protected void configureEnemyGeneration() {
        // 简单难度：敌机生成较慢，精英敌机概率低
        enemyGenerateInterval = 800;       // 敌机生成周期(ms)
        eliteEnemyProbability = 0.2;       // 精英敌机产生概率
        elitePlusGenerateInterval = 8000;  // 超级精英敌机生成周期(ms)
    }

    @Override
    protected void configureBossSettings() {
        // 简单难度：无Boss敌机
        hasBoss = false;
        bossScoreThreshold = Integer.MAX_VALUE; // 设置极高阈值，确保不会生成Boss
        bossHp = 0;                             // Boss血量设为0
    }

    @Override
    protected void configureDifficultyIncrease() {
        // 简单难度：难度不随时间增加
        increaseDifficulty = false;
        // 设置背景图片路径
        backgroundImagePath = "images/bg.jpg";
    }

    @Override
    protected void increaseDifficultyOverTime() {
        // 简单难度不需要实现此方法，因为难度不会增加
        // 但仍需实现以满足抽象方法要求
    }
}