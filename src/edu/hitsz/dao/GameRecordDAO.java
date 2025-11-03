package edu.hitsz.dao;

import java.util.List;

/**
 * 游戏记录数据访问对象接口
 * 定义数据操作的标准方法
 */
public interface GameRecordDAO {
    /**
     * 保存游戏记录
     * @param record 游戏记录对象
     */
    void saveRecord(GameRecord record);
    
    /**
     * 获取指定难度的前N名记录
     * @param difficulty 游戏难度
     * @param limit 记录数量限制
     * @return 游戏记录列表
     */
    List<GameRecord> getTopRecords(String difficulty, int limit);
    
    /**
     * 获取指定难度的所有记录
     * @param difficulty 游戏难度
     * @return 游戏记录列表
     */
    List<GameRecord> getAllRecords(String difficulty);
    
    /**
     * 根据ID获取记录
     * @param id 记录ID
     * @return 游戏记录对象，如果不存在返回null
     */
    GameRecord getRecordById(int id);
    
    /**
     * 更新游戏记录
     * @param record 游戏记录对象
     */
    void updateRecord(GameRecord record);
    
    /**
     * 删除游戏记录
     * @param id 记录ID
     */
    void deleteRecord(int id);
}