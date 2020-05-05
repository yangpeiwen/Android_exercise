package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//第五章：增加一个activity，用来显示问题的答案
public class CheatActivity extends AppCompatActivity {
    //5-10创建静态的字符串，这是我们需要通过EXTRA传递过去的key，为了方便识别来自各个activity的，我们一般前面加上包名
    private  static final String EXTRA_ANSWER_IS_TRUE = "com.example.quiz.answer_is_true";
    //
    //5-13创建静态的字符串，这是我们需要通过EXTRA回传过去的key，为了方便识别来自各个activity的，我们一般前面加上包名
    private static final String EXTRA_ANSWER_SHOW = "com.example.quiz.answer_show";
    //
    //5-11CheatActivity接受后还需要解析出所传输的数值，先定义一个boolean接收
    private boolean mAnswerIsTrue;
    //
    //5-12 增加个按钮和textview在第二个Activity用来显示答案
    private TextView mAnswerTextView;
    private Button mShowAnswer;
    //
    /*5-10封装了本应该在MainActivity中完成的“将额外信息放入intent传递给CheatActivity的方法”
    Intent i = new Intent(packageContext,CheatActivity.class);是原本的语句
    增加了一个putExtra，因为Extra是个键值对，所以第一个参数就是key名：com.example.quiz.answer_is_true，第二个参数才是我们对应的要传过去的数值，也就是answerIsTrue
     */
    public static Intent newIntent(Context packageContext,boolean answerIsTrue){
        Intent i = new Intent(packageContext,CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue);
        return i;
    }
    /*5-13封装了将返回信息通过Extra传递给MainActivity的方法，与上面的类似，只不过Intent写法不同，最后的
    setResult是为将结果代码和intent打包，是一个常用的子Activity返回方法，能够返回数据和一个结果代码例如RESULT_OK，也可以是
    其他的代码，主要是提供给主Activity判断用的。
     */
    private void setAnswerShownResult(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOW,isAnswerShown);
        setResult(RESULT_OK,data);
    }
    //end
    //5-13
    public static boolean wasAnswerShow(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOW,false);
    }
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        /*5-11要从Intent的extra获取数据需要用到这个方法，getBooleanExtra方法，需要指定extra的名字（也就是键值对的键key），第二个参数指定默认值
         getIntent是Activity的getIntent方法，用来返回用startActivity方法转发的Intent对象
         */
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);

        //5-12，前面搞定了接受信息部分，现在开始进行常规的按键展示信息
        mAnswerTextView = (TextView)findViewById(R.id.answer_text_view);
        mShowAnswer = (Button)findViewById(R.id.show_answer_button);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                //5-13调用之前写好的方法,将信息回传
                setAnswerShownResult(true);
            }
        });
        //end
    }

}
