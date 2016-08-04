package personal.lingchen.demo.rxandroidtest.DouBan;

import java.io.Serializable;

/**
 * Created by ozner_67 on 2016/8/1.
 */
public class MovieEntity implements Serializable {
    private String start;
    private String count;
    private String total;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getSubjects() {
        return subjects;
    }

    public void setSubjects(Object subjects) {
        this.subjects = subjects;
    }

    private String title;
    private Object subjects;

    @Override
    public String toString() {
        return "start:" + start + "\ntitle:" + title + "\ntotal:" + total + "\ncount:" + count + "\nsubjects:" + subjects.toString();
    }
}
