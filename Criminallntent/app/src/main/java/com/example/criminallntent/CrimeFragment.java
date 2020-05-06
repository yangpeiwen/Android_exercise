package com.example.criminallntent;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;

//import androidx.fragment.app.Fragment;


//7-1这里我们定义一个Fragment类，用其生命周期来完成各种所需任务，和Activity类似
//这个Fragment的xml中说明是个线性布局，包含一个Edittext
public class CrimeFragment extends Fragment {
    private Crime mCrime;
    //就如图在activity操作一样，这里的Fragment需要一个文本输入框，使用Edittext,并且在
    //Fragment的xml中配置这个文本输入框
    private EditText mTitleField;
    //新增的时间按钮
    // 和勾选框
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    /*托管CrimeFragment的activity的intent得到信息后，需要取出。这里是通过bundle来取的
      Bundle相当于Fragment的一个私有空间，每个fragment实例都带一个Bundle对象，和Extra类似，也是键值对。
      创建Bundle需要在fragment创建后，添加给activity前完成。所以一般是通过新建newInstance的静态方法给
      Fragment类，实现bundle。
      下面创建的就是bundle传输的 键值对 的键
     */
    private static final String ARG_CRIME_ID = "crime_id";
    //newInstance方法在做到构造Fragment的同时还要提取activity传递的信息
    public static CrimeFragment newInstance(UUID crimeId){
        //获取到activity的intent附带的值（crimeId）后，新建Bundle并调用put方法提取
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,crimeId);
        //新建一个fragment调用setArguments方法后获取Bundle的内容，并返回Fragment
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //onCreate首先要创建Fragment实例，但注意导入布局要在onCreateView覆盖方法
        UUID crimeId = (UUID)getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }
    //注意Fragment的onCreateView基本等同于我们以前在Activity中用的onCreate，不光用来创建视图
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedinstanceState){
        //覆盖onCreateView方法，生成了视图V
        View v = inflater.inflate(R.layout.fragment_crime,container,false);

        //这里要生成一个输入文本的文本框监听器，先创建实例
        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //this space intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s){
                // this one too
            }
        });
        //常规按键的设置，点击后开启时间相关，这里暂时禁用，12章开启
        mDateButton = (Button)v.findViewById(R.id.crime_date);
        mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setEnabled(false);

        /*勾选框：这里设置勾选框，并且设置勾选框监听器，当勾选框改变时就会自动触发下面覆盖的方法
        基本写法和按键是一样的
         */
        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        return v;
    }
}
