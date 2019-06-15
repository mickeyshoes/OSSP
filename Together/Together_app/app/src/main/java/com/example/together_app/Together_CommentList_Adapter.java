package com.example.together_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Together_CommentList_Adapter extends BaseAdapter {

    Context context;
    public ArrayList<Together_CommentItem> commentItems = new ArrayList<Together_CommentItem>();
    LayoutInflater inflater;

    @Override
    public int getCount() {
        return commentItems.size();
    }

    @Override
    public Object getItem(int position) {
        return commentItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_together_listview_comment_item, parent, false);
        }

        TextView TextView_ID = (TextView)convertView.findViewById(R.id.TextView_ID);
        TextView TextView_Chat = (TextView)convertView.findViewById(R.id.TextView_Chat);
        TextView TextView_date = (TextView)convertView.findViewById(R.id.TextView_date);

        Together_CommentItem tci = commentItems.get(position);

        TextView_ID.setText(tci.getCID());
        TextView_Chat.setText(tci.getCAbout());
        TextView_date.setText(tci.getCTime());

        return convertView;
    }

    public void addItem(String id, String chat, String date) {
        Together_CommentItem tci = new Together_CommentItem(id, chat, date);
        commentItems.add(tci);
    }

}
