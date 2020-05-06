package com.example.criminallntent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//用来装载CrimeFragment列表项的一个数据池类，crime数组对象存储在一个单例里，单例是个特殊java类
//创建实例时，单例只能创建一个实例，应用在内存里面存在多久，单例就能存在多久，所以无论activity和fragment
//生命周期如何变化，都可以通过单例来提取
public class CrimeLab {
    //安卓惯例中，变量前面带s表示为静态变量
    private static CrimeLab sCrimeLab;
    //创建空的Crime数组用来保存Cirme对象
    //注意List和ArrayList都是数组类，ArrayList继承自List，我们一般声明变量用List，实现和使用时用用一种具体
    //子类，比如这里的ArrayList
    private List<Crime> mCrimes;

    public static CrimeLab get(Context context){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }
    //<>符号表示List中的元素类型可以基于变量声明传入的抽象参数来确定
    //这里先新建100个crime
    private CrimeLab(Context context){
        mCrimes = new ArrayList<>();
        for(int i = 0;i<100;i++){
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0);
            mCrimes.add(crime);
        }
    }
    public List<Crime> getCrimes(){
        return mCrimes;
    }
    public Crime getCrime(UUID id){
        for(Crime crime : mCrimes){
            if(crime.getId().equals(id)){
                return crime;
            }
        }
        return null;
    }
}
