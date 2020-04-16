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

package team.see.cloud.swagger.config.properties;

import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * yaml 配置类
 *
 * @author WANGY
 * @date 2019/5/5 10:10
 */
@Data
@ConfigurationProperties(SeeSwaggerProperties.PREFIX)
public class SeeSwaggerProperties {

    public static final String PREFIX = "see.swagger";

    /**
     * Default is false, Whether swagger should be enabled.
     */
    private boolean enabled;

    /**
     * Consistent with the `zuul.prefix` setting
     */
    private String prefix = "";

    /**
     * Exclude service list
     */
    private List<String> excludeServices = Lists.newArrayList();

    /**
     *
     */
    private String url = "/v2/api-docs";

    /**
     * version
     */
    private String version = "2.0";

    public void setPrefix(String prefix) {
        if (StringUtils.isNotBlank(prefix) && !StringUtils.startsWith(prefix, "/")) {
            prefix = String.format("/%s", prefix);
        }
        this.prefix = prefix;
    }

    public void setUrl(String url) {
        if (StringUtils.isNotBlank(url) && !StringUtils.startsWith(url, "/")) {
            url = String.format("/%s", url);
        }
        this.url = url;
    }
}
