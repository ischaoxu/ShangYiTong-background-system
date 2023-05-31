package com.atguigu.syt.enums;

public enum DictTypeEnum {


    HOSTYPE(1L, "医院等级"),
    CERTIFICATES_TYPE(2L, "证件类型"),
    ;

    private Long dictTypeId;
    private String name;

    DictTypeEnum(Long dictTypeId, String name) {
        this.dictTypeId = dictTypeId;
        this.name = name;
    }

    public Long getDictTypeId() {
        return dictTypeId;
    }

    public void setDictTypeId(Long dictTypeId) {
        this.dictTypeId = dictTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
