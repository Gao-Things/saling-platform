package com.usyd.capstone.common.config;

import com.github.dreamyoung.mprelation.AutoMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoMapperConfig {
    @Bean
    public AutoMapper autoMapper()
    {
        //配置实体类所在目录（可多个,暂时不支持通过符*号配置,逗号分隔）
        return new AutoMapper(new String[]{"com.usyd.capstone.entity",
                });
    }
}
