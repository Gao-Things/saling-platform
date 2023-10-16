package com.usyd.capstone.mapper;

import com.usyd.capstone.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yyf
 * @since 2023年10月16日
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

}
