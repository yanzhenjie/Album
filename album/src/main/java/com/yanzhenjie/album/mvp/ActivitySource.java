/*
 * Copyright 2018 Yan Zhenjie
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
package com.yanzhenjie.album.mvp;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.yanzhenjie.album.R;

/**
 * Created by YanZhenjie on 2017/12/8.
 */
class ActivitySource extends Source<Activity> {

    private View mView;

    private Toolbar mActionBar;
    private Drawable mActionBarIcon;
    private MenuClickListener mMenuItemSelectedListener;

    ActivitySource(Activity activity) {
        super(activity);
        mView = activity.findViewById(android.R.id.content);
    }

    @Override
    void prepare() {
        Toolbar toolbar = getHost().findViewById(R.id.toolbar);
        setActionBar(toolbar);
    }

    @Override
    void setActionBar(Toolbar actionBar) {
        this.mActionBar = actionBar;

        Activity activity = getHost();
        if (mActionBar != null) {
            setTitle(activity.getTitle());
            mActionBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (mMenuItemSelectedListener != null) {
                        mMenuItemSelectedListener.onMenuClick(item);
                    }
                    return true;
                }
            });
            mActionBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMenuItemSelectedListener != null) {
                        mMenuItemSelectedListener.onHomeClick();
                    }
                }
            });
            mActionBarIcon = mActionBar.getNavigationIcon();
        }
    }

    @Override
    MenuInflater getMenuInflater() {
        return new SupportMenuInflater(getContext());
    }

    @Override
    Menu getMenu() {
        return mActionBar == null ? null : mActionBar.getMenu();
    }

    @Override
    void setMenuClickListener(MenuClickListener selectedListener) {
        this.mMenuItemSelectedListener = selectedListener;
    }

    @Override
    void setDisplayHomeAsUpEnabled(boolean showHome) {
        if (mActionBar != null) {
            if (showHome) {
                mActionBar.setNavigationIcon(mActionBarIcon);
            } else {
                mActionBar.setNavigationIcon(null);
            }
        }
    }

    @Override
    void setHomeAsUpIndicator(@DrawableRes int icon) {
        setHomeAsUpIndicator(ContextCompat.getDrawable(getContext(), icon));
    }

    @Override
    void setHomeAsUpIndicator(Drawable icon) {
        this.mActionBarIcon = icon;
        if (mActionBar != null) {
            mActionBar.setNavigationIcon(icon);
        }
    }

    @Override
    final void setTitle(CharSequence title) {
        if (mActionBar != null)
            mActionBar.setTitle(title);
    }

    @Override
    final void setTitle(@StringRes int title) {
        if (mActionBar != null)
            mActionBar.setTitle(title);
    }

    @Override
    final void setSubTitle(CharSequence title) {
        if (mActionBar != null)
            mActionBar.setSubtitle(title);
    }

    @Override
    final void setSubTitle(@StringRes int title) {
        if (mActionBar != null)
            mActionBar.setSubtitle(title);
    }

    @Override
    Context getContext() {
        return getHost();
    }

    @Override
    View getView() {
        return mView;
    }

    @Override
    void closeInputMethod() {
        Activity activity = getHost();
        View focusView = activity.getCurrentFocus();
        if (focusView != null) {
            InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        }
    }
}