package edu.hitsz.application;

import edu.hitsz.application.Main;
import edu.hitsz.application.AudioManager;
import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.aircraft.EnemyFactory;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BloodProp;
import edu.hitsz.prop.FireProp;
import edu.hitsz.prop.BombProp;
import edu.hitsz.prop.FirePlusProp;
import edu.hitsz.prop.PropFactory;
import edu.hitsz.prop.BloodPropFactory;
import edu.hitsz.prop.FirePropFactory;
import edu.hitsz.prop.BombPropFactory;
import edu.hitsz.prop.FirePlusPropFactory;
import edu.hitsz.dao.GameRecordService;
import edu.hitsz.difficulty.AbstractDifficultyTemplate;
import edu.hitsz.difficulty.DifficultyFactory;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public class Game extends JPanel {

    private int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 40;

    private final HeroAircraft heroAircraft;
    
    /**
     * 单例实例
     */
    private static Game instance;
    private final List<AbstractAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<AbstractProp> props;
    
    /**
     * 随机数生成器，用于随机产生敌机和道具
     */
    private final Random random;
    
    /**
     * 敌机工厂实例
     */
    private final EnemyFactory mobEnemyFactory;
    private final EnemyFactory eliteEnemyFactory;
    private final EnemyFactory elitePlusEnemyFactory;
    private final EnemyFactory bossEnemyFactory;
    
    /**
     * Boss敌机出现的分数阈值
     */
    private int bossScoreThreshold = 100;
    
    /**
     * 是否已生成Boss敌机（每达到一次阈值生成一个）
     */
    private int bossGenerationCount = 0;
    
    /**
     * 游戏难度
     */
    private String difficulty;
    
    /**
     * 游戏记录服务
     */
    private GameRecordService gameRecordService;
    
    /**
     * 超级精英敌机生成周期计数
     */
    private int elitePlusGenerateTimer = 0;
    
    /**
     * 超级精英敌机生成周期（ms）
     */
    private int elitePlusGenerateInterval = 6000; // 降低周期，更容易出现
    
    /**
     * 难度模板实例
     */
    private AbstractDifficultyTemplate difficultyTemplate;
    
    /**
     * 当前背景图片
     */
    private BufferedImage currentBackgroundImage;
    
    /**
     * 道具工厂实例
     */
    private final PropFactory bloodPropFactory;
    private final PropFactory firePropFactory;
    private final PropFactory bombPropFactory;
    private final PropFactory firePlusPropFactory;

    /**
     * 屏幕中出现的敌机最大数量
     */
    private int enemyMaxNumber = 5;

    /**
     * 当前得分
     */
    private int score = 0;
    /**
     * 当前时刻
     */
    private int time = 0;

    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    private int cycleDuration = 600;
    private int cycleTime = 0;

    /**
     * 游戏结束标志
     */
    private boolean gameOverFlag = false;
    
    /**
     * 音乐开关状态
     */
    private boolean musicEnabled = true;
    
    /**
     * 音效管理器实例
     */
    private AudioManager audioManager;

    public Game(String difficulty) {
        // 使用单例模式获取英雄机实例
        heroAircraft = HeroAircraft.getInstance(
                Main.WINDOW_WIDTH / 2,
                Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight() ,
                0, 0, HeroAircraft.DEFAULT_HP);
        
        // 设置Game实例
        instance = this;

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();
        random = new Random();
        
        // 初始化工厂实例
        mobEnemyFactory = new MobEnemyFactory();
        eliteEnemyFactory = new EliteEnemyFactory();
        elitePlusEnemyFactory = new ElitePlusEnemyFactory();
        bossEnemyFactory = new BossEnemyFactory();
        bloodPropFactory = new BloodPropFactory();
        firePropFactory = new FirePropFactory();
        bombPropFactory = new BombPropFactory();
        firePlusPropFactory = new FirePlusPropFactory();
        
        // 初始化游戏难度和记录服务
        this.difficulty = difficulty;
        this.gameRecordService = new GameRecordService();
        
        // 从Main获取音乐设置
        this.musicEnabled = Main.isMusicEnabled();
        
        // 初始化音效管理器
        audioManager = AudioManager.getInstance();
        audioManager.setEnabled(musicEnabled);
        
        // 使用难度工厂创建对应的难度模板
        this.difficultyTemplate = DifficultyFactory.createDifficulty(difficulty);
        // 应用难度设置
        applyDifficultySettings();

        /**
         * Scheduled 线程池，用于定时任务调度
         * 关于alibaba code guide：可命名的 ThreadFactory 一般需要第三方包
         * apache 第三方库： org.apache.commons.lang3.concurrent.BasicThreadFactory
         */
        this.executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("game-action-%d").daemon(true).build());

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);
        
        System.out.println("游戏难度设置为: " + difficulty);
        System.out.println("音乐设置: " + (musicEnabled ? "开启" : "关闭"));
        // 显示当前排行榜
        gameRecordService.printLeaderboard(difficulty, 10);
    }
    
    /**
     * 应用难度设置
     * 使用模板模式应用当前难度的所有设置
     */
    private void applyDifficultySettings() {
        // 调用模板方法应用难度设置
        difficultyTemplate.applyDifficultySettings();
        
        // 设置游戏参数
        enemyMaxNumber = difficultyTemplate.getEnemyMaxNumber();
        cycleDuration = difficultyTemplate.getEnemyGenerateInterval();
        bossScoreThreshold = difficultyTemplate.getBossScoreThreshold();
        elitePlusGenerateInterval = difficultyTemplate.getElitePlusGenerateInterval();
        
        // 设置英雄机血量，使用默认生命值常量
        heroAircraft.setHp(HeroAircraft.DEFAULT_HP);
        
        // 加载背景图片
        loadBackgroundImage();
        
        System.out.println("应用难度设置：" + difficulty);
        System.out.println("敌机最大数量：" + enemyMaxNumber);
        System.out.println("敌机生成周期：" + cycleDuration + "ms");
        System.out.println("Boss阈值：" + bossScoreThreshold);
        System.out.println("是否有Boss：" + difficultyTemplate.hasBoss());
        System.out.println("难度是否随时间增加：" + difficultyTemplate.shouldIncreaseDifficulty());
        System.out.println("背景图片：" + difficultyTemplate.getBackgroundImagePath());
    }
    
    /**
     * 更新游戏难度
     * 根据当前时间和分数更新难度参数
     */
    private void updateGameDifficulty() {
        if (difficultyTemplate != null && difficultyTemplate.shouldIncreaseDifficulty()) {
            difficultyTemplate.updateDifficulty(time, score);
            
            // 更新游戏参数
            enemyMaxNumber = difficultyTemplate.getEnemyMaxNumber();
            cycleDuration = difficultyTemplate.getEnemyGenerateInterval();
            elitePlusGenerateInterval = difficultyTemplate.getElitePlusGenerateInterval();
        }
    }
    
    /**
     * 加载背景图片
     */
    private void loadBackgroundImage() {
        try {
            String imagePath = difficultyTemplate.getBackgroundImagePath();
            // 尝试从类路径加载图片
            ClassLoader classLoader = getClass().getClassLoader();
            java.net.URL imageUrl = classLoader.getResource(imagePath);
            
            if (imageUrl != null) {
                currentBackgroundImage = ImageIO.read(imageUrl);
            } else {
                // 如果类路径中找不到，尝试从文件系统加载
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    currentBackgroundImage = ImageIO.read(imageFile);
                } else {
                    System.out.println("无法加载背景图片: " + imagePath + "，使用默认背景");
                    // 使用默认背景图片
                    currentBackgroundImage = ImageManager.BACKGROUND_IMAGE;
                }
            }
        } catch (IOException e) {
            System.out.println("加载背景图片出错: " + e.getMessage());
            // 出错时使用默认背景
            currentBackgroundImage = ImageManager.BACKGROUND_IMAGE;
        }
    }
    
    // 兼容旧版构造函数
    public Game() {
        this("NORMAL"); // 默认普通难度
    }
    
    /**
     * 获取音乐开关状态
     * @return 音乐是否开启
     */
    public boolean isMusicEnabled() {
        return musicEnabled;
    }
    
    /**
     * 设置音乐开关状态
     * @param musicEnabled 音乐是否开启
     */
    public void setMusicEnabled(boolean musicEnabled) {
        this.musicEnabled = musicEnabled;
        if (audioManager != null) {
            audioManager.setEnabled(musicEnabled);
        }
        System.out.println("音乐设置已更新: " + (musicEnabled ? "开启" : "关闭"));
    }
    
    /**
     * 获取Game类的单例实例
     * @return Game实例
     */
    public static Game getInstance() {
        return instance;
    }
    
    /**
     * 获取英雄机实例
     * @return 英雄机实例
     */
    public HeroAircraft getHeroAircraft() {
        return heroAircraft;
    }
    
    /**
     * 获取敌机子弹列表
     * @return 敌机子弹列表
     */
    public List<BaseBullet> getEnemyBullets() {
        return enemyBullets;
    }
    
    /**
     * 获取敌机列表
     * @return 敌机列表
     */
    public List<AbstractAircraft> getEnemyAircrafts() {
        return enemyAircrafts;
    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {
        // 启动游戏时播放背景音乐
        if (musicEnabled) {
            audioManager.playBgm();
        }

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {

            time += timeInterval;


            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                System.out.println(time);
                
                // 更新超级精英敌机生成计时器
                elitePlusGenerateTimer += cycleDuration;
                
                // 检查是否需要生成Boss敌机（当分数达到阈值，并且当前没有Boss敌机时，且当前难度支持Boss）
                if (difficultyTemplate.hasBoss() && 
                    score >= bossScoreThreshold * (bossGenerationCount + 1) && 
                    !enemyAircrafts.stream().anyMatch(aircraft -> aircraft instanceof BossEnemy)) {
                    // 生成Boss敌机
                    BossEnemy bossEnemy = (BossEnemy) bossEnemyFactory.createEnemyAircraft();
                    int bossHp = difficultyTemplate.getBossHp();
                    
                    // 设置Boss血量
                    // 通过反射设置maxHp，因为AbstractAircraft没有提供公共的setMaxHp方法
                    try {
                        java.lang.reflect.Field maxHpField = AbstractAircraft.class.getDeclaredField("maxHp");
                        maxHpField.setAccessible(true);
                        maxHpField.set(bossEnemy, bossHp);
                    } catch (Exception e) {
                        System.out.println("设置Boss maxHp失败: " + e.getMessage());
                    }
                    
                    // 设置当前血量
                    bossEnemy.setHp(bossHp);
                    
                    // 对于困难难度，增加Boss生成次数记录
                    if (difficultyTemplate instanceof edu.hitsz.difficulty.HardDifficulty) {
                        ((edu.hitsz.difficulty.HardDifficulty)difficultyTemplate).incrementBossGenerationCount();
                    }
                    
                    enemyAircrafts.add(bossEnemy);
                    bossGenerationCount++;
                    System.out.println("Boss敌机出现！当前分数：" + score);
                    // 播放Boss背景音乐
                    if (musicEnabled) {
                        audioManager.playBossBgm();
                    }
                }
                
                // 检查是否需要生成超级精英敌机（每隔一定周期随机产生）
                else if (elitePlusGenerateTimer >= elitePlusGenerateInterval && 
                         enemyAircrafts.size() < enemyMaxNumber && 
                         !enemyAircrafts.stream().anyMatch(aircraft -> aircraft instanceof BossEnemy)) {
                    // 重置计时器
                    elitePlusGenerateTimer = 0;
                    // 80%概率生成超级精英敌机（提高概率以便测试）
                    if (random.nextDouble() < 0.5) {
                        enemyAircrafts.add(elitePlusEnemyFactory.createEnemyAircraft());
                        System.out.println("超级精英敌机出现！");
                    }
                }
                
                // 新敌机产生（当没有Boss敌机时才生成普通敌机）
                else if (enemyAircrafts.size() < enemyMaxNumber && 
                         !enemyAircrafts.stream().anyMatch(aircraft -> aircraft instanceof BossEnemy)) {
                    // 随机决定产生普通敌机或精英敌机，精英敌机出现概率为30%
                    if (random.nextDouble() < 0.3) {
                        // 使用精英敌机工厂产生精英敌机
                        enemyAircrafts.add(eliteEnemyFactory.createEnemyAircraft());
                    } else {
                        // 使用普通敌机工厂产生普通敌机
                        enemyAircrafts.add(mobEnemyFactory.createEnemyAircraft());
                    }
                }
                // 飞机射出子弹
                shootAction();
            }

            // 子弹移动
            bulletsMoveAction();

            // 飞机移动
            aircraftsMoveAction();
            
            // 道具移动
            propsMoveAction();

            // 更新游戏难度
            updateGameDifficulty();
            
            // 撞击检测
            crashCheckAction();

            // 后处理
            postProcessAction();

            //每个时刻重绘界面
            repaint();

            // 游戏结束检查英雄机是否存活
            if (heroAircraft.getHp() <= 0) {
                // 游戏结束
                executorService.shutdown();
                gameOverFlag = true;
                System.out.println("Game Over!");
                System.out.println("最终得分: " + score);
                // 播放游戏结束音效
                if (musicEnabled) {
                    audioManager.playGameOverSound();
                }
                // 延迟释放音频资源，确保游戏结束音效播放完成
                new Thread(() -> {
                    try {
                        Thread.sleep(3000); // 等待3秒，确保游戏结束音效播放完成
                        audioManager.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
                
                // 保存游戏记录
                if (gameRecordService != null) {
                    // 使用固定玩家名称，无需交互
                    String playerName = "Player";
                    gameRecordService.saveGameRecord(playerName, score, difficulty);
                }
                
                // 延迟后显示排行榜界面
                new Thread(() -> {
                    try {
                        // 等待音效播放完成
                        Thread.sleep(3000);
                        
                        // 在Swing EDT中显示排行榜界面
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            // 关闭当前游戏窗口
                            JFrame currentFrame = (JFrame) javax.swing.SwingUtilities.getWindowAncestor(Game.this);
                            if (currentFrame != null) {
                                currentFrame.dispose();
                            }
                            
                            // 根据难度显示对应的排行榜界面
                            JFrame leaderboardFrame = new JFrame("排行榜 - " + difficulty);
                            JPanel leaderboardPanel;
                            
                            try {
                                if ("EASY".equals(difficulty)) {
                                    edu.hitsz.Swing.easyline easyLine = new edu.hitsz.Swing.easyline();
                                    leaderboardPanel = easyLine.getNormalPanel();
                                } else if ("HARD".equals(difficulty)) {
                                    edu.hitsz.Swing.hardline hardLine = new edu.hitsz.Swing.hardline();
                                    leaderboardPanel = hardLine.getNormalPanel();
                                } else {
                                    edu.hitsz.Swing.normalline normalLine = new edu.hitsz.Swing.normalline();
                                    leaderboardPanel = normalLine.getNormalPanel();
                                }
                                
                                leaderboardFrame.setContentPane(leaderboardPanel);
                                leaderboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                leaderboardFrame.pack();
                                leaderboardFrame.setLocationRelativeTo(null);
                                leaderboardFrame.setVisible(true);
                            } catch (Exception e) {
                                e.printStackTrace();
                                // 如果界面显示失败，继续在控制台显示排行榜
                                gameRecordService.printLeaderboard(difficulty, 10);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }

        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }

    //***********************
    //      Action 各部分
    //***********************

    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private void shootAction() {
        // 敌机射击
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            // 精英敌机、超级精英敌机和Boss敌机都可以发射子弹
            if (enemyAircraft instanceof EliteEnemy || 
                enemyAircraft instanceof ElitePlusEnemy || 
                enemyAircraft instanceof BossEnemy) {
                enemyBullets.addAll(enemyAircraft.shoot());
            }
        }

        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }
    
    private void propsMoveAction() {
        for (AbstractProp prop : props) {
            prop.forward();
        }
    }


    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // 敌机子弹攻击英雄
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                // 英雄机被敌机子弹击中
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    // 播放子弹击中音效
                    if (musicEnabled) {
                        audioManager.playBulletHitSound();
                    }
                    if (enemyAircraft.notValid()) {
                        // 获得分数
                        if (enemyAircraft instanceof BossEnemy) {
                            score += 100; // Boss敌机得分高
                        } else if (enemyAircraft instanceof ElitePlusEnemy) {
                            score += 30; // 超级精英敌机得分中等
                        } else if (enemyAircraft instanceof EliteEnemy) {
                            score += 20; // 精英敌机得分
                        } else {
                            score += 10; // 普通敌机得分
                        }
                        
                        // 根据敌机类型生成道具
                        if (enemyAircraft instanceof BossEnemy) {
                            // Boss敌机坠毁后随机掉落<=3个道具
                            int propCount = random.nextInt(4); // 0-3个道具
                            for (int i = 0; i < propCount; i++) {
                                generateProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY());
                            }
                        } else if (enemyAircraft instanceof ElitePlusEnemy || enemyAircraft instanceof EliteEnemy) {
                            // 超级精英敌机和精英敌机坠毁后随机掉落<=1个道具
                            generateProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY());
                        }
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // 我方获得道具，道具生效
        for (AbstractProp prop : props) {
            if (prop.notValid()) {
                continue;
            }
            if (heroAircraft.crash(prop)) {
                // 英雄机碰撞道具
                prop.active();
                prop.vanish();
                // 播放获得道具音效
                if (musicEnabled) {
                    audioManager.playGetSupplySound();
                }
            }
        }
    }
    
    /**
     * 在指定位置生成随机道具
     * @param x 位置x坐标
     * @param y 位置y坐标
     */
    private void generateProp(int x, int y) {
        // 随机决定是否生成道具，生成概率为70%
        if (random.nextDouble() < 0.7) {
            // 生成随机偏移，避免多个道具重叠
            int offsetX = random.nextInt(21) - 10; // -10到10的随机偏移
            int offsetY = random.nextInt(21) - 10;
            
            // 使用工厂模式创建道具
            // 随机决定生成哪种道具
            int propType = random.nextInt(4); // 0-3，增加FirePlusProp的生成概率
            AbstractProp prop;
            
            switch (propType) {
                case 0:
                    // 使用加血道具工厂生成加血道具
                    prop = bloodPropFactory.createProp(x + offsetX, y + offsetY);
                    break;
                case 1:
                    // 使用火力道具工厂生成火力道具
                    prop = firePropFactory.createProp(x + offsetX, y + offsetY);
                    break;
                case 2:
                    // 使用炸弹道具工厂生成炸弹道具
                    prop = bombPropFactory.createProp(x + offsetX, y + offsetY);
                    break;
                case 3:
                    // 使用子弹加强道具工厂生成子弹加强道具
                    prop = firePlusPropFactory.createProp(x + offsetX, y + offsetY);
                    break;
                default:
                    // 默认使用加血道具工厂生成加血道具
                    prop = bloodPropFactory.createProp(x + offsetX, y + offsetY);
                    break;
            }
            
            props.add(prop);
        }
    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        // 检查是否有Boss敌机被击毁
        boolean hasBossEnemy = enemyAircrafts.stream().anyMatch(aircraft -> aircraft instanceof BossEnemy) && 
                               enemyAircrafts.stream().anyMatch(aircraft -> !aircraft.notValid());
        
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        
        // 检查Boss敌机是否被击毁
        boolean bossDestroyed = false;
        for (AbstractAircraft enemy : enemyAircrafts) {
            if (enemy instanceof BossEnemy && enemy.notValid()) {
                bossDestroyed = true;
                // 播放炸弹爆炸音效
                if (musicEnabled) {
                    audioManager.playBombExplosionSound();
                }
                break;
            }
        }
        
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);
        
        // 如果Boss敌机被击毁，切回普通背景音乐
        if (bossDestroyed && !gameOverFlag) {
            if (musicEnabled) {
                audioManager.playBgm();
            }
        }
    }


    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     * @param  g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景,图片滚动
        BufferedImage bgImage = (currentBackgroundImage != null) ? currentBackgroundImage : ImageManager.BACKGROUND_IMAGE;
        g.drawImage(bgImage, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(bgImage, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);

        paintImageWithPositionRevised(g, enemyAircrafts);
        paintImageWithPositionRevised(g, props);

        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = ImageManager.get(object);
            assert image != null : objects.getClass().getName() + " has no image!";
        
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }


}
