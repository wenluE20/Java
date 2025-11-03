package edu.hitsz.dao;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 游戏记录数据访问对象实现类
 * 使用文件存储游戏记录数据
 */
public class GameRecordDAOImpl implements GameRecordDAO {
    private static final String DATA_FILE_PATH = "game_records.dat";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static int nextId = 1; // 用于生成记录ID
    
    public GameRecordDAOImpl() {
        // 初始化时从文件加载记录，获取最大ID
        initNextId();
    }
    
    private void initNextId() {
        List<GameRecord> allRecords = getAllRecordsFromFile();
        if (!allRecords.isEmpty()) {
            nextId = allRecords.stream()
                    .mapToInt(GameRecord::getId)
                    .max()
                    .orElse(0) + 1;
        }
    }
    
    /**
     * 从文件中读取所有游戏记录
     */
    private List<GameRecord> getAllRecordsFromFile() {
        List<GameRecord> records = new ArrayList<>();
        File file = new File(DATA_FILE_PATH);
        
        if (!file.exists()) {
            return records;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    try {
                        int id = Integer.parseInt(parts[0]);
                        String playerName = parts[1];
                        int score = Integer.parseInt(parts[2]);
                        Date recordTime = DATE_FORMAT.parse(parts[3]);
                        String difficulty = parts[4];
                        
                        records.add(new GameRecord(id, playerName, score, recordTime, difficulty));
                    } catch (NumberFormatException | ParseException e) {
                        // 忽略格式错误的行
                        System.err.println("解析记录失败: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("读取游戏记录文件失败: " + e.getMessage());
        }
        
        return records;
    }
    
    /**
     * 将所有游戏记录写入文件
     */
    private void saveAllRecordsToFile(List<GameRecord> records) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE_PATH))) {
            for (GameRecord record : records) {
                String line = String.format("%d|%s|%d|%s|%s",
                        record.getId(),
                        record.getPlayerName(),
                        record.getScore(),
                        DATE_FORMAT.format(record.getRecordTime()),
                        record.getDifficulty());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("保存游戏记录文件失败: " + e.getMessage());
        }
    }
    
    @Override
    public void saveRecord(GameRecord record) {
        List<GameRecord> allRecords = getAllRecordsFromFile();
        
        // 设置记录ID
        if (record.getId() == 0) {
            record.setId(nextId++);
        }
        
        allRecords.add(record);
        saveAllRecordsToFile(allRecords);
    }
    
    @Override
    public List<GameRecord> getTopRecords(String difficulty, int limit) {
        List<GameRecord> allRecords = getAllRecordsFromFile();
        
        // 筛选指定难度的记录，按分数降序排序，取前N条
        return allRecords.stream()
                .filter(record -> difficulty.equals(record.getDifficulty()))
                .sorted(Comparator.comparingInt(GameRecord::getScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<GameRecord> getAllRecords(String difficulty) {
        List<GameRecord> allRecords = getAllRecordsFromFile();
        
        // 筛选指定难度的记录，按分数降序排序
        return allRecords.stream()
                .filter(record -> difficulty.equals(record.getDifficulty()))
                .sorted(Comparator.comparingInt(GameRecord::getScore).reversed())
                .collect(Collectors.toList());
    }
    
    @Override
    public GameRecord getRecordById(int id) {
        List<GameRecord> allRecords = getAllRecordsFromFile();
        
        return allRecords.stream()
                .filter(record -> record.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public void updateRecord(GameRecord record) {
        List<GameRecord> allRecords = getAllRecordsFromFile();
        
        // 查找并更新记录
        for (int i = 0; i < allRecords.size(); i++) {
            if (allRecords.get(i).getId() == record.getId()) {
                allRecords.set(i, record);
                saveAllRecordsToFile(allRecords);
                return;
            }
        }
    }
    
    @Override
    public void deleteRecord(int id) {
        List<GameRecord> allRecords = getAllRecordsFromFile();
        
        // 移除指定ID的记录
        allRecords.removeIf(record -> record.getId() == id);
        saveAllRecordsToFile(allRecords);
    }
}