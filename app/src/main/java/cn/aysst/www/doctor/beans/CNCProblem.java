package cn.aysst.www.doctor.beans;

public class CNCProblem {

    private String brand;
    private String type;
    private String question;
    private String questype;
    private String solution;

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


}
