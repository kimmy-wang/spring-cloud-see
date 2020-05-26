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

package com.upcwangying.cloud.see.core;

import com.nepxion.banner.BannerConstant;
import com.nepxion.banner.Description;
import com.nepxion.banner.LogoBanner;
import com.taobao.text.Color;

/**
 * Created by WANGY
 *
 * @author WANGY
 * @date 2019/5/14 16:53
 */
public class BannerTest {
    public static void main(String[] args) {
        System.setProperty(BannerConstant.BANNER_SHOWN_ANSI_MODE, "true");
        SeeBanner.show(auth(), new Description(BannerConstant.SITE + ":", "http://cloud.see.team", 0, 1));
        SeeBanner.show(swagger(), new Description(BannerConstant.SITE + ":", "http://cloud.see.team", 0, 1));
    }

    private static LogoBanner swagger() {
        return new LogoBanner(BannerTest.class,
                "/com/upcwangying/cloud/see/swagger/resource/banner.txt",
                "Welcome to See Swagger", 2, 8,
                new Color[] { Color.red, Color.magenta }, true);
    }

    private static LogoBanner auth() {
        return new LogoBanner(BannerTest.class,
                "/com/upcwangying/cloud/see/auth/resource/banner.txt",
                "Welcome to See Auth", 2, 6,
                new Color[] { Color.red, Color.magenta }, true);
    }
}
