package com.example.criminallntent;

//import androidx.fragment.app.Fragment;

import android.support.v4.app.Fragment;
//9-10第二个托管Fragment的Activity，我们要实现一个页表activity，每个表象装的是Crime，但由Fragment构成
//实现方法和第一个托管Fragment的activity一样，唯一区别只是Fragment不同，所以这里继承之前写好的抽象类SingleFragmentActivity
//并且实现抽象方法createFragment()
public class CrimeListActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment(){
        return new CrimeListFragment();
    }
}
