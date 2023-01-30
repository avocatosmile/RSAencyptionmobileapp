package ac.ke.retro.encryption;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class admin extends AppCompatActivity implements Dialog.DialogListener {
    private DBHelper db;
    String []values  = new String[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_admin);
        Button delete = findViewById(R.id.delete);
        db= new DBHelper(this);
        Button update = findViewById(R.id.update);

        displayusers();



        delete.setOnClickListener(new View.OnClickListener() {
                                      public void onClick(View view) {
                                          EditText user = findViewById(R.id.user);
                                          String username = user.getText().toString();


                                          boolean deleteuser = db.deleteuser(username);
                                          if (deleteuser == true) {
                                              Toast.makeText(admin.this, username + "account has been deleted,", Toast.LENGTH_SHORT).show();
                                          } else {
                                              Toast.makeText(admin.this, "try again", Toast.LENGTH_SHORT).show();
                                          }
                                          displayusers();
                                      }


                                  }

        );
        update.setOnClickListener(new View.OnClickListener()
                                  {
                                      public void onClick(View view)
                                      {

                                          opendialog();
                                          displayusers();




                                      }
                                  }

        );

    }

    @Override
    public void applytexts(String username, String password) {

        values[0] = username ;
        values[1]= password ;
        EditText user = findViewById(R.id.user);
        String oldu = user.getText().toString();


        boolean updateuser = db.UpdateData(oldu, username , password);
        if (updateuser == true) {
            Toast.makeText(admin.this, oldu + "account has been deleted,", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(admin.this, "try again", Toast.LENGTH_SHORT).show();
        }

    }

    public void opendialog() {
        Dialog dialog =new Dialog();
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    public void displayusers() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.pp);
        TextView textView = new TextView(this);
        layout.addView(textView);
        Cursor res = db.revealData();
       // TextView users = findViewById(R.id.allusers);
        if (res.getCount() == 0) {
            System.out.println("sean was here");
            //nothing because there is no result
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("Username: " + res.getString(0) + "\n");
            buffer.append("Password: " + res.getString(1) + "\n");
            buffer.append("\n");

            //showMessage("Data", buffer.toString());
        }

        textView.append(buffer.toString());

    }
    public void Updateuser(){
      //  Cursor res = db.UpdateData();
        EditText user = findViewById(R.id.user);
        String username = user.getText().toString();
    }
}