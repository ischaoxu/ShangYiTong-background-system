package com.atguigu.syt.enums;

public enum HospitalStatusEnum {

    DISABLED(0, "未上线"),
    ENABLED(1, "已上线"),
    ;

    private Integer status;
    private String name;

    HospitalStatusEnum(Integer status, String name) {
        this.status = status;
        this.name = name;
    }

    public static String getStatusNameByStatus(Integer status) {
        HospitalStatusEnum arrObj[] = HospitalStatusEnum.values();
        for (HospitalStatusEnum obj : arrObj) {
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
