package com.example.sdahymnal;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HymnActivity extends AppCompatActivity {

    Cursor cursor;
    DbHelper dbHelper;
    StringBuilder stringBuilder = new StringBuilder();
    String favorite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hymn);

        Intent intent = getIntent();
        Context context = getApplicationContext();
        String hymnNumber = intent.getStringExtra("number");
        dbHelper = new DbHelper(context);
        cursor = readDatabase(hymnNumber);
        LinearLayout linearLayout = findViewById(R.id.root);
        LinearLayout container = findViewById(R.id.progress_container);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setIndeterminate(true);

        Handler handler = new Handler(Looper.getMainLooper());

        container.addView(progressBar);

        new Thread(() -> {

            try {

                writeDatabase(getResources().openRawResource(R.raw.hymnal));

            }catch (Exception e){
                e.printStackTrace();
            }

            handler.post(() -> {
                linearLayout.removeView(container);
                HymnAdapter hymnAdapter = new HymnAdapter(cursor);
                recyclerView.setAdapter(hymnAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));


            });

        }).start();


        cursor.moveToFirst();
        favorite = cursor.getString(0);
        while (cursor.moveToNext()){
            stringBuilder.append("\n").append(cursor.getString(1));
        }




    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String title = item.getTitle().toString();
//        String text = cursor.getString(1);
        switch (title){
            case "Copy": ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("text", stringBuilder.toString());
                clipboard.setPrimaryClip(clipData); break;
            case "Like":
                File file = new File(getFilesDir(), "favourites.txt");
                try {
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.append("\n").append(cursor.getString(0));
                    fileWriter.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "Share":
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, cursor.getString(0));
                shareIntent.putExtra(Intent.EXTRA_TEXT,stringBuilder.toString());
                startActivity(Intent.createChooser(shareIntent, "Share Via"));
        }

        return true;
    }

    public void writeDatabase(InputStream inputStream) throws IOException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+ BaseColumns._ID+" FROM "+ DbContract.Entry.TABLE_NAME, null);
        if (cursor.getCount()!=17670){
            db.execSQL(DbHelper.SQL_DELETE_ENTRIES);
            dbHelper.onCreate(db);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine())!=null){
                ContentValues values = new ContentValues();
                values.put(DbContract.Entry.HYMN_NUMBER, line.substring(0, 3));
                values.put(DbContract.Entry.HYMN_TEXT, line.substring(4));
                db.insert(DbContract.Entry.TABLE_NAME, null, values);
            }
        }
        cursor.close();
    }


    public Cursor readDatabase(String hymnNumber) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        String[] projection = {
//                BaseColumns._ID,
                DbContract.Entry.HYMN_NUMBER,
                DbContract.Entry.HYMN_TEXT
        };
        String selection = DbContract.Entry.HYMN_NUMBER + " = ?";
        String[] selectionArgs = {hymnNumber};

        return sqLiteDatabase.query(
                DbContract.Entry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null                    // The sort order
        );
    }
}