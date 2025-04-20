package com.paprika.userdataapp.repository;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.paprika.userdataapp.dataaccess.ManagerDataBase;
import com.paprika.userdataapp.entities.User;

import java.util.ArrayList;

public class UserRepository {
    private ManagerDataBase dataBase;
    private Context context;
    private View view;
    private User user;

    public UserRepository(Context context, View view) {
        this.context = context;
        this.view = view;
        this.dataBase = new ManagerDataBase(context);
    }

    public void insertUser(User user) {
        try {
            SQLiteDatabase dataBaseSql = this.dataBase.getWritableDatabase();

            if (dataBaseSql != null) {
                ContentValues values = new ContentValues();
                values.put("use_document", user.getDocument());
                values.put("use_name", user.getName());
                values.put("use_lastname", user.getLastName());
                values.put("use_user", user.getUser());
                values.put("use_pass", user.getPass());
                values.put("use_status", "1");

                long response= dataBaseSql.insert("users", null, values);
                String message = (response>=1) ? "se registró correctamente" : "no se registro correctamente";
                Snackbar.make(this.view, message, Snackbar.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.i("ErrorWilson", "insertUser"+e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteUser(int document) {
        try (SQLiteDatabase db = dataBase.getWritableDatabase()) {
            int response = db.delete("users", "use_document = ?", new String[]{String.valueOf(document)});
            String message = (response >= 1) ? "Eliminado correctamente" : "No se eliminó correctamente";
            Snackbar.make(this.view, message, Snackbar.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("UserRepository", "deleteUser: " + e.getMessage());
        }
    }

    public void updateUser(User user) {
        try (SQLiteDatabase db = dataBase.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put("use_name", user.getName());
            values.put("use_lastname", user.getLastName());
            values.put("use_user", user.getUser());
            values.put("use_pass", user.getPass());

            int response = db.update("users", values, "use_document = ?", new String[]{String.valueOf(user.getDocument())});
            String message = (response >= 1) ? "Actualizado correctamente" : "No se actualizó correctamente";
            Snackbar.make(this.view, message, Snackbar.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("UserRepository", "updateUser: " + e.getMessage());
        }
    }


    public User getUserByDocument(int document) {
        SQLiteDatabase db = dataBase.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE use_document = ?", new String[]{String.valueOf(document)});

        User user = null;
        if (cursor.moveToFirst()) {
            user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            user.setStatus(cursor.getString(5));
        }
        cursor.close();
        return user;
    }

    public ArrayList<User> getUsersByLastName(String lastName) {
        SQLiteDatabase db = dataBase.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE use_lastname LIKE ?", new String[]{lastName + "%"});

        ArrayList<User> users = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                User user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                user.setStatus(cursor.getString(5));
                users.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return users;
    }



    public ArrayList<User> getUserList() {
        SQLiteDatabase dataBaseSql = this.dataBase.getReadableDatabase();
        String query = "SELECT * FROM users WHERE use_status = 1;";
        ArrayList<User> users = new ArrayList<>();

        Cursor cursor = dataBaseSql.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();

                user.setDocument(cursor.getInt(0));
                user.setName(cursor.getString(1));
                user.setLastName(cursor.getString(2));
                user.setUser(cursor.getString(3));
                user.setPass(cursor.getString(4));
                user.setStatus(cursor.getString(5));

                users.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        dataBaseSql.close();

        return users;
    }




}
