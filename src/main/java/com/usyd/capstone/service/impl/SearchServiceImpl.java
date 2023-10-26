package com.usyd.capstone.service.impl;

import com.usyd.capstone.entity.Search;
import com.usyd.capstone.mapper.SearchMapper;
import com.usyd.capstone.service.SearchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Mengting
 * @since 2023年10月25日
 */
@Service
public class SearchServiceImpl extends ServiceImpl<SearchMapper, Search> implements SearchService {

}
