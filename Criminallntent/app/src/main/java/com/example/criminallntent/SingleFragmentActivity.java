package com.example.criminallntent;

import android.os.Bundle;

//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentActivity;
//import androidx.fragment.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


//第二个托管Fragment的Activity，我们要实现一个页表activity，每个表象装的是Crime，但由Fragment构成
//所以实际上主要区别是Fragment，托管的activity代码差不多，所以这里建立一个通用抽象超类
public abstract class SingleFragmentActivity extends FragmentActivity {
    //和之前代码唯一不同在于，这里新增了一个抽象类，createFragment，具体的FragmentActivity只需要继承并实现这个，其他代码
    //都可以复用
    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if(fragment == null){
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }
    }
}
