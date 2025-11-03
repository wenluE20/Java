package edu.hitsz.dao;

import java.util.List;

/**
 * 游戏记录业务逻辑层
 * 处理游戏记录相关的业务操作
 */
public class GameRecordService {
    private GameRecordDAO gameRecordDAO;
    
    public GameRecordService() {
        // 使用具体的数据访问对象实现
        this.gameRecordDAO = new GameRecordDAOImpl();
    }
    
    /**
     * 保存新的游戏记录
     * @param playerName 玩家名称
     * @param score 游戏得分
     * @param difficulty 游戏难度
     */
    public void saveGameRecord(String playerName, int score, String difficulty) {
        GameRecord record = new GameRecord(playerName, score, difficulty);
        gameRecordDAO.saveRecord(record);
        System.out.println("游戏记录已保存: " + record);
    }
    
    /**
     * 获取指定难度的排行榜
     * @param difficulty 游戏难度
     * @param limit 显示的记录数量
     * @return 游戏记录列表
     */
    public List<GameRecord> getTopRecords(String difficulty, int limit) {
        return gameRecordDAO.getTopRecords(difficulty, limit);
    }
    
    /**
     * 打印指定难度的排行榜
     * @param difficulty 游戏难度
     * @param limit 显示的记录数量
     */
    public void printLeaderboard(String difficulty, int limit) {
        List<GameRecord> records = getTopRecords(difficulty, limit);
        
        System.out.println("\n============== 排行榜 ==============");
        System.out.println("难度: " + difficulty);
        System.out.println("排名 | 玩家名称 | 得分 | 记录时间");
        System.out.println("------------------------------------");
        
        if (records.isEmpty()) {
            System.out.println("暂无记录");
        } else {
            for (int i = 0; i < records.size(); i++) {
                GameRecord record = records.get(i);
                System.out.printf("%2d   | %-8s | %4d | %s\n",
                        (i + 1),
                        record.getPlayerName(),
                        record.getScore(),
                        record.getRecordTime());
            }
        }
        System.out.println("====================================\n");
    }
    
    /**
     * 处理游戏结束逻辑
     * @param playerName 玩家名称
     * @param score 游戏得分
     * @param difficulty 游戏难度
     */
    public void handleGameOver(String playerName, int score, String difficulty) {
        // 保存游戏记录
        saveGameRecord(playerName, score, difficulty);
        
        // 显示排行榜
        printLeaderboard(difficulty, 10);
    }
    
    /**
     * 删除指定ID的游戏记录
     * @param id 记录ID
     */
    public void deleteRecord(int id) {
        gameRecordDAO.deleteRecord(id);
        System.out.println("游戏记录已删除: ID = " + id);
    }
}