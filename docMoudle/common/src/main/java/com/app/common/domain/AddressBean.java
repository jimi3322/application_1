package com.app.common.domain;

import java.util.List;

public class AddressBean extends SelectInfo {
    private List<AddressBean> children;

    public List<AddressBean> getChildren() {
        return children;
    }

    public void setChildren(List<AddressBean> children) {
        this.children = children;
    }

}
