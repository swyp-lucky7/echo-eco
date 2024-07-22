package teamseven.echoeco.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class SessionInterceptor implements HandlerInterceptor {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // spring security 세션에 저장된 user 이름 가져오기
        Object name = request.getAttribute("userName");
        if (name != null) {
            if(modelAndView != null) {
                modelAndView.addObject("userName", (String) name);
            }
        }
    }
}
