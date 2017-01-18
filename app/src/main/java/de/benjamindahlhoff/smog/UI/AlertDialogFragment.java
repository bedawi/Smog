package de.benjamindahlhoff.smog.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import de.benjamindahlhoff.smog.R;


public class AlertDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Error")
                .setMessage("There has been an error!")
                .setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
