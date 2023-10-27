package com.usyd.capstone.common.config;
import com.usyd.capstone.common.compents.JwtToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableWebSecurity
@ComponentScan("com.usyd.capstone")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new JwtToken(), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/public/**").permitAll() // 公开访问的API
                .antMatchers("/actuator/health").permitAll()
                .antMatchers("/chat/**").permitAll()
                .antMatchers("/notification/**").permitAll()
                .antMatchers("/imserver/**").permitAll()
                .antMatchers("/normal/**").hasRole("NORMAL")
                .antMatchers("/admin/**").hasRole("ADMIN") // 需要ADMIN角色才能访问的URL

//                  .antMatchers("/user/**").authenticated() // 需要登录才能访问的URL
                .anyRequest().authenticated() // 其他URL需要登录才能访问
                .and()
                .cors()
                .and()
                .formLogin()
                .loginPage("/login") // 登录页面的URL
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }
}