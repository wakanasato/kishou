package com.esri.geoevent.adapter.kishou;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class XmlParser {
//  テスト用のメインメソッド
    public static void main(String[] args) throws IOException {
//        System.out.println(System.getProperty("file.encoding"));
        XmlParser p = new XmlParser();
//        XML エンドポイントから　Document オブジェクトを作成（parser メソッドで XML としてパース）
//        HTTP通信エラーキャッチ
        Document doc = Jsoup.connect("http://www.data.jma.go.jp/developer/xml/feed/extra.xml").parser(Parser.xmlParser()).get();
        String xml = doc.toString();
        p.parseXML(xml);
    }

    //    トランスポートからの XML オブジェクトを受けて、Json オブジェクトを返すメソッド
    List<JsonNode> parseXML(String xml) {
////        System.out.println(System.getProperty("file.encoding"));
//        String url = "http://www.data.jma.go.jp/developer/xml/feed/extra.xml";
////        Jsoup が使う文字コードは実行環境依存みたいなので、ここでちゃんと UTF-8 を使うように指定する (pom に文字コードを記述すれば大丈夫だった)
//        Document doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);

        List<JsonNode> jsonNodes = new ArrayList<>();

        try {
//            トランスポートから渡されてデコードされた XML 文字列を Document オブジェクトとしてパースする
            Document initialDoc = null;

            initialDoc = Jsoup.parse(xml);

//            アクセスするリンクを抽出（他の情報用にも使えるようにメソッド化）
//            infoType は GeoEvent Manager で設定した情報種の値
            List<String> links = getLinks(initialDoc, "気象特別警報・警報・注意報");
            if (links.size() > 0) {
                //            抽出したそれぞれのリンクにアクセスして情報を抜き取っていく
                for (String link : links) {
                    Document exDoc = Jsoup.connect(link).get();

//                Information type の値毎に取れる値が違うので、ループを回す
//                infoTypes.size() でNull判定
                    Elements infoTypes = exDoc.getElementsByTag("Information");
                    if (infoTypes.size() > 0) {
                        for (Element infoType : infoTypes) {
                            switch (infoType.attr("type") /*KishouInboundAdapter.region*/) {
                                case "気象警報・注意報（市町村等）":
//                            警報の種類でデータを抜き出す（他の Information type でも使えるようにメソッド化）
                                    Elements items = getItems(exDoc, "気象警報・注意報（市町村等）");
//                            item タグを一つの XML オブジェクト考えて、ループ処理
//                            List<Element> items = keihouType.getElementsByTag("item");
                                    if (items.size() > 0) {
                                        for (Element item : items) {
//                                Area タグから regionname と regioncode を抜き出す
                                            Elements area = item.getElementsByTag("Area");
//                                Kind タグから注意報/警報情報を抜き出す
                                            Elements kind = item.getElementsByTag("Kind");
//                                値をセットしていく（他の Information type でも使えるようにメソッド化）
                                            InfoBeans bean = new InfoBeans();
                                            setValue(bean, area, kind);
//                                セットした値を Json にして、Json 配列に追加していく
                                            JsonNode json = JsonConverter.toJsonObject(bean);
                                            jsonNodes.add(json);
                                        }
                                    } else {
                                        throw new IllegalArgumentException("Could not get any item from the item tag");
                                    }
                                case "気象警報・注意報（警報注意報種別毎）":
                                case "気象警報・注意報（府県予報区等）":
                                case "気象警報・注意報（一次細分区域等）":
                                case "気象警報・注意報（市町村等をまとめた地域等）":
                            }
                        }
                    } else {
                        throw new IllegalArgumentException("Could not get any info from the information tag");
                    }
                }
            } else {
                throw new IllegalArgumentException("Could not get any link");
            }
        } catch (JsonProcessingException | NullPointerException e) {
            KishouInboundAdapter.log.error("The input XML value(s) is null or not formatted correctly");
            KishouInboundAdapter.log.error(e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            KishouInboundAdapter.log.error("The input XML is not valid or not supported");
            KishouInboundAdapter.log.error(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            KishouInboundAdapter.log.error(e.getMessage());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            KishouInboundAdapter.log.error(e.getMessage());
            e.printStackTrace();
        }
//        String result = jsonNodes.toString();
        System.out.println("取得したレコード数は " + jsonNodes.size() + " 件です");
        //        Json オブジェクトのrリストを、KishouInboundAdapter に返す
        try {
            return jsonNodes;
        } catch (NullPointerException e) {
            KishouInboundAdapter.log.error("Could not retrieve any data", e);
            e.printStackTrace();
            return null;
        }
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

    private void setValue(InfoBeans bean, Elements area, Elements kind) throws UnsupportedOperationException, IllegalAccessException {
//        各注意報/警報からコードを抽出してセット
        for (int i = 0; i < kind.size(); i++) {
            String kindName = kind.get(i).getElementsByTag("Name").text();
            int kindCode = Integer.parseInt(kind.get(i).getElementsByTag("Code").text());

            switch (kindCode) {
                case 2:
                    bean.setType_snowStorm("暴風雪警報");
                    bean.setAlertFlag(true);
                    break;
                case 3:
                    bean.setType_heavyRain("大雨警報");
                    bean.setAlertFlag(true);
                    break;
                case 4:
                    bean.setType_flood("洪水警報");
                    bean.setAlertFlag(true);
                    break;
                case 5:
                    bean.setType_storm("暴風警報");
                    bean.setAlertFlag(true);
                    break;
                case 6:
                    bean.setType_wave("波浪警報");
                    bean.setAlertFlag(true);
                    break;
                case 7:
                    bean.setType_heavySnow("大雪警報");
                    bean.setAlertFlag(true);
                    break;
                case 8:
                    bean.setType_tidal("高潮警報");
                    bean.setAlertFlag(true);
                    break;
                case 10:
                    bean.setType_heavyRain("大雨注意報");
                    bean.setWarningFlag(true);
                    break;
                case 12:
                    bean.setType_heavySnow("大雪注意報");
                    bean.setWarningFlag(true);
                    break;
                case 13:
                    bean.setType_blizzard("風雪注意報");
                    bean.setWarningFlag(true);
                    break;
                case 14:
                    bean.setType_lightning("雷注意報");
                    bean.setWarningFlag(true);
                    break;
                case 15:
                    bean.setType_strongWind("強風注意報");
                    bean.setWarningFlag(true);
                    break;
                case 16:
                    bean.setType_wave("波浪注意報");
                    bean.setWarningFlag(true);
                    break;
                case 18:
                    bean.setType_flood("洪水注意報");
                    bean.setWarningFlag(true);
                    break;
                case 19:
                    bean.setType_tidal("高潮注意報");
                    bean.setWarningFlag(true);
                    break;
                case 23:
                    bean.setType_lowTemp("低温注意報");
                    bean.setWarningFlag(true);
                    break;
                case 32:
                    bean.setType_blizzard("暴風雪特別警報");
                    bean.setSpAlertFlag(true);
                    break;
                case 33:
                    bean.setType_flood("洪水特別警報");
                    bean.setSpAlertFlag(true);
                    break;
                case 35:
                    bean.setType_storm("暴風特別警報");
                    bean.setSpAlertFlag(true);
                    break;
                case 36:
                    bean.setType_heavySnow("大雪特別警報");
                    bean.setSpAlertFlag(true);
                    break;
                case 37:
                    bean.setType_wave("波浪特別警報");
                    bean.setSpAlertFlag(true);
                    break;
                case 38:
                    bean.setType_tidal("高潮特別警報");
                    bean.setSpAlertFlag(true);
                    break;
                case 0:
//                    次バージョンでは前の状態をキャッシュするようにして、値があったもの（Null じゃないもの）に対して解除を入れるようにする
//                    現状は 0 （解除）だった場合は全て Null （何も値をセットしない）
                    break;
                default:
                    KishouInboundAdapter.log.info("Found " + kindName + " code:" + kindCode + " but out of the scope");
            }
            //        area タグから Areaname をセット
            area.stream().map(element -> element.getElementsByTag("Name"))
                    .forEach(element -> bean.setRegion_name(element.text()));
//        area タグから Regioncode をセット
            area.stream().map(element -> element.getElementsByTag("Code"))
                    .forEach(element -> bean.setRegion_code(element.text()));
        }
    }

//    文字コードデバッグ用の関数
//    public static int getByteLength(String string, Charset charset) {
//        return string.getBytes(charset).length;
//    }
}
