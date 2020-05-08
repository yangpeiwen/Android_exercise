package com.example.criminallntent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;
import java.util.UUID;

//新的托管CrimeFragment的activity，新布局由ViewPager实例组成。
// 新的托管界面能够通过滑动屏幕查看Crime的明细，也就是能够通过滑动切换不同的CrimeFragment界面
public class CrimePagerActivity extends FragmentActivity {
    private  static final String EXTRA_CRIME_ID = "com.example.criminallntent.crime_id";

    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    //惯例新增newintent方法,这样list那边就可以调用这里的newintent方法启动了，并且增加一些EXTRA信息
    public static Intent newIntent(Context packageContext, UUID crimeId){
        Intent intent = new Intent(packageContext,CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        //新建ViewPager 找到其对应的xml
        mViewPager = (ViewPager)findViewById(R.id.activity_crime_pager_view_pager);

        //通过CrimeLab获取数据集
        mCrimes = CrimeLab.get(this).getCrimes();
        //需要新建一个fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        //需要使用到adapter子类FragmentStatePagerAdapter类的两个有用的方法，getCount和gitItem
        //所以这里用匿名内部类的方式来新建FragmentStatePagerAdapter实例，并且设置给adapter
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return 0;
            }
        });
        //创建UUID
        UUID crimeId = (UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);
    }
}
