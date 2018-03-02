package com.example.samri.notebookapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;

import uk.co.senab.photoview.PhotoViewAttacher;

public class PdfFragment extends Fragment implements View.OnClickListener {
    static String path;
    ImageView imgView;
    Button btnPrevious, btnNext;
    static int pageIndex;
    PdfRenderer pdfRenderer;
    PdfRenderer.Page curPage;
    ParcelFileDescriptor descriptor;
    EditText et;
    static Bitmap bitmap;
    public PdfFragment() {}
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //get the path of file
        path = getArguments().getString("keyPath");
        return inflater.inflate(R.layout.activity_pdf_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        et=(EditText)view.findViewById(R.id.gotos);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int ind=0;
                try {
                    ind = Integer.parseInt(et.getText().toString());
                    pageIndex=ind;
                }
                catch(NumberFormatException n)
                {


                }
                displayPage(ind);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //initializing the views
        imgView = (ImageView)view.findViewById(R.id.imgView);
        btnPrevious = (Button)view.findViewById(R.id.btnPrevious);
        btnNext = (Button)view.findViewById(R.id.btnNext);
        //set click listener on buttons
        btnPrevious.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(imgView);
        pAttacher.update();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onStart() {
        super.onStart();
        try {
            openPdfRenderer();
            displayPage(pageIndex);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Sorry! This pdf is protected with password.", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onStop() {
        try {
            closePdfRenderer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch(IllegalStateException e)
        {

        }
        super.onStop();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void openPdfRenderer(){
        Toast.makeText(getContext(),path, Toast.LENGTH_LONG).show();
        File file = new File(path);
        descriptor = null;
        pdfRenderer = null;
        try {
            descriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            pdfRenderer = new PdfRenderer(descriptor);
        } catch (Exception e) {
            Toast.makeText(getContext(), "There's some error", Toast.LENGTH_LONG).show();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void closePdfRenderer() throws IOException {
        if (curPage != null) {
         try {
             curPage.close();
         }
        catch(IllegalStateException e)
        {
        }
        }
            if (pdfRenderer != null)
            pdfRenderer.close();
        if(descriptor !=null)
            descriptor.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void displayPage(int index){
        if(pdfRenderer.getPageCount() <= index)
            return;
        //close the current page
        try {
            if (curPage != null)
                curPage.close();
        }
        catch(IllegalStateException i)
        {

        }

            //open the specified page
        curPage = pdfRenderer.openPage(index);
        //get page width in points(1/72")
        int pageWidth = curPage.getWidth();
        //get page height in points(1/72")
        int pageHeight = curPage.getHeight();
        //returns a mutable bitmap
        bitmap = Bitmap.createBitmap(pageWidth, pageHeight, Bitmap.Config.ARGB_8888);
        //render the page on bitmap
        curPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        //display the bitmap
        imgView.setImageBitmap(bitmap);
        //enable or disable the button accordingly
        int pageCount = pdfRenderer.getPageCount();
        btnPrevious.setEnabled(0 != index);
        btnNext.setEnabled(index + 1 < pageCount);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPrevious: {
                //get the index of previous page
                int index = curPage.getIndex()-1;
                pageIndex--;
                displayPage(index);
                break;
            }
            case R.id.btnNext: {
                pageIndex++;
                //get the index of previous page
                int index = curPage.getIndex()+1;
                displayPage(index);
                break;
            }
        }
    }
}
