package com.example.tommy.carquiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import java.util.Collections;
import java.util.Random;

public class Main4Activity extends Main2Activity {

    int[] imageListAE= {
            R.drawable.kia_ceed,
            R.drawable.toyota_supra,
            R.drawable.hyundai_coupe,
            R.drawable.subaru_impreza,
            R.drawable.mazda_rx8,
            R.drawable.mitsubishi_lancer,
            R.drawable.nissan_350z,
            R.drawable.lexus_lc,
            R.drawable.honda_nsx,
            R.drawable.infiniti_fx
    };

    public void setupList()
    {
        Random random =new Random();
        int imageSelected = imageListAE[random.nextInt(imageListAE.length)];
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
    public void gameOver() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Main4Activity.this);
        alertDialog.setMessage("Špatně! Počet dosažených bodů je: " + score ).setCancelable(false)
                .setPositiveButton("Znovu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(getApplicationContext(),Main4Activity.class));
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
}
