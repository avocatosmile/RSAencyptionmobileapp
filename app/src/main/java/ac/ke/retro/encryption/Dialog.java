package ac.ke.retro.encryption;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Dialog extends AppCompatDialogFragment {
    @NonNull

    private EditText newUsername;
    private EditText newPassword;
    private DialogListener listener ;
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        View view =inflater.inflate(R.layout.layout_dialog,null);

        builder.setView(view)
                .setTitle("Enter new details")
                .setNegativeButton("cancel",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface ,int i){

                    }
                })
                .setPositiveButton("save",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface ,int i){
                            String username = newUsername.getText().toString();
                            String password = newPassword.getText().toString();
                            listener.applytexts(username, password);
                    }
                });
        newUsername =view.findViewById(R.id.newusername);
        newPassword= view.findViewById(R.id.newpassword);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener  =(DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement Dialoglistener");
        }
    }

    public interface DialogListener{
        void applytexts(String username  , String password);
    }
}
