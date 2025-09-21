package com.example.nettyim.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.nettyim.entity.SensitiveWord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 敏感词Mapper
 */
@Mapper
public interface SensitiveWordMapper extends BaseMapper<SensitiveWord> {
    
    /**
     * 查询所有启用的敏感词
     */
    @Select("SELECT * FROM sensitive_words WHERE status = 1 ORDER BY level DESC, word")
    List<SensitiveWord> selectAllEnabled();
    
    /**
     * 按分类查询敏感词
     */
    @Select("SELECT * FROM sensitive_words WHERE category = #{category} AND status = 1 ORDER BY level DESC")
    List<SensitiveWord> selectByCategory(@Param("category") Integer category);
    
    /**
     * 按等级查询敏感词
     */
    @Select("SELECT * FROM sensitive_words WHERE level = #{level} AND status = 1")
    List<SensitiveWord> selectByLevel(@Param("level") Integer level);
    
    /**
     * 检查敏感词是否存在
     */
    @Select("SELECT COUNT(*) FROM sensitive_words WHERE word = #{word}")
    Integer checkWordExists(@Param("word") String word);
    
    /**
     * 批量插入敏感词
     */
    @Select("INSERT INTO sensitive_words (word, category, level, action, replacement, status, creator_id, created_at, updated_at) VALUES " +
            "<foreach collection='words' item='item' separator=','>" +
            "(#{item.word}, #{item.category}, #{item.level}, #{item.action}, #{item.replacement}, #{item.status}, #{item.creatorId}, NOW(), NOW())" +
            "</foreach>")
    Integer batchInsert(@Param("words") List<SensitiveWord> words);
}