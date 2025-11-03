package edu.hitsz.Swing;

import edu.hitsz.application.Main;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AircraftWar {
    private JPanel mainPanel;
    private JPanel modelPanel;
    private JPanel MusicPanel;
    private JButton easybutton;
    private JButton normalbutton;
    private JButton hardbutton;
    private JComboBox switchbox;
    private JLabel musiclabel;
    
    // 游戏设置单例
    private static GameSettings settings = new GameSettings();

    public AircraftWar() {
        // 初始化音乐模式下拉框默认值为"开"
        switchbox.setSelectedIndex(0);
        
        easybutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 设置简单难度并启动游戏
                settings.setDifficulty("EASY");
                settings.setMusicEnabled(switchbox.getSelectedItem().equals("开"));
                startGame();
            }
        });
        
        normalbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 设置普通难度并启动游戏
                settings.setDifficulty("NORMAL");
                settings.setMusicEnabled(switchbox.getSelectedItem().equals("开"));
                startGame();
            }
        });
        
        hardbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 设置困难难度并启动游戏
                settings.setDifficulty("HARD");
                settings.setMusicEnabled(switchbox.getSelectedItem().equals("开"));
                startGame();
            }
        });
        
        switchbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 更新音乐设置
                settings.setMusicEnabled(switchbox.getSelectedItem().equals("开"));
            }
        });
    }
    
    private void startGame() {
        // 关闭当前窗口
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        
        // 启动游戏主界面
        Main.startGame(settings.getDifficulty(), settings.isMusicEnabled());
    }
    
    public JPanel getMainPanel() {
        return mainPanel;
    }
    
    // 游戏设置类
    public static class GameSettings {
        private String difficulty = "NORMAL";
        private boolean musicEnabled = true;
        
        public String getDifficulty() {
            return difficulty;
        }
        
        public void setDifficulty(String difficulty) {
            this.difficulty = difficulty;
        }
        
        public boolean isMusicEnabled() {
            return musicEnabled;
        }
        
        public void setMusicEnabled(boolean musicEnabled) {
            this.musicEnabled = musicEnabled;
        }
    }
    
    public static GameSettings getSettings() {
        return settings;
    }
    
    public static void main(String[] args) {
        // 启动选择界面
        JFrame frame = new JFrame("Aircraft War - 选择难度");
        frame.setContentPane(new AircraftWar().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null); // 窗口居中
        frame.setVisible(true);
    }
}
