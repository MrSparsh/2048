package com.example.android.a2048;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{
    private LinearLayout scoreLayout;
    private LinearLayout highScoreLayout;
    private TextView scoreTextView;
    private TextView highScoreTextView;
    private long score;
    private long highScore;
    private final int UP = 0;
    private final int DOWN = 1;
    private final int LEFT = 2;
    private final int RIGHT = 3;
    private GestureDetectorCompat gestureDetectorCompat;
    private TextView[][] cellTextViewMatrix;
    private int[][] cellValueMatrix;
    ArrayList<Pair<Integer,Integer> > blankPairs;
    HashMap<Integer,Integer>  correspondingColor;
    Button playAgainButton;
    boolean isGame;
    SharedPreferences sharedPreferences;

    public void reset(View v) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cellValueMatrix[i][j] = 0;
                fillCellTextView(cellTextViewMatrix[i][j], cellValueMatrix[i][j]);
                blankPairs.add(new Pair<Integer, Integer>(i, j));
            }
        }
        playAgainButton.setVisibility(View.INVISIBLE);
        isGame=true;
        score = 0;
        scoreTextView.setText(""+score);
        highScore = sharedPreferences.getLong("highScore",0);
        highScoreTextView.setText(""+highScore);
        fillRandomNo();
        fillRandomNo();
    }

    boolean isGameOver(){
        if(cellValueMatrix[0][0]==0)
            return false;
        for(int i=0;i<4;i++) {
            for(int j=1;j<4;j++){
                if(cellValueMatrix[i][j]==0 || cellValueMatrix[i][j]==cellValueMatrix[i][j-1])
                    return false;
            }
        }
        for(int j=0;j<4;j++) {
            for(int i=1;i<4;i++){
                if(cellValueMatrix[i][j]==0 || cellValueMatrix[i][j]==cellValueMatrix[i-1][j])
                    return false;
            }
        }
        return true;
    }

    void fillRandomNo() {
        if(blankPairs.isEmpty())
            return;
        Random random = new Random();
        int randomIndex = random.nextInt(blankPairs.size());
        Pair<Integer,Integer> randomBlankCell = blankPairs.get(randomIndex);
        int x = randomBlankCell.first;
        int y = randomBlankCell.second;
        blankPairs.remove(randomIndex);
        int fillValue = random.nextInt(2);
        if(fillValue==0)
            cellValueMatrix[x][y]=2;
        else
            cellValueMatrix[x][y]=4;
        fillCellTextView(cellTextViewMatrix[x][y],cellValueMatrix[x][y]);
    }

    void fillCellTextView(TextView textView,int num) {
        if(num==0)
            textView.setText(" ");
        else
            textView.setText(""+num);
        switch(num)
        {
            case 0: textView.setBackgroundColor(Color.LTGRAY);
                textView.setTextColor(Color.BLACK);
                break;
            case 2: textView.setBackgroundColor(Color.rgb(240,240,240));
                textView.setTextColor(Color.BLACK);
                break;
            case 4: textView.setBackgroundColor(Color.rgb(255,255,224));
                textView.setTextColor(Color.BLACK);
                break;
            case 8: textView.setBackgroundColor(Color.rgb(255,200,100));
                textView.setTextColor(Color.WHITE);
                break;
            case 16: textView.setBackgroundColor(Color.rgb(255,140,30));
                textView.setTextColor(Color.WHITE);
                break;
            case 32: textView.setBackgroundColor(Color.rgb(255,100,65));
                textView.setTextColor(Color.WHITE);
                break;
            case 64: textView.setBackgroundColor(Color.rgb(250,80,100));
                textView.setTextColor(Color.WHITE);
                break;
            case 128: textView.setBackgroundColor(Color.rgb(255,220,0));
                textView.setTextColor(Color.WHITE);
                break;
            case 256: textView.setBackgroundColor(Color.rgb(240,240,0));
                textView.setTextColor(Color.BLACK);
                break;
            case 512: textView.setBackgroundColor(Color.rgb(245,245,0));
                textView.setTextColor(Color.BLACK);
                break;
            case 1024: textView.setBackgroundColor(Color.rgb(250,250,0));
                textView.setTextColor(Color.BLACK);
                break;
            case 2048: textView.setBackgroundColor(Color.rgb(255,255,0));
                textView.setTextColor(Color.BLACK);
                break;
            default: textView.setBackgroundColor(Color.rgb(255,255,0));
                textView.setTextColor(Color.BLACK);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        scoreLayout = (LinearLayout) findViewById(R.id.scoreLayout);
        highScoreLayout = (LinearLayout)findViewById(R.id.highScoreLayout);
        scoreTextView = (TextView) scoreLayout.findViewById(R.id.scoreTextView);
        highScoreTextView = (TextView)highScoreLayout.findViewById(R.id.highScoreTextView);
        gestureDetectorCompat = new GestureDetectorCompat(this,this);
        score = 0;
        scoreTextView.setText(""+score);
        cellTextViewMatrix = new TextView[][]{
                {(TextView)findViewById(R.id.cell1),(TextView)findViewById(R.id.cell2),(TextView)findViewById(R.id.cell3),(TextView)findViewById(R.id.cell4)},
                {(TextView)findViewById(R.id.cell5),(TextView)findViewById(R.id.cell6),(TextView)findViewById(R.id.cell7),(TextView)findViewById(R.id.cell8)},
                {(TextView)findViewById(R.id.cell9),(TextView)findViewById(R.id.cell10),(TextView)findViewById(R.id.cell11),(TextView)findViewById(R.id.cell12)},
                {(TextView)findViewById(R.id.cell13),(TextView)findViewById(R.id.cell14),(TextView)findViewById(R.id.cell15),(TextView)findViewById(R.id.cell16)}
        };
        cellValueMatrix = new int[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
        blankPairs = new ArrayList<Pair<Integer,Integer> >();
        for(int i=0;i<4;i++) {
            for (int j = 0; j < 4; j++)
                blankPairs.add(new Pair<Integer, Integer>(i, j));
        }
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
                fillCellTextView(cellTextViewMatrix[i][j],cellValueMatrix[i][j]);
        }
        fillRandomNo();
        fillRandomNo();
        playAgainButton = (Button)findViewById(R.id.playAgainButton);
        playAgainButton.setVisibility(View.INVISIBLE);
        isGame=true;
        sharedPreferences = this.getSharedPreferences("com.example.android.a2048",MODE_PRIVATE);
        highScore = sharedPreferences.getLong("highScore",0);
        highScoreTextView.setText(""+highScore);
        correspondingColor = new HashMap<Integer, Integer>();
        correspondingColor.put(0,Color.argb(1,220,0,0));

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void onSwipeUp() {
        if(!isGame)
            return;
        boolean onSwipeUpChange=false;
        for(int j=0;j<4;j++)
        {
            for(int i=1;i<4;i++)
            {
                if(cellValueMatrix[i][j]==0)
                    continue;
                int k=i-1;
                while(k>=0 && cellValueMatrix[k][j]==0)
                    k--;
                if( k==-1 || (cellValueMatrix[k][j]!=cellValueMatrix[i][j] && (k+1)!=i) ) {
                    cellValueMatrix[k + 1][j] = cellValueMatrix[i][j];
                    onSwipeUpChange=true;
                    blankPairs.remove(new Pair<Integer, Integer>(k+1,j) );
                    cellValueMatrix[i][j]=0;
                } else if(cellValueMatrix[k][j]==cellValueMatrix[i][j]){
                    cellValueMatrix[k][j] += cellValueMatrix[i][j];
                    onSwipeUpChange=true;
                    score+=2*cellValueMatrix[i][j];
                    scoreTextView.setText(""+score);
                    cellValueMatrix[i][j]=0;
                }
            }
            for(int i=0;i<4;i++) {
                fillCellTextView(cellTextViewMatrix[i][j],cellValueMatrix[i][j]);
                if(cellValueMatrix[i][j]==0 && !blankPairs.contains(new Pair<Integer,Integer>(i,j)))
                        blankPairs.add(new Pair<Integer,Integer>(i,j));
            }
        }
        if(onSwipeUpChange)
            fillRandomNo();
        if(isGameOver()) {
            highScore = sharedPreferences.getLong("highScore",0);
            if(score>highScore)
                sharedPreferences.edit().putLong("highScore",score).apply();
            Toast.makeText(getApplicationContext(), "Game Over", Toast.LENGTH_SHORT).show();
            isGame=false;
            playAgainButton.setVisibility(View.VISIBLE);
        }
    }

    private  void onSwipeDown() {
        if(!isGame)
            return;
        boolean onSwipeDownChange = false;
        for(int j=0;j<4;j++)
        {
            for(int i=2;i>=0;i--)
            {
                if(cellValueMatrix[i][j]==0)
                    continue;
                int k=i+1;
                while(k<=3 && cellValueMatrix[k][j]==0)
                    k++;
                if( k==4 || (cellValueMatrix[k][j]!=cellValueMatrix[i][j] && (k-1)!=i) ) {
                    onSwipeDownChange = true;
                    cellValueMatrix[k-1][j] = cellValueMatrix[i][j];
                    blankPairs.remove(new Pair<Integer, Integer>(k-1,j) );
                    cellValueMatrix[i][j]=0;
                } else if(cellValueMatrix[k][j]==cellValueMatrix[i][j]){
                    onSwipeDownChange = true;
                    cellValueMatrix[k][j] += cellValueMatrix[i][j];
                    score+=2*cellValueMatrix[i][j];
                    scoreTextView.setText(""+score);
                    cellValueMatrix[i][j]=0;
                }
            }
            for(int i=0;i<4;i++) {
                fillCellTextView(cellTextViewMatrix[i][j],cellValueMatrix[i][j]);
                if(cellValueMatrix[i][j]==0 && !blankPairs.contains(new Pair<Integer,Integer>(i,j)))
                    blankPairs.add(new Pair<Integer,Integer>(i,j));
            }
        }
        if(onSwipeDownChange)
            fillRandomNo();
        if(isGameOver()) {
            highScore = sharedPreferences.getLong("highScore",0);
            if(score>highScore)
                sharedPreferences.edit().putLong("highScore",score).apply();
            Toast.makeText(getApplicationContext(), "Game Over", Toast.LENGTH_SHORT).show();
            isGame=false;
            playAgainButton.setVisibility(View.VISIBLE);
        }
    }

    private  void onSwipeLeft() {
        if(!isGame)
            return;
        boolean onSwipeLeftChange = false;
        for(int i=0;i<4;i++)
        {
            for(int j=1;j<4;j++)
            {
                if(cellValueMatrix[i][j]==0)
                    continue;
                int k=j-1;
                while(k>=0 && cellValueMatrix[i][k]==0)
                    k--;
                if( k==-1 || (cellValueMatrix[i][k]!=cellValueMatrix[i][j] && (k+1)!=j) ) {
                    onSwipeLeftChange = true;
                    cellValueMatrix[i][k+1] = cellValueMatrix[i][j];
                    blankPairs.remove(new Pair<Integer, Integer>(i,k+1) );
                    cellValueMatrix[i][j]=0;
                } else if(cellValueMatrix[i][k]==cellValueMatrix[i][j]){
                    onSwipeLeftChange = true;
                    cellValueMatrix[i][k] += cellValueMatrix[i][j];
                    score+=2*cellValueMatrix[i][j];
                    scoreTextView.setText(""+score);
                    cellValueMatrix[i][j]=0;
                }
            }
            for(int j=0;j<4;j++) {
                fillCellTextView(cellTextViewMatrix[i][j],cellValueMatrix[i][j]);
                if(cellValueMatrix[i][j]==0 && !blankPairs.contains(new Pair<Integer,Integer>(i,j)))
                    blankPairs.add(new Pair<Integer,Integer>(i,j));
            }
        }
        if(onSwipeLeftChange)
            fillRandomNo();
        if(isGameOver()) {
            highScore = sharedPreferences.getLong("highScore",0);
            if(score>highScore)
                sharedPreferences.edit().putLong("highScore",score).apply();
            Toast.makeText(getApplicationContext(), "Game Over", Toast.LENGTH_SHORT).show();
            isGame=false;
            playAgainButton.setVisibility(View.VISIBLE);
        }
    }

    private  void onSwipeRight() {
        if(!isGame)
            return;
        boolean onSwipeRightChange = false;
        for(int i=0;i<4;i++)
        {
            for(int j=2;j>=0;j--)
            {
                if(cellValueMatrix[i][j]==0)
                    continue;
                int k=j+1;
                while(k<=3 && cellValueMatrix[i][k]==0)
                    k++;
                if( k==4 || (cellValueMatrix[i][k]!=cellValueMatrix[i][j] && (k-1)!=j) ) {
                    onSwipeRightChange = true;
                    cellValueMatrix[i][k-1] = cellValueMatrix[i][j];
                    blankPairs.remove(new Pair<Integer, Integer>(i,k-1) );
                    cellValueMatrix[i][j]=0;
                } else if(cellValueMatrix[i][k]==cellValueMatrix[i][j]){
                    onSwipeRightChange = true;
                    cellValueMatrix[i][k] += cellValueMatrix[i][j];
                    score+=2*cellValueMatrix[i][j];
                    scoreTextView.setText(""+score);
                    cellValueMatrix[i][j]=0;
                }
            }
            for(int j=0;j<4;j++) {
                fillCellTextView(cellTextViewMatrix[i][j],cellValueMatrix[i][j]);
                if(cellValueMatrix[i][j]==0 && !blankPairs.contains(new Pair<Integer,Integer>(i,j)))
                    blankPairs.add(new Pair<Integer,Integer>(i,j));
            }
        }
        if(onSwipeRightChange)
            fillRandomNo();
        if(isGameOver()) {
            highScore = sharedPreferences.getLong("highScore",0);
            if(score>highScore)
                sharedPreferences.edit().putLong("highScore",score).apply();
            Toast.makeText(getApplicationContext(), "Game Over", Toast.LENGTH_SHORT).show();
            isGame=false;
            playAgainButton.setVisibility(View.VISIBLE);
        }
    }

    int getDirection(double x1,double y1,double x2,double y2) {
        if(Math.abs(y2-y1)>Math.abs(x2-x1)) {
            if(y2>y1)
                return DOWN;
            else
                return UP;
        } else{
            if(x2>x1)
                return RIGHT;
            else
                return LEFT;
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        int dir = getDirection(e1.getX(),e1.getY(),e2.getX(),e2.getY());
        if(dir == UP)
            onSwipeUp();
        else if(dir == DOWN)
            onSwipeDown();
        else if(dir == LEFT)
            onSwipeLeft();
        else
            onSwipeRight();
        return true;
    }







    ////// Of No use
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

}
