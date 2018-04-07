/*
 * Copyright © 2018 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.album.mvp.ioc;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by YanZhenjie on 2018/4/7.
 */
public class IocInject {

    public static void bind(Activity hostSource) {
        bind(hostSource, hostSource.getWindow().getDecorView());
    }

    public static void bind(Object host, View source) {
        bindView(host, source);
        bindOnClick(host, source);
    }

    private static void bindView(Object host, View source) {
        Class<?> clazz = host.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            BindView bindView = field.getAnnotation(BindView.class);
            if (bindView != null) {
                try {
                    View view = source.findViewById(bindView.value());
                    if (!field.isAccessible()) field.setAccessible(true);
                    field.set(host, view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void bindOnClick(Object host, View source) {
        Class<?> clazz = host.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            BindClick onClick = method.getAnnotation(BindClick.class);
            if (onClick != null) {
                int[] idArray = onClick.value();
                for (int id : idArray) {
                    View view = source.findViewById(id);
                    view.setOnClickListener(new ClickListener(host, method));
                }
            }
        }
    }

    public static void unBind(Object host) {
        unBindView(host);
    }

    private static void unBindView(Object host) {
        Class<?> clazz = host.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            BindView bindView = field.getAnnotation(BindView.class);
            if (bindView != null) {
                try {
                    if (!field.isAccessible()) field.setAccessible(true);
                    field.set(host, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class ClickListener implements View.OnClickListener {

        private Object mHost;
        private Method mMethod;

        private ClickListener(Object host, Method method) {
            this.mHost = host;
            this.mMethod = method;
        }

        @Override
        public void onClick(View v) {
            try {
                mMethod.invoke(mHost, v);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}