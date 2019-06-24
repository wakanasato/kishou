/*
 | Copyright 2014 Esri
 |
 | Licensed under the Apache License, Version 2.0 (the "License");
 | you may not use this file except in compliance with the License.
 | You may obtain a copy of the License at
 |
 |    http://www.apache.org/licenses/LICENSE-2.0
 |
 | Unless required by applicable law or agreed to in writing, software
 | distributed under the License is distributed on an "AS IS" BASIS,
 | WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 | See the License for the specific language governing permissions and
 | limitations under the License.
 */
package com.esri.geoevent.solutions.adapter.kishou;

import com.esri.ges.adapter.AdapterDefinition;
import com.esri.ges.adapter.genericJson.JsonInboundAdapter;
import com.esri.ges.core.component.ComponentException;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class KishouInboundAdapter extends JsonInboundAdapter {
    static final private Log log = LogFactory.getLog(KishouInboundAdapter.class);
    private String charSetValue;
    static List<String> fields = new ArrayList<>();

    public KishouInboundAdapter(AdapterDefinition adapterDefinition) throws ComponentException {
        super(adapterDefinition);
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        String rawFields = properties.get("fields").getValueAsString();
        fields = Arrays.asList(rawFields.split(","));
        charSetValue = properties.get("charSetValue;").getValueAsString();
    }

    private Charset otherCharSet = Charset.forName("euc-jp");
    private Charset UTF8 = Charset.forName("UTF8");

    @Override
    public void receive(ByteBuffer buf, String str) {
        // Here write codes which would be neccessary for GeoEvent data processing
        // Do not write any codes here about how the content of HTML should be parsed
        ByteBuffer bb = buf;
        CharSequence charseq = decode(buf);
        String charseqToString = charseq.toString();

        try {
            // URL to be used for this sample "https://db.netkeiba.com/?pid=horse_top"
            // Pass the HTML string to the method which covers how to parse the HTML
            XmlParser hp = new XmlParser();
            // Get the result from HtmlParser in List
            String result = hp.parseHTML(charseqToString);

            // Encode the string and pass it onto the super class (Generic Json Adapter)
            bb = encode(result);
            super.receive(bb, str);

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private CharSequence decode(ByteBuffer buf) {
        // Decode a byte buffer into a CharBuffer
        CharBuffer isodcb = null;
        CharsetDecoder isodecoder = otherCharSet.newDecoder();
        try {
            isodcb = isodecoder.decode(buf);
        } catch (CharacterCodingException e) {
            log.error(e);
        }
        return (CharSequence) isodcb;
    }

    private ByteBuffer encode(String str) {
        // Encode a string into a byte buffer
        ByteBuffer bb = null;
        CharsetEncoder isoencoder = UTF8.newEncoder();
        try {
            bb = isoencoder.encode(CharBuffer.wrap(str));
        } catch (CharacterCodingException e) {
            log.error(e);
        }
        return bb;
    }
}