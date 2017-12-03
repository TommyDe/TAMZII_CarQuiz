package com.example.tommy.carquiz;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;


/**
 * Created by Tommy on 1.12.2017.
 */

public class AnswerAdapter extends BaseAdapter {

    private char[] answer;
    private Context context;

    public AnswerAdapter(char[] answer, Context context) {
        this.answer = answer;
        this.context = context;
    }

    @Override
    public int getCount() {
        return answer.length;
    }

    @Override
    public Object getItem(int position) {
        return answer[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Button button;
        if (view==null)
        {
            button=new Button(context);
            button.setLayoutParams(new GridView.LayoutParams(85,85));
            button.setPadding(8,8,8,8);
            button.setText(String.valueOf(answer[position]));
            button.setTextColor(Color.CYAN);
            button.setBackgroundColor(Color.DKGRAY);
        }
        else
            button=(Button)view;
        return button;
    }
}
