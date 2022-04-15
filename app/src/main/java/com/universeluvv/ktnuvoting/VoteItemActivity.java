package com.universeluvv.ktnuvoting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.universeluvv.ktnuvoting.InfoClass.StudentInfo;
import com.universeluvv.ktnuvoting.InfoClass.VoteDetailInfo;
import com.universeluvv.ktnuvoting.VoteItemFragment.EndVoteFragment;
import com.universeluvv.ktnuvoting.VoteItemFragment.ProgressVoteFragment;

import java.util.ArrayList;

public class VoteItemActivity extends AppCompatActivity {

    String  votename;
    StudentInfo info =new StudentInfo();

    ArrayList<String> item_name = new ArrayList<String>();
    ArrayList<Integer> item_cnt = new ArrayList<Integer>();
    int votechecking;
    int voteauto;

    VoteDetailInfo detailInfo;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    DatabaseReference ds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_item);

        Intent intent =getIntent();
        info.setId(intent.getStringExtra("userid"));
        info.setMajor(intent.getStringExtra("major"));
        votename =intent.getStringExtra("votename");


        ds=FirebaseDatabase.getInstance().getReference();
        votedataset();

        Toolbar mytoolbar = findViewById(R.id.vote_item_toolbar);
        setSupportActionBar(mytoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
    }


    //드로우레이아웃 회원 정보를 위한 버튼이벤트

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.voteitem_appbar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home : finish(); break;
            case R.id.vote_delete_btn :
                if(detailInfo.getOrganizer().equals(info.getName())){
                    ds=FirebaseDatabase.getInstance().getReference().child("vote");
                    ds.child(votename).removeValue();
                }else Toast.makeText(VoteItemActivity.this,"해당 투표의 진행자가 아닙니다.",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void votedataset(){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction =fragmentManager.beginTransaction();

        DatabaseReference drawerdb = FirebaseDatabase.getInstance().getReference();
        drawerdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot votedata =dataSnapshot.child("vote").child(votename);
                DataSnapshot studentdata=dataSnapshot.child("student").child(info.getMajor()).child(info.getId());

                for(DataSnapshot ds: votedata.child("item").getChildren() ){
                    item_name.add(ds.getKey());
                    item_cnt.add(ds.getValue(Integer.TYPE));
                }

                if(studentdata.child("votechecking").child(votename).exists()) votechecking=1;
                else  votechecking =0;


                if(votedata.child("participant").child(info.getMajor()).getValue(Integer.TYPE).equals(1)) voteauto =1;
                else voteauto =0;

                detailInfo =new VoteDetailInfo(votechecking,voteauto,item_name,item_cnt);
                detailInfo.setOrganizer(votedata.child("organizer").getValue().toString());
                detailInfo.setName(votename);
                detailInfo.setCurrent_cnt(votedata.child("current_cnt").getValue(Integer.TYPE));
                detailInfo.setTotal_cnt(votedata.child("total_cnt").getValue(Integer.TYPE));
                detailInfo.setTerm(votedata.child("term").getValue().toString());

                if(studentdata.child("coin").child(votename).exists()) {
                    detailInfo.setCoinexist(1);
                }else detailInfo.setCoinexist(0);


                 String address = studentdata.child("walletaddress").getValue().toString();

                if(votedata.child("check").getValue(Integer.TYPE).equals(1)) {
                    detailInfo.setCheck(1);
                    fragmentTransaction.replace(R.id.vote_item_fragment,new ProgressVoteFragment(info.getId(), info.getMajor(),address,detailInfo)).commit();
                }
                else if(votedata.child("check").getValue(Integer.TYPE).equals(2)){
                    detailInfo.setCheck(2);
                    fragmentTransaction.replace(R.id.vote_item_fragment,new EndVoteFragment(detailInfo)).commit();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}

