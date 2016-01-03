package com.digitcreativestudio.safian.adadosen.Lecturers;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
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

    public LecturersAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_lecturer, viewGroup, false);
    }

    @Override
    public void bindView(View vi, Context context, Cursor cursor) {
        TextView name = (TextView) vi.findViewById(R.id.name);
        TextView nip = (TextView) vi.findViewById(R.id.nip);
        TextView lastModify = (TextView) vi.findViewById(R.id.lastModify);
        TextView modifiedBy = (TextView) vi.findViewById(R.id.modifiedBy);
        final ToggleButton status = (ToggleButton) vi.findViewById(R.id.status);
        LinearLayout parentLL = (LinearLayout) (name.getParent());

        vi.setId(cursor.getInt(cursor.getColumnIndex(LecturerEntry._ID)));
        name.setText(cursor.getString(cursor.getColumnIndex(LecturerEntry.COLUMN_NAME)));
        nip.setText(cursor.getString(cursor.getColumnIndex(LecturerEntry.COLUMN_NIP)));
        lastModify.setText(Utils.getFriendlyDate(new Date(cursor.getLong(cursor.getColumnIndex(LecturerEntry.COLUMN_LAST_MODIFY)))));
        modifiedBy.setText(cursor.getString(cursor.getColumnIndex(LecturerEntry.COLUMN_MODIFIED_BY)));

        status.setOnCheckedChangeListener(null);
        status.setChecked(cursor.getString(cursor.getColumnIndex(LecturerEntry.COLUMN_STATUS)).equals("1"));
        status.setTag(cursor.getInt(cursor.getColumnIndex(LecturerEntry._ID)));

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)status.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.button_user));
        else status.setBackgroundDrawable(context.getDrawable(R.drawable.button_user));

        status.setOnCheckedChangeListener(new LecturerOnChangeListener(context, cursor.getPosition()));
    }

}
