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
package com.esri.geoevent.solutions.adapter.html;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.esri.ges.adapter.genericJson.JsonInboundAdapter;
import com.esri.ges.adapter.AdapterDefinition;
import com.esri.ges.core.component.ComponentException;


public class HtmlInboundAdapter extends JsonInboundAdapter {
    static final private Log log = LogFactory.getLog(HtmlInboundAdapter.class);
    private String charSetValue;

    public HtmlInboundAdapter(AdapterDefinition adapterDefinition) throws ComponentException {
        super(adapterDefinition);
    }

    @Override
    public void afterPropertiesSet() {
//        charSetValue = properties.get("charSetValue;").getValueAsString();
        super.afterPropertiesSet();
    }

    private Charset otherCharSet = Charset.forName("euc-jp");
    private Charset UTF8 = Charset.forName("UTF8");

    @Override
    public void receive(ByteBuffer buf, String str) {
        ByteBuffer bb = buf;
        CharSequence charseq = decode(buf);
        String charseqToString = charseq.toString();

        List<InfoBeans> result = new ArrayList<>();

        try {
            HtmlParser hp = new HtmlParser();
            result = hp.parseHTML(charseqToString);

            ObjectMapper mapper = new ObjectMapper();
            ArrayNode newArrayNode = mapper.createArrayNode();

            List<String> values = result.stream().flatMap(bean -> bean.getAllAsList().stream()).collect(Collectors.toList());

            for (int i = 0; i < result.size(); i++) {
                ObjectNode newItemNode = mapper.createObjectNode();

                newItemNode.put("date", values.get(i++));
                newArrayNode.add(newItemNode);
                newItemNode.put("place", values.get(i++));
                newArrayNode.add(newItemNode);
                newItemNode.put("weather", values.get(i++));
                newArrayNode.add(newItemNode);
                newItemNode.put("raceName", values.get(i++));
                newArrayNode.add(newItemNode);
                newItemNode.put("horseNo", values.get(i++));
                newArrayNode.add(newItemNode);
                newItemNode.put("famous", values.get(i++));
                newArrayNode.add(newItemNode);
                newItemNode.put("score", values.get(i++));
                newArrayNode.add(newItemNode);
                newItemNode.put("jockey", values.get(i++));
                newArrayNode.add(newItemNode);
                newItemNode.put("cycle", values.get(i++));
                newArrayNode.add(newItemNode);
                newItemNode.put("situation", values.get(i++));
                newArrayNode.add(newItemNode);
                newItemNode.put("time", values.get(i));
                newArrayNode.add(newItemNode);
            }

            String newJsonString = JsonConverter.toString(newArrayNode);

            bb = encode(newJsonString);
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