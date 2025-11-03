package edu.hitsz.application;

import edu.hitsz.Swing.AircraftWar;
import javax.swing.*;
import java.awt.*;

/**
 * 程序入口
 * @author hitsz
 */
public class Main {

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;
    
    // 当前游戏实例
    private static Game currentGame;
    
    // 音乐开关状态
    private static boolean musicEnabled = true;

    public static void main(String[] args) {
        // 启动选择界面
        SwingUtilities.invokeLater(() -> {
            // 检查是否已经有AircraftWar实例在运行
            try {
                // 尝试启动选择界面
                JFrame frame = new JFrame("Aircraft War - 选择难度");
                frame.setContentPane(new AircraftWar().getMainPanel());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null); // 窗口居中
                frame.setVisible(true);
            } catch (Exception e) {
                // 如果选择界面启动失败，直接启动默认难度的游戏
                startGame("NORMAL", true);
            }
        });
    }
    
    /**
     * 静态方法：启动游戏主界面
     * @param difficulty 游戏难度
     * @param musicEnabled 音乐是否开启
     */
    public static void startGame(String difficulty, boolean musicEnabled) {
        Main.musicEnabled = musicEnabled;
        
        SwingUtilities.invokeLater(() -> {
            System.out.println("启动游戏 - 难度: " + difficulty + ", 音乐: " + (musicEnabled ? "开启" : "关闭"));

            // 获得屏幕的分辨率，初始化 Frame
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            JFrame frame = new JFrame("Aircraft War");
            frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            frame.setResizable(false);
            //设置窗口的大小和位置,居中放置
            frame.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                    WINDOW_WIDTH, WINDOW_HEIGHT);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // 设置游戏背景图片
            ImageManager.setDifficultyBackground(difficulty);
            
            // 创建游戏实例
            currentGame = new Game(difficulty);
            frame.add(currentGame);
            frame.setVisible(true);
            currentGame.action();
        });
    }
    
    public static boolean isMusicEnabled() {
        return musicEnabled;
    }
    
    public static Game getCurrentGame() {
        return currentGame;
    }
}
