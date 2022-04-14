package com.universeluvv.ktnuvoting.InfoClass;

import java.util.ArrayList;

public class VoteDetailInfo {

    private String name;
    private String term;
    private int check;
    private String organizer;

    private int coinexist;

    private int completevotecheck ;
    private int participant ;
    private ArrayList<String> item ;
    private ArrayList<Integer> item_cnt ;
    private int current_cnt;
    private int  total_cnt;

    public  VoteDetailInfo(){

    }

    public VoteDetailInfo(int completevotecheck, int participant, ArrayList<String> item, ArrayList<Integer> item_cnt) {
        this.completevotecheck = completevotecheck;
        this.participant = participant;
        this.item = item;
        this.item_cnt = item_cnt;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }


    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public ArrayList<String> getItem() {
        return item;
    }

    public void setItem(ArrayList<String> item) {
        this.item = item;
    }


    public int getCurrent_cnt() {
        return current_cnt;
    }

    public void setCurrent_cnt(int current_cnt) {
        this.current_cnt = current_cnt;
    }

    public int getTotal_cnt() {
        return total_cnt;
    }

    public void setTotal_cnt(int total_cnt) {
        this.total_cnt = total_cnt;
    }

    public ArrayList<Integer> getItem_cnt() {
        return item_cnt;
    }

    public void setItem_cnt(ArrayList<Integer> item_cnt) {
        this.item_cnt = item_cnt;
    }


    public int getCompletevotecheck() {
        return completevotecheck;
    }

    public void setCompletevotecheck(int completevotecheck) {
        this.completevotecheck = completevotecheck;
    }

    public int getParticipant() {
        return participant;
    }

    public void setParticipant(int participant) {
        this.participant = participant;
    }

    public int getCoinexist() {
        return coinexist;
    }

    public void setCoinexist(int coinexist) {
        this.coinexist = coinexist;
    }
}
