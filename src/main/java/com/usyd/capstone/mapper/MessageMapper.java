package com.usyd.capstone.mapper;

import com.usyd.capstone.common.DTO.MessageHistoryDTO;
import com.usyd.capstone.common.DTO.MessageUserDTO;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    List<MessageUserDTO> getMessageListByUserId(Integer userId);

    List<MessageHistoryDTO> getMessageListByUserIdAndRemoteUserId(Integer userId, Integer remoteUserId);

    Boolean updateDeleteUserIdByCondition(Integer userId, Integer remoteUserId);
}
