package com.example.casclient3.MyModel;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apereo.cas.authentication.UsernamePasswordCredential;

import javax.validation.constraints.Size;

public class MyUsernamePasswordCredential extends UsernamePasswordCredential {

    @Size(min = 5,max = 5, message = "require capcha")
    private String capcha;

    public String getCapcha() {
        return capcha;
    }

    public MyUsernamePasswordCredential setCapcha(String capcha) {
        this.capcha = capcha;
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(this.capcha)
                .toHashCode();
    }
}
