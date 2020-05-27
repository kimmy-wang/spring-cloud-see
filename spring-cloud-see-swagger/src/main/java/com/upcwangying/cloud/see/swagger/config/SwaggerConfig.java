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

package com.upcwangying.cloud.see.swagger.config;

import com.google.common.collect.Lists;
import com.upcwangying.cloud.see.swagger.config.properties.SeeSwaggerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.List;

import static com.upcwangying.cloud.see.swagger.utils.BeanCreators.createSwaggerResource;


/**
 * Swagger ui配置类
 *
 * @author WANGY
 */
@RequiredArgsConstructor
public class SwaggerConfig implements SwaggerResourcesProvider {

    private static final String FORMAT_PATTERN = "%s/%s%s";

    private final SeeSwaggerProperties properties;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public List<SwaggerResource> get() {
        final List<SwaggerResource> resources = Lists.newArrayList();
        final List<String> services = discoveryClient.getServices();
        final List<String> swaggerExcludeServices = properties.getExcludeServices();
        final String prefix = properties.getPrefix();
        final String url = properties.getUrl();
        final String version = properties.getVersion();
        services.stream().forEach(service -> {
            if (null != swaggerExcludeServices && swaggerExcludeServices.size() > 0) {
                if (!swaggerExcludeServices.contains(service)) {
                    resources.add(createSwaggerResource(service, String.format(FORMAT_PATTERN, prefix, service, url), version));
                }
            } else {
                resources.add(createSwaggerResource(service, String.format(FORMAT_PATTERN, prefix, service, url), version));
            }
        });
        return resources;
    }

}
