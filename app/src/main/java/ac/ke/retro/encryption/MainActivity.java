package ac.ke.retro.encryption;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    public String Username;
    private DBHelper db;
    private static final String Extraname ="name";
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);


        EditText username = findViewById(R.id.Username);
        EditText password = findViewById(R.id.Password);
        db= new DBHelper(this);
        Button Login = findViewById(R.id.Login);

        Button signup = findViewById(R.id.signup);



     //   final ArrayList array_list = (ArrayList) dbHelper.getAllData();
       // final ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this,       android.R.layout.simple_list_item_1, array_list);
      //  listView.setAdapter(arrayAdapter);
        Login.setOnClickListener(new View.OnClickListener(){
         public void onClick(View view){

             String Password = password.getText().toString();
              Username = username.getText().toString();

             if(Username.equals("")||password.equals("")) {
                 Toast.makeText(MainActivity.this, "enter in all fields", Toast.LENGTH_SHORT).show();
             }
             else if(Username.equals("Admin")||Username.equals("admin")&&Password.equals("root")){
                 Intent i   = new Intent(getApplicationContext(),admin.class);

                 startActivity(i);
             }
             else{
                 boolean checkuser = db.checkUsernamepassword(Username, Password);

                if(checkuser==true){
                    Toast.makeText(MainActivity.this, "welcome back ,"+Username, Toast.LENGTH_SHORT).show();
                    Intent i   = new Intent(MainActivity.this ,encrypt.class);
                    i.putExtra("name", Username);
                    startActivity(i);
                }
                else{
                    Toast.makeText(MainActivity.this, "Details wrong try again,", Toast.LENGTH_SHORT).show();
                }

             }


          }
        }


        );
        signup.setOnClickListener(new View.OnClickListener()
                                     {
                                         public void onClick(View view)
                                         {
                                             gotosignup();
                                         }
                                     }

        );





    }



    private void gotosignup()
    {

        Intent i  = new Intent(this ,Signup.class);


        startActivity(i);
    }
    private void gotoadmin()
    {

        Intent i  = new Intent(this ,admin.class);


        startActivity(i);
    }






    public void showMessage(String title, String Message){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle(title);
            builder.setMessage(Message);
            builder.show();
        }



}