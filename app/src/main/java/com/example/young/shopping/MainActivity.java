package com.example.young.shopping;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

public class MainActivity extends AppCompatActivity {
    private List<Map<String, Object>> data;
    public List<Map<String, Object>> shoplist_data;
    private SimpleAdapter simpleAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayout shop;
    private FloatingActionButton fab;
    private CommonAdapter<Map<String, Object>> commonAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shop = (LinearLayout)findViewById(R.id.shop);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        SetString();
        SetRecyclerView();
        SetListView();
        SetFab();
        Random random = new Random();
        Map<String, Object> t = data.get(random.nextInt(data.size()));
        Intent mIntent = new Intent("StaticBroadcast");
        Intent mIntent_1 = new Intent("WidgetBroadcast");
        Bundle bundle = new Bundle();
        bundle.putString("name", t.get("name").toString());
        bundle.putString("price", t.get("price").toString());
        bundle.putInt("id",(int)t.get("id"));
        bundle.putString("detail", t.get("detail").toString());
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), (int)t.get("id"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        bundle.putByteArray("pic",  baos.toByteArray());
        mIntent.putExtras(bundle);
        mIntent_1.putExtras(bundle);
        sendBroadcast(mIntent);
        sendBroadcast(mIntent_1);
        EventBus.getDefault().register(this);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        shop.setVisibility(View.VISIBLE);
        fab.setImageResource(R.mipmap.mainpage);
        mRecyclerView.setVisibility(View.INVISIBLE);
        fab.setTag(1);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册事件
        EventBus.getDefault().unregister(this);
    }
    private void SetFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().equals(0)) {
                    shop.setVisibility(View.VISIBLE);
                    fab.setImageResource(R.mipmap.mainpage);
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    v.setTag(1);
                } else {
                    fab.setImageResource(R.mipmap.shoplist);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    shop.setVisibility(View.INVISIBLE);
                    v.setTag(0);
                }
            }
        });
    }
    private void SetListView() {
        shoplist_data = new ArrayList<>();
        final ListView mListView = (ListView)findViewById(R.id.listview);
        simpleAdapter = new SimpleAdapter(this, shoplist_data, R.layout.item,
                new String[] {"first", "name", "price"}, new int[] { R.id.first, R.id.name, R.id.price});
        mListView.setAdapter(simpleAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StartIntent(shoplist_data.get(position));
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(view.getContext());
                mBuilder.setTitle("移除物品");
                mBuilder.setMessage("从购物车移除"+shoplist_data.get(position).get("name".toString()));
                mBuilder.setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                shoplist_data.remove(position);
                                simpleAdapter.notifyDataSetChanged();
                            }
                        });
                mBuilder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //  do nothing
                            }
                        });
                mBuilder.show();
                return false;
            }
        });
    }
    private void  SetString() {
        data = new ArrayList<>();
        String[] first = new String[] {"E", "A", "D", "K", "W", "M", "F", "M", "L", "B"};
        String[] name = new String[] {"Enchated Forest", "Arla Milk", "Devondale Milk",
                "Kindle Oasis", "waitrose 早餐麦片", "Mcvitie's 饼干", "Ferrero Rocher",
                "Maltesers", "Lindt", "Borggreve"};
        String[] prize = new String[] { "￥ 5.00", "￥ 59.00", "￥ 79.00", "￥ 2399.00",
                "￥ 179.00", "￥ 14.90", "￥ 132.59", "￥ 141.43", "￥ 139.43", "￥ 28.90"};
        String[] type = new String[] { "作者", "产地", "产地", "版本", "重量", "产地",
                "重量", "重量", "重量", "重量"};
        String[] detail = new String[] { "Johanna Basford", "德国", "澳大利亚", "8GB", "2Kg",
                "英国", "300g", "118g", "249g", "640g"};
        int []id = new int[] {R.mipmap.enchatedforest, R.mipmap.arla, R.mipmap.devondale,
                R.mipmap.kindle, R.mipmap.waitrose, R.mipmap.mcvitie, R.mipmap.ferrero,
                R.mipmap.maltesers, R.mipmap.lindt, R.mipmap.borggreve};
        for (int i = 0; i < 10; i++) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("first", first[i]);
            temp.put("name", name[i]);
            temp.put("price", prize[i]);
            temp.put("detail", type[i]+"  "+detail[i]);
            temp.put("id", id[i]);
            data.add(temp);
        }
    }
    private void SetRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commonAdapter = new CommonAdapter<Map<String, Object>>(this, R.layout.recyclerview_item, data) {
            @Override
            public void convert(ViewHolder holder, Map<String, Object> s) {
                TextView name = holder.getView(R.id.name);
                String _name = s.get("name").toString();
                name.setText(_name);
                TextView first = holder.getView(R.id.first);
                first.setText(_name.substring(0, 1));
            }
        };
        commonAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                StartIntent(data.get(position));
            }
            @Override
            public void onLongClick(int position) {
                data.remove(position);
                commonAdapter.notifyDataSetChanged();
                String message = "移除第"+position+"个商品";
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(commonAdapter);
        animationAdapter.setDuration(1000);
        mRecyclerView.setAdapter(animationAdapter);
        mRecyclerView.setItemAnimator(new OvershootInLeftAnimator());
    }
    private void StartIntent(Map<String, Object> t) {
        Intent intent = new Intent(MainActivity.this, Detail.class);
        intent.putExtra("name", t.get("name").toString());
        intent.putExtra("price", t.get("price").toString());
        intent.putExtra("id",(int)t.get("id"));
        intent.putExtra("detail", t.get("detail").toString());
        startActivityForResult(intent, 1);
    }
    private void addShoplist(Bundle extras) {
        for (int i = 0; i < shoplist_data.size(); i++) {
            if (shoplist_data.get(i).get("name").equals(extras.getString("name"))){
                return;
            }
        }
        Map<String, Object> temp = new LinkedHashMap<>();
        temp.put("first", extras.getString("name").substring(0, 1));
        temp.put("name", extras.getString("name"));
        temp.put("price", extras.getString("price"));
        temp.put("detail", extras.getString("detail"));
        temp.put("id", extras.getInt("id"));
        shoplist_data.add(temp);
        simpleAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            addShoplist(extras);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Detail.MessageEvent event) {
        Bundle bundle = event.getBundle();
        addShoplist(bundle);
    }
}

