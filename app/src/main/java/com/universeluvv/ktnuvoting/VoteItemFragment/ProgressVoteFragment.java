package com.universeluvv.ktnuvoting.VoteItemFragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.universeluvv.ktnuvoting.AdapterPackage.WalletMakeDialog;
import com.universeluvv.ktnuvoting.BalanceOfToken;
import com.universeluvv.ktnuvoting.CreateWallet;
import com.universeluvv.ktnuvoting.GetTokenTask;
import com.universeluvv.ktnuvoting.InfoClass.VoteDetailInfo;
import com.universeluvv.ktnuvoting.KnutToken;
import com.universeluvv.ktnuvoting.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressVoteFragment extends Fragment {

    private    String id, major, address;

    TextView title, term, complete_tv;
    RadioButton radio[]=new RadioButton[6];
    Button sumbit , getToken;
    LinearLayout linearLayout;
    RadioGroup radioGroup;

    VoteDetailInfo vote;
    DatabaseReference ds;

    String candidate[] = new String[6]; // 투표 후보 주소

    public ProgressVoteFragment(String id, String major,String address, VoteDetailInfo vote) {
        this.id= id;
        this.major =major;
        this.vote =vote;
        this.address =address;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_progress_vote, container, false);
            Context context =container.getContext();

            title =view.findViewById(R.id.voteitem_progress_title_tv);
            term =view.findViewById(R.id.voteitem_progress_term_tv);
            complete_tv =view.findViewById(R.id.voteitem_complete_tv);
            radio[0]= view.findViewById(R.id.vote_radio0);
        radio[1]= view.findViewById(R.id.vote_radio1);
        radio[2]= view.findViewById(R.id.vote_radio2);
        radio[3]= view.findViewById(R.id.vote_radio3);
        radio[4]= view.findViewById(R.id.vote_radio4);
        radio[5] =view.findViewById(R.id.invaildvote_radio);
              sumbit =view.findViewById(R.id.voteitem_submit_btn);
            linearLayout =view.findViewById(R.id.itemshow_linear);
            radioGroup =view.findViewById(R.id.vote_ratiogroup);

         // 투표 후보 주소

        candidate[0] = "0x9BC9dA1Bbde104b38d902EA17BC087fe5C293A92";
        candidate[1] = "0xb08D52DceBf299A4F1330947612109C4A89097dE";
        candidate[2] = "0x73406dd65E679B979177CfD033deDC3312553527";
        candidate[3] = "0x4F13B97ff4e997Dd01589B8d305a8D66EBa584DA";
        candidate[4] = "0x0862c7722Babb384AC8B4D82d1d279f77Ce2309f";
        candidate[5] = "0x603Ca6aCccd810f89d5a88Ad3dF251e6C92173fD";
        getToken = view.findViewById(R.id.get_token_btn);



            completecheck();

           ds =FirebaseDatabase.getInstance().getReference();
            sumbit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(vote.getCoinexist()==1){
                        int k = 0;
                        for(int i=0; i<6; i++){
                            if(radio[i].isChecked()) {
                                try {
                                    GetTokenTask getTokenTask = new GetTokenTask(getActivity(),major,id,vote,false,i);
                                    getTokenTask.execute(candidate[i]);

                                }catch (Exception e){
                                }


                                k = i;

                                break;
                            }
                            if(i==5) Toast.makeText(getActivity(),"한가지 항목을 선택하여 주세요,",Toast.LENGTH_SHORT).show();
                        }
                        int finalK = k;

                    }else Toast.makeText(getActivity(),"투표권이 없거나 해당 투표자가 아닙니다.",Toast.LENGTH_SHORT).show();




                }   //onClick
            });

            // 투표권 얻기 버튼
        getToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vote.getParticipant()==0) Toast.makeText(getActivity(),"해당 투표가 가능한 투표자가 아닙니다.",Toast.LENGTH_SHORT).show();
                else if(vote.getCoinexist() == 0) {
                    WalletMakeDialog wdialog =new WalletMakeDialog(context,2, new WalletMakeDialog.WalletMakeDialogListener() {
                        @Override
                        public void Clickbtn(String pwd) {
                            CreateWallet createWallet = new CreateWallet();
                            String toAddr = createWallet.ImportWallet(pwd);

                            if (toAddr.equals("fail")){
                                Toast.makeText(getActivity(), "비밀번호가 틀렸습니다.",Toast.LENGTH_SHORT).show();
                            }
                            else {

                                try {
                                    GetTokenTask getTokenTask = new GetTokenTask(getActivity(),true,major,id,vote.getName());
                                    getTokenTask.execute(toAddr);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            vote.setCoinexist(1);
                                        }
                                    }).start();

                                    //vote.setCoinexist(1);
                                }catch (Exception e){

                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    wdialog.show();
                } else Toast.makeText(getActivity(),"투표권이 있습니다.",Toast.LENGTH_SHORT).show();


            } // onClick
        });
        return view;
    }


    public void completecheck(){

        title.setText(vote.getName());
        term.setText("투표 기한: "+vote.getTerm());

        if(vote.getCompletevotecheck()==1){
            getToken.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
            complete_tv.setVisibility(View.VISIBLE);
            sumbit.setVisibility(View.GONE);
        }else if(vote.getCompletevotecheck()==0){
            getToken.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            complete_tv.setVisibility(View.GONE);
            sumbit.setVisibility(View.VISIBLE);
        }


        int a=0; boolean t =false;
        for(int i=0; i < vote.getItem().size(); i++){
            radio[i].setVisibility(View.VISIBLE);
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

        for(int i=0; i < vote.getItem().size(); i++){
            radio[i].setText(vote.getItem().get(i));
        }

    }



}
