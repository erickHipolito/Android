package com.example.createpdf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.createpdf.Constants.*;

public class MainActivity extends AppCompatActivity  {
    // variables for our buttons.
    Button generatePDFbtn;

    EditText edTextLoad, edTextDriver, edTextCoDriver, edTextTruck, edTextTrailer;
    EditText edTextOrigin, edTextDestination, edTextFuel, edTextQuickPay, edTextGrossPay, edTextNetPay;
    EditText edTextLumper, edTextBroker, edTextRemarks;

    DatePicker pickerDateLoaded, pickerDateUnloaded;
    String dateLoaded, dateUnloaded;

    // declaring width and height
    // for our PDF file.
    int pageHeight = 842; //1120;
    int pagewidth = 595;  //792;

    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;

    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing our variables.
        generatePDFbtn = findViewById(R.id.idBtnGeneratePDF);
        bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.trailer1);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);


        edTextLoad = (EditText) findViewById(R.id.edText1);
        edTextDriver = (EditText)findViewById(R.id.edText2);
        edTextCoDriver = (EditText)findViewById(R.id.edText3);
        edTextTruck = (EditText)findViewById(R.id.edText4);
        edTextTrailer = (EditText)findViewById(R.id.edText5);

        edTextOrigin = (EditText)findViewById(R.id.edText6);
        edTextDestination = (EditText)findViewById(R.id.edText7);
        //date
        pickerDateLoaded = (DatePicker) findViewById(R.id.dateLoaded);
        pickerDateUnloaded = (DatePicker) findViewById(R.id.dateUnLoaded);

        edTextFuel = (EditText)findViewById(R.id.edText8);
        edTextQuickPay = (EditText)findViewById(R.id.edText9);
        edTextGrossPay = (EditText)findViewById(R.id.edText10);
        edTextNetPay = (EditText)findViewById(R.id.edText11);
        edTextLumper = (EditText)findViewById(R.id.edText12);
        edTextBroker = (EditText)findViewById(R.id.edText13);
        edTextRemarks = (EditText) findViewById(R.id.layoutRemarks);


        // below code is used for
        // checking our permissions.
        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        generatePDFbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*System.out.println("Remarks: "+edTextRemarks.getText().toString());
                System.out.println("Selected Date: "+ pickerDateLoaded.getDayOfMonth()+"/"+ (pickerDateLoaded.getMonth())+"/"+pickerDateLoaded.getYear());
                */
                dateLoaded = pickerDateLoaded.getDayOfMonth()+"/"+ (pickerDateLoaded.getMonth())+"/"+pickerDateLoaded.getYear();
                dateUnloaded = pickerDateUnloaded.getDayOfMonth()+"/"+ (pickerDateUnloaded.getMonth())+"/"+pickerDateUnloaded.getYear();

                // calling method to
                // generate our PDF file.
                generatePDF(edTextLoad.getText().toString(), edTextDriver.getText().toString(), edTextCoDriver.getText().toString(),
                        edTextTruck.getText().toString(), edTextTrailer.getText().toString(), dateLoaded, edTextOrigin.getText().toString(),
                        dateUnloaded, edTextDestination.getText().toString(), edTextFuel.getText().toString(),
                        edTextQuickPay.getText().toString(), edTextGrossPay.getText().toString(), edTextNetPay.getText().toString(),
                        edTextLumper.getText().toString(), edTextBroker.getText().toString(), edTextRemarks.getText().toString());
            }
        });
    }

    private String getCurrenDate(){
        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(now);
        return currentDate;
    }

    private void generatePDF(String load, String driver, String coDriver, String truck,
                             String trailer, String dateLoad,  String origin,
                             String dateUnloaded, String destination, String fuel, String quickPay,
                             String grossPay, String netPay, String lumper, String broker,
                             String remarks) {
        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        Paint header = new Paint();
        Paint paint = new Paint();
        Paint title = new Paint();
        Paint body = new Paint();
        Paint data = new Paint();

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        // below line is used for setting
        // start page for our PDF file.
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        // creating a variable for canvas
        // from our page of PDF.
        Canvas canvas = myPage.getCanvas();

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        canvas.drawBitmap(scaledbmp, 56, 40, paint);
        header.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        header.setTextSize(20);
        header.setColor(ContextCompat.getColor(this, R.color.black));
        canvas.drawText(HEADER_DOCUMENT, 209,50, header);


        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.setTextSize(8);

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.setColor(ContextCompat.getColor(this, R.color.black)); //purple_200

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText(COMPANY_NAME, 209, 80, title);
        canvas.drawText(ADDRES_PART_ONE, 209, 95, title);
        canvas.drawText(ADDRES_PART_SECOND, 209, 110, title);
        canvas.drawText(PHONE_NUMBER, 209, 125, title);
        canvas.drawText(EMAIL_ONE, 209, 140, title);
        canvas.drawText(EMAIL_SECOND, 209, 155, title);

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        body.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//NORMAL
        body.setColor(ContextCompat.getColor(this, R.color.black));
        body.setTextSize(10);

        //data settings
        data.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//NORMAL
        data.setColor(ContextCompat.getColor(this, R.color.black));
        data.setTextSize(10);
        data.setTextAlign(Paint.Align.LEFT);

        if(load != null) {
            canvas.drawText(load, 160, 200, data);
        }

        if(driver != null){
            canvas.drawText(driver, 160, 210, data);
        }

        if(coDriver != null){
            canvas.drawText(coDriver, 160, 220, data);
        }

        if(truck != null){
            canvas.drawText(truck, 160, 230, data);
        }

        if(trailer != null){
            canvas.drawText(trailer, 160, 240, data);
        }
        if(dateLoad != null){
            canvas.drawText(dateLoad, 160, 250, data);
        }
        if(origin != null){
            canvas.drawText(origin, 160, 260, data);
        }
        if(dateUnloaded != null){
            canvas.drawText(dateUnloaded, 160, 270, data);
        }
        if(destination != null){
            canvas.drawText(destination, 160, 280, data);
        }
        if(fuel != null){
            if(fuel.isEmpty()){
                canvas.drawText("$ 0.0", 160, 290, data);
            }else {
                canvas.drawText("$ "+fuel, 160, 290, data);
            }
        }
        if(quickPay != null){
            if(quickPay.isEmpty()){
                canvas.drawText("$ 0.0", 160, 300, data);
            }else{
                canvas.drawText("$ "+quickPay, 160, 300, data);
            }
        }

        if(grossPay != null){
            if(grossPay.isEmpty()){
                canvas.drawText("$ 0.0", 160, 310, data);
            }else{
                canvas.drawText("$ "+grossPay, 160, 310, data);
            }
        }

        if(netPay != null){
            if(netPay.isEmpty()){
                canvas.drawText("$ 0.0", 160, 320, data);
            }else{
                canvas.drawText("$ "+netPay, 160, 320, data);
            }
        }

        if(lumper != null){
            if(lumper.isEmpty()){
                canvas.drawText("$ 0.0", 160, 330, data);
            }else{
                canvas.drawText("$ "+lumper, 160, 330, data);
            }
        }


        if(broker != null){
            canvas.drawText(broker, 160, 340, data);
        }
        if(remarks != null){
            canvas.drawText(remarks, 160, 350, data);
        }
        // below line is used for setting
        // our text to center of PDF.
        body.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Load: ", 130, 200, body);
        canvas.drawText("Driver: ", 130, 210, body);
        canvas.drawText("Co-Driver: ", 130, 220, body);
        canvas.drawText("Truck: ", 130, 230, body);
        canvas.drawText("Trailer: ", 130, 240, body);
        canvas.drawText("Date Loaded: ", 130, 250, body);
        canvas.drawText("Origin: ", 130, 260, body);
        canvas.drawText("Date Unloaded: ", 130, 270, body);
        canvas.drawText("Destination: ", 130, 280, body);
        canvas.drawText("Fuel Advanced: ", 130, 290, body);
        canvas.drawText("Quick Pay: ", 130, 300, body);
        canvas.drawText("Gross Pay: ", 130, 310, body);
        canvas.drawText("Net Pay: ", 130, 320, body);
        canvas.drawText("Lumper: ", 130, 330, body);
        canvas.drawText("Broker: ", 130, 340, body);
        canvas.drawText("Notes: ", 130, 350, body);

        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage);

        // below line is used to set the name of
        // our PDF file and its path.
        //File file = new File(Environment.getExternalStorageDirectory(), "GFG.pdf");

        File ruta= null;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            ruta = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), DIRECTORY_NAME);
            if (!ruta.exists()) {
                ruta.mkdirs();
            }
        }
        String nowDate = getCurrenDate();
        File file = new File(ruta, DOCUMENT_NAME + nowDate+".pdf");

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(MainActivity.this, MESSAGE_SUCCESS, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // below line is used
            // to handle error
            Toast.makeText(MainActivity.this, MESSAGE_NOT_SUCCESS, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }


}