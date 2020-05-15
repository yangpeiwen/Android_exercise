package com.example.criminallntent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import database.CrimeDbSchema.CrimeDbSchema;
//这样就可以直接以CrimeTable.Cols.UUID的形式使用CrimeDbSchema.CrimeTable中的String常量
import database.CrimeDbSchema.CrimeDbSchema.CrimeTable;

//  xxxBaseHelper类一般就是xxx数据库的创建初始化方法
/*
一般数据库初始化遵循以下流程：
1.确认目标数据库是否存在
2.如果不存在，首先创建数据库，然后创建数据库表以及必须的初始化数据
3.如果存在，打开并确认CrimeDbSchema是否是最新版本
4.如果是旧版本就，运行升级代码

android提供的SQLiteOpenHelper类一般可以完成这些，所以直接创建CrimeBaseHelper继承SQLiteOpenHelper
 */
public class CrimeBaseHelper extends SQLiteOpenHelper {
    //定义相关参数
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";

    //构造方法
    public CrimeBaseHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }
    //onCreate负责首次创建数据库
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + CrimeTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                CrimeTable.Cols.UUID + ", " +
                CrimeTable.Cols.TITLE + ", " +
                CrimeTable.Cols.DATE + ", " +
                CrimeTable.Cols.SOLVED +
                ")"
        );

    }
    //onUpgrade负责后续升级数据库
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }

}
