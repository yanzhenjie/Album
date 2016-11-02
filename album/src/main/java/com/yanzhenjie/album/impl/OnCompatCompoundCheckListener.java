/*
 * AUTHOR：Yan Zhenjie
 *
 * DESCRIPTION：create the File, and add the content.
 *
 * Copyright © ZhiMore. All Rights Reserved
 *
 */
package com.yanzhenjie.album.impl;

import android.widget.CompoundButton;

/**
 * Created by Yan Zhenjie on 2016/10/18.
 */
public interface OnCompatCompoundCheckListener {

    void onCheck(CompoundButton compoundButton, int position, boolean isChecked);

}
