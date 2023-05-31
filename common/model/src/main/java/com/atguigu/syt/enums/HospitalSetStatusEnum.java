package com.atguigu.syt.enums;

public enum HospitalSetStatusEnum {

    DISABLED(0, "禁用"),
    ENABLED(1, "可用"),
    ;

    private Integer status;
    private String name;

    HospitalSetStatusEnum(Integer status, String name) {
        this.status = status;
        this.name = name;
    }

    public static String getStatusNameByStatus(Integer status) {
        HospitalSetStatusEnum arrObj[] = HospitalSetStatusEnum.values();
        for (HospitalSetStatusEnum obj : arrObj) {
            if (status.intValue() == obj.getStatus().intValue()) {
                return obj.getName();
            }
        }
        return "";
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
