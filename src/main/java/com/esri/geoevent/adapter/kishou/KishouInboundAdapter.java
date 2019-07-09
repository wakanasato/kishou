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
package com.esri.geoevent.adapter.kishou;

import com.esri.ges.adapter.AdapterDefinition;
import com.esri.ges.adapter.InboundAdapterBase;
import com.esri.ges.core.component.ComponentException;
import com.esri.ges.core.geoevent.FieldException;
import com.esri.ges.core.geoevent.GeoEvent;
import com.esri.ges.messaging.MessagingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class KishouInboundAdapter extends InboundAdapterBase {
    static final Log log = LogFactory.getLog(KishouInboundAdapter.class);
    static String region;
    static String infoType;
    ObjectMapper mapper = new ObjectMapper();

    public KishouInboundAdapter(AdapterDefinition adapterDefinition) throws ComponentException {
        super(adapterDefinition);
        region = getProperty("region").getValueAsString();
        infoType = getProperty("infoType").getValueAsString();
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
    }
//    バイトバッファで受信したイベントをデコード/エンコードする際の文字列を指定
//    private Charset otherCharSet = Charset.forName("euc-jp");
    private Charset UTF8 = Charset.forName("UTF8");

    public void receive(ByteBuffer buf, String channelId) {
        // トランスポートからバイトバッファを受け取り、デコード
        ByteBuffer bb = buf;
        CharSequence charseq = decode(bb);
        String charseqToString = charseq.toString();

        // デコードしたデータを XMLParser に投げる
        XmlParser hp = new XmlParser();
        // パースされたデータを受け取って、エンコードして、ベースクラスのメソッドでジオイベントに変換
        List<JsonNode> jsonNodes = hp.parseHTML(charseqToString);

        for (JsonNode json : jsonNodes) {
            bb = encode(json.toString());
            GeoEvent geoevent = adapt(bb, channelId);
            geoEventListener.receive(geoevent);
        }
    }

    @Override
    public GeoEvent adapt(ByteBuffer buf, String channelId) {
        GeoEvent geoevent;
//        トランスポートからバイトバッファを受け取り、デコード
        CharSequence charseq = decode(buf);
        String charseqToString = charseq.toString();

        try {
            geoevent = geoEventCreator.create(((AdapterDefinition) definition).getGeoEventDefinition("Kishou-XML").getGuid());
            JsonNode json = mapper.readTree(charseqToString);
            AtomicInteger index = new AtomicInteger();
            for (JsonNode element : json) {
                if (element.isTextual()){
                    geoevent.setField(index.getAndIncrement(), element.textValue());
                } else if (element.isInt()){
                    geoevent.setField(index.getAndIncrement(), element.intValue());
                } else {
                    throw new FieldException(element + " is invalid value");
                }
            };

            return geoevent;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        } catch (FieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    private CharSequence decode(ByteBuffer buf) {
        // Decode a byte buffer into a CharBuffer
        CharBuffer isodcb = null;
        CharsetDecoder isodecoder = UTF8.newDecoder();
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