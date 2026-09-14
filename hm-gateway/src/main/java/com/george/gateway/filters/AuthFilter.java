package com.george.gateway.filters;

import cn.hutool.core.text.AntPathMatcher;
import com.george.gateway.config.AuthProperties;
import com.george.gateway.util.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered {
    private final AuthProperties authProperties;
    private final JwtTool jwtTool;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求对象request
        ServerHttpRequest request = exchange.getRequest();

        //判断是否需要拦截
        String path = request.getPath().toString();
        for (String pathPattern : authProperties.getExcludePaths()) {
            if (antPathMatcher.match(path, pathPattern)) {
                return chain.filter(exchange);
            }
        }

        //如果令牌错误，返回401
        HttpHeaders headers = request.getHeaders();
        ServerHttpResponse response = exchange.getResponse();
        List<String> authorization = headers.get("Authorization");
        if (authorization == null || authorization.isEmpty()) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        //获取token
        String token = authorization.getFirst();

        //解析用户id信息
        Long userId = jwtTool.parseToken(token);

        //传递用户信息
        //headers.add("userId", String.valueOf(userId));
        ServerWebExchange swe = exchange.mutate()
                .request(builder -> builder.header("userId", String.valueOf(userId)))
                .build();

        return chain.filter(swe);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
