package com.example.criminallntent;

/*
CursorWrapper是一个表数据处理工具，可以将数据表的原始字段值封装成所需要的类型例如
String uuidString = cursor.getString(cursor.getColumnIndex(CrimeTable.Cols.UUID));
String title = cursor.getString(cursor.getColumnIndex(CrimeTable.Cols.TITLE));
long date = cursor.getLong(cursor.getColumnIndex(CrimeTable.Cols.DATA));
int isSolved = cursor.getInt(cursor.getColumnIndex(CrimeTable.Cols.SOLVED));
每取出一条记录都要执行上面的步骤去除数据的4个部分，所以本类就是用来封装这些重复操作。
 */

public class CrimeCursorWrapper {
}
