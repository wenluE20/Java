package edu.hitsz.application;


import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.ElitePlusEnemy;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.prop.BloodProp;
import edu.hitsz.prop.FireProp;
import edu.hitsz.prop.BombProp;
import edu.hitsz.prop.FirePlusProp;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 综合管理图片的加载，访问
 * 提供图片的静态访问方法
 *
 * @author hitsz
 */
public class ImageManager {

    /**
     * 类名-图片 映射，存储各基类的图片 <br>
     * 可使用 CLASSNAME_IMAGE_MAP.get( obj.getClass().getName() ) 获得 obj 所属基类对应的图片
     */
    private static final Map<String, BufferedImage> CLASSNAME_IMAGE_MAP = new HashMap<>();

    public static BufferedImage BACKGROUND_IMAGE;
    public static BufferedImage HERO_IMAGE;
    public static BufferedImage HERO_BULLET_IMAGE;
    public static BufferedImage ENEMY_BULLET_IMAGE;
    public static BufferedImage MOB_ENEMY_IMAGE;
    public static BufferedImage ELITE_ENEMY_IMAGE;
    public static BufferedImage ELITE_PLUS_ENEMY_IMAGE;
    public static BufferedImage BOSS_ENEMY_IMAGE;
    public static BufferedImage BLOOD_PROP_IMAGE;
    public static BufferedImage FIRE_PROP_IMAGE;
    public static BufferedImage BOMB_PROP_IMAGE;
    public static BufferedImage FIRE_PLUS_PROP_IMAGE;

    // 背景图片路径映射
    private static final Map<String, String> DIFFICULTY_BACKGROUND_MAP = new HashMap<>();
    
    static {
        // 初始化难度与背景图片的映射关系
        DIFFICULTY_BACKGROUND_MAP.put("EASY", "src/images/bg1.jpg");
        DIFFICULTY_BACKGROUND_MAP.put("NORMAL", "src/images/bg2.jpg");
        DIFFICULTY_BACKGROUND_MAP.put("HARD", "src/images/bg3.jpg");
        // 默认背景图片
        DIFFICULTY_BACKGROUND_MAP.put("DEFAULT", "src/images/bg.jpg");
        
        try {
            // 默认加载普通难度的背景图片
            setDifficultyBackground("NORMAL");

            HERO_IMAGE = ImageIO.read(new FileInputStream("src/images/hero.png"));
            MOB_ENEMY_IMAGE = ImageIO.read(new FileInputStream("src/images/mob.png"));
            ELITE_ENEMY_IMAGE = ImageIO.read(new FileInputStream("src/images/elite.png"));
            // 超级精英敌机图片
            try {
                ELITE_PLUS_ENEMY_IMAGE = ImageIO.read(new FileInputStream("src/images/elitePlus.png"));
            } catch (IOException e) {
                // 如果找不到超级精英敌机的图片，使用精英敌机的图片作为后备
                System.out.println("超级精英敌机图片未找到，使用精英敌机图片作为后备");
                ELITE_PLUS_ENEMY_IMAGE = ELITE_ENEMY_IMAGE;
            }
            
            // Boss敌机图片
            try {
                BOSS_ENEMY_IMAGE = ImageIO.read(new FileInputStream("src/images/boss.png"));
            } catch (IOException e) {
                // 如果找不到Boss敌机的图片，使用精英敌机的图片作为后备
                System.out.println("Boss敌机图片未找到，使用精英敌机图片作为后备");
                BOSS_ENEMY_IMAGE = ELITE_ENEMY_IMAGE;
            }
            HERO_BULLET_IMAGE = ImageIO.read(new FileInputStream("src/images/bullet_hero.png"));
            ENEMY_BULLET_IMAGE = ImageIO.read(new FileInputStream("src/images/bullet_enemy.png"));
            BLOOD_PROP_IMAGE = ImageIO.read(new FileInputStream("src/images/prop_blood.png"));
            FIRE_PROP_IMAGE = ImageIO.read(new FileInputStream("src/images/prop_bullet.png")); // 火力道具使用子弹道具图片
            BOMB_PROP_IMAGE = ImageIO.read(new FileInputStream("src/images/prop_bomb.png"));
            // 子弹加强道具图片
            try {
                FIRE_PLUS_PROP_IMAGE = ImageIO.read(new FileInputStream("src/images/prop_bulletPlus.png"));
            } catch (IOException e) {
                // 如果找不到子弹加强道具的图片，使用火力道具的图片作为后备
                System.out.println("子弹加强道具图片未找到，使用火力道具图片作为后备");
                FIRE_PLUS_PROP_IMAGE = FIRE_PROP_IMAGE;
            }

            CLASSNAME_IMAGE_MAP.put(HeroAircraft.class.getName(), HERO_IMAGE);
            CLASSNAME_IMAGE_MAP.put(MobEnemy.class.getName(), MOB_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EliteEnemy.class.getName(), ELITE_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(ElitePlusEnemy.class.getName(), ELITE_PLUS_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BossEnemy.class.getName(), BOSS_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(HeroBullet.class.getName(), HERO_BULLET_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EnemyBullet.class.getName(), ENEMY_BULLET_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BloodProp.class.getName(), BLOOD_PROP_IMAGE);
            CLASSNAME_IMAGE_MAP.put(FireProp.class.getName(), FIRE_PROP_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BombProp.class.getName(), BOMB_PROP_IMAGE);
            CLASSNAME_IMAGE_MAP.put(FirePlusProp.class.getName(), FIRE_PLUS_PROP_IMAGE);

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    /**
     * 根据游戏难度设置对应的背景图片
     * @param difficulty 游戏难度，可选值："EASY", "NORMAL", "HARD"
     */
    public static void setDifficultyBackground(String difficulty) {
        try {
            String backgroundPath = DIFFICULTY_BACKGROUND_MAP.getOrDefault(difficulty, DIFFICULTY_BACKGROUND_MAP.get("DEFAULT"));
            
            // 尝试加载指定路径的背景图片
            try {
                BACKGROUND_IMAGE = ImageIO.read(new FileInputStream(backgroundPath));
                System.out.println("成功加载背景图片: " + backgroundPath + " (难度: " + difficulty + ")");
            } catch (IOException e) {
                // 如果指定难度的背景图片加载失败，尝试其他可用的背景图片
                System.out.println("加载指定背景图片失败: " + backgroundPath + "，尝试加载备用图片");
                
                // 尝试加载bg1到bg5.jpg
                for (int i = 1; i <= 5; i++) {
                    try {
                        String altPath = "src/images/bg" + i + ".jpg";
                        BACKGROUND_IMAGE = ImageIO.read(new FileInputStream(altPath));
                        System.out.println("成功加载备用背景图片: " + altPath);
                        return;
                    } catch (IOException ignored) {
                        // 继续尝试下一个图片
                    }
                }
                
                // 如果所有备用图片都加载失败，使用默认背景图片
                BACKGROUND_IMAGE = ImageIO.read(new FileInputStream(DIFFICULTY_BACKGROUND_MAP.get("DEFAULT")));
                System.out.println("使用默认背景图片: " + DIFFICULTY_BACKGROUND_MAP.get("DEFAULT"));
            }
        } catch (IOException e) {
            System.err.println("无法加载背景图片: " + e.getMessage());
        }
    }

    public static BufferedImage get(String className){
        return CLASSNAME_IMAGE_MAP.get(className);
    }

    public static BufferedImage get(Object obj){
        if (obj == null){
            return null;
        }
        return get(obj.getClass().getName());
    }

}
