package com.example.together_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Together_Listview_Adapter extends BaseAdapter {

    public ArrayList<Together_Listview_PostingItem> postingItems = new ArrayList<Together_Listview_PostingItem>();
    @Override
    public int getCount() {
        return postingItems.size();
    }

    @Override
    public Object getItem(int position) {
        return postingItems.get((position));
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_together_listview_listitem, parent, false);
        }

        TextView depart = (TextView) convertView.findViewById(R.id.departure_item);
        TextView arrive = (TextView) convertView.findViewById(R.id.arrival_item);
        TextView limit = (TextView) convertView.findViewById(R.id.plimit_item);

        Together_Listview_PostingItem tlp = postingItems.get(position);

        depart.setText(tlp.getPDeparture());
        arrive.setText(tlp.getPArrival());
        limit.setText(tlp.getPLimit());

        return convertView;
    }

    public void addItem(String departure, String arrival, String limit){

        Together_Listview_PostingItem tlp = new Together_Listview_PostingItem(departure, arrival, limit);
        postingItems.add(tlp);
    }
}
