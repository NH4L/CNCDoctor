package cn.aysst.www.doctor.beans;

public class myBean {
    protected String title;
    protected String cate;
    protected String brand;

    public myBean(String title,String cate,String brand){
        this.brand=brand;
        this.cate=cate;
        this.title=title;
    }

    public String getBrand() {
        return brand;
    }

    public String getCate() {
        return cate;
    }

    public String getTitle() {
        return title;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
