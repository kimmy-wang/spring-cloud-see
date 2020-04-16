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

package team.see.cloud.swagger;

import com.nepxion.banner.BannerConstant;
import com.nepxion.banner.Description;
import com.nepxion.banner.LogoBanner;
import com.spring4all.swagger.EnableSwagger2Doc;
import com.taobao.text.Color;
import team.see.cloud.core.SeeBanner;
import team.see.cloud.swagger.config.SwaggerConfig;
import team.see.cloud.swagger.config.properties.SeeSwaggerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static team.see.cloud.swagger.config.properties.SeeSwaggerProperties.PREFIX;


/**
 * 自动装配类
 *
 * @author WANGY
 * @date 2019/5/5 10:07
 */
@Configuration
@EnableConfigurationProperties(SeeSwaggerProperties.class)
@ConditionalOnProperty(prefix = PREFIX, name = "enabled", havingValue = "true")
@ConditionalOnClass(value = {DiscoveryClient.class})
@EnableSwagger2Doc
public class SeeSwaggerAutoConfiguration {

    static {
        System.setProperty(BannerConstant.BANNER_SHOWN_ANSI_MODE, "true");
        LogoBanner logoBanner = new LogoBanner(SeeSwaggerAutoConfiguration.class,
                "/team/see/cloud/swagger/resource/banner.txt",
                "Welcome to See Swagger", 2, 8,
                new Color[] { Color.red, Color.magenta }, true);
        SeeBanner.show(logoBanner, new Description(BannerConstant.SITE + ":", "http://cloud.see.team", 0, 1));
    }

    @Bean
    @Primary
    public SwaggerConfig swaggerConfig(final SeeSwaggerProperties seeSwaggerProperties) {
        return new SwaggerConfig(seeSwaggerProperties);
    }

}
