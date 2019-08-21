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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class KishouInboundAdapter extends InboundAdapterBase {
    static final Log log = LogFactory.getLog(KishouInboundAdapter.class);
    static String region;
    static String infoType;
    private Charset UTF8 = Charset.forName("UTF8");
    //    private Charset otherCharSet = Charset.forName("euc-jp");

    public KishouInboundAdapter(AdapterDefinition adapterDefinition) throws ComponentException {
        super(adapterDefinition);
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        region = getProperty("region").getValueAsString();
        infoType = getProperty("infoType").getValueAsString();
    }

    //    トランスポートからのメッセージを受け取り、後続処理にメッセージを渡すためのメソッド
    @Override
    public void receive(ByteBuffer buf, String channelId) {
//        トランスポートからバイトバッファを受け取り、デコード
        CharSequence charseq = decode(buf);
        String charseqToString = charseq.toString();

//        デコードしたデータを XMLParser に投げてパース
        List<JsonNode> jsonNodes = new XmlParser().parseXML(charseqToString);

//        parsXML メソッドの戻り値は JsonNode のリストのため、ループ処理で単体の JSON を GeoEvent オブジェクトに変換して、receive メソッドに渡すようにする
        for (JsonNode json : jsonNodes) {
            GeoEvent geoevent = convertToGeoEvent(json);
            geoEventListener.receive(geoevent);
        }
    }

    //   JsonNode オブジェクトを GeoEvent オブジェクトに変換するためのメソッド
    public GeoEvent convertToGeoEvent(JsonNode json) {
        GeoEvent geoevent;
        try {
//            adapter-definition.xml で定義した GeoEvent 定義を元に GeoEvent オブジェクトを作成
//            ジオイベント定義が見つからなかった場合のハンドリング
            geoevent = geoEventCreator.create(((AdapterDefinition) definition).getGeoEventDefinition("Kishou-XML").getGuid());
//            GeoEvent オブジェクトに引数で渡された JSON をセットしていく

            StringBuilder warnigs = new StringBuilder();
            Iterator<Map.Entry<String, JsonNode>> jsonIt = json.fields();
            while (jsonIt.hasNext()) {
                Map.Entry<String, JsonNode> jsonElement = jsonIt.next();
                switch (jsonElement.getKey()) {
                    case "areaName":
                        geoevent.setField("areaName", (jsonElement.getValue().textValue()));
                        break;
                    case "regioncode":
                        geoevent.setField("regioncode", jsonElement.getValue().textValue());
                        break;
                    case "spAlertFlag":
                        if (jsonElement.getValue().asBoolean()) {
                            geoevent.setField("status", "特別警報あり");
                        }
                        break;
                    case "alertFlag":
                        if (jsonElement.getValue().asBoolean()) {
                            geoevent.setField("status", "警報あり");
                        }
                        break;
                    case "warningFlag":
                        if (jsonElement.getValue().asBoolean()) {
                            geoevent.setField("status", "注意報あり");
                        }
                        break;
                    default:
                        if (!jsonElement.getValue().isNull())
                            warnigs.append(jsonElement.getValue().textValue() + ", ");
                }
            }
            geoevent.setField("warnings", warnigs.toString());

            return geoevent;
        } catch (MessagingException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return null;
        } catch (FieldException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    //    継承してるからオーバーライドが必須になっているが、実際は使わないメソッド
    @Override
    protected GeoEvent adapt(ByteBuffer byteBuffer, String s) {
        return null;
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
