package ac.ke.retro.encryption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class viewfiles extends AppCompatActivity {

    private DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBHelper(this);
        setContentView(R.layout.activity_viewfiles);
        Intent i = getIntent();
        String id =i.getStringExtra("id");


        displayem(id) ;

        }



    public void displayem(String id) {

        Cursor res = db.revealfile(id);
        TextView TextView= findViewById(R.id.potatoview);
        if (res.getCount() == 0) {
            System.out.println(id);
            //nothing because there is no result
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {


            String enf = res.getString(7) + "\n";
            System.out.println(enf);


            TextView.append(enf);



        }



    }
}




