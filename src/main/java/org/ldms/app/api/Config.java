package org.ldms.app.api;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api.general")
public class Config {
    private Integer interestPrecision;

    public Integer getInterestPrecision() {
        return interestPrecision;
    }

    public void setInterestPrecision(Integer interestPrecision) {
        this.interestPrecision = interestPrecision;
    }
}