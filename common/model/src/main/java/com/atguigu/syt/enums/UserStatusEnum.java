package com.atguigu.syt.enums;

public enum UserStatusEnum {

    LOCK(0, "锁定"),
    NORMAL(1, "正常"),
    ;
    private Integer status;
    private String name;

    UserStatusEnum(Integer status, String name) {
        this.status = status;
        this.name = name;
    }

    public static String getStatusNameByStatus(Integer status) {
        UserStatusEnum arrObj[] = UserStatusEnum.values();
        for (UserStatusEnum obj : arrObj) {
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
