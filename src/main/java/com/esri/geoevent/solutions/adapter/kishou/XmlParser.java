package com.esri.geoevent.solutions.adapter.kishou;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class XmlParser {

//    public static void main(String[] args) throws IOException {
//        XmlParser p = new XmlParser();
////        XML エンドポイントから　Document オブジェクトを作成（parser メソッドで XML としてパース）
//        Document doc = Jsoup.connect("http://www.data.jma.go.jp/developer/xml/feed/extra.xml").parser(Parser.xmlParser()).get();
//        p.parseHTML(doc);
//    }

    String parseHTML(String xml) throws IOException {
        int errorCount = 0;
        //実行時間計測用
        long start = System.currentTimeMillis();

        Document doc = Jsoup.parse(xml);

        List<Element> linkElement;
        List<String> links = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode newJsonArrayNode = mapper.createArrayNode();

        try {
//            気象警報・注意報と XML リンクでフィルターして、アクセスする エンドポイントを抽出（実際はプロパティにして Switch 文を書くと思う）
            links = doc.getElementsByTag("entry").stream()
                    .filter(element -> element.getElementsByTag("title").text().equals("気象警報・注意報"))
                    .filter(element -> element.getElementsByTag("link").attr("type").equals("application/xml"))
                    .map(element -> element.getElementsByTag("link").attr("href"))
                    .collect(Collectors.toList());

////            link タグから link エレメントを取得
//            linkElement = doc.getElementsByTag("link");
//            for (int i = 0; i < linkElement.size(); i++) {
////                type の要素が application/xml の場合のみ link エレメントから href を取得して、link リストに格納
//                if (linkElement.get(i).attr("type").equals("application/xml")){
//                    links.add(linkElement.get(i).attr("href"));
//                }
//            }


            for (int i = 0; i < links.size(); i++) {
                doc = Jsoup.connect(links.get(i)).get();
                Elements infoTypes = doc.getElementsByTag("Information");

                for (int j = 0; j < infoTypes.size(); j++) {

                    switch (infoTypes.get(j).attr("type")) {

                        case "気象警報・注意報（市町村等）":
                            List<Element> xmlObjects = doc.getElementsByTag("item");
                            List<Elements> area = xmlObjects.stream()
                                    .map(element -> element.getElementsByTag("Area"))
                                    .collect(Collectors.toList());
                            for (int k = 0; k < area.size(); k++) {
                                InfoBeans bean = new InfoBeans();
                                bean.setAreaName(area.get(k).select("Name").text());
                                try {
                                    bean.setAreaCode(Integer.valueOf(area.get(k).select("Code").text()));
                                } catch (NumberFormatException e) {
                                    System.out.println(area.get(k).select("Code").text() + " は整数じゃないよ");
                                }
                                ObjectNode json = JsonConverter.toJsonObject(bean);
                                newJsonArrayNode.add(json);
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

        String result = newJsonArrayNode.toString();
        System.out.println(result);
        return result;
    }

    ;
}
