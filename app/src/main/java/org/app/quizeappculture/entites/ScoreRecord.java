package org.app.quizeappculture.entites;

import java.util.Date;

public class ScoreRecord {
    private String userId;
    private int score;
    private long durationInSeconds;
    private Date timestamp;


    public ScoreRecord(){}

    public ScoreRecord(String userId, int score, long durationInSeconds, Date timestamp) {
        this.userId = userId;
        this.score = score;
        this.durationInSeconds = durationInSeconds;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(long durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
