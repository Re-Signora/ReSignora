package edu.hitsz.dao;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * 历史记录数据对象接口
 * @author Chiro
 */
public interface HistoryDAO {
    /**
     * 获取所有历史数据
     * @return 历史数据列表
     */
    List<HistoryObject> getAll();

    /**
     * 使用名字查询列表
     * @param name 名称
     * @return 查询到的数据项目
     */
    Optional<HistoryObject> getByName(String name);

    /**
     * 使用 utc 为主键更新，newHistory 的 utc 会被更新
     * @param utc 指定的 utc
     * @param newHistory 新的数据
     * @return 是否更新成功
     */
    Boolean updateByUtc(int utc, HistoryObject newHistory);

    /**
     * 使用 utc 为主键删除对应项目
     * @param utc 指定的 utc
     * @return 是否删除成功
     */
    Boolean deleteByUtc(int utc);

    /**
     * 删除所有项目
     */
    void deleteAll();

    /**
     * 添加一个项目
     * @param historyObject 项目
     */
    void addOne(HistoryObject historyObject);

    /**
     * 写入磁盘数据
     */
    void dump();

    /**
     * 读取磁盘数据
     */
    void load();
}
