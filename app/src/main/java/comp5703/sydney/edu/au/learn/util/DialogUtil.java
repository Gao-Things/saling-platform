package comp5703.sydney.edu.au.learn.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import comp5703.sydney.edu.au.learn.R;

public class DialogUtil {
    public static void showCustomDialog(Activity activity, Context context, String titleText, View.OnClickListener confirmListener, View.OnClickListener cancelListener) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_custom_layout, null);

        TextView title = dialogView.findViewById(R.id.dialogTitle);
        title.setText(titleText);

        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button confirmButton = dialogView.findViewById(R.id.confirm_button);
        Button cancelButton = dialogView.findViewById(R.id.cancel_button);

        // Set the listeners for the buttons
        confirmButton.setOnClickListener(v -> {
            dialog.dismiss();
            if (confirmListener != null) {
                confirmListener.onClick(v);
            }
        });

        cancelButton.setOnClickListener(v -> {
            dialog.dismiss();
            if (cancelListener != null) {
                cancelListener.onClick(v);
            }
        });
    }
}
