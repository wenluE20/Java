package edu.hitsz.dao;

import java.util.Date;

/**
 * 游戏记录数据传输对象（DTO）
 * 用于在业务层和数据访问层之间传递数据
 */
public class GameRecord {
    private int id;
    private String playerName;
    private int score;
    private Date recordTime;
    private String difficulty;
    
    public GameRecord() {
        this.recordTime = new Date();
    }
    
    public GameRecord(String playerName, int score, String difficulty) {
        this.playerName = playerName;
        this.score = score;
        this.difficulty = difficulty;
        this.recordTime = new Date();
    }
    
    public GameRecord(int id, String playerName, int score, Date recordTime, String difficulty) {
        this.id = id;
        this.playerName = playerName;
        this.score = score;
        this.recordTime = recordTime;
        this.difficulty = difficulty;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public Date getRecordTime() {
        return recordTime;
    }
    
    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }
    
    public String getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    @Override
    public String toString() {
        return "GameRecord{" +
                "id=" + id +
                ", playerName='" + playerName + '\'' +
                ", score=" + score +
                ", recordTime=" + recordTime +
                ", difficulty='" + difficulty + '\'' +
                '}';
    }
}