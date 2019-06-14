package com.example.together_park;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter  {

    Context context;
    ArrayList<item> adapter_item = new ArrayList<item>();
    LayoutInflater inflater;

    public ListAdapter(){
//        this.context = context;
//        this.adapter_item = adapter_item;
//        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return adapter_item.size();
    }

    @Override
    public Object getItem(int position) {
        return adapter_item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
//        View v = convertView;
//        RecyclerView.ViewHolder holder;
//        if(v==null){
//            v = inflater.inflate(R.layout.listitem,null);
//        }
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem, parent, false);
        }

        TextView TextView_ID = (TextView)convertView.findViewById(R.id.TextView_ID);
        TextView TextView_Chat = (TextView)convertView.findViewById(R.id.TextView_Chat);
        TextView TextView_date = (TextView)convertView.findViewById(R.id.TextView_date);

        item input_item = adapter_item.get((position));

        TextView_ID.setText(input_item.getId());
        TextView_Chat.setText(input_item.getChat());
        TextView_date.setText(input_item.getDatatime());

        return convertView;
    }

    public void addItem(String id, String chat, String date) {
        item item_ary = new item(id, chat, date);


        adapter_item.add(item_ary);
    }
}