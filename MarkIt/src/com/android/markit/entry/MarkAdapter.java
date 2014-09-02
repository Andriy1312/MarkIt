package com.android.markit.entry;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import com.android.markit.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MarkAdapter extends ArrayAdapter<Mark> {

    private final String LATITUDE = "Lat: ";
    private final String LONGITUDE = "Long: ";
    private final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
    private final Context mContext;
    private final List<Mark> entryList;

    public MarkAdapter(Context context, int resource, List<Mark> list) {
        super(context, resource, list);
        mContext = context;
        entryList = list;
    }

    static class ViewHub {
        TextView coordsTextView;
        TextView timeTextView;
    }

    @Override
    public int getCount() {
        return entryList.size();
    }

    @Override
    public Mark getItem(int position) {
        return entryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if(convertView == null) {
            LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflator.inflate(R.layout.check_list_item, null);
            final ViewHub viewHolder = new ViewHub();
            viewHolder.coordsTextView = (TextView)view.findViewById(R.id.text_coords);
            viewHolder.timeTextView = (TextView)view.findViewById(R.id.text_time);
            view.setTag(viewHolder);
        }
        else
            view = convertView;
        ViewHub holder = (ViewHub) view.getTag();
        String latitude = LATITUDE + Double.toString(entryList.get(position).getLatitude());
        String longitude = LONGITUDE + Double.toString(entryList.get(position).getLongitude());
        String time = dateUTCForm(entryList.get(position).getTime());
        holder.coordsTextView.setText(latitude + ", " + longitude);
        holder.timeTextView.setText(time);
        return view;
    }

    @SuppressLint("SimpleDateFormat")
    private String dateUTCForm(Long time) { 
        DateFormat format = new SimpleDateFormat(DATE_FORMAT);
        Date date = new Date(time);
        String formatted = format.format(date);
        return formatted;
    }
}