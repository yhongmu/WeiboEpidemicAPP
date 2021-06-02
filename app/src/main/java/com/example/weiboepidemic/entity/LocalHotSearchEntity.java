package com.example.weiboepidemic.entity;

public class LocalHotSearchEntity implements Comparable<LocalHotSearchEntity> {
    public String searchKey;
    public String realURL;
    public String createTime;
    public int year;
    public int month;
    public int day;

    @Override
    public int compareTo(LocalHotSearchEntity o) {
        if (this.year > o.year) {
            return -1;
        } else if (this.year < o.year) {
            return 1;
        } else {
            if (this.month > o.month) {
                return -1;
            } else if (this.month < o.month) {
                return 1;
            } else if (this.day == o.day) {
                return -1;
            } else return Integer.compare(o.day, this.day);
        }
    }
}
