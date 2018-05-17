/*
 * Copyright 2017 theman.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package theman;

import jota.model.Bundle;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author theman
 */
public class Publish {

    private String log = null;
    private String infoLog = null;
    private BalanceRecorder balanceRecorder = null;
    private String balance = null;
    private Bundle bundle =null;

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public String getLog() {
        return log;
    }

    public Publish setLog(String log) {
        this.log = log;
        return this;
    }

    public String getInfoLog() {
        return infoLog;
    }

    public Publish setInfoLog(String infoLog) {
        this.infoLog = infoLog;
        return this;
    }

    public BalanceRecorder getBalanceRecorder() {
        return balanceRecorder;
    }

    public Publish setBalanceRecorder(BalanceRecorder b) {
        this.balanceRecorder = b;
        return this;
    }

    public String getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public Publish setBalance(String balance) {
        this.balance = balance;
        return this;
    }

}
