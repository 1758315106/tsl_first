package com.cdtu.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.cdtu.reggie.common.BaseContext;
import com.cdtu.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
* 检查用户是否完成登录
*/
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

        public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


        //1. 获取本次请求的URI
        String requestURI = request.getRequestURI();
        log.info("拦截到请求，{}",request.getRequestURI());
        //定义不需要过滤的请求
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };

        //2. 判断本次请求, 是否需要登录, 才可以访问
        boolean check = check(urls,requestURI);
        //3. 如果不需要，则直接放行

        if (check){
            log.info("本次请求放行",requestURI);
            filterChain.doFilter(request,response);
            return;
        }

      // 4. 判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("user") != null){
            log.info("用户登录成功，账号为{}",request.getSession().getAttribute("user"));
            Long userId = (Long)request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request,response);
            return;
        }

        if (request.getSession().getAttribute("employee") != null){
            log.info("用户登录成功，账号为{}",request.getSession().getAttribute("employee"));
            Long empId = (Long)request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request,response);
            return;
        }
        log.info("用户未登录");

        //5. 如果未登录, 则返回未登录结果

        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    public boolean check(String[] urls,String requestURI){
        for(String url: urls){
        boolean match = PATH_MATCHER.match(url,requestURI);
        if (match){
            return true;
             }
        }
        return false;
    }
}
