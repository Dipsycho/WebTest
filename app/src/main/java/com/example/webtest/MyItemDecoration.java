package com.example.webtest;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Dark on 2017/5/28.
 */

public class MyItemDecoration extends RecyclerView.ItemDecoration{
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //设定底部边距为1px
        outRect.set(0, 0, 0, 1);
    }
}
