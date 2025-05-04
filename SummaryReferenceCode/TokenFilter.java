package com.itheima.filter;


import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.Emp;
import com.itheima.utils.CurrentHolder;
import com.itheima.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
@Slf4j
@WebFilter(urlPatterns = "/*")
public class TokenFilter implements Filter {

    private final EmpMapper empMapper;

    public TokenFilter(@Autowired EmpMapper empMapper) {
        this.empMapper = empMapper;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 1.获取到请求路径
        String requestURI = request.getRequestURI(); // URI指的是资源的路径
        // 2.判断是不是登录请求,如果路径中包含login,则说明是登录操作,放行
        if (requestURI.contains("/login")) {
            log.info("登录请求,直接放行");
            // 放行
            filterChain.doFilter(request, response);
            return;
        }
        // 3.获取请求头中的token
        String token = request.getHeader("token");
        // 4.判断token是否存在,如果不存在返回401,说明用户没有登录,转到登录界面
        if (token == null || token.isEmpty()) {
            log.info("用户未登录");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        // 5.如果token存在,校验令牌,如果令牌解析报错,则返回错误信息,响应401状态码
        // 如果解析成功了,还要去库里面检验一下员工存不存在
        try {
            Claims claims = JwtUtils.parseToken(token);
            Integer empId = Integer.valueOf(claims.get("id").toString());
            // 检验一下当前员工是否还在职
            Emp currEmp = empMapper.findById(empId);
            if (currEmp == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            CurrentHolder.setCurrentId(empId);
        } catch (Exception e) {
            log.info("令牌非法,响应状态码401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        // 6.校验通过放行请求和响应资源
        log.info("令牌合法,放行");
        filterChain.doFilter(request, response);

        // 7.删除线程局部变量中的empId
        CurrentHolder.remove();
    }
}
