package cn.aysst.www.doctor.beans;

import android.util.Log;

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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CNCProblem){
            CNCProblem cnc = (CNCProblem) obj;
            if (cnc.brand.equals(brand) && cnc.type.equals(type) &&
                    cnc.question.equals(question) && cnc.solution.equals(solution) && cnc.questype.equals(questype)) {
                Log.d("重复","return true " + cnc.question);
                return true;
            }
        }
        return false;
    }

}
