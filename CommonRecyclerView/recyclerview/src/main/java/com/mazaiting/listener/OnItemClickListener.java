package com.mazaiting.listener;

import android.view.View;

/**
 * item点击事件
 * Created by mazaiting on 2017/9/23.
 */
public interface OnItemClickListener {
  /**
   * 条目点击
   * @param position 当前位置
   * @param itemView 当前视图
   */
  void onItemClick(int position, View itemView);
}