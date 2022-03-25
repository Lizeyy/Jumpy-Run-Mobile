package com.game;

public class RankList {
    private String nick;
    private int points;

    public RankList(String nick, int points) {
        this.nick = nick;
        this.points = points;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
