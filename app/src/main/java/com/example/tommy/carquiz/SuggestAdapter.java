package com.example.tommy.carquiz;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;

import java.util.List;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by Tommy on 1.12.2017.
 */

public class SuggestAdapter extends BaseAdapter {

    private List<String> suggest;
    private Context context;
    private Main2Activity main2Activity;

    public SuggestAdapter(List<String> suggest, Context context, Main2Activity main2Activity) {
        this.suggest = suggest;
        this.context = context;
        this.main2Activity = main2Activity;
    }

    @Override
    public int getCount() {
        return suggest.size();
    }

    //@Override
    public Object getItem(int position) {
        return suggest.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        Button button;
        if (view==null) {
            if (suggest.get(position).equals("null")) {
                button = new Button(context);
                button.setLayoutParams(new GridView.LayoutParams(85, 85));
                button.setPadding(8, 8, 8, 8);
                button.setTextColor(Color.CYAN);
                button.setBackgroundColor(Color.DKGRAY);
            }
            else {
                button = new Button(context);
                button.setLayoutParams(new GridView.LayoutParams(85, 85));
                button.setPadding(8, 8, 8, 8);
                button.setBackgroundColor(Color.DKGRAY);
                button.setTextColor(Color.CYAN);
                button.setText(suggest.get(position));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(String.valueOf(main2Activity.answer).contains(suggest.get(position)))
                        {
                            char compare = suggest.get(position).charAt(0);
                            for (int i=0;i<main2Activity.answer.length;i++)
                            {
                                if(compare==main2Activity.answer[i])
                                    Common.submit_answer[i]=compare;
                            }
                            AnswerAdapter answerAdapter=new AnswerAdapter(Common.submit_answer,context);
                            main2Activity.gridViewAnswer.setAdapter(answerAdapter);
                            answerAdapter.notifyDataSetChanged();

                            main2Activity.suggest.set(position,"null");
                            main2Activity.suggestAdapter=new SuggestAdapter(main2Activity.suggest,context,main2Activity);
                            main2Activity.gridViewSuggest.setAdapter(main2Activity.suggestAdapter);
                            main2Activity.suggestAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            vibe();
                        }

                    }
                });
            }
        }
        else
            button= (Button)view;
        return button;
    }
    private void vibe() {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(150);
        }
    }
}
