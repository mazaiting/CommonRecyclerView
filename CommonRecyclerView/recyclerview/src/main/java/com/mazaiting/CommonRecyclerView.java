package com.mazaiting;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.mazaiting.decoration.SpaceItemDecoration;
import com.mazaiting.listener.OnItemClickListener;
import com.mazaiting.listener.OnItemLongClickListener;
import com.mazaiting.listener.OnLoadMoreListener;

/**
 * 封装RecyclerView,使其更简单易用
 * Created by mazaiting on 2017/9/21.
 */

public class CommonRecyclerView extends RecyclerView {
  /**手势监听*/
  private GestureDetector mGestureDetector;
  /**条目点击监听*/
  private OnItemClickListener mOnItemClickListener;
  /**条目长按监听*/
  private OnItemLongClickListener mItemLongClickListener;
  /**滑动监听*/
  private OnScrollListener mOnScrollListener;
  /**加载更多监听*/
  private OnLoadMoreListener mOnLoadMoreListener;
  /**自动加载更多*/
  private boolean mIsAutoLoadMore = false;
  /**最后一个可见位置*/
  private int mLastVisiblePosition = 0;
  /**设备上下文*/
  private Context mContext;

  public CommonRecyclerView(Context context) {
    this(context, null);
  }

  public CommonRecyclerView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CommonRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    this.mContext = context;
    init();
  }

  /**
   * 初始化
   */
  private void init() {
    mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
      @Override public void onLongPress(MotionEvent e) {
        super.onLongPress(e);
        if (null != mItemLongClickListener){
          View childView = findChildViewUnder(e.getX(), e.getY());
          if (null != childView){
            int position = getChildLayoutPosition(childView);
            mItemLongClickListener.onItemLongClick(position, childView);
          }
        }
      }

      @Override public boolean onSingleTapUp(MotionEvent e) {
        if (null != mItemLongClickListener){
          View childView = findChildViewUnder(e.getX(), e.getY());
          if (null != childView){
            int position = getChildLayoutPosition(childView);
            mOnItemClickListener.onItemClick(position, childView);
            return true;
          }
        }
        return super.onSingleTapUp(e);
      }
    });

    // 添加条目点击
    addOnItemTouchListener(new SimpleOnItemTouchListener(){
      @Override public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return mGestureDetector.onTouchEvent(e);
      }
    });
    // 设置加载更多处理
    super.addOnScrollListener(new OnScrollListener() {
      @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        // 设置加载更多
        if (newState == RecyclerView.SCROLL_STATE_SETTLING && mIsAutoLoadMore && null != mOnLoadMoreListener){
          mOnLoadMoreListener.onLoadMore();
        }
        // 设置滑动监听
        if (null != mOnScrollListener){
          mOnScrollListener.onScrollStateChanged(recyclerView, newState);
        }
      }

      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        // 设置加载更多
        if (mIsAutoLoadMore && null != mOnLoadMoreListener){
          mLastVisiblePosition = getLastVisiblePosition();
        }
        // 设置滑动监听
        if (null != mOnScrollListener){
          mOnScrollListener.onScrolled(recyclerView, dx, dy);
        }
      }
    });
  }

  /**
   * 获取最后一条展示的位置
   */
  private int getLastVisiblePosition() {
    int position;
    if (getLayoutManager() instanceof LinearLayoutManager){
      position = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
    } else if (getLayoutManager() instanceof GridLayoutManager){
      position = ((GridLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
    } else if (getLayoutManager() instanceof StaggeredGridLayoutManager){
      StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
      int[] lastPositions =
          layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
      position = getMaxPosition(lastPositions);
    } else {
      position = getLayoutManager().getItemCount() - 1;
    }
    return position;
  }

  /**
   * 获取最大位置
   * @param lastPositions 位置数组
   */
  private int getMaxPosition(int[] lastPositions) {
    int maxPosition = Integer.MIN_VALUE;
    for (int lastPosition : lastPositions) {
      maxPosition = Math.max(maxPosition, lastPosition);
    }
    return maxPosition;
  }

  /**
   * 设置是否允许自动加载更多，默认为true
   * 设置之后，还需要设置加载更多的监听
   * @param autoLoadMore true 自动加载
   */
  public void setAutoLoadMore(boolean autoLoadMore) {
    mIsAutoLoadMore = autoLoadMore;
  }

  /**
   * 设置条目间距
   * @param rect 矩形
   */
  public void setItemDecoration(Rect rect){
    this.addItemDecoration(new SpaceItemDecoration(rect));
  }

  /**
   * 设置条目间距
   * @param space 距离值
   */
  public void setItemDecoration(int space){
    setItemDecoration(space, space, space, space);
  }

  /**
   * 设置条目间距
   * @param top 上边距
   * @param bottom 下边距
   * @param left 左边距
   * @param right 右边距
   */
  public void setItemDecoration(int top,int bottom,int left,int right){
    Rect rect = new Rect(left, top, right, bottom);
    setItemDecoration(rect);
  }

  /**
   * 设置加载更多监听, setAutoLoadMore必须要设置
   * @param onLoadMoreListener 加载更多监听
   */
  public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
    mOnLoadMoreListener = onLoadMoreListener;
  }

  /**
   * 添加滑动监听
   * @param onScrollListener 滑动监听
   */
  public void addOnScrollListener(OnScrollListener onScrollListener) {
    mOnScrollListener = onScrollListener;
  }

  /**
   * 设置点击事件
   * @param onItemClickListener  条目点击监听
   */
  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    this.mOnItemClickListener = onItemClickListener;
  }

  /**
   * 设置长按事件
   * @param itemLongClickListener  条目长按监听
   */
  public void setOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
    this.mItemLongClickListener = itemLongClickListener;
  }
}
