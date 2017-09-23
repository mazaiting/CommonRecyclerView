package com.mazaiting.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ViewHolder工具抽象类
 * Created by mazaiting on 2017/9/23.
 */

public class ViewHolder extends RecyclerView.ViewHolder {
  private Context mContext;
  private SparseArray<View> mViews;
  private View mConvertView;

  public ViewHolder(View itemView) {
    super(itemView);
    mConvertView = itemView;
    mViews = new SparseArray<>();
  }

  /**
   * 设置上下文
   */
  public void setContext(Context context) {
    mContext = context;
  }

  /**
   * 根据id得到布局中的View(使用SparseArray保管，提高效率)
   *
   * @param viewId 布局id
   * @param <T> View
   */
  public <T extends View> T getView(int viewId) {
    View view = mViews.get(viewId);
    if (null == view) {
      view = mConvertView.findViewById(viewId);
      mViews.put(viewId, view);
    }
    return (T) view;
  }

  /**
   * 得到当前item对应的View
   */
  public View getConvertView() {
    return mConvertView;
  }

  /*================== 一切有可能的操作控件的方法 begin ==================*/

  /**
   * 设置TextView显示文字, 并返回this
   */
  public ViewHolder setText(int viewId, String text) {
    TextView tv = getView(viewId);
    tv.setText(text);
    return this;
  }

  /**
   * 设置TextView的文字颜色，并返回this
   */
  public ViewHolder setTextColor(int viewId, int colorId) {
    TextView tv = getView(viewId);
    if (null == mContext) throw new IllegalArgumentException("Please set context.");
    tv.setTextColor(mContext.getResources().getColor(colorId));
    return this;
  }

  /**
   * 设置ImageView的图片，并返回this
   */
  public ViewHolder setImageResource(int viewId, int resId) {
    ImageView iv = getView(viewId);
    iv.setImageResource(resId);
    return this;
  }

  /**
   * 设置ImageView的图片，并返回this
   */
  public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
    ImageView iv = getView(viewId);
    iv.setImageBitmap(bitmap);
    return this;
  }

  /**
   * 设置ImageView的图片，并返回this
   */
  public ViewHolder setImageFileResource(int viewId, String path) {
    ImageView iv = getView(viewId);
    Bitmap bitmap = BitmapFactory.decodeFile(path);
    iv.setImageBitmap(bitmap);
    return this;
  }

  /**
   * 设置背景颜色，并返回this
   */
  public ViewHolder setBackgroundColor(int viewId, int colorId) {
    View view = getView(viewId);
    if (null == mContext) throw new IllegalArgumentException("Please set context.");
    view.setBackgroundColor(mContext.getResources().getColor(colorId));
    return this;
  }

  /**
   * 设置背景资源，并返回this
   */
  public ViewHolder setBackgrounResource(int viewId, int resId) {
    View view = getView(viewId);
    view.setBackgroundResource(resId);
    return this;
  }

  /**
   * 设置显隐，并返回this
   */
  public ViewHolder setViewVisibility(int viewId, int visibility) {
    View view = getView(viewId);
    view.setVisibility(visibility);
    return this;
  }

  /**
   * 设置是否可用，并返回this
   */
  public ViewHolder setEnabled(int viewId, boolean enabled) {
    View view = getView(viewId);
    view.setEnabled(enabled);
    return this;
  }

  /**
   * 设置是否可获取焦点，并返回this
   */
  public ViewHolder setFocusable(int viewId, boolean focusable) {
    View view = getView(viewId);
    view.setFocusable(focusable);
    return this;
  }

    /*================== 一切有可能操作控件的方法 end ==================*/
}
