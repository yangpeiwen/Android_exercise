package com.example.criminallntent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import database.CrimeDbSchema.CrimeDbSchema;

//用来装载CrimeFragment列表项的一个数据池类，crime数组对象存储在一个单例里，单例是个特殊java类
//创建实例时，单例只能创建一个实例，应用在内存里面存在多久，单例就能存在多久，所以无论activity和fragment
//生命周期如何变化，都可以通过单例来提取
public class CrimeLab {
    //安卓惯例中，变量前面带s表示为静态变量
    private static CrimeLab sCrimeLab;
    //创建空的Crime数组用来保存Cirme对象
    //注意List和ArrayList都是数组类，ArrayList继承自List，我们一般声明变量用List，实现和使用时用用一种具体
    //子类，比如这里的ArrayList

    /*
    14.7不再使用mCrimes数组来存取数据，改用数据库，所需下面涉及到mCrimes的变量全部注释
     */

   // private List<Crime> mCrimes;

    public static CrimeLab get(Context context){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }
    /*14.7改用数据库后原来这里的用mCrime数组的构造方法就不用了

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

     */
    public List<Crime> getCrimes(){
//        return mCrimes;
        return new ArrayList<>();
    }
    public Crime getCrime(UUID id){
   //     for(Crime crime : mCrimes){
   //         if(crime.getId().equals(id)){
   //             return crime;
    //        }
  //      }
        return null;
    }

    //代码14.4   CrimeLab改用SQlite数据库来完成
    //定义一个Context和SQLiteDatebase数据库
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private CrimeLab(Context context){
        //16章会用到Context
        mContext = context.getApplicationContext();

        /*调用CrimeBaseHepler的创建实例后，调用getWritableDatabase方法
        CrimeBaseHelper会做以下的事情：
        1.打开默认地址/data/data/com.example.criminallntent/databases/crimeBase.db，如果不存在就先创建crimeBase.db数据库文件
        2.如果是首次创建数据库，调用onCreate方法，保存最新版本号
        3.如果已经创建过数据库就检查版本号，如果CrimeOpenHelper中的版本号高就调用onUpgrade方法升级
         */
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
   //     mCrimes = new ArrayList<>();

    }
    //ContentValues类是负责数据库写入和更新操作。是一个键值存储类，类似之前的BUndle，
    //将Crime记录转换为ContentValues实际就是在CrimeLab中创建ContentValues实例
    private static ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        values.put(CrimeDbSchema.CrimeTable.Cols.UUID,crime.getId().toString());
        values.put(CrimeDbSchema.CrimeTable.Cols.TITLE,crime.getTitle());
        values.put(CrimeDbSchema.CrimeTable.Cols.DATE,crime.getDate().getTime());
        values.put(CrimeDbSchema.CrimeTable.Cols.SOLVED,crime.isSolved()?1:0);

        return values;
    }
    //数据插入
    public void addCrime(Crime c){
        //将Crime数据转换为ContentValues类型
        ContentValues values = getContentValues(c);
        //调用ContentValues的insert方法插入数据，
        //insert(String,String,ContentValues)有三个参数,第一个数据库表名。最后一个是要写入的数据。第二个参数现在暂时不需要了解
        mDatabase.insert(CrimeDbSchema.CrimeTable.NAME,null,values);
    }
    //数据更新方法，这里要后面多看看！！！！！！！！！！！！！
    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeDbSchema.CrimeTable.NAME,values, CrimeDbSchema.CrimeTable.Cols.UUID + " = ?",new String[]{ uuidString });
    }
    //读取数据库要用到query的方法，
    //参数table是要查询的数据表，参数columns指定要依次获取那些字段的值，参数where和whereArgs的作用和update方法的一样
    private Cursor queryCrimes(String whereClause ,String[] whereArgs){
        Cursor cursor = mDatabase.query(
              CrimeDbSchema.CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null

        );
        return cursor;
    }

}
