package lxk.framework;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class CrossFilter implements Filter {

    private static List<String> allowOrigin = Arrays.asList("http://yipianzhen.com", "https://yipianzhen.com");

    private static String allowHeards = "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, Cookie";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String origin = request.getHeader("Origin");
        if (request.getMethod() == "OPTIONS" && origin != null){
            if (!StringUtils.isEmpty(origin) && allowOrigin.contains(origin)) {
                response.addHeader("Access-Control-Allow-Origin", origin);
                String requestHeaders = request.getHeader("Access-Control-Request-Headers");
                String reqHeard = !StringUtils.isEmpty(requestHeaders) ? allowHeards.concat(", ").concat(requestHeaders) : allowHeards;
                response.addHeader("Access-Control-Allow-Headers", reqHeard);
                response.addHeader("Access-Control-Expose-Headers", reqHeard);
                response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
                response.addHeader("Access-Control-Max-Age", "3600");// 最大有效缓存时间5分钟
                response.addHeader("Access-Control-Allow-Credentials", "true");

                // response.setHeader("Access-Control-Allow-Origin", "*");
                // response.setHeader("Access-Control-Allow-Methods", "*");
                // response.setHeader("Access-Control-Max-Age", "3600");
                // response.setHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
            }
        }
       // OPTIONS 请求  springMVC是不是直接  return  了？
        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {}
    @Override
    public void destroy() {}

}
