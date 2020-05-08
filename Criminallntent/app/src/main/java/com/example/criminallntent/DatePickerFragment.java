package com.example.criminallntent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {
/*
DatePickerFragment这个Fragment由CrimePagerActivity托管，负责显示对话框，对话框使用AlertDialog类
所以需要1.创建AlertDialog
2.借助FragmentManager再屏幕上显示对话框
 */

    //DatePickerFragment继承自DialogFragment，记住Fragment和activity类似。在普通的Fragment中，有个类似onCreate的onCreateView就是
    //FragmentManager调用这个Fragment的创建方法。而这里的DialogFragment也有类似的创建方法叫onCreateDialog，也是托管的activity的FM必定会调用
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //日历对象DatePicker需要Date初始化，但不能直接用，要先创建个Calendar对象，用Date配置，再从Calendar中取回所用信息
        Date date = (Date) getArguments().getSerializable(ARG_DATE);  //惯例通过键获取Bundle传过来的数值

        Calendar calendar = Calendar.getInstance();   //新建Claendar
        //下面是Calendar的使用方法
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int mounth = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        //使用inflater的from的方式新建一个view，然后调用inflate方法将xml解析为view对象（视图）
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date,null);

        //这里新建用来显mDatePicker，惯例绑定xml并且使用calender提供的数据初始化
        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);
        mDatePicker.init(year,mounth,day,null);

        //创建一个带标题，OK按钮和日历的AlertDialog
        //使用的是AlertDialog.Builder类，先将Context参数传入AlertDialog.Builder类的构造方法，返回一个AlertDialog.Builder实例，然后调用三个
        //AlertDialog.Builder的方法来配置对话框，也就是setView,setTitle和setPositiveButton，注意OK按钮需要两个参数一个是字符串资源另外一个是监听器对象，用来监听按钮
        //最后调用.create完成对话框的创建
        return new AlertDialog.Builder(getActivity()).setView(v).setTitle(R.string.date_picker_title).
                setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
                    /*注意这里在setPositiveButton的时候用了个匿名内部函数，因为用户点对话框的positive按钮时，需要从DatePicker中获取日期并且回传给CrimeFragment
                    在onCreateDialog的方法中，第二个参数实现DialogInterface.OnClickListener监听器接口，在onClick方法中获取日期使用下面写好的的回调sendResult方法，将修改后的数据回传。
                     */
                    @Override
                    public void onClick(DialogInterface dialog,int which){
                        int year = mDatePicker.getYear();
                        int mounth = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        Date date = new GregorianCalendar(year,mounth,day).getTime();
                        sendResult(Activity.RESULT_OK,date);
                    }
                })
                .create();
    }

    /*
    装填好Bundle的Fragment构造方法
    crimeFragment详情中点击打开对话框DatePickerFragment，需要传递日期信息，也就是Fragment与Fragment之间传递信息
    这里通过Bundle来传递，先在DatePickerFragment中封装好newInstance，需要的时候用作构造方法
     */
    private static final String ARG_DATE = "date";  //设置键
    private DatePicker mDatePicker;  //设置值
    public static DatePickerFragment newInstance(Date date){
        //键值装填Bundle
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE,date);
        //作为Fragment构造方法，这里返回装填好Bundle的Fragment
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /*
    DatePickerFragment管理着一个日历，用户修改之后要将信息回传给CrimeFragment，之前在CrimeFragment按钮监听器中已经将两个Fragment相关联了。
    如何实现DatePickerFragment回传给CrimeFragment呢？直接调用CrimeFragment实例的onActivityResult方法就能回传了
     */
    public static final String EXTRA_DATE = "com.example.criminallntent";    //EXTRA所需要的“键”
    private void  sendResult(int resultCode,Date date){
        if(getTargetFragment() == null){
            return;
        }
        //创建新intent，并添加EXTRA信息
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE,date);
        //getTargetFragment获取目标Fragment也就是CrimeFragment，然后调用前面说的onActivityResult方法，
        //这个方法需要三个参数：请求代码（和CrimeFragment监听器中的setTargetFragment方法刚好匹配，告诉目标Fragment返回结果来自的地方），结果代码，intent（包含EXTRA数据）
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
