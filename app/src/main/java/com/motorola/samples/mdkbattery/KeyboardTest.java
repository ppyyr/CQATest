package com.motorola.samples.mdkbattery;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyboardTest extends Activity{

    private static final String TAG = "KeyboardTest";

    private List<String> testList = new ArrayList<>();
    Map<Integer, Integer> keycodeMap = new HashMap<>();

    private GridView mGridView;
    private KeycodeAdapter mKeycodeAdapter;

    private int[] ARRAY_KEYCODE = {
            KeyEvent.KEYCODE_F1, KeyEvent.KEYCODE_F2, KeyEvent.KEYCODE_F3, KeyEvent.KEYCODE_F4, KeyEvent.KEYCODE_F5,
            KeyEvent.KEYCODE_F6, KeyEvent.KEYCODE_F7, KeyEvent.KEYCODE_F8, KeyEvent.KEYCODE_F9, KeyEvent.KEYCODE_F10,
            KeyEvent.KEYCODE_F11, KeyEvent.KEYCODE_F12, KeyEvent.KEYCODE_0, KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_2,
            KeyEvent.KEYCODE_3, KeyEvent.KEYCODE_4, KeyEvent.KEYCODE_5, KeyEvent.KEYCODE_6, KeyEvent.KEYCODE_7,
            KeyEvent.KEYCODE_8, KeyEvent.KEYCODE_9, KeyEvent.KEYCODE_A, KeyEvent.KEYCODE_B, KeyEvent.KEYCODE_C,
            KeyEvent.KEYCODE_D, KeyEvent.KEYCODE_E, KeyEvent.KEYCODE_F, KeyEvent.KEYCODE_G, KeyEvent.KEYCODE_H,
            KeyEvent.KEYCODE_I, KeyEvent.KEYCODE_J, KeyEvent.KEYCODE_K, KeyEvent.KEYCODE_L, KeyEvent.KEYCODE_M,
            KeyEvent.KEYCODE_N, KeyEvent.KEYCODE_O, KeyEvent.KEYCODE_P, KeyEvent.KEYCODE_Q, KeyEvent.KEYCODE_R,
            KeyEvent.KEYCODE_S, KeyEvent.KEYCODE_T, KeyEvent.KEYCODE_U, KeyEvent.KEYCODE_V, KeyEvent.KEYCODE_W,
            KeyEvent.KEYCODE_X, KeyEvent.KEYCODE_Y, KeyEvent.KEYCODE_Z
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.keyboard_test);
        init();
    }

    private void init() {
        mGridView = (GridView) findViewById(R.id.keyboard_gridview);
        String[] keycodes = getResources().getStringArray(R.array.keycode_list);
        List<String> list = Arrays.asList(keycodes);
        testList.addAll(list);
        mKeycodeAdapter = new KeycodeAdapter(this, testList);
        mGridView.setAdapter(mKeycodeAdapter);

        int[] positions = getResources().getIntArray(R.array.keycode_position);
        for (int i=0; i < ARRAY_KEYCODE.length; i++) {
            keycodeMap.put(ARRAY_KEYCODE[i], positions[i]);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e(TAG,"keyCode is :"+ keyCode);
        if (keycodeMap.containsKey(keyCode)) {
            Log.e(TAG, "get keycode and set view color");
            int position = keycodeMap.get(keyCode);
            mKeycodeAdapter.updateData(position);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class KeycodeAdapter extends BaseAdapter {

        private Context mContext;
        private List<KeycodeEntity> mKeycodeList = new ArrayList<>();

        public KeycodeAdapter(Context context, List<String> keycodeList) {
            mContext = context;
            for (String keycode : keycodeList) {
                KeycodeEntity entity = new KeycodeEntity(keycode, false);
                mKeycodeList.add(entity);
            }
        }

        public void setData(List<String> list) {
            mKeycodeList.clear();
            for (String keycode : list) {
                KeycodeEntity entity = new KeycodeEntity(keycode, false);
                mKeycodeList.add(entity);
            }
            notifyDataSetChanged();
        }

        public void updateData(int position) {
            boolean state = mKeycodeList.get(position).getState();
            mKeycodeList.get(position).setState(!state);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mKeycodeList.size();
        }

        @Override
        public Object getItem(int position) {
            return mKeycodeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.kerboard_list_item, null);
                viewHolder.tv_keycode = (TextView) convertView.findViewById(R.id.keycode);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_keycode.setText(mKeycodeList.get(position).value);
            if (mKeycodeList.get(position).getState()) {
                GradientDrawable shapeDrawable = (GradientDrawable)viewHolder.tv_keycode.getBackground();
                shapeDrawable.setColor(getColor(R.color.keycode_pressed_color));
            } else {
                GradientDrawable shapeDrawable = (GradientDrawable)viewHolder.tv_keycode.getBackground();
                shapeDrawable.setColor(getColor(R.color.keycode_default_color));
            }
            return convertView;
        }

        private class ViewHolder {
            TextView tv_keycode;
        }

        private class KeycodeEntity {
            String value;
            boolean state;

            KeycodeEntity(String value, boolean state) {
                this.value = value;
                this.state = state;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getValue() {
                return value;
            }

            public void setState(boolean state) {
                this.state = state;
            }

            public boolean getState() {
                return state;
            }
        }
    }
}
