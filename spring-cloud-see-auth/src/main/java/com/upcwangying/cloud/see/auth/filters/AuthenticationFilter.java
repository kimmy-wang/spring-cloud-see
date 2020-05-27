/*
 *
 * MIT License
 *
 * Copyright (c) 2019 cloud.see.team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.upcwangying.cloud.see.auth.filters;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.upcwangying.cloud.samples.core.utils.JwtUtil;
import com.upcwangying.cloud.samples.core.utils.ResultVOUtils;
import com.upcwangying.cloud.see.auth.enums.HttpMethod;
import com.upcwangying.cloud.see.auth.properties.SeeAuthProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullStringAsEmpty;
import static com.upcwangying.cloud.samples.core.enums.ResultEnum.*;
import static com.upcwangying.cloud.samples.core.utils.JwtUtil.*;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * Authentication
 *
 * @author WANGY
 */
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends ZuulFilter {
    private AntPathMatcher pathMatcher = new AntPathMatcher();
    public static final String IS_CHECK_PERMISSION = "isCheckPermission";
    public static final String REQUEST_ATTRIBUTE_USER_ID = "request_attribute_user_id";
    public static final String REQUEST_ATTRIBUTE_USERNAME = "request_attribute_username";

    private final RouteLocator routeLocator;
    private final UrlPathHelper urlPathHelper;
    private final SeeAuthProperties properties;

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return properties.getAuthFilterOrder();
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String method = request.getMethod();
        String requestURI = urlPathHelper.getPathWithinApplication(request);
        Route route = routeLocator.getMatchingRoute(requestURI);
        String path = route.getPath();

        Map<HttpMethod, List<String>> ignoredPatterns = properties.getIgnoredPatterns();
        if (ignoredPatterns != null && !ignoredPatterns.isEmpty()) {
            List<String> patterns = ignoredPatterns.get(HttpMethod.valueOf(method));
            if (patterns != null && patterns.size() > 0) {
                boolean flag = true;
                for (String pattern: patterns) {
                    if (pathMatcher.match(pattern, path)) {
                        flag = false;
                        break;
                    }
                }
                return flag;
            }
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        HttpServletResponse response = context.getResponse();
        response.setCharacterEncoding("UTF-8");
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);

        String token = request.getHeader(HEADER_AUTH);
        if (StringUtils.isBlank(token)) {
            context.setSendZuulResponse(false);
            context.setResponseBody(JSON.toJSONString(ResultVOUtils.error(PARAM_ERROR), WriteNullStringAsEmpty));
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            context.set(IS_CHECK_PERMISSION, false);
            return null;
        }

        Map<String,String> userMap = JwtUtil.validateToken(token);
        String userId = userMap.get(TOKEN_F_ID);
        String username = userMap.get(TOKEN_F_USERNAME);
        String isExpired = userMap.get(TOKEN_F_IS_EXPIRED);
        if (JwtUtil.EXPIRED.equals(isExpired)) {
            context.setSendZuulResponse(false);
            context.setResponseBody(JSON.toJSONString(ResultVOUtils.error(TOKEN_EXPIRED), WriteNullStringAsEmpty));
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            context.set(IS_CHECK_PERMISSION, false);
            return null;
        }

        if (StringUtils.isBlank(username)) {
            context.setSendZuulResponse(false);
            context.setResponseBody(JSON.toJSONString(ResultVOUtils.error(TOKEN_ERROR), WriteNullStringAsEmpty));
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            context.set(IS_CHECK_PERMISSION, false);
            return null;
        }
        context.set(IS_CHECK_PERMISSION, true);
        context.addZuulRequestHeader(REQUEST_ATTRIBUTE_USER_ID, userId);
        context.addZuulRequestHeader(REQUEST_ATTRIBUTE_USERNAME, username);
        return null;
    }
}
