package com.example.tommy.carquiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main2Activity extends Activity {

    public List<String> suggest=new ArrayList<>();

    public AnswerAdapter answerAdapter;
    public SuggestAdapter suggestAdapter;

    public Button submitBTN;
    public GridView gridViewAnswer, gridViewSuggest;
    public TextView textScore;

    public ImageView questionIMG;

    public char[] answer;

    public int score=10;

    Vibrator vibrator;

    int[] imageListEU= {
            R.drawable.skoda_octavia,
            R.drawable.porsche_911,
            R.drawable.range_rover,
            R.drawable.vw_scirocco,
            R.drawable.fiat_500,
            R.drawable.ferrari_458,
            R.drawable.bugatti_chiron,
            R.drawable.bmw_m3,
            R.drawable.renault_megane,
            R.drawable.mercedes_sls
    };

    String correct;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
        Help();
        preferences=getSharedPreferences("Pref", Context.MODE_PRIVATE);
        score=preferences.getInt("score",10);
        textScore.setText("Body: "+score);
    }

    private void vibe() {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(150);
        }
    }

    private void initView() {
        gridViewAnswer=(GridView)findViewById(R.id.gridViewAnswer);
        gridViewSuggest=(GridView)findViewById(R.id.gridViewSuggest);
        questionIMG=(ImageView) findViewById(R.id.car);
        textScore=(TextView)findViewById(R.id.textView);

        setupList();

        submitBTN=(Button)findViewById(R.id.submitBTN);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result="";
                for (int i=0;i<Common.submit_answer.length;i++)
                    result+=String.valueOf(Common.submit_answer[i]);
                if (result.equals(correct))
                {
                    Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_SHORT).show();

                    Common.count=0;
                    Common.submit_answer=new char[correct.length()];

                    AnswerAdapter answerAdapter = new AnswerAdapter(setupNullList(),getApplicationContext());
                    gridViewAnswer.setAdapter(answerAdapter);
                    answerAdapter.notifyDataSetChanged();

                    SuggestAdapter suggestAdapter = new SuggestAdapter(suggest,getApplicationContext(),Main2Activity.this);
                    gridViewAnswer.setAdapter(suggestAdapter);
                    suggestAdapter.notifyDataSetChanged();

                    score=score+5;
                    textScore.setText("Body: "+score);

                    setupList();
                }
                else
                {
                    Toast.makeText(Main2Activity.this, "Špatná odpověď",Toast.LENGTH_SHORT).show();
                    vibe();
                    gameOver();
                    editor=preferences.edit();
                    editor.putInt("score",score);
                    editor.apply();
                }
            }
        });
    }

    public void setupList()
    {
        Random random =new Random();
        int imageSelected = imageListEU[random.nextInt(imageListEU.length)];
        questionIMG.setImageResource(imageSelected);

        correct=getResources().getResourceName(imageSelected);
        correct=correct.substring(correct.lastIndexOf("/")+1);

        answer=correct.toCharArray();

        Common.submit_answer=new char[answer.length];

        suggest.clear();
        for(char item:answer)
        {
            suggest.add(String.valueOf(item));
        }
        for(int i=answer.length;i<answer.length*2;i++)
            suggest.add(Common.alphabet[random.nextInt(Common.alphabet.length)]);

        Collections.shuffle(suggest);

        answerAdapter=new AnswerAdapter(setupNullList(),this);
        suggestAdapter=new SuggestAdapter(suggest,this,this);

        answerAdapter.notifyDataSetChanged();
        suggestAdapter.notifyDataSetChanged();

        gridViewAnswer.setAdapter(answerAdapter);
        gridViewSuggest.setAdapter(suggestAdapter);

    }

    private char[] setupNullList()
    {
        char result[]=new char[answer.length];
        for (int i=0;i<answer.length;i++)
            result[i]=' ';
        return result;
    }

    public void Help() {
        questionIMG.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        questionIMG.getDrawable().setColorFilter(Color.argb(120, 0, 255, 255), PorterDuff.Mode.SRC_ATOP);
                        vibe();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        questionIMG.getDrawable().clearColorFilter();
                        gameHelp();
                        break;
                    }

                }
                return true;
            }
        });
    }

    public void gameOver() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Main2Activity.this);
        alertDialog.setMessage("Špatně! Počet dosažených bodů je: " + score ).setCancelable(false)
                .setPositiveButton("Znovu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(getApplicationContext(),Main2Activity.class));
                    }
                })
                .setNegativeButton("Konec", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                });
        alertDialog.show();
    }

    public void gameHelp() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
        alertDialog.setMessage("Chcete zobrazit nápovědu ?").setCancelable(false)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),correct,Toast.LENGTH_LONG).show();
                score=score-2;
                if (score<=0) {
                    gameOver();
                }
                else{
                    textScore.setText("Body: " + score);
                    editor = preferences.edit();
                    editor.putInt("score", score);
                    editor.apply();
                }
                vibe();
            }
        })
                .setPositiveButton("I s info", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String result = correct.replaceAll("_", "/");
                        String url ="https://www.parkers.co.uk/"+result;
                        Intent intent= new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        score=score-4;
                        if (score<=0) {
                            gameOver();
                        }
                        else{
                            textScore.setText("Body: " + score);
                            editor = preferences.edit();
                            editor.putInt("score", score);
                            editor.apply();
                        }

                        vibe();
                    }
                })
                .setNegativeButton("Cencel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onBackPressed(){
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        editor = preferences.edit();
        editor.putInt("score", score);
        editor.apply();
        finish();
    }

}

