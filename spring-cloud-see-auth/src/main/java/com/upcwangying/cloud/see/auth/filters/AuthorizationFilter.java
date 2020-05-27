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
import com.upcwangying.cloud.samples.core.utils.ResultVOUtils;
import com.upcwangying.cloud.samples.core.vo.ResultVO;
import com.upcwangying.cloud.samples.user.common.entity.PermissionOutput;
import com.upcwangying.cloud.see.auth.properties.SeeAuthProperties;
import com.upcwangying.cloud.see.auth.service.UserPermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullStringAsEmpty;
import static com.upcwangying.cloud.samples.core.enums.ResultEnum.PERMISSION_DENIED;
import static com.upcwangying.cloud.see.auth.filters.AuthenticationFilter.IS_CHECK_PERMISSION;
import static com.upcwangying.cloud.see.auth.filters.AuthenticationFilter.REQUEST_ATTRIBUTE_USER_ID;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * Authorization
 *
 * @author WANGY
 */
@Slf4j
@RequiredArgsConstructor
public class AuthorizationFilter extends ZuulFilter {

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    private final RouteLocator routeLocator;
    private final UrlPathHelper urlPathHelper;
    private final UserPermissionService userPermissionService;
    private final SeeAuthProperties seeAuthProperties;

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return seeAuthProperties.getPermissionFilterOrder();
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        Object requestAttributeUsername = context.get(IS_CHECK_PERMISSION);
        return requestAttributeUsername != null && (Boolean) requestAttributeUsername;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String requestURI = urlPathHelper.getPathWithinApplication(request);
        Route route = routeLocator.getMatchingRoute(requestURI);
        String path = route.getPath();
        Map<String, String> zuulRequestHeaders = context.getZuulRequestHeaders();

        String userId = zuulRequestHeaders.get(REQUEST_ATTRIBUTE_USER_ID.toLowerCase());
        // notice:
        //  1.使用Feign调用user服务(/users/{id}/permissions), 获取用户权限集合
        //  2.判断当前Url是否在权限集合中, 若不在集合中, 则返回权限不足
        //  /**, /*, /?,
        ResultVO<List<PermissionOutput>> resultVO = userPermissionService.getUserPermissionByUserId(userId);
        Integer code = resultVO.getCode();
        Object data = resultVO.getData();
        if (code != 200 || !(data instanceof Collection)) {
            context.setSendZuulResponse(false);
            context.setResponseBody(JSON.toJSONString(ResultVOUtils.error(PERMISSION_DENIED), WriteNullStringAsEmpty));
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            return null;
        }

        List<PermissionOutput> permissionOutputList =  resultVO.getData();
        Map<String, List<PermissionOutput>> permissionMap = permissionOutputList.stream()
                .collect(Collectors.groupingBy(PermissionOutput::getRequestMethod));
        List<PermissionOutput> permissionOutputs = permissionMap.get(request.getMethod());
        boolean flag = false;
        if (permissionOutputs != null && !permissionOutputs.isEmpty()) {
            for (PermissionOutput permissionOutput : permissionOutputs) {
                if (pathMatcher.match(permissionOutput.getPermissionPath(), path)) {
                    flag = true;
                    break;
                }
            }
        }

        if (!flag) {
            context.setSendZuulResponse(false);
            context.setResponseBody(JSON.toJSONString(ResultVOUtils.error(PERMISSION_DENIED), WriteNullStringAsEmpty));
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            return null;
        }

        return null;
    }

}
