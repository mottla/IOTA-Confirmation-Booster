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
package theman.utils;

import java.util.ArrayList;
import theman.BalanceRecorder;
import theman.interfaces.IControler;

/**
 *
 * @author theman
 */
public class HashList extends ArrayList<BalanceRecorder> {

    private IControler con;

    public HashList(IControler con) {
        this.con = con;
    }

    public boolean addNew(String[] e) {
        BalanceRecorder b = new BalanceRecorder(e[0], e[1], "");
        con.addHashToSwing(b);
        return super.add(b);
    }

    @Override
    public boolean add(BalanceRecorder b) {
        con.addHashToSwing(b);
        return super.add(b);
    }
}
