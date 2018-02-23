package com.afomic.yearbook.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.afomic.yearbook.R;

/**
 * Created by afomic on 12/18/17.
 *
 */

public class AddCommentDialog extends DialogFragment {
    CommentDialogListener mListener;
    public static AddCommentDialog newInstance(){
        return new AddCommentDialog();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener=(CommentDialogListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener=null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Comment");
        View v= LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_comment,null,false);
        builder.setView(v);
        final EditText editText=v.findViewById(R.id.edt_comment);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onSubmit(editText.getText().toString());
                dismiss();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        return builder.create();
    }
    public interface CommentDialogListener{
        void onSubmit(String comment);
    }
}
