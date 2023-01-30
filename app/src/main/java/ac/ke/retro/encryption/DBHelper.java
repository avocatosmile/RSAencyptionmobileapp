package ac.ke.retro.encryption;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

class DBHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "EncryptionDB";

    private static final int dbVersion = 1;


    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME,null,1);  // 1 referes to the database version number

    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
//,MKey TEXT, privatek TEXT ,publick TEXT
        db.execSQL("CREATE TABLE users( username TEXT PRIMARY KEY,  password TEXT);");

        db.execSQL("CREATE TABLE files( Fileid INTEGER PRIMARY KEY AUTOINCREMENT ,Filename  TEXT ,username TEXT,  Filepath TEXT  ,encryptedKey TEXT   , privatekey TEXT ,publickey TEXT ,enf TEXT);");
/*
        db.execSQL("CREATE TABLE file(fileid INTEGER PRIMARY KEY AUTOINCREMENT, filename " +
                "TEXT,  file TEXT, id INTERGER ,FOREIGN KEY(id) REFERENCES details(id));");

        db.execSQL("CREATE TABLE Keys(fileid INTEGER PRIMARY KEY AUTOINCREMENT, encrpytedKey" +
                "TEXT,  encyptedfile TEXT, publickey TEXT , privatekey TEXT,FOREIGN KEY(fileid) REFERENCES details(fileid));");
*/
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

        db.execSQL("drop Table if exists users");
        db.execSQL("drop Table if exists files");
        /*
        Log.w(this.getClass().getName(), DATABASE_NAME + "database upgrade to version "
                +newVersion +" old data lost");
        db.execSQL("DROP TABLE IF EXISTS details");
        onCreate(db);
*/
    }
    public boolean insertfile(  String Filename , String username, String Filepath,String  encrypedKey  ,String  privatekey , String publickey ,String enf   ){
        SQLiteDatabase DB =getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("Filename",Filename);
        contentValues.put("username",username);
        contentValues.put("Filepath",Filepath);
        contentValues.put("encryptedKey", encrypedKey  );
        contentValues.put("privatekey",privatekey);
        contentValues.put("publickey",publickey);
        contentValues.put("enf",enf);


        long result = DB.insert("files",null , contentValues);
        System.out.println(result);
        if(result==-1){
            return false;
        }
        else{
            return true;
        }
    }


    public boolean insertData(  String username , String password  ){
        SQLiteDatabase DB =getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("username",username);
        contentValues.put("password",password);


        long result = DB.insert("users",null , contentValues);
        if(result==-1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean UpdateData(  String oldu,String username , String password  ){
        SQLiteDatabase DB =getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("username",username);
        contentValues.put("password",password);
        Cursor cursor = DB.rawQuery("select * from users where username= ?",new String[]{oldu});
        if(cursor.getCount()>0) {
            long result = DB.update("users", contentValues, "username =?",new String[]{oldu});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }
        else{
            return  false ;
        }


    }

    public boolean deleteuser(  String username  ){
        SQLiteDatabase DB =this.getWritableDatabase();


        Cursor cursor = DB.rawQuery("select * from users where username= ?",new String[]{username});

        if(cursor.getCount()>0) {
            long result = DB.delete("users", "username=?", new String[]{username});

            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }
        else {
            System.out.println("Here");
            return false;
        }
    }


    public Cursor revealData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+"users",null);
        return res;
    }
    public Cursor revealfileData(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from files where username = ?",new String[] {name});
        return res;
    }
    public boolean checkUsername(  String username ){
        SQLiteDatabase DB =this.getWritableDatabase();
        Cursor cursor =DB.rawQuery("Select * from users where username = ?",new String[] {username});
        if(cursor.getCount()>0){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean checkUsernamepassword(String username , String password ){
        SQLiteDatabase DB =this.getWritableDatabase();
        Cursor cursor =DB.rawQuery("Select * from users where username = ? and  password = ?",new String[] {username,password});
        if(cursor.getCount()>0){
            return true;
        }
        else{
            return false;
        }



    }
    public Cursor revealfile(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from files where Fileid = ?",new String[] {id});
        return res;
    }
    /*









    /*
    public long insertDetails(String firstname, String Username, String password)
    {

        ContentValues rowValues =new ContentValues();

        rowValues.put("Username",Username);
        rowValues.put("name", firstname);

        rowValues.put("password",password);

        return database.insertOrThrow("details",null,rowValues);

    public long insertfile(String filename, String file ,int id)
    {

        ContentValues rowValues =new ContentValues();

        rowValues.put("filename",filename);
        rowValues.put("file", file);
        rowValues.put("id", id);

        return database.insertOrThrow("file",null,rowValues);
    }




    public long getNumberOfRecords()
    {
        Cursor c=database.query("details",null,null,null,null,null,null);
        return c.getCount();
    }
    /*
    public <UserModal> ArrayList<UserModal> readUser() {

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursorUsers = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

            ArrayList<UserModal> userModalArrayList = new ArrayList<>();

            if (cursorUsers.moveToFirst()) {
                do {
                // on below line we are adding the data from cursor to our array list.
                userModalArrayList.add(new <UserModal>(cursorUsers.getString(1),
                        cursorUsers.getString(4),
                        cursorUsers.getString(2),
                        cursorUsers.getString(3)));
                } while (cursorUsers.moveToNext());
                // moving our cursor to next.
            }

             cursorUsers.close();
            return  userModalArrayList;
                }

*/

}