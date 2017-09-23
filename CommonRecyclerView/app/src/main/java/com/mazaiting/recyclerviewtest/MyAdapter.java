package com.mazaiting.recyclerviewtest;

import android.content.Context;
import android.view.View;
import com.mazaiting.adapter.HeaderAndFooterAdapter;
import com.mazaiting.adapter.ViewHolder;
import java.util.List;

/**
 * 适配器
 * Created by mazaiting on 2017/9/21.
 */

public class MyAdapter extends HeaderAndFooterAdapter<String> {

  public MyAdapter(Context context, List<String> list, View view1, View view2) {
    super(context, list);
    addHeaderView(view1);
    addFooterView(view2);
  }

  @Override protected int getLayoutId() {
    return R.layout.item_view;
  }

  @Override protected void onBindItemViewHolder(ViewHolder holder, int position, String item) {
    holder.setText(R.id.textView, mList.get(position));
  }
}
