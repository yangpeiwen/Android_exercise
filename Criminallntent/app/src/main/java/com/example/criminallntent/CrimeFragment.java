package com.example.criminallntent;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

//import androidx.fragment.app.Fragment;


//7-1这里我们定义一个Fragment类，用其生命周期来完成各种所需任务，和Activity类似
//这个Fragment的xml中说明是个线性布局，包含一个Edittext
public class CrimeFragment extends Fragment {
    /*

     */
    private static final int REQUEST_DATE = 0;



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

    /* 建立fragment与fragment之间的关联，类似activity中实现数据回传用到startActivityForResult一样
    这里使用的是Fragment的 setTargetFragment(Fragment fragment,int requestCode)方法可以将两个Fragment建立关联。两个参数分别为
    目标fragment和请求代码。CrimeFragment.java中要创建请求代码常量，然后将CrimeFragment设为DatePickerFragment实例的目标fragment
     */
    private static final String DIALOG_DATE = "DialogDate";   //请求代码常量

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
        //mDateButton.setEnabled(false);
        //十二章为这个按钮设置个监听器，用来打开另外一个显示时间和对话框的Fragment，所以这里是用Fragment调用Fragment
        //作为一个Fragment必须要添加到FragmentManager中，所以这里也新建一个FM
        //然后新建所需要用到的DatePickerFragment实例，并调用fragment实例的show方法，就可以将这个fragment显示到屏幕了
        //show方法可以传入fragmentManager或者fragmentTransaction参数。这里传的是FragmentManager参数
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                // DatePickerFragment dialog = new DatePickerFragment(); 弃用标准的Fragment构造方法
                //启用装填好Bundle信息的Fragment构造方法，传入日数据
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());

                //12-8上面提到的通过setTargetFragment可以将两个Fragment进行关联，然后回传数据等等
                //这个方法使得DatePickerFragment将CrimeFragment设置为目标Fragment，并且与之关联
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
                //

                dialog.show(manager,DIALOG_DATE);
            }
        });



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
    /* 12-11CirmeFragment打开DatePickerFragment也就是日历Fragment后修改了时间，并且返回新修改的时间给CirmeFragment
    这里就是通过覆盖onActivityResult方法，从extra中获取日期数据，重新设置Cirme记录的日期，然后刷新日期按钮的显示

    这里的思路可以参考上一个应用写的Activity之间的返回，这两段代码非常相似！！！！！！
     */
    @Override
    public void onActivitiResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            mDateButton.setText(mCrime.getDate().toString());
        }
    }

    //14.7用户可能在CrimeFragment中修改Crime实例，修改完成后，我们需要刷新CrimeLab的Crime数据，这可以通过在CrimeFragment.java中覆盖
    //CrimeFragment.onPause方法完成
    @Override
    public void onPause() {
        super.onPause();
        //onPause后直接调用个updateCrime方法
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }
}
