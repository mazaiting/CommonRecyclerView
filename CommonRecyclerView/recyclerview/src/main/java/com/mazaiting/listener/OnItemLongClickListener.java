package com.mazaiting.listener;

import android.view.View;

/**
 * item长按监听
 * Created by mazaiting on 2017/9/23.
 */
public interface OnItemLongClickListener {
  /**
   * 条目点击
   * @param position 当前位置
   * @param itemView 当前视图
   */
  void onItemLongClick(int position, View itemView);
}
