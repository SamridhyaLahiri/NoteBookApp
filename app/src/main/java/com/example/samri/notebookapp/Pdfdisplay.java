package com.example.samri.notebookapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.samri.notebookapp.PdfFragment.bitmap;
import static com.example.samri.notebookapp.PdfFragment.pageIndex;
import static com.example.samri.notebookapp.PdfFragment.path;

public class Pdfdisplay extends AppCompatActivity {
    Bitmap b;
    TextView pdf;
    private TextRecognizer detector;
    Button bu;
    private String parsedText;
    EditText tx;
    LinearLayout pv;
    int enable=0;
    int i=0;
    Button prev,next;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfdisplay);
        pv=(LinearLayout) findViewById(R.id.pview);
        pdf=(TextView)findViewById(R.id.pdf);
        tx=(EditText) findViewById(R.id.tx);
        bu=(Button)findViewById(R.id.b);
        prev=(Button)findViewById(R.id.prev);
        next=(Button)findViewById(R.id.next);
        ActivityCompat.requestPermissions(Pdfdisplay.this, new
                String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},20);
        gettext(pageIndex);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                --pageIndex;
                gettext(pageIndex);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ++pageIndex;
                gettext(pageIndex);
            }
        });
        tx.setText("");
        bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pv.getVisibility()==VISIBLE)
                {
                    pv.setVisibility(GONE);
                    bu.setText("Exit FullScreen");
                }
                else {
                    pv.setVisibility(VISIBLE);
                    bu.setText("Enter FullScreen");
                }
            }
        });
    }
    public void gettext(int page) {
        try {
            parsedText = "";
            PdfReader reader = new PdfReader(path);
            parsedText = parsedText + PdfTextExtractor.getTextFromPage(reader,page+1).trim() + "\n"; //Extracting the content from the different pages
            pdf.setText(parsedText);
            reader.close();
        } catch (Exception e) {
            Toast.makeText(Pdfdisplay.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        selection();
    }
    public void selection()
    {
        pdf.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                int min = 0;
                int max = pdf.getText().length();
                if (pdf.isFocused()) {
                    final int selStart = pdf.getSelectionStart();
                    final int selEnd = pdf.getSelectionEnd();
                    min = Math.max(0, Math.min(selStart, selEnd));
                    max = Math.max(0, Math.max(selStart, selEnd));
                }
                final CharSequence selectedText = pdf.getText().subSequence(min, max); //this is your desired string
                if(enable>0)
                    tx.setText(tx.getText().toString()+selectedText);
                menu.removeItem(android.R.id.selectAll);
                menu.removeItem(android.R.id.cut);
                menu.removeItem(android.R.id.copy);
                menu.removeItem(android.R.id.shareText);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu2) //if menu 1 selected display toast
        {
            if(enable>0) {
                enable = 0;
                Toast.makeText(Pdfdisplay.this,"Select note making disabled",Toast.LENGTH_LONG).show();
            }
            else if(enable==0) {
                enable++;
                Toast.makeText(Pdfdisplay.this,"Select note making enabled",Toast.LENGTH_LONG).show();
            }
        }
        if(item.getItemId()==R.id.menu3)
        {
            checkpermissions();
        }
        return super.onOptionsItemSelected(item);
    }
    void checkpermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //if permission granted, initialize the views
            save();
        } else {
            //show the dialog requesting to grant permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
    public void save()
    {
        String name=path.substring(path.lastIndexOf('/')+1,path.length()-3);
        i++;
        try {
            File root = new File(Environment.getExternalStorageDirectory(),"Notebook/"+name);
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root,"file"+i+".txt");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(tx.getText().toString());
            writer.flush();
            writer.close();
            Toast.makeText(Pdfdisplay.this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
