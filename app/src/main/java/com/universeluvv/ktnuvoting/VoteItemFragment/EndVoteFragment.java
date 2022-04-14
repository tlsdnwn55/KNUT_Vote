package com.universeluvv.ktnuvoting.VoteItemFragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.universeluvv.ktnuvoting.AdapterPackage.VoteItemListviewAdapter;
import com.universeluvv.ktnuvoting.InfoClass.VoteDetailInfo;
import com.universeluvv.ktnuvoting.InfoClass.VoteItemInfo;
import com.universeluvv.ktnuvoting.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EndVoteFragment extends Fragment {


    VoteDetailInfo vote;

    ArrayList<VoteItemInfo> voteItemInfos =new ArrayList<VoteItemInfo>();

    public EndVoteFragment(VoteDetailInfo vote) {
        this.vote =vote;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_end_vote, container, false);

        TextView name = view.findViewById(R.id.voteitem_end_name_tv);
        TextView term =view.findViewById(R.id.voteitem_end_term_tv);
        ListView listView =view.findViewById(R.id.end_listview);

        TextView total =view.findViewById(R.id.end_total_tv);
        TextView current = view.findViewById(R.id.end_current_tv);
        TextView totalrate = view.findViewById(R.id.total_rate_tv);


        name.setText(vote.getName());
        term.setText("종료시간: "+vote.getTerm());

        total.setText("총 투표권: "+vote.getTotal_cnt());
        current.setText("유효표: "+vote.getCurrent_cnt());

        float tmp =(float)vote.getCurrent_cnt() / (float)vote.getTotal_cnt()*100;


        totalrate.setText("("+String.format("%.2f" , tmp)+"%)");

        int a=0; boolean t =false;

        for(int i=0; i < vote.getItem().size(); i++){
            if(vote.getItem().get(i).equals("무효표")){
                t=true;
                a=i;
            }
        }
        if(t){
            vote.getItem().remove(a);
            vote.getItem().add("무효표");
            int temp =vote.getItem_cnt().get(a);
            vote.getItem_cnt().remove(a);
            vote.getItem_cnt().add(temp);
        }

        for(int i=0; i<vote.getItem().size();i++){
            voteItemInfos.add( new VoteItemInfo(vote.getItem().get(i),vote.getCurrent_cnt(),vote.getItem_cnt().get(i)));
        }

        VoteItemListviewAdapter adapter =new VoteItemListviewAdapter(voteItemInfos);
        listView.setAdapter(adapter);



        return view;
    }

}
