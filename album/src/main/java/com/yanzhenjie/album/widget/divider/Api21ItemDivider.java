/*
 * Copyright 2017 Yan Zhenjie
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
package com.yanzhenjie.album.widget.divider;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * <p>The implementation of divider adds dividers around the list.</p>
 * Created by YanZhenjie on 2017/8/14.
 */
public class Api21ItemDivider extends Divider {

    private Drawable mDivider;
    private int mDividerWidth;
    private int mDividerHeight;

    /**
     * @param color divider line color.
     */
    public Api21ItemDivider(@ColorInt int color) {
        this(color, 4, 4);
    }

    /**
     * @param color         line color.
     * @param dividerWidth  line width.
     * @param dividerHeight line height.
     */
    public Api21ItemDivider(@ColorInt int color, int dividerWidth, int dividerHeight) {
        this.mDivider = new ColorDrawable(color);
        this.mDividerWidth = dividerWidth;
        this.mDividerHeight = dividerHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int orientation = getOrientation(layoutManager);
            int position = parent.getChildLayoutPosition(view);
            int spanCount = getSpanCount(layoutManager);
            int childCount = parent.getAdapter().getItemCount();

            boolean firstRaw = isFirstRaw(orientation, position, spanCount, childCount);
            boolean lastRaw = isLastRaw(orientation, position, spanCount, childCount);
            boolean firstColumn = isFirstColumn(orientation, position, spanCount, childCount);
            boolean lastColumn = isLastColumn(orientation, position, spanCount, childCount);

            if (orientation == RecyclerView.VERTICAL) {
                if (spanCount == 1) {
                    if (firstRaw) {
                        outRect.set(mDividerWidth, mDividerHeight, mDividerWidth, 0); // ┃━┃x
                    } else if (lastRaw) {
                        outRect.set(mDividerWidth, mDividerHeight, mDividerWidth, mDividerHeight); // ┃━┃━
                    } else {
                        outRect.set(mDividerWidth, mDividerHeight, mDividerWidth, 0); // ┃━┃x
                    }
                } else {
                    if (firstRaw && firstColumn) {
                        outRect.set(mDividerWidth, mDividerHeight, 0, 0); // ┃━xx
                    } else if (firstRaw && lastColumn) {
                        outRect.set(mDividerWidth, mDividerHeight, mDividerWidth, 0); // ┃━┃x
                    } else if (firstRaw) {
                        outRect.set(mDividerWidth, mDividerHeight, 0, 0); // ┃━xx
                    } else if (lastRaw && firstColumn) {
                        outRect.set(mDividerWidth, mDividerHeight, 0, mDividerHeight); // ┃━x━
                    } else if (lastRaw && lastColumn) {
                        outRect.set(mDividerWidth, mDividerHeight, mDividerWidth, mDividerHeight); // ┃━┃━
                    } else if (lastRaw) {
                        outRect.set(mDividerWidth, mDividerHeight, 0, mDividerHeight); // ┃━x━
                    } else if (firstColumn) {
                        outRect.set(mDividerWidth, mDividerHeight, 0, 0); // ┃━xx
                    } else if (lastColumn) {
                        outRect.set(mDividerWidth, mDividerHeight, mDividerWidth, 0); // ┃━┃x
                    } else {
                        outRect.set(mDividerWidth, mDividerHeight, 0, 0); // ┃━xx
                    }
                }
            } else {
                if (spanCount == 1) {
                    if (firstColumn) {
                        outRect.set(mDividerWidth, mDividerHeight, 0, mDividerHeight); // ┃━x━
                    } else if (lastColumn) {
                        outRect.set(mDividerWidth, mDividerHeight, mDividerWidth, mDividerHeight); // ┃━┃━
                    } else {
                        outRect.set(mDividerWidth, mDividerHeight, 0, mDividerHeight); // ┃━x━
                    }
                } else {
                    if (firstColumn && firstRaw) {
                        outRect.set(mDividerWidth, mDividerHeight, 0, 0); // ┃━xx
                    } else if (firstColumn && lastRaw) {
                        outRect.set(mDividerWidth, mDividerHeight, 0, mDividerHeight); // ┃━x━
                    } else if (firstColumn) {
                        outRect.set(mDividerWidth, mDividerHeight, 0, 0); // ┃━xx
                    } else if (lastColumn && firstRaw) {
                        outRect.set(mDividerWidth, mDividerHeight, mDividerWidth, mDividerHeight); // ┃━┃━
                    } else if (lastColumn && lastRaw) {
                        outRect.set(mDividerWidth, 0, mDividerWidth, mDividerHeight); // ┃x┃━
                    } else if (lastColumn) {
                        outRect.set(mDividerWidth, 0, mDividerWidth, mDividerHeight); // ┃x┃━
                    } else if (firstRaw) {
                        outRect.set(mDividerWidth, mDividerHeight, 0, 0); // ┃━xx
                    } else if (lastRaw) {
                        outRect.set(mDividerWidth, mDividerHeight, 0, mDividerHeight); // ┃━x━
                    } else {
                        outRect.set(mDividerWidth, mDividerHeight, 0, 0); // ┃━xx
                    }
                }
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            outRect.set(mDividerWidth, mDividerHeight, mDividerWidth, mDividerHeight); // ┃━┃━
        }
    }

    private int getOrientation(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getOrientation();
        }
        return RecyclerView.VERTICAL;
    }

    private int getSpanCount(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return 1;
    }

    private boolean isFirstRaw(int orientation, int position, int columnCount, int childCount) {
        if (orientation == RecyclerView.VERTICAL) {
            return position < columnCount;
        } else {
            if (columnCount == 1) return true;
            return position % columnCount == 0;
        }
    }

    private boolean isLastRaw(int orientation, int position, int columnCount, int childCount) {
        if (orientation == RecyclerView.VERTICAL) {
            if (columnCount == 1) {
                return position + 1 == childCount;
            } else {
                int lastRawItemCount = childCount % columnCount;
                int rawCount = (childCount - lastRawItemCount) / columnCount + (lastRawItemCount > 0 ? 1 : 0);

                int rawPositionJudge = (position + 1) % columnCount;
                if (rawPositionJudge == 0) {
                    int rawPosition = (position + 1) / columnCount;
                    return rawCount == rawPosition;
                } else {
                    int rawPosition = (position + 1 - rawPositionJudge) / columnCount + 1;
                    return rawCount == rawPosition;
                }
            }
        } else {
            if (columnCount == 1) return true;
            return (position + 1) % columnCount == 0;
        }
    }

    private boolean isFirstColumn(int orientation, int position, int columnCount, int childCount) {
        if (orientation == RecyclerView.VERTICAL) {
            if (columnCount == 1) return true;
            return position % columnCount == 0;
        } else {
            return position < columnCount;
        }
    }

    private boolean isLastColumn(int orientation, int position, int columnCount, int childCount) {
        if (orientation == RecyclerView.VERTICAL) {
            if (columnCount == 1) return true;
            return (position + 1) % columnCount == 0;
        } else {
            if (columnCount == 1) {
                return position + 1 == childCount;
            } else {
                int lastRawItemCount = childCount % columnCount;
                int rawCount = (childCount - lastRawItemCount) / columnCount + (lastRawItemCount > 0 ? 1 : 0);

                int rawPositionJudge = (position + 1) % columnCount;
                if (rawPositionJudge == 0) {
                    int rawPosition = (position + 1) / columnCount;
                    return rawCount == rawPosition;
                } else {
                    int rawPosition = (position + 1 - rawPositionJudge) / columnCount + 1;
                    return rawCount == rawPosition;
                }
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        drawHorizontal(c, layoutManager);
        drawVertical(c, layoutManager);
    }

    public void drawHorizontal(Canvas c, RecyclerView.LayoutManager layoutManager) {
        c.save();
        int orientation = getOrientation(layoutManager);
        int spanCount = getSpanCount(layoutManager);
        int childCount = layoutManager.getChildCount();

        for (int i = 0; i < childCount; i++) {
            final View child = layoutManager.getChildAt(i);
            // Top, nothing.

            // Bottom, draw.
            if (!isLastRaw(orientation, i, spanCount, childCount)) {
                final int left = child.getLeft();
                final int top = child.getBottom();
                final int right = child.getRight() + (isLastColumn(orientation, i, spanCount, childCount) ? 0 : mDividerWidth);
                final int bottom = top + mDividerHeight;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
        c.restore();
    }

    public void drawVertical(Canvas c, RecyclerView.LayoutManager layoutManager) {
        c.save();
        int orientation = getOrientation(layoutManager);
        int spanCount = getSpanCount(layoutManager);
        int childCount = layoutManager.getChildCount();

        for (int i = 0; i < childCount; i++) {
            final View child = layoutManager.getChildAt(i);
            // Left, nothing.

            // Right, draw.
            if (!isLastColumn(orientation, i, spanCount, childCount)) {
                final int left = child.getRight();
                final int top = child.getTop();
                final int right = left + mDividerWidth;
                final int bottom = child.getBottom() + (isLastRaw(orientation, i, spanCount, childCount) ? 0 : mDividerWidth);
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
        c.restore();
    }

    @Override
    public int getHeight() {
        return mDividerHeight;
    }

    @Override
    public int getWidth() {
        return mDividerWidth;
    }
}