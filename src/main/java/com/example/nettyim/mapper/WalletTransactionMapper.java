package com.example.nettyim.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.nettyim.entity.WalletTransaction;
import org.apache.ibatis.annotations.Mapper;

/**
 * 钱包交易记录数据访问层
 */
@Mapper
public interface WalletTransactionMapper extends BaseMapper<WalletTransaction> {
}