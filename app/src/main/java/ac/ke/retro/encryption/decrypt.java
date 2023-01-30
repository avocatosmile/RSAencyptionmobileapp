package ac.ke.retro.encryption;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class decrypt extends AppCompatActivity {
    private DBHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);
        getSupportActionBar().hide();
        db = new DBHelper(this);
        Intent i = getIntent();
        String us =i.getStringExtra("name");
        displayem(us);












    }
    public void displayem(String us) {
        Cursor res = db.revealfileData(us);
        TextView files = findViewById(R.id.files);
        LinearLayout layout = (LinearLayout) findViewById(R.id.aa);
        if (res.getCount() == 0) {
            //nothing because there is no result
            return;
        }
        /*
        LinearLayout layout = (LinearLayout) findViewById(R.id.aa);
        Button button = new Button(this);
        layout.addView(button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                displayem(); // call to the method defined
            }

                new AlertDialog.Builder(this).setTitle("Stored data").setMessage("The details that currently exist in our database are: \n \n " + buffer.toString())
                .setNeutralButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // go back to form
                    }
                }).show();

    }
        });*/


        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            TextView TextView = new TextView(this) ;
            layout.addView(TextView);

            String id ="ID : " +res.getString(0) + "\n";
            String file ="filename: encrypted"+res.getString(0)+ "\n" ;
            String name ="Username: " +res.getString(2) + "\n";
            String filename="original filename: " +res.getString(3) + "\n";
            String encryptedkey ="encrypted key:  " +res.getString(4) + "\n";
            String privatekey ="private key:" +res.getString(5) + "\n";
            String publickey = "public key:" +res.getString(6) + "\n";


            String m2=res.getString(0) ;

            //showMessage("Data", buffer.toString());
            Button button = new Button(this);
            button.setText("view file");
            layout.addView(button);
            TextView.append(id );
            TextView.append( file);
            TextView.append( name);
            TextView.append( filename);
            TextView.append( encryptedkey);
            TextView.append( privatekey);
            TextView.append( publickey);

            button.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {

                    Intent i = new Intent(getApplicationContext(), viewfiles.class);

                    i.putExtra("id" ,m2);
                    startActivity(i);



                }
            });


        }



    }
}