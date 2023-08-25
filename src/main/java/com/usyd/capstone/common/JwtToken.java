package com.usyd.capstone.common;
import com.usyd.capstone.common.Enums.ROLE;
import com.usyd.capstone.common.Enums.SystemSecretKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JwtToken extends OncePerRequestFilter {

    private static final long EXPIRATION_TIME = 86400000; // 24小时，单位为毫秒

    public static String generateToken(String email, ROLE role) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        String token = Jwts.builder()
                .setSubject(email)
                .claim("role", role.getValue())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, SystemSecretKey.JWT_SECRET_KEY.getValue())
                .compact();

        //未测试，但是留个思路在这
        //如果role是多个的
//        List<String> roles = /* your list of roles */;
//        String token = Jwts.builder()
//                .setSubject(email)
//                .claim("roles", roles)
//                .setIssuedAt(now)
//                .setExpiration(expiration)
//                .signWith(SignatureAlgorithm.HS256, SystemSecretKey.JWT_SECRET_KEY.getValue())
//                .compact();
        return token;
    }

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader(HEADER_STRING);

        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            String token = header.replace(TOKEN_PREFIX, "");

            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(SystemSecretKey.JWT_SECRET_KEY.getValue())
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();
                @SuppressWarnings("unchecked")
                String role = (String) claims.get("role");

                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(role));
                //未测试，但是留个思路在这
                //如果role是多个的
//                @SuppressWarnings("unchecked")
//                List<String> roles = (List<String>) claims.get("roles");
//
//                List<SimpleGrantedAuthority> authorities = roles.stream()
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (Exception e) {
                // Token validation failed
            }
        }

        chain.doFilter(request, response);
    }

}
