package ac.ke.retro.encryption;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Signup extends AppCompatActivity {
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signup);
        db= new DBHelper(this);

        EditText UsernameSignup = findViewById(R.id.UsernameSignup);

        EditText Password = findViewById(R.id.registerPassword);
        EditText CPassword = findViewById(R.id.confirmPassword);



        Button signup = findViewById(R.id.signUP);

        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                String Username = UsernameSignup.getText().toString();

                String password = Password.getText().toString();
                String Cpassword = CPassword.getText().toString();
                if(Username.equals("")||password.equals("")||Cpassword.equals("")) {
                    Toast.makeText(Signup.this, "enter in all fields", Toast.LENGTH_SHORT).show();
                }else{
                        if (password.equals(Cpassword)) {
                            boolean checkuser = db.checkUsername(Username);
                            if (checkuser == false) {
                                boolean insert = db.insertData(Username, password);

                                if (insert == true) {
                                    Toast.makeText(Signup.this, "successfull", Toast.LENGTH_SHORT).show();
                                    Intent intent =new Intent(getApplicationContext(), MainActivity.class);
                                  //  intent.putExtra("name", Username);
                                    startActivity(intent);

                                }
                                else{
                                    Toast.makeText(Signup.this, "failed", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(Signup.this, "account already created", Toast.LENGTH_SHORT).show();
                            }
                        }


                        else{
                          Toast.makeText(Signup.this, "password not matching", Toast.LENGTH_SHORT).show();
                        }
                }
            }
        });


    }

}