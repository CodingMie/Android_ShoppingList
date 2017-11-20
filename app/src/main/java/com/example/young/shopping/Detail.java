package com.example.young.shopping;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.young.shopping.R;
import com.example.young.shopping.receiver.DynamicReceiver;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;

/**
 * Created by Young on 2017/10/22.
 */

public class Detail extends Activity {
    private DynamicReceiver dynamicReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        ListView operationlist = (ListView) findViewById(R.id.detail_list);
        String[] operations = new String[] {"一键下单", "分享产品",
                "不感兴趣", "查看更多商品促销信息"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, operations);
        operationlist.setAdapter(arrayAdapter);
        IntentFilter dynamic_filter = new IntentFilter();
        dynamicReceiver = new DynamicReceiver();
        dynamic_filter.addAction("DynamicAction");
        dynamic_filter.addAction("WidgetDynamic");
        registerReceiver(dynamicReceiver, dynamic_filter);
        final ImageView star = (ImageView)findViewById(R.id.favorite);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (star.getTag().equals(0)) {
                    star.setImageResource(R.mipmap.full_star);
                    star.setTag(1);
                } else {
                    star.setImageResource(R.mipmap.empty_star);
                    star.setTag(0);
                }
            }
        });
        ImageView back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unregisterReceiver(dynamicReceiver);
                finish();
            }
        });
        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            TextView name = (TextView) findViewById(R.id.name);
            TextView price = (TextView)findViewById(R.id.price);
            TextView detail = (TextView)findViewById(R.id.detail);
            ImageView pic = (ImageView)findViewById(R.id.pic);
            name.setText(extras.getString("name"));
            price.setText(extras.getString("price"));
            detail.setText(extras.getString("detail"));
            pic.setImageResource(extras.getInt("id"));
        }
        ImageView buy = (ImageView)findViewById(R.id.buy);
        setResult(RESULT_CANCELED);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "商品已添加到购物车", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new MessageEvent(extras));
                Intent mIntent = new Intent("DynamicAction");
                Intent mIntent_1 = new Intent("WidgetDynamic");
                mIntent.putExtra("name", extras.get("name").toString());
                mIntent.putExtra("price", extras.get("price").toString());
                mIntent.putExtra("id",(int)extras.get("id"));
                mIntent.putExtra("detail", extras.get("detail").toString());
                Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), (int)extras.get("id"));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                mIntent.putExtra("pic",  baos.toByteArray());
                mIntent_1.putExtras(mIntent.getExtras());
                sendBroadcast(mIntent);
                sendBroadcast(mIntent_1);
            }
        });
    }
    public static class MessageEvent {
        Bundle mBundle;
        public MessageEvent(Bundle bundle) {
            mBundle = bundle;
        }
        public Bundle getBundle() {
            return mBundle;
        }
    }
}

