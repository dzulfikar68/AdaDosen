package com.digitcreativestudio.safian.adadosen.Lecturers;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.digitcreativestudio.safian.adadosen.Data.DBContract.LecturerEntry;
import com.digitcreativestudio.safian.adadosen.R;
import com.digitcreativestudio.safian.adadosen.Utils.Utils;

import java.util.Date;

/**
 * Created by faqih_000 on 1/2/2016.
 */
public class LecturersAdapter extends CursorAdapter {
    Context context;

    Dialog commentDialog;

    public LecturersAdapter(Context context, Cursor c, int flags, Dialog dialog) {
        super(context, c, flags);
        this.commentDialog = dialog;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_lecturer, viewGroup, false);
    }

    @Override
    public void bindView(View vi, final Context context, final Cursor cursor) {
        TextView name = (TextView) vi.findViewById(R.id.name);
        TextView nip = (TextView) vi.findViewById(R.id.nip);
        TextView lastModify = (TextView) vi.findViewById(R.id.lastModify);
        TextView modifiedBy = (TextView) vi.findViewById(R.id.modifiedBy);
        TextView comment = (TextView) vi.findViewById(R.id.comment);
        final ToggleButton status = (ToggleButton) vi.findViewById(R.id.status);
        LinearLayout parentLL = (LinearLayout) (name.getParent());

        final int id = cursor.getInt(cursor.getColumnIndex(LecturerEntry._ID));
        final boolean statusToggle = cursor.getString(cursor.getColumnIndex(LecturerEntry.COLUMN_STATUS)).equals("1");
        final int position = cursor.getPosition();

        String nameTxt = cursor.getString(cursor.getColumnIndex(LecturerEntry.COLUMN_TITLE_PREFIX)) +" "
                +cursor.getString(cursor.getColumnIndex(LecturerEntry.COLUMN_NAME))+", "
                +cursor.getString(cursor.getColumnIndex(LecturerEntry.COLUMN_TITLE_SUFFIX));
        vi.setId(id);
        name.setText(nameTxt.trim());
        nip.setText(cursor.getString(cursor.getColumnIndex(LecturerEntry.COLUMN_NIP)));
        lastModify.setText(Utils.getFriendlyDate(new Date(cursor.getLong(cursor.getColumnIndex(LecturerEntry.COLUMN_LAST_MODIFY)))));
        modifiedBy.setText(cursor.getString(cursor.getColumnIndex(LecturerEntry.COLUMN_MODIFIED_BY)));

        if(!cursor.getString(cursor.getColumnIndex(LecturerEntry.COLUMN_COMMENT)).equals("")){
            comment.setText(cursor.getString(cursor.getColumnIndex(LecturerEntry.COLUMN_COMMENT)));
            ((View) comment.getParent()).setVisibility(View.VISIBLE);
        }else{
            ((View) comment.getParent()).setVisibility(View.GONE);
        }

        status.setOnCheckedChangeListener(null);
        status.setChecked(statusToggle);
        status.setTag(cursor.getInt(cursor.getColumnIndex(LecturerEntry._ID)));

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)status.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.button_user));
        else status.setBackgroundDrawable(context.getDrawable(R.drawable.button_user));

        ((View) lastModify.getParent()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentDialog.show();
                commentDialog.findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        commentDialog.dismiss();
                        Utils.checkLecturerUpdate(
                                context,
                                String.valueOf(id),
                                String.valueOf(statusToggle),
                                String.valueOf(position),
                                ((EditText) commentDialog.findViewById(R.id.comment)).getText().toString(),
                                commentDialog
                        );
                        ((EditText) commentDialog.findViewById(R.id.comment)).setText("");
                    }
                });
            }
        });

        status.setOnCheckedChangeListener(new LecturerOnChangeListener(context, id, commentDialog));
    }

}
