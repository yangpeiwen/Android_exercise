package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private TextView mQuestionTextView;
    //设置要用到的字符串
    private static final String KEY_INDEX = "index";
    private static final String TAG = "QuizActivity";

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_oceans,true),
            new Question(R.string.question_mideast,false),
            new Question(R.string.question_americas,true),
            new Question(R.string.question_asia,true),
            new Question(R.string.question_africa,false)
    };

    private int mCurrentIndex = 0;
    private void updateQuestion(){
        //获取各个string的id,下面的通过方法封装
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        //调用setText可以通过不同id动态获得文本
        mQuestionTextView.setText(question);
    }
    //检查用户回答是否正确，通过将Question类数组的是否正确与用户的输入比较，调用不同的ID给Toast，把这个方法放入监听器中
    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if(userPressedTrue == answerIsTrue){
            messageResId = R.string.correct_toast;
        }else{
            messageResId = R.string.incorrect_toast;
        }
        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //提出问题的textview不用直接写了，通过各种问题的资源id来绑定
        //先指定questiontextview，但question_text_view没有在xml中指定标题文本
        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        //获取各个string的id,下面的通过方法封装
       // int question = mQuestionBank[mCurrentIndex].getTextResId();
        //调用setText可以通过不同id动态获得文本
        //mQuestionTextView.setText(question);

        //三个按钮
        mTrueButton = (Button)findViewById(R.id.true_button);
        mFalseButton = (Button)findViewById(R.id.false_button);
        mNextButton = (Button)findViewById(R.id.next_button);
        //按钮的监听
        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        //这个按钮不断地切换问题，并更新
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        updateQuestion();
    }
    //这里重写了onSaveInstanceState，保存下mCurrentIndex的数值，使得旋转屏幕后造成的activity重启能够读取到之前显示提问的状态
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG,"onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX,mCurrentIndex);
    }
}
