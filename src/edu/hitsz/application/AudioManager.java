package edu.hitsz.application;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * 音效管理器
 * 负责游戏中所有音频的加载和播放
 */
public class AudioManager {
    
    // 音效文件路径
    private static final String BGM_PATH = "src/videos/bgm.wav";
    private static final String BGM_BOSS_PATH = "src/videos/bgm_boss.wav";
    private static final String BULLET_HIT_PATH = "src/videos/bullet_hit.wav";
    private static final String BOMB_EXPLOSION_PATH = "src/videos/bomb_explosion.wav";
    private static final String GET_SUPPLY_PATH = "src/videos/get_supply.wav";
    private static final String GAME_OVER_PATH = "src/videos/game_over.wav";
    
    // 当前播放的背景音乐Clip
    private Clip bgmClip = null;
    private Clip bossBgmClip = null;
    
    // 音效开关状态
    private boolean enabled = true;
    
    // 单例实例
    private static volatile AudioManager instance;
    
    private AudioManager() {
    }
    
    /**
     * 获取音效管理器单例
     * @return AudioManager实例
     */
    public static AudioManager getInstance() {
        if (instance == null) {
            synchronized (AudioManager.class) {
                if (instance == null) {
                    instance = new AudioManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * 设置音效开关
     * @param enabled 是否开启音效
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            // 如果关闭音效，停止所有正在播放的音频
            stopBgm();
            stopBossBgm();
        }
    }
    
    /**
     * 播放背景音乐
     */
    public void playBgm() {
        if (!enabled) return;
        
        // 先停止可能播放的Boss背景音乐
        stopBossBgm();
        
        if (bgmClip == null || !bgmClip.isRunning()) {
            playLoopSound(BGM_PATH, true);
        }
    }
    
    /**
     * 播放Boss背景音乐
     */
    public void playBossBgm() {
        if (!enabled) return;
        
        // 先停止普通背景音乐
        stopBgm();
        
        if (bossBgmClip == null || !bossBgmClip.isRunning()) {
            playLoopSound(BGM_BOSS_PATH, false);
        }
    }
    
    /**
     * 停止背景音乐
     */
    public void stopBgm() {
        stopClip(bgmClip);
        bgmClip = null;
    }
    
    /**
     * 停止Boss背景音乐
     */
    public void stopBossBgm() {
        stopClip(bossBgmClip);
        bossBgmClip = null;
    }
    
    /**
     * 播放子弹击中音效
     */
    public void playBulletHitSound() {
        if (enabled) {
            playSound(BULLET_HIT_PATH);
        }
    }
    
    /**
     * 播放炸弹爆炸音效
     */
    public void playBombExplosionSound() {
        if (enabled) {
            playSound(BOMB_EXPLOSION_PATH);
        }
    }
    
    /**
     * 播放获得道具音效
     */
    public void playGetSupplySound() {
        if (enabled) {
            playSound(GET_SUPPLY_PATH);
        }
    }
    
    /**
     * 播放游戏结束音效
     */
    public void playGameOverSound() {
        if (enabled) {
            // 先停止所有背景音乐
            stopBgm();
            stopBossBgm();
            // 播放游戏结束音效
            playSound(GAME_OVER_PATH);
        }
    }
    
    /**
     * 播放单次音效
     * @param filePath 音频文件路径
     */
    private void playSound(String filePath) {
        new Thread(() -> {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
                
                // 监听播放完成事件，释放资源
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                        try {
                            audioInputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.out.println("音效加载失败: " + filePath);
                e.printStackTrace();
            }
        }).start();
    }
    
    /**
     * 播放循环音效
     * @param filePath 音频文件路径
     * @param isNormalBgm 是否为普通背景音乐
     */
    private void playLoopSound(String filePath, boolean isNormalBgm) {
        new Thread(() -> {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                
                // 保存当前播放的Clip
                if (isNormalBgm) {
                    bgmClip = clip;
                } else {
                    bossBgmClip = clip;
                }
                
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.out.println("背景音乐加载失败: " + filePath);
                e.printStackTrace();
            }
        }).start();
    }
    
    /**
     * 停止指定的音频剪辑
     * @param clip 要停止的音频剪辑
     */
    private void stopClip(Clip clip) {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
    
    /**
     * 释放所有资源
     */
    public void release() {
        stopBgm();
        stopBossBgm();
    }
}