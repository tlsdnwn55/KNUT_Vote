package com.universeluvv.ktnuvoting.InfoClass;

public class VoteItemInfo {

    private String name;
    private  int total_cnt, current_cnt;

    public VoteItemInfo(String name, int total_cnt, int current_cnt) {
        this.name = name;
        this.total_cnt = total_cnt;
        this.current_cnt = current_cnt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotal_cnt() {
        return total_cnt;
    }

    public void setTotal_cnt(int total_cnt) {
        this.total_cnt = total_cnt;
    }

    public int getCurrent_cnt() {
        return current_cnt;
    }

    public void setCurrent_cnt(int current_cnt) {
        this.current_cnt = current_cnt;
    }
}
