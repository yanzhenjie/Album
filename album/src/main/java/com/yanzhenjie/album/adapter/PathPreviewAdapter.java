package com.yanzhenjie.album.adapter;

import java.util.List;

/**
 * Created by yanzhenjie on 17-3-29.
 */
public class PathPreviewAdapter extends BasicPreviewAdapter<String> {

    public PathPreviewAdapter(List<String> previewList) {
        super(previewList);
    }

    @Override
    protected String getImagePath(String s) {
        return s;
    }
}
