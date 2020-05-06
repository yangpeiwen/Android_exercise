package com.example.criminallntent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.util.UUID;

//import androidx.fragment.app.Fragment;

/* 9-9这里使用SingleFragmentActivity的抽象代码复用，注释的是老版本，
因为源代码中的Fragmentmanager以及其他操作都是一样的，唯一不同的是不同的Fragment类型
所以通过继承新建Fragment的抽象方法来具体实现


//第七章建立一个Activity托管Fragment，需要继承FragmentActivity
public class CrimeActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        //Fragment在Activity中是用FragmentManager管理的，管理fragment队列和fragment事务回退栈
        //这里使用getSupportFragmentManager获取FragmentManager实例
        FragmentManager fm = getSupportFragmentManager();

        /*在fragmentManager中，资源id是唯一用来标识各个fragment的名称，这里首先
        新建一个fragment并试图向fragmentManager获取，如果存在直接得到（因为设备旋转或回收内存时销毁activity，但Fragment
        会将Fragment队列保留下来，所以重新创建时可以直接获取）。
        如果获取不到，说明之前没有，要新建一个Fm事务并且添加这个fragment，这时fragment为null，直接new一个fragment并且
        调用fragmentManager的方法，创建一个Fragment事务，进行一个添加操作，最后提交

        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if(fragment == null){
            fragment = new CrimeFragment();
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();

        }
    }
}  */

//抽象代码和原来代码唯一区别就是源代码需要完成创建Fragment，而抽象代码用一个抽象方法完成，需要继承后具体实现
//这里实现抽象方法的就是源代码的部分
public class CrimeActivity extends SingleFragmentActivity{
    //这个Override是实现SingleFragmentActivity中抽象方法的，用来创建CrimeFragment
    @Override
    protected Fragment createFragment(){
        /*原版的构造方法
          return new CrimeFragment();
         */
        //新版的构造方法，是CrimeFragment中重写的newinstance方法，通过传入从intent获取的UUId来给Fragment
        UUID crimeId = (UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }
    //下面的部分为新增

    //如同之前例子一样，CrimeActivity被打开后，需要得到CrimeListFragment传递过来的额外信息，说明是哪个列表
    // 这里也用Extra来传递信息，所以直接在这里定义完整的newIntent方法。
    public static final String EXTRA_CRIME_ID = "com.example.criminallntent.crime_id";
    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

}