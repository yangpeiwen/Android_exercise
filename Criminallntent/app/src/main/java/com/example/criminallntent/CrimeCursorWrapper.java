package com.example.criminallntent;

/*
CursorWrapper是一个表数据处理工具，可以将数据表的原始字段值封装成所需要的类型例如
String uuidString = cursor.getString(cursor.getColumnIndex(CrimeTable.Cols.UUID));
String title = cursor.getString(cursor.getColumnIndex(CrimeTable.Cols.TITLE));
long date = cursor.getLong(cursor.getColumnIndex(CrimeTable.Cols.DATA));
int isSolved = cursor.getInt(cursor.getColumnIndex(CrimeTable.Cols.SOLVED));
每取出一条记录都要执行上面的步骤去除数据的4个部分，所以本类就是用来封装这些重复操作。
 */

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import database.CrimeDbSchema.CrimeDbSchema;

//创建了一个Cursor封装类，该类继承了Cursor类全部方法，这样封装就是为了定制新方法，以便操作内部Cursor
public class CrimeCursorWrapper extends CursorWrapper {

    public CrimeCursorWrapper(Cursor cursor){
        super(cursor);
    }

    //新增getCrime方法
    public Crime getCirme() {
        String uuidString = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SOLVED));

        Crime crime = new Crime(UUID.fromString(uuidString));
              crime.setTitle(title);
              crime.setDate(new Date(date));
              crime.setSolved(isSolved != 0);

              return crime;
    }

}
