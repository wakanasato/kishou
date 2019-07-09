package com.esri.geoevent.adapter.kishou;

import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class XmlParser {
//  テスト用のメインメソッド
    public static void main(String[] args) throws IOException {
//        System.out.println(System.getProperty("file.encoding"));
        XmlParser p = new XmlParser();
//        XML エンドポイントから　Document オブジェクトを作成（parser メソッドで XML としてパース）
        Document doc = Jsoup.connect("http://www.data.jma.go.jp/developer/xml/feed/extra.xml").parser(Parser.xmlParser()).get();
        String xml = doc.toString();
        p.parseHTML(xml);
    }

    List<JsonNode> parseHTML(String xml) {
////        System.out.println(System.getProperty("file.encoding"));
//        String url = "http://www.data.jma.go.jp/developer/xml/feed/extra.xml";
////        Jsoup が使う文字コードは実行環境依存みたいなので、ここでちゃんと UTF-8 を使うように指定する (pom に文字コードを記述すれば大丈夫だった)
//        Document doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);

        InfoBeans bean = new InfoBeans();
        List<JsonNode> jsonNodes = new ArrayList<>();

        try {
//            トランスポートから渡されてデコードされた XML 文字列を Document オブジェクトにパースする
            Document doc = Jsoup.parse(xml);
//            アクセスする複数のリンクを抽出（他の情報用にも使えるようにメソッド化）
//            第二引数は取得したい情報の種類をユーザーに選ばせるようにしたいので、プロパティ化する予定
            List<String> links = getLinks(doc, "気象特別警報・警報・注意報" /*KishouInboundAdapter.region*/);

//            抜き出したそれぞれのリンクにアクセスして情報を抜き取っていく
            for (int i = 0; i < links.size(); i++) {
                doc = Jsoup.connect(links.get(i)).get();

//                Information type の値毎に取れる値が違うので、ループを回す
//                実際は Information type をプロパティ化して、どの粒度で情報を取得するかユーザーに選ばせるようにすると思う。
                Elements infoTypes = doc.getElementsByTag("Information");
                for (int j = 0; j < infoTypes.size(); j++) {
                    switch (infoTypes.get(j).attr("type") /*KishouInboundAdapter.region*/) {
                        case "気象警報・注意報（市町村等）":
//                            警報の種類でデータを抜き出す（他の Information type でも使えるようにメソッド化）
                            Elements items = getItems(doc, "気象警報・注意報（市町村等）" /*KishouInboundAdapter.region*/);
//                            item タグを一つの XML オブジェクト考えて、ループ処理
//                            List<Element> items = keihouType.getElementsByTag("item");
                            for (int k = 0; k < items.size(); k++) {
//                                Area タグから regionname と regioncode を抜き出す
                                Elements area = items.get(k).getElementsByTag("Area");
//                                Kind タグから注意報/警報情報を抜き出す
                                Elements kind = items.get(k).getElementsByTag("Kind");
//                                値をセットしていく（他の Information type でも使えるようにメソッド化）
                                setValue(bean, area, kind);
//                                セットした値を Json にして、Json 配列に追加していく
                                JsonNode json = JsonConverter.toJsonObject(bean);
                                jsonNodes.add(json);
                            }
                        case "気象警報・注意報（警報注意報種別毎）":
                        case "気象警報・注意報（府県予報区等）":
                        case "気象警報・注意報（一次細分区域等）":
                        case "気象警報・注意報（市町村等をまとめた地域等）":
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
//        Json オブジェクトの配列を、Adapter に返す


//        String result = jsonNodes.toString();
        System.out.println("取得したレコード数は " + jsonNodes.size() + " 件です");
        return jsonNodes;
    }

    private List<String> getLinks(Document doc, String type) {
        return doc.getElementsByTag("entry").stream()
                .filter(element -> element.getElementsByTag("title").text().equals(type))
                .filter(element -> element.getElementsByTag("link").attr("type").equals("application/xml"))
                .map(element -> element.getElementsByTag("link").attr("href"))
                .collect(Collectors.toList());
    }

    private Elements getItems(Document doc, String type) {
        return doc.getElementsByTag("Information").stream()
                .filter(infoType -> infoType.attr("type").equals(type))
                .map(infoType -> infoType.getElementsByTag("item"))
                .collect(Collectors.toList()).get(0);
    }

    private void setValue(InfoBeans bean, Elements area, Elements kind) {

        area.stream().map(element -> element.getElementsByTag("Name"))
                .forEach(element -> bean.setAreaName(element.text()));
        area.stream().map(element -> element.getElementsByTag("Code"))
                .forEach(element -> bean.setRegioncode(element.text()));

//        各注意報/警報からコードを抽出してセット(正規表現でやっているが、実際は注意報/警報コードでやる)
        for (int i = 0; i < kind.size(); i++) {
            String kindName = kind.get(i).getElementsByTag("Name").text();
            int kindCode = Integer.parseInt(kind.get(i).getElementsByTag("Code").text());
            if(kindName.matches("雷.*")){
                bean.setLightningCode(kindCode);
            } else if(kindName.matches("大雨.*")){
                bean.setHeavyrainCode(kindCode);
            } else if(kindName.matches("洪水.*")){
                bean.setFloodCode(kindCode);
            } else if(kindName.matches("強風.*")){
                bean.setStrongwingCode(kindCode);
            } else if(kindName.matches("濃霧.*")){
                bean.setFlogCode(kindCode);
            } else if (kindName.matches("解除")) {
                bean.setLightningCode(00);
                bean.setHeavyrainCode(00);
                bean.setStrongwingCode(00);
                bean.setFloodCode(00);
                bean.setFlogCode(00);
            } else {
                KishouInboundAdapter.log.error(kind.get(i).getElementsByTag("Name").text() + " is not valid");
            }
        }
    }

//    文字コードデバッグ用の関数
//    public static int getByteLength(String string, Charset charset) {
//        return string.getBytes(charset).length;
//    }
}