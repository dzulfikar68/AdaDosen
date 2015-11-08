package com.digitcreativestudio.safian.adadosen.Lecturers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.digitcreativestudio.safian.adadosen.Auth.SessionManager;
import com.digitcreativestudio.safian.adadosen.R;

import java.util.ArrayList;

/**
 * Created by faqih_000 on 11/7/2015.
 */
public class LecturersAdapter extends BaseAdapter {
    private Activity mActivity;
    private SessionManager session;
    private ArrayList<Lecturer> mLecturers;

    private static LayoutInflater inflater = null;

    public LecturersAdapter(Activity activity, ArrayList<Lecturer> lecturers) {
        mActivity = activity; mLecturers = lecturers;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        session = new SessionManager(mActivity.getApplicationContext());
    }
    public int getCount() {
        return mLecturers.size();
    }
    public Object getItem(int position) {
        return mLecturers.get(position);
    }
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_lecturer, null);

        TextView id = (TextView) vi.findViewById(R.id.id);
        TextView name = (TextView) vi.findViewById(R.id.name);
        TextView nip = (TextView) vi.findViewById(R.id.nip);
        TextView lastModify = (TextView) vi.findViewById(R.id.lastModify);
        TextView modifiedBy = (TextView) vi.findViewById(R.id.modifiedBy);
        ToggleButton status = (ToggleButton) vi.findViewById(R.id.status);
        LinearLayout parentLL = (LinearLayout) (name.getParent());

        Lecturer lecturer = mLecturers.get(position);
        //id.setText(lecturer.getId());
        name.setText(lecturer.getName());
        nip.setText(lecturer.getNip());
        lastModify.setText(lecturer.getLastModify());
        modifiedBy.setText(lecturer.getModifiedBy());
        parentLL.setId(lecturer.getId());

        status.setOnCheckedChangeListener(null);
        status.setChecked(lecturer.getStatus());
        status.setTag(lecturer.getId());

        if(session.isLoggedIn()){
            status.setOnCheckedChangeListener(new LecturerOnChangeListener(mActivity));
            status.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.button_user));
        }else{
            status.setEnabled(false);
            status.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.button_guest));
        }
        return vi;
    }
}
