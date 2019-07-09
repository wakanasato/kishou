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
import com.esri.ges.adapter.genericJson.JsonInboundAdapter;
import com.esri.ges.core.component.ComponentException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;


public class KishouInboundAdapter extends JsonInboundAdapter {
    static final Log log = LogFactory.getLog(KishouInboundAdapter.class);
    static String region;
    static String infoType;

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

    @Override
    public void receive(ByteBuffer buf, String str) {
//        トランスポートからバイトバッファを受け取り、デコード
        ByteBuffer bb = buf;
        CharSequence charseq = decode(buf);
        String charseqToString = charseq.toString();

        try {
//            デコードしたデータを XMLParser に投げる
            XmlParser hp = new XmlParser();
//            パースされたデータを受け取って、エンコードして、ベースクラスのメソッドでジオイベントに変換
            String result = hp.parseHTML(charseqToString);
            bb = encode(result);
            super.receive(bb, str);

        } catch (IOException e) {
            e.printStackTrace();
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