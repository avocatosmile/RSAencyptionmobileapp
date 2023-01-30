package ac.ke.retro.encryption;

import static android.util.Base64.encodeToString;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
// efn is encrypted file name

public class encrypt  extends AppCompatActivity {


    private MainActivity login;
    private String EFN;
    private DBHelper db;
    private MainActivity main;
    private RSA rsa;
    ActivityResultLauncher<Intent> resultLauncher;
    int counter = 1;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_encrypt);
        db = new DBHelper(this);

        Intent i = getIntent();

        // String  user= i.getStringExtra(MainActivity.Extraname);
        TextView username = findViewById(R.id.username);
        String us =getIntent().getStringExtra("name");
        String us2 = "USERNAME :" +us ;
        username.setText(us2);

        //ProgressBar  progressBar=findViewById(R.id.loading);
        Button chooseFile = findViewById(R.id.bt_select);
        Button encryptbtn = findViewById(R.id.press);
        Button filespage =findViewById(R.id.filespage);
        final String[] x = new String[1];
        final String[] name = {new String()};
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts
                        .StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        Intent data = result.getData();

                        if (data != null) {

                            Uri sUri = data.getData();


                            String sPath = sUri.getPath();

                            System.out.println(sPath);
                            String str = sPath;
                            String[] arrOfStr = str.split(":", 2);
                            String actualpath = arrOfStr[1];

                            TextView filename = findViewById(R.id.fileN);
                            String info = "Filename : " + actualpath;
                            //   loading2.setProgress(50);
                            filename.append(info);
                            try {
                                File file = new File(actualpath);
                                Scanner Reader = new Scanner(file);

                                while (Reader.hasNextLine()) {
                                    String content = Reader.nextLine();
                                    if (x[0] == null) {
                                        System.out.println("null value not added");
                                        x[0] = content;
                                    } else {
                                        x[0] = x[0] + content + "\n";

                                    }

                                }
                                name[0] =actualpath;
                                Reader.close();
                                System.out.println(x[0]);
                            } catch (FileNotFoundException e) {
                                System.out.println("An error occurred.");
                                e.printStackTrace();
                            }

                        }
                    }
                });

        encryptbtn.setOnClickListener(new View.OnClickListener() {

                                          @RequiresApi(api = Build.VERSION_CODES.O)
                                          public void onClick(View view) {


                                              Encryption(x[0], name[0], us );
                                              counter++;

                                          }
                                      }

        );
        filespage.setOnClickListener(new View.OnClickListener() {

                                          @RequiresApi(api = Build.VERSION_CODES.O)
                                          public void onClick(View view) {

                                              Intent i = new Intent(getApplicationContext(), decrypt.class);
                                              i.putExtra("name", us);

                                              startActivity(i);

                                          }
                                      }

        );
        chooseFile.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (ActivityCompat.checkSelfPermission(
                                encrypt.this,
                                Manifest.permission
                                        .READ_EXTERNAL_STORAGE)
                                != PackageManager
                                .PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(
                                    encrypt.this,
                                    new String[]{
                                            Manifest.permission
                                                    .READ_EXTERNAL_STORAGE},
                                    1);
                        } else {

                            selectfile();
                        }
                    }
                });


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String [] rsaEncrypt(String plainText){


        // Get an instance of the RSA key generator
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGenerator.initialize(4096);

        // Generate the KeyPair
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Get the public and private key
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        System.out.println("Original Text  : "+plainText);

        // Encryption
        byte[] cipherTextArray = new byte[0];
        try {
            cipherTextArray = encrypt(plainText, publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String encryptedText = Base64.getEncoder().encodeToString(cipherTextArray);
        System.out.println("Encrypted Text : "+encryptedText );
        System.out.println("Public key : "+publicKey);
        System.out.println("Private key : "+privateKey);
        String pubkey = publicKey.toString() ;
        String privkey =privateKey.toString() ;
        String [] values = new String[5];
        values[1]=encryptedText;
       values[2] =pubkey;
       values[3]=privkey;
        return (values) ;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String Encryption(String Orginal ,String filepath, String us  ) {

        final String SECRET_KEY = RandomString.getAlphaNumericString(256);
        final String SALTVALUE = "abcdefg";
        String output = encrypt(Orginal, SECRET_KEY, SALTVALUE);
        System.out.println("works the output:" + output);

        String [] values  =rsaEncrypt(SECRET_KEY);
        new AlertDialog.Builder(this).setTitle("Encrypted File").setMessage("Your data:\n" +
                output).setNeutralButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getApplicationContext(), encrypt.class);
                startActivity(i);
                // do nothing - it will close when clicked
            }
        })
                .setPositiveButton("save file", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String [] filenameAndpath  =writeToFile(output);
                        boolean insert = db.insertfile(filenameAndpath[0],us,filepath, values[1], values[3] ,values[2],output);
                        if (insert == true) {
                            Toast.makeText(encrypt.this, "successfull", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(getApplicationContext(), decrypt.class);
                            i.putExtra("name", us);

                            startActivity(i);

                        } else {
                            Toast.makeText(encrypt.this, "unsuccessfull", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).show();

        return output;


    }


    public static class RandomString {

        // function to generate a random string of length n
        static String getAlphaNumericString(int n) {

            // chose a Character random from this String
            String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

            // create StringBuffer size of AlphaNumericString
            StringBuilder sb = new StringBuilder(n);

            for (int i = 0; i < n; i++) {

                // generate a random number between
                int index = (int) (AlphaNumericString.length() * Math.random());

                // add Character one by one in end of sb
                sb.append(AlphaNumericString.charAt(index));
            }

            return sb.toString();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encrypt(String strToEncrypt, String SECRET_KEY, String SALTVALUE) {

        try {
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALTVALUE.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            System.out.println("Error is " + e.toString());
        }
        return null;
    }


    private void selectfile() {
        // Initialize intent
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // set type
        intent.setType("text/plain");
        // Launch intent
        resultLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);

        // check condition
        if (requestCode == 1 && grantResults.length > 0
                && grantResults[0]
                == PackageManager.PERMISSION_GRANTED) {
            // When permission is granted
            // Call method
            selectfile();
        } else {
            // When permission is denied
            // Display toast
            Toast
                    .makeText(getApplicationContext(),
                            "Permission Denied",
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private String [] writeToFile(String message) {
        try {
////data/data/ac.ke.retro.encryption/files/example1.txt file path

            String name = "encryptedfile" + counter;
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(name,
                    Context.MODE_PRIVATE));
            outputStreamWriter.write(message);
            outputStreamWriter.close();
            System.out.println("file made");

            String path = getFilesDir()+"/"+name ;
            System.out.println(path);
            String [] values = new String[3];
            values[0]= name ;
            values[1] =path;
            return  values;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String [] lols = new String[0];
        return lols ;

    }


    public static byte[] encrypt (String plainText,PublicKey publicKey ) throws Exception
    {
        //Get Cipher Instance RSA With ECB Mode and OAEPWITHSHA-512ANDMGF1PADDING Padding
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");

        //Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        //Perform Encryption
        byte[] cipherText = cipher.doFinal(plainText.getBytes()) ;

        return cipherText;
    }













}

