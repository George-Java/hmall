package com.hmall.common.interceptors;
import com.hmall.common.utils.UserContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
@ConditionalOnClass(RequestInterceptor.class)
public class UserInfoFeignInterceptor {
    @Bean
    public RequestInterceptor userInfoRequestInterceptor() {
        return (RequestTemplate template) -> {
            Long userId = UserContext.getUser();
            if (userId != null) template.header("userId", userId.toString());
        };
    }
}
