package com.example.tommy.carquiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import java.util.Collections;
import java.util.Random;

public class Main3Activity extends Main2Activity {
    int[] imageListUSA= {
            R.drawable.chevrolet_corvette,
            R.drawable.plymouth_barracuda,
            R.drawable.ford_f150,
            R.drawable.dodge_challenger,
            R.drawable.ford_mustang,
            R.drawable.dodge_viper,
            R.drawable.cadillac_escalade,
            R.drawable.pontiac_gto,
            R.drawable.hummer_h3,
            R.drawable.chevrolet_camaro
    };

    public void setupList()
    {
        Random random =new Random();
        int imageSelected = imageListUSA[random.nextInt(imageListUSA.length)];
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
    }public void gameOver() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Main3Activity.this);
        alertDialog.setMessage("Špatně! Počet dosažených bodů je: " + score ).setCancelable(false)
                .setPositiveButton("Znovu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(getApplicationContext(),Main3Activity.class));
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
