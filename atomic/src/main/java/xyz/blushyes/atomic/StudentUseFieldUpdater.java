package xyz.blushyes.atomic;

public class StudentUseFieldUpdater {
    public volatile Float score;

    public StudentUseFieldUpdater(float score) {
        this.score = score;
    }
}