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

package com.upcwangying.cloud.see.auth.properties;

import com.google.common.collect.Maps;
import com.upcwangying.cloud.see.auth.enums.HttpMethod;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;

/**
 * Created by WANGY
 *
 * @author WANGY
 * @date 2019/5/6 13:37
 */
@Data
@ConfigurationProperties(SeeAuthProperties.PREFIX)
public class SeeAuthProperties {

    public static final String PREFIX = "see.auth";

    /**
     * Default is false, Whether swagger should be enabled.
     */
    private boolean enabled;

    /**
     * zuul pre-filter order
     */
    private int authFilterOrder = PRE_DECORATION_FILTER_ORDER - 2;

    /**
     * zuul pre-filter order
     */
    private int permissionFilterOrder = PRE_DECORATION_FILTER_ORDER - 1;

    /**
     *
     */
    private Map<HttpMethod, List<String>> ignoredPatterns = Maps.newHashMap();
}
