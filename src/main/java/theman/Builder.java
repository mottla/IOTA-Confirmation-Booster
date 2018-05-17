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

import cfb.pearldiver.PearlDiverLocalPoW;
import jota.IotaLocalPoW;
import jota.pow.ICurl;
import jota.pow.SpongeFactory;

/**
 *
 * @author theman
 */
public class Builder {

    public ICurl customCurl;
    public String protocol, host, port;
    public IotaLocalPoW localPoW;

    public Builder(String protocol, String host, String port) {
        this.customCurl = SpongeFactory.create(SpongeFactory.Mode.KERL);
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.localPoW = new PearlDiverLocalPoW();
    }
    
    
}
