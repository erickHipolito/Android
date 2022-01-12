package com.example.truckingpdf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    String DIRECTORY_NAME = "MyPDFs";
    String DOCUMENT_NAME = "MyPDF.pdf";

    EditText edText;
    Button btnPrint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edText = findViewById(R.id.edText);
        btnPrint = findViewById(R.id.btnPrint);

        //Valida Permisos
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 1000);
        }

        //Genera Documento
        btnPrint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                createPDF();
                Toast.makeText(MainActivity.this, "Generated PDF", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void createPDF(){
        Document document = new Document();

        try {
            File file = createFichero(DOCUMENT_NAME);
            System.out.println("estamos en createPDF ++ ");
            if(file == null) {
                System.out.println("Fichero vacio");
            }else {
                FileOutputStream ficheroPDF = new FileOutputStream(file.getAbsolutePath());

                PdfWriter writer = PdfWriter.getInstance(document, ficheroPDF);

                document.open();

                document.add(new Paragraph("TABLA \n\n"));
                document.add(new Paragraph(edText.getText().toString() + "\n\n"));

                //Insert into table
                PdfPTable table = new PdfPTable(5);
                for (int i = 0; i < 15; i++) {
                    table.addCell("CELDA " + i);
                }

                document.add(table);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    public File createFichero(String nameFile){
        File ruta = getRuta();

        File fichero = null;
        System.out.println("nombre archivo: "+ nameFile);
        if(ruta != null){
            System.out.println("Ruta: "+ ruta.getAbsolutePath());
            fichero = new File(ruta, nameFile);
        }
        System.out.println("Ruta completa: "+ fichero.getAbsolutePath());
        return fichero;
    }

    public File getRuta(){
        File ruta = null;
        System.out.println("estamos en getRuta().....");
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            ruta = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), DIRECTORY_NAME);


            //if(ruta != null){
                    if(!ruta.exists()){
                        ruta.mkdirs();
                    }
            //}
        }
        System.out.println("ruta creada: "+ruta.getAbsolutePath());
        return ruta;
    }
}