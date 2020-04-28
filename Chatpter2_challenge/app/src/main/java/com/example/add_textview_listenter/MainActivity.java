package com.example.add_textview_listenter;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    /*  普通Button，现在是挑战项目3的状态，这里先注释掉
    private Button mNextButton;
    private Button mPreButton;
    */
    // 挑战练习3：ImageButton继承自ImageView，类似于Button继承自TextView，这两者都继承自View，基本用法差不多
    private ImageButton mNextImageButton;
    private ImageButton mPreImageButton;

    private TextView mQuestionTextView;

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
        mQuestionTextView = findViewById(R.id.question_text_view);
        //获取各个string的id,下面的通过方法封装
        // int question = mQuestionBank[mCurrentIndex].getTextResId();
        //调用setText可以通过不同id动态获得文本
        //mQuestionTextView.setText(question);

        //三个按钮
        mTrueButton = findViewById(R.id.true_button);
        mFalseButton = findViewById(R.id.false_button);
        /*
        mNextButton = (Button)findViewById(R.id.next_button);
        mPreButton = (Button)findViewById(R.id.pre_button);
        */
        mNextImageButton = findViewById(R.id.next_button);
        mPreImageButton = findViewById(R.id.pre_button);

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
        mNextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mPreImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + mQuestionBank.length - 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        /*挑战练习2，增加一个向前的按键pre，只要增加一个数组长度再-1即可，直接-1会导致数组越位，报错
        mPreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + mQuestionBank.length - 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        */
        /*这个按钮不断地切换问题，并更新
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        */
        updateQuestion();

        //挑战练习1：为TextView也增加个监听器，跳转到下一题，因为TextView也是View的子类，所以和Button一样可以直接
        //设置View.OnClick-Listener监听器
        mQuestionTextView.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
            updateQuestion();
        }
        });
    }
}
