package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // 第五章，新增一个activity用来显示答案
    private Button mCheatButton;
    //
    //5-13的请求代码，用来区分是哪个子Activity返回的消息
    private static  final int REQUEST_CODE_CHEAT = 0;
    //
    //存储来自CheatActivity的信息，判断用户受否作弊
    private  boolean mIsCheater;
    //
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
    //5-13，增加一个作弊检验，如果之前看过答案，这里显示作弊
    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;

        if (mIsCheater == true) {
            messageResId = R.string.judement_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        }
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

        //四个按钮
        mTrueButton = (Button)findViewById(R.id.true_button);
        mFalseButton = (Button)findViewById(R.id.false_button);
        mNextButton = (Button)findViewById(R.id.next_button);
        mCheatButton = (Button)findViewById(R.id.cheat_button);
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

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*普通的启动方式
                start CheatActivity,这里通过intent启动另外一个CheatActivity，通过调用startActivity呼叫
                操作系统的ActivityManager，然后ActivityManager负责创建Activity实例并调用其onCreate方法。
                而intent类可以告诉Activitymanager启动该哪个activity以及如何找到它，这是两个参数，分别是Class参数告诉启动
                哪个activity，Context参宿和告诉在哪里可以找到这个activity。
                Intent i = new Intent(MainActivity.this,CheatActivity.class);
                //上面的mainactivity就是Context参数，说明在这里找到，cheatactivity.class是class参数说明是启动哪个activity
                 */

                /*带有extra信息的启动方式，因为我们要在启动CheatActivity同时给CheatActivity发送一些信息，这里直接在CheatActivity封装成一个方法，在创建intent实例时就完成
                也就是下面的CheatActivity.newIntent方法。answerIsTrue是需要通过extra传递的信息，
                因为第二个Activity是展示当前问题答案的，所以要传递的是一个boolean类型*/
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i = CheatActivity.newIntent(MainActivity.this,answerIsTrue);
                //startActivity(i);暂时使用，用下面的startActivityForResult替换

                //5-13同理，这里也需要接收来自CheatActivity的信息，所以不再使用之前的startActivity
                //转而使用能够接收返回信息的startActivityForResult(i,REQUEST_CODE_CHEAT)
                //而具体的如何处理则重写回调方法onActivityResult来完成
                startActivityForResult(i,REQUEST_CODE_CHEAT);
            }
        });

        //这个按钮不断地切换问题，并更新,并且每个问题默认状态下是没有作弊的，设定为false
        //只有看了答案会被修改为true
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false
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
    //5-13，一个接收子Activity返回后数据的回调方法，用来判断子Activity调用的setresult方法中结果代码是哪些，来采取不同的行动
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if(data == null){
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShow(data);
        }
    }

}
