/*
 * Copyright 2017 Mottl.
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author Mottl
 */
public class BalanceRecorder {

    private final String hash;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
    
    @Override
    public boolean equals(Object o){
        if(!(o instanceof BalanceRecorder)){
            return false;
        }
        BalanceRecorder other = (BalanceRecorder) o;
        return hash.equals(other.getHash());
    }

    public String getHash() {
        return hash;
    }

    public int getconfirmatioDuration() {
        return confirmatioDuration;
    }

    public String getDisplayname() {
        return displayname;
    }
    private final String address;
    private int confirmatioDuration = 0;
    //private final int securitylevel;
    private final int alertWhenChangedByValueOf;
    private long balance;
    private final String displayname;
    private final String creationTime;
    private String confirmeTime;
    

    public BalanceRecorder(String displayname, String hash, String address) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        this.address = address;
        this.alertWhenChangedByValueOf = 0;
        this.hash = hash;
        this.displayname = displayname;
        this.creationTime = sdf.format(cal.getTime());
    }

    public BalanceRecorder(String displayname, String hash, String address, int alertWhenChangedByValueOf) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        this.address = address;
        //this.securitylevel = securitylevel;
        this.alertWhenChangedByValueOf = alertWhenChangedByValueOf;
        //this.alertWhenBalanceOutsideIntervall = new long[]{0L, 2779530283277761L};
        //this.alertWhenEmpty = true;
        this.hash = hash;
        this.displayname = displayname;
        this.creationTime = sdf.format(cal.getTime());
    }

    public void setValidated() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        this.confirmeTime = sdf.format(cal.getTime());
    }

//    public BalanceRecorder(String address, int securitylevel, int alertWhenChangedByValueOf, long[] alertWhenBalanceOutsideIntervall, boolean alertWhenEmpty) {
//        this.address = address;
//        this.securitylevel = securitylevel;
//        this.alertWhenChangedByValueOf = alertWhenChangedByValueOf;
//        this.alertWhenBalanceOutsideIntervall = alertWhenBalanceOutsideIntervall;
//        this.alertWhenEmpty = alertWhenEmpty;
//    }
    public String getAddress() {
        return address;
    }

    public int getAlertWhenChangedByValueOf() {
        return alertWhenChangedByValueOf;
    }

    public long getBalance() {
        return balance;
    }

    void setBalance(long parseLong) {
        this.balance=parseLong;
    }

}
