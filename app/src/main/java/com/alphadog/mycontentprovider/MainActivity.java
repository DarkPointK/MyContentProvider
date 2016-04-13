package com.alphadog.mycontentprovider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {

    private static final String PROVIDER_NAME = "com.alphadog.mycontentprovider";
    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/string");

    private Cursor c;
    private String s;
    private boolean fresh = true;
    private Uri seleUri;
    private int id;

    @InjectView(R.id.tv)
    TextView tv;
    @InjectView(R.id.et)
    EditText et;
    @InjectView(R.id.add)
    Button add;
    @InjectView(R.id.del)
    Button del;
    @InjectView(R.id.update)
    Button update;
    @InjectView(R.id.query)
    Button query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fresh = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @OnClick({R.id.add, R.id.del, R.id.update, R.id.query})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
                add();
                break;
            case R.id.del:
                del();
                break;
            case R.id.update:
                update();
                break;
            case R.id.query:
                query();
                break;
        }
    }


    private void add() {
        ContentValues cv = new ContentValues();
        cv.put("str", et.getText().toString());
        Uri uri = getContentResolver().insert(CONTENT_URI, cv);
        if (uri != null) {
            Toast.makeText(getBaseContext(), uri.toString(), LENGTH_SHORT).show();
        }
    }

    private void del() {
//        seleUri = ContentUris.withAppendedId(CONTENT_URI, id);
//        Log.i("将要删除" + seleUri, "!");

//        条件只传id，因为在StringDataBase中有完整处理where语句
        int uri = getContentResolver().delete(CONTENT_URI, "" + id, null);
        Toast.makeText(getBaseContext(), "删除成功" + id, LENGTH_SHORT).show();
        fresh = true;
    }

    private void update() {
        ContentValues cv = new ContentValues();

        if (!c.getString(1).equals(et.getText().toString())) {
            cv.put("str", et.getText().toString());
            int uri = getContentResolver().update(CONTENT_URI, cv, "" + id, null);
            Log.i("修改id为" + id + "的值为", " " + et.getText().toString());
            Toast.makeText(getBaseContext(), "修改成功" + id+"为"+et.getText().toString(), LENGTH_SHORT).show();
            fresh = true;
        } else {
            Toast.makeText(this, "没做任何更改！", LENGTH_SHORT).show();
        }
    }

    private void query() {
        s = et.getText().toString();
        if (!s.equals("") && fresh) {
            s = "str = '" + et.getText().toString() + "'";
            Log.i("正在搜索的s为", s);
            Cursor cursor = getContentResolver().query(CONTENT_URI, null, s, null, null);

//                CursorLoader cl=new CursorLoader(getBaseContext(),CONTENT_URI,null,null,sa2,null);
//                Cursor c=cl.loadInBackground();

            if (cursor == null || cursor.getCount() <= 0) {
                Toast.makeText(this, "然而什么都没搜到", LENGTH_SHORT).show();
                return;
            }
            Log.i("没搜到的话不应该显示这个", "！！！！！");
            c = cursor;
        }
        display();
    }

    private void display() {
        if (c != null && c.moveToNext()) {
            Log.i("c", c.getString(0) + "   " + c.getString(1));
            tv.setText(c.getString(0));
            id = c.getInt(0);

            fresh = false;
        } else {
            Toast.makeText(this, "搜索结束，队列将刷新并从头开始", LENGTH_SHORT).show();
            fresh = true;
//            c.moveToFirst();
//            tv.setText(c.getString(0));
        }
    }

}
