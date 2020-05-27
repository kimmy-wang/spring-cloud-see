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

package com.upcwangying.cloud.see.auth;

import com.nepxion.banner.BannerConstant;
import com.nepxion.banner.Description;
import com.nepxion.banner.LogoBanner;
import com.taobao.text.Color;
import com.upcwangying.cloud.see.auth.filters.AuthenticationFilter;
import com.upcwangying.cloud.see.auth.filters.AuthorizationFilter;
import com.upcwangying.cloud.see.auth.properties.SeeAuthProperties;
import com.upcwangying.cloud.see.auth.service.UserPermissionService;
import com.upcwangying.cloud.see.core.SeeBanner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UrlPathHelper;

/**
 * Created by WANGY
 *
 * @author WANGY
 */
@Configuration
@EnableConfigurationProperties(SeeAuthProperties.class)
@ConditionalOnProperty(prefix = SeeAuthProperties.PREFIX, name = "enabled", havingValue = "true")
@ConditionalOnClass
public class SeeAuthAutoConfiguration {

    static {
        System.setProperty(BannerConstant.BANNER_SHOWN_ANSI_MODE, "true");
        LogoBanner logoBanner = new LogoBanner(SeeAuthAutoConfiguration.class,
                "/com/upcwangying/cloud/see/auth/resource/banner.txt",
                "Welcome to See Auth", 2, 6,
                new Color[] { Color.red, Color.magenta }, true);
        SeeBanner.show(logoBanner, new Description(BannerConstant.SITE + ":", "http://cloud.see.team", 0, 1));
    }

    @Bean
    public AuthenticationFilter authFilter(final RouteLocator routeLocator,
                                           final UrlPathHelper urlPathHelper,
                                           final SeeAuthProperties seeAuthProperties) {
        return new AuthenticationFilter(routeLocator, urlPathHelper, seeAuthProperties);
    }

    @Bean
    @ConditionalOnBean({UserPermissionService.class})
    public AuthorizationFilter authorizationFilter(final RouteLocator routeLocator,
                                                final UrlPathHelper urlPathHelper,
                                                final UserPermissionService userPermissionService,
                                                final SeeAuthProperties seeAuthProperties) {
        return new AuthorizationFilter(routeLocator, urlPathHelper, userPermissionService, seeAuthProperties);
    }
}
