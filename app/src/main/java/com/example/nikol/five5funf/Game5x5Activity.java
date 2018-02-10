package com.example.nikol.five5funf;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class Game5x5Activity extends AppCompatActivity implements View.OnClickListener {

    private int numberOfElements;

    private MemoryButton[] buttons;

    private int[] buttonGraphicLocation;
    private int[] buttonGraphics;

    private MemoryButton selectedButton1;
    private MemoryButton selectedButton2;

    private boolean isBusy = false;

    @Override
    protected void onCreate(Bundle newInstanceState) {
        super.onCreate(newInstanceState);
        setContentView(R.layout.activity_game5x5);

        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid_layout_5x5);

        int numColumns = gridLayout.getColumnCount();
        int numRows = gridLayout.getRowCount();

        numberOfElements = numColumns * numRows;

        buttons = new MemoryButton[numberOfElements];

        buttonGraphics = new int[numberOfElements / 2];

        buttonGraphics[0] = R.drawable.nula;
        buttonGraphics[1] = R.drawable.jedan;
        buttonGraphics[2] = R.drawable.dva;
        buttonGraphics[3] = R.drawable.tri;
        buttonGraphics[4] = R.drawable.cetiri;
        buttonGraphics[5] = R.drawable.pet;
        buttonGraphics[6] = R.drawable.sest;
        buttonGraphics[7] = R.drawable.sedam;

        buttonGraphicLocation = new int[numberOfElements];

        shuffleButtonGraphics();

        for(int r = 0; r<numRows; r++) {
            for(int c=0; c<numColumns; c++){
                MemoryButton tempButton = new MemoryButton(this, r, c, buttonGraphics[ buttonGraphicLocation[r * numColumns + c]]);
                tempButton.setId(View.generateViewId());
                tempButton.setOnClickListener(this);
                buttons[r * numColumns + c] = tempButton;
                gridLayout.addView(tempButton);
            }
        }

    }


    protected void shuffleButtonGraphics() {

        Random rand = new Random();

        for(int i=0; i<numberOfElements; i++){
            buttonGraphicLocation[i] = i % (numberOfElements / 2);
        }

        for(int i=0; i<numberOfElements; i++) {
            int temp = buttonGraphicLocation[i];

            int swapIndex = rand.nextInt(16);

            buttonGraphicLocation[i] = buttonGraphicLocation [swapIndex];

            buttonGraphicLocation [swapIndex] = temp;
        }
    }



    @Override
    public void onClick(View view) {

        if(isBusy)
            return;

        MemoryButton button = (MemoryButton) view;

        if(button.isMatched)
            return;

        if(selectedButton1 == null) {
            selectedButton1 = button;
            selectedButton1.flip();
            return;
        }

        if (selectedButton1.getId() == button.getId()) {
            return;
        }

        if(selectedButton1.getFrontDrawableId() == button.getFrontDrawableId()){
            button.flip();
            button.setMatched(true);
            selectedButton1.setMatched(true);

            selectedButton1.setEnabled(false);
            button.setEnabled(false);
            button.getBackground().setAlpha(0);
            selectedButton1 = null;

            return;
        }

        else {
            selectedButton2 = button;
            selectedButton2.flip();
            isBusy = true;

            final android.os.Handler handler = new android.os.Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    selectedButton2.flip();
                    selectedButton1.flip();
                    selectedButton1=null;
                    selectedButton2=null;
                    isBusy=false;
                }
            }, 500);
        }
    }

}
