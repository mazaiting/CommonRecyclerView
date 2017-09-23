package com.mazaiting.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 条目间隔
 * Created by mazaiting on 2017/9/23.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
  private Rect mRect;
  public SpaceItemDecoration(Rect rect) {
    this.mRect = rect;
  }

  @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
      RecyclerView.State state) {
    outRect.left = mRect.left;
    outRect.top = mRect.top;
    outRect.right = mRect.right;
    outRect.bottom = mRect.bottom;
  }
}
