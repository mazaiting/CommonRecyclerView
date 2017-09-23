package com.mazaiting.recyclerviewtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.mazaiting.CommonRecyclerView;
import com.mazaiting.listener.OnItemClickListener;
import com.mazaiting.listener.OnItemLongClickListener;
import com.mazaiting.listener.OnLoadMoreListener;
import com.mazaiting.manager.WrapContentLinearLayoutManager;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  CommonRecyclerView recyclerView;
  static List<String> sList;
  static {
    sList = new ArrayList<>();
    for (int i = 0; i < 50; i++){
      sList.add("item"+i);
    }
  }
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    recyclerView = (CommonRecyclerView) findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));
    recyclerView.setItemDecoration(20);
    final View view1 = View.inflate(this, R.layout.item_view1, null);
    final View view2 = View.inflate(this, R.layout.item_view2, null);
    final MyAdapter myAdapter = new MyAdapter(this, sList, view1, view2);
    recyclerView.setAdapter(myAdapter);
    // 单击事件
    recyclerView.setOnItemClickListener(
        (position, itemView) -> Toast.makeText(MainActivity.this, position + "点击", Toast.LENGTH_SHORT).show());
    // 长按事件
    recyclerView.setOnItemLongClickListener(
        (position, itemView) -> Toast.makeText(MainActivity.this, position + "长按", Toast.LENGTH_SHORT).show());
    // 自动加载更多
    recyclerView.setOnLoadMoreListener(
        () -> Toast.makeText(MainActivity.this, "加载更多", Toast.LENGTH_SHORT).show());
  }
}
