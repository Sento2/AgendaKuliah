package com.kel4.agendakuliah.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.kel4.agendakuliah.model.Task;
import com.kel4.agendakuliah.model.User;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "agenda_kuliah.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "id";
    public static final String COL_USER_NAME = "username";
    public static final String COL_USER_PASS = "password";
    public static final String COL_USER_EMAIL = "email";

    public static final String TABLE_TASKS = "tasks";
    public static final String COL_TASK_ID = "id";
    public static final String COL_TASK_TITLE = "title";
    public static final String COL_TASK_COURSE = "course";
    public static final String COL_TASK_DEADLINE = "deadline";
    public static final String COL_TASK_PRIORITY = "priority";
    public static final String COL_TASK_DONE = "is_done";
    public static final String COL_TASK_USER_ID = "user_id";
    public static final String COL_TASK_DESC = "description";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_NAME + " TEXT NOT NULL UNIQUE, " +
                COL_USER_PASS + " TEXT NOT NULL, " +
                COL_USER_EMAIL + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_TASKS + " (" +
                COL_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TASK_TITLE + " TEXT NOT NULL, " +
                COL_TASK_COURSE + " TEXT, " +
                COL_TASK_DEADLINE + " TEXT, " +
                COL_TASK_PRIORITY + " INTEGER DEFAULT 1, " +
                COL_TASK_DONE + " INTEGER DEFAULT 0, " +
                COL_TASK_DESC + " TEXT, " +
                COL_TASK_USER_ID + " INTEGER, " +
                "FOREIGN KEY(" + COL_TASK_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean registerUser(String username, String password, String email) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USER_NAME, username);
        cv.put(COL_USER_PASS, password);
        cv.put(COL_USER_EMAIL, email);
        long result = db.insert(TABLE_USERS, null, cv);
        db.close();
        return result != -1;
    }

    public User checkLogin(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_USER_ID, COL_USER_NAME, COL_USER_EMAIL},
                COL_USER_NAME + "=? AND " + COL_USER_PASS + "=?",
                new String[]{username, password}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
            cursor.close();
            db.close();
            return user;
        }
        if (cursor != null) cursor.close();
        db.close();
        return null;
    }

    public long addTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TASK_TITLE, task.getTitle());
        cv.put(COL_TASK_COURSE, task.getCourse());
        cv.put(COL_TASK_DEADLINE, task.getDeadline());
        cv.put(COL_TASK_PRIORITY, task.getPriority());
        cv.put(COL_TASK_DESC, task.getDescription());
        cv.put(COL_TASK_DONE, 0);
        cv.put(COL_TASK_USER_ID, task.getUserId());
        long id = db.insert(TABLE_TASKS, null, cv);
        db.close();
        return id;
    }

    public List<Task> getActiveTasks(int userId) { return getTasksByStatus(userId, 0); }
    public List<Task> getDoneTasks(int userId) { return getTasksByStatus(userId, 1); }

    private List<Task> getTasksByStatus(int userId, int isDone) {
        List<Task> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, null,
                COL_TASK_USER_ID + "=? AND " + COL_TASK_DONE + "=?",
                new String[]{String.valueOf(userId), String.valueOf(isDone)},
                null, null, COL_TASK_PRIORITY + " DESC");
        if (cursor.moveToFirst()) {
            do {
                Task t = new Task();
                t.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_TASK_ID)));
                t.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COL_TASK_TITLE)));
                t.setCourse(cursor.getString(cursor.getColumnIndexOrThrow(COL_TASK_COURSE)));
                t.setDeadline(cursor.getString(cursor.getColumnIndexOrThrow(COL_TASK_DEADLINE)));
                t.setPriority(cursor.getInt(cursor.getColumnIndexOrThrow(COL_TASK_PRIORITY)));
                t.setDone(cursor.getInt(cursor.getColumnIndexOrThrow(COL_TASK_DONE)) == 1);
                t.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_TASK_DESC)));
                t.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_TASK_USER_ID)));
                list.add(t);
            } while (cursor.moveToNext());
        }
        cursor.close(); db.close();
        return list;
    }

    public int updateTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TASK_TITLE, task.getTitle());
        cv.put(COL_TASK_COURSE, task.getCourse());
        cv.put(COL_TASK_DEADLINE, task.getDeadline());
        cv.put(COL_TASK_PRIORITY, task.getPriority());
        cv.put(COL_TASK_DESC, task.getDescription());
        cv.put(COL_TASK_DONE, task.isDone() ? 1 : 0);
        int rows = db.update(TABLE_TASKS, cv, COL_TASK_ID + "=?", new String[]{String.valueOf(task.getId())});
        db.close();
        return rows;
    }

    public boolean deleteTask(int taskId) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABLE_TASKS, COL_TASK_ID + "=?", new String[]{String.valueOf(taskId)});
        db.close();
        return rows > 0;
    }
}