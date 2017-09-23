package com.mazaiting.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * 包含添加头视图与尾视图的Adapter
 * Created by mazaiting on 2017/9/21.
 */

public abstract class HeaderAndFooterAdapter<T> extends RecyclerView.Adapter<ViewHolder>{
  /** 头视图--HeaderView的ItemViewType的生成基准值，生成规则为基准值+当前HeaderView的个数 */
  private static final int TYPE_HEADER = 10000;
  /** 尾视图--FooterView的ItemViewType的生成基准值，生成规则为基准值+当前的FooterView的个数 */
  private static final int TYPE_FOOTER = 20000;
  /** 存储HeaderView，key值作为对应HeaderView的ItemViewType */
  private SparseArray<View> mHeaderViews = new SparseArray<>(0);
  /** 存储FooterView，key值作为对应HeaderView的ItemViewType */
  private SparseArray<View> mFooterViews = new SparseArray<>(0);
  /**列表数据*/
  protected List<T> mList = null;
  /**头View是否可用*/
  private boolean mIsHeaderViewEnable = false;
  /**尾View是否可用*/
  private boolean mIsFooterViewEnable = false;
  /**设备上下文*/
  private Context mContext = null;
  public HeaderAndFooterAdapter(Context context, List<T> list){
    this.mContext = context;
    this.mList = list;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = null;
    if (isHeaderViewEnable() && null != mHeaderViews.get(viewType)){
      view = mHeaderViews.get(viewType);
    } else if (isFooterViewEnable() && null != mFooterViews.get(viewType)){
      view = mFooterViews.get(viewType);
    } else {
      view =  LayoutInflater.from(mContext).inflate(getLayoutId(), parent, false);
    }
    return new ViewHolder(view);
  }

  /**
   * 获取布局ID
   */
  protected abstract int getLayoutId();

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    if (isFooterView(position) || isHeaderView(position)) return;
    int count = position - getHeaderViewCount();
    T item = getItem(count);
    onBindItemViewHolder(holder, count, item);
  }

  @Override
  public final int getItemViewType(int position) {
    if(isHeaderView(position)) {//FooterView
      return mHeaderViews.keyAt(position);
    }
    if(isFooterView(position)){//HeaderView
      return mFooterViews.keyAt(position - getHeaderViewCount() - getItemDataCount());
    }
    return getItemViewTypeForData(position);
  }

  /**
   * 绑定条目视图
   */
  protected abstract void onBindItemViewHolder(ViewHolder holder, int position, T item);

  /**
   * 获取position位置的数据
   * @param position 当前位置
   */
  public T getItem(int position){
    return null == mList ? null : mList.get(position);
  }

  /**
   * 添加数据
   * @param list 数据列表
   */
  public void addAll(List<T> list){
    int positionStart = getHeaderViewCount();
    if (null == this.mList){
      this.mList = list;
    } else {
      positionStart += this.mList.size();
      this.mList.addAll(list);
    }
    notifyItemRangeInserted(positionStart, list.size());
  }

  /**
   * 添加单条数据
   * @param item 数据
   */
  public void add(T item){
    if (null == mList){
      mList = new ArrayList<>(1);
    }
    int size = getItemDataCount();
    mList.add(item);
    notifyItemInserted(size);
  }

  /**
   * 在指定位置添加数据
   * @param item 添加的条目
   * @param position 当前位置
   */
  public void add(T item, int position){
    if (null == mList){
      mList = new ArrayList<>();
    }
    mList.add(position, item);
    notifyItemInserted(position);
  }

  /**
   * 更新当前位置数据
   * @param item 修改的数据
   * @param position 当前位置
   */
  public void update(T item, int position){
    if (null == mList){
      mList = new ArrayList<>();
    }
    mList.set(position, item);
    notifyItemChanged(position);
  }

  /**
   * 重置数据
   * @param list 列表数据
   */
  public void reset(List<T> list){
    this.mList = list;
    notifyDataSetChanged();
  }

  /**
   * 展示的总数据条目(包括HeaderView和FooterView)
   */
  @Override public int getItemCount() {
    return getItemDataCount() + getHeaderViewCount() + getFooterViewCount();
  }

  /**
   * 要展示的有效数据数(包括HeaderView和FooterView)
   */
  private final int getItemDataCount() {
    return null == mList ? 0 : mList.size();
  }

  /**
   * 获取待展示的position索引出数据的viewType
   * @param position 位置
   */
  public int getItemViewTypeForData(int position){
    return super.getItemViewType(position);
  }

  /**
   * 判断position位置是否为FooterView的索引
   * @param position 位置
   */
  public boolean isFooterViewPosition(int position){
    return position >= getItemDataCount() + getHeaderViewCount();
  }

  /**
   * 判断position位置是否为HeaderView索引
   * @param position 位置
   */
  public boolean isHeaderViewPosition(int position){
    return position < getHeaderViewCount();
  }

  /**
   * 获取HeaderView的总数
   */
  public int getHeaderViewCount() {
    return isHeaderViewEnable() ? mHeaderViews.size() : 0;
  }

  /**
   * 获取getFooterViewCount总数
   */
  public int getFooterViewCount(){
    return isFooterViewEnable() ? mFooterViews.size() : 0;
  }

  /**
   * 头视图是否可用, 默认不可用
   */
  public boolean isHeaderViewEnable() {
    return mIsHeaderViewEnable;
  }

  /**
   * 设置HeaderView是否可用
   */
  public void setHeaderViewEnable(boolean enable) {
    mIsHeaderViewEnable = enable;
  }

  /**
   * 尾布局是否可用, 默认不可用
   */
  public boolean isFooterViewEnable() {
    return mIsFooterViewEnable;
  }

  /**
   * 设置FooterView是否可用
   */
  public void setFooterViewEnable(boolean enable) {
    mIsFooterViewEnable = enable;
  }

  /**
   * 判断当前位置是否为FooterView
   * @param position 位置
   */
  public boolean isFooterView(int position){
    return isFooterViewEnable() && isFooterViewPosition(position);
  }

  /**
   * 判断position位置是否为HeaderView
   * @param position 位置
   */
  public boolean isHeaderView(int position){
    return isHeaderViewEnable() && isHeaderViewPosition(position);
  }

  /**
   * 添加一个HeaderView, 必须在返回Holder之前调用
   * @param headerView 头布局
   */
  public void addHeaderView(View headerView){
    if (null == headerView) throw new NullPointerException("HeaderView is null.");
    if (!isHeaderViewEnable()) setHeaderViewEnable(true);
    mHeaderViews.put(TYPE_HEADER + getHeaderViewCount(), headerView);
    notifyItemInserted(getHeaderViewCount() - 1);
  }

  /**
   * 添加一个FooterView, 必须在返回Holder之前调用
   * @param footerView 尾布局
   */
  public void addFooterView(View footerView){
    if (null == footerView) throw new NullPointerException("FooterView is null.");
    if (!isFooterViewEnable()) setFooterViewEnable(true);
    mFooterViews.put(TYPE_FOOTER + getFooterViewCount(), footerView);
    Log.e("ksdjfl", getItemCount()+"");
    notifyItemInserted(getItemCount());
  }

  /**
   * 如果你的RecyclerView的LayoutManager是GridLayoutManager或StaggeredGridLayoutManager时，
   * 如果就这样添加HeaderView或FooterView，会发现HeaderView或FooterView不会独立的占据一行。
   * 这是因为设置了SpanSize的缘故，所以我们需要针对这两种LayoutManager进行处理
   */
  @Override public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
    final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
    if (layoutManager instanceof GridLayoutManager){
      ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
        @Override public int getSpanSize(int position) {
          return getNewSpanSize(((GridLayoutManager) layoutManager).getSpanCount(), position);
        }
      });
    }
  }

  @Override public void onViewAttachedToWindow(ViewHolder holder) {
    super.onViewAttachedToWindow(holder);
    int position = holder.getLayoutPosition();
    if (isHeaderView(position) || isFooterView(position)){
      ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
      if (null != layoutParams && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams){
        StaggeredGridLayoutManager.LayoutParams lp =
            (StaggeredGridLayoutManager.LayoutParams) layoutParams;
        lp.setFullSpan(true);
      }
    }
  }

  /**
   * 设置新的SpanSize
   * @param spanCount 数量
   * @param position 位置
   */
  private int getNewSpanSize(int spanCount, int position) {
    if (isHeaderView(position) || isFooterView(position)){
      return spanCount;
    }
    return 1;
  }
}
