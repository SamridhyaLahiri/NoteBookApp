package com.example.samri.notebookapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import static android.content.Intent.EXTRA_ORIGINATING_URI;
import static android.content.Intent.EXTRA_STREAM;


public class CreateNotes extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notes);
        Intent intent = getIntent();
        String name = intent.getStringExtra("keyName");
        String path = intent.getStringExtra("keyPath");
               if (path== null) {
                   path = intent.getDataString();
               path=path.substring(7,path.length());
                   path=path.replaceAll("%20"," ");
                   //File f=new File("")
                 //  path = path.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
               }
//Toast.makeText(CreateNotes.this,intent.getDataString(),Toast.LENGTH_LONG).show();
      // Toast.makeText(CreateNotes.this,path,Toast.LENGTH_LONG).show();

        setTitle(name);

        PdfFragment fragment = new PdfFragment();
        Bundle bundle = new Bundle();
        bundle.putString("keyPath", path);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame, fragment, "Pdf renderer")
                .commit();
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.men,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu1) //if menu 1 selected display toast
        {
            Intent j = new Intent(CreateNotes.this, Pdfdisplay.class);
            startActivity(j);
        }
        return super.onOptionsItemSelected(item);
    }
}
