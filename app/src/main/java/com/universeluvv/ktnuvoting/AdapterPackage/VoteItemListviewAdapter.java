package com.universeluvv.ktnuvoting.AdapterPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.universeluvv.ktnuvoting.InfoClass.VoteItemInfo;
import com.universeluvv.ktnuvoting.R;

import java.util.ArrayList;

public class VoteItemListviewAdapter extends BaseAdapter {

    ArrayList<VoteItemInfo> info =new ArrayList<VoteItemInfo>();

    public VoteItemListviewAdapter(ArrayList<VoteItemInfo> name) {
        this.info = name;
    }


    @Override
    public int getCount() {
        return info.size();
    }


    @Override
    public VoteItemInfo getItem(int position) {
        return info.get(position);
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
            convertView = inflater.inflate(R.layout.voteitem_listview, parent, false); }

        TextView name =convertView.findViewById(R.id.cadidate_tv);
        TextView rate =convertView.findViewById(R.id.voterate_tv);
        ProgressBar progressBar =convertView.findViewById(R.id.voterate_progressbar);
        LinearLayout linearLayout =convertView.findViewById(R.id.endlistview_linear);

        int total = info.get(pos).getTotal_cnt();
        int current =info.get(pos).getCurrent_cnt();
        float itemrate = (float)current / (float)total *100 ;

        name.setText(info.get(pos).getName());
        rate.setText(current+"표("+itemrate+"%)");
        progressBar.setMax(100);
        progressBar.setProgress((int)itemrate);

        int temp = pos%7;

        switch (temp){
            case 0: linearLayout.setBackgroundResource(R.drawable.main_listview_shape_0); break;
            case 1: linearLayout.setBackgroundResource(R.drawable.main_listview_shape_1); break;
            case 2: linearLayout.setBackgroundResource(R.drawable.main_listview_shape_2); break;
            case 3: linearLayout.setBackgroundResource(R.drawable.main_listview_shape_3); break;
            case 4: linearLayout.setBackgroundResource(R.drawable.main_listview_shape_4); break;
            case 5: linearLayout.setBackgroundResource(R.drawable.main_listview_shape_5); break;
            case 6: linearLayout.setBackgroundResource(R.drawable.main_listview_shape_6); break;
        }

        return convertView;
    }
}
