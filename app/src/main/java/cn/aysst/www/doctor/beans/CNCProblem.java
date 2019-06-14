package cn.aysst.www.doctor.beans;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class CNCProblem implements Parcelable {

    public static final int SEARCH_TYPE_BY_ID = 0;
    public static final int SEARCH_TYPE_BY_QUES = 1;

    private String brand;
    private String type;
    private String question;
    private String questype;
    private String solution;
    private String time;
    private double percentage;
    private String searchType;
    private String id;
    private String solutionDetail;
    private String idDetail;
    private String questionDetail;
    private String typeDetail;
    private String url;



    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getBrand() {
        return brand;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
    public String getQuestion() {
        return question;
    }

    public void setQuestype(String questype) {
        this.questype = questype;
    }
    public String getQuestype() {
        return questype;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
    public String getSolution() {
        return solution;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getTime() {
        return time;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
    public double getPercentage() {
        return percentage;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }
    public String getSearchType() {
        return searchType;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setIdDetail(String idDetail) {
        this.idDetail = idDetail;
    }
    public String getIdDetail() {
        return idDetail;
    }

    public void setQuestionDetail(String questionDetail) {
        this.questionDetail = questionDetail;
    }
    public String getQuestionDetail() {
        return questionDetail;
    }

    public void setSolutionDetail(String solutionDetail) {
        this.solutionDetail = solutionDetail;
    }
    public String getSolutionDetail() {
        return solutionDetail;
    }

    public void setTypeDetail(String typeDetail) {
        this.typeDetail = typeDetail;
    }
    public String getTypeDetail() {
        return typeDetail;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "brand: " + brand + "\n" +
                "type: " + type + "\n" +
                "question: " + question + "\n" +
                "questype: " + questype + "\n" +
                "solution: " + solution + "\n" +
                "time: " + time + "\n" +
                "percentage: " + percentage + "\n" +
                "searchType: " + searchType + "\n" +
                "id: " + id + "\n" +
                "solutionDetail: " + solutionDetail + "\n" +
                "idDetail: " + idDetail + "\n" +
                "questionDetail: " + questionDetail + "\n" +
                "typeDetail: " + typeDetail + "\n" +
                "url: " + url;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CNCProblem){
            CNCProblem cnc = (CNCProblem) obj;
            if (cnc.solution.equals(solution)) {
                Log.d("重复","return true " + cnc.question);
                return true;
            }
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(brand);
        dest.writeString(type);
        dest.writeString(searchType);
        dest.writeString(solution);
        dest.writeString(time);
        dest.writeDouble(percentage);
        dest.writeString(questype);
        dest.writeString(id);
        dest.writeString(question);
        dest.writeString(questionDetail);
        dest.writeString(solutionDetail);
        dest.writeString(typeDetail);
        dest.writeString(idDetail);
        dest.writeString(url);
    }

    public static final Parcelable.Creator<CNCProblem> CREATOR = new Parcelable.Creator<CNCProblem>(){
        @Override
        public CNCProblem createFromParcel(Parcel source) {
            CNCProblem cnc = new CNCProblem();
            cnc.setBrand(source.readString());
            cnc.setType(source.readString());
            cnc.setSearchType(source.readString());
            cnc.setSolution(source.readString());
            cnc.setTime(source.readString());
            cnc.setPercentage(source.readDouble());
            cnc.setQuestype(source.readString());
            cnc.setId(source.readString());
            cnc.setQuestion(source.readString());
            cnc.setIdDetail(source.readString());
            cnc.setQuestionDetail(source.readString());
            cnc.setSolutionDetail(source.readString());
            cnc.setTypeDetail(source.readString());
            cnc.setUrl(source.readString());
            return cnc;
        }

        @Override
        public CNCProblem[] newArray(int size) {
            return new CNCProblem[size];
        }
    };
}
