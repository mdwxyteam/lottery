package com.md.luck.lottery.config.authority;

import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.Customer;
import com.md.luck.lottery.service.WexinService;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final UserDetailsService userDetailsService;
    private final WexinService weixinService;
    private final JwtTokenUtil jwtTokenUtil;
    private final String tokenHeader;

    public JwtAuthorizationTokenFilter(@Qualifier("jwtUserDetailsService") UserDetailsService userDetailsService,
                                       WexinService weixinService, JwtTokenUtil jwtTokenUtil, @Value("${jwt.token}") String tokenHeader) {
        this.userDetailsService = userDetailsService;
        this.weixinService = weixinService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.tokenHeader = tokenHeader;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String requestHeader = request.getHeader(this.tokenHeader);
        String username = null;
        String openid = null;
        String authToken = null;
        // 微信端登录的接口
        if (requestHeader != null && requestHeader.startsWith("Weixin")) {
//        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            authToken = requestHeader.substring(6);
            try {
                openid = jwtTokenUtil.getDataFromToken(authToken);
            } catch (AuthenticationException e) {
                log.error(e);
            }
        }
        if (openid != null && SecurityContextHolder.getContext().getAuthentication() == null) {

//            SecurityUserDetails userDetails = new SecurityUserDetails(customer.getNickName(), null);
            ResponMsg responMsgObj = weixinService.getRoleByOpenid(openid);
            SecurityUserDetails userDetails = (SecurityUserDetails) responMsgObj.getData();
            userDetails.setOpenid(openid);
            if (jwtTokenUtil.validateOpenidToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                request.setAttribute("openid", openid);
            }

        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }
        chain.doFilter(request, response);
    }
}
