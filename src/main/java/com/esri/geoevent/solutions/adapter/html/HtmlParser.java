package com.esri.geoevent.solutions.adapter.html;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.text.html.HTML;
import java.io.IOException;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;


public class HtmlParser  {

    public static void main(String[] args) throws IOException {
        HtmlParser p = new HtmlParser();
        String test = "horseName,popularity,date,place,placeName,weather,raceName,horseNo,famous,score,jockey,cycle,situation,time";
        List<String> fields = new ArrayList<>(Arrays.asList(test.split(",")));

        p.parseHTML(test, fields);
    }

    // Write codes below to determine how the HTML should be parsed
    String parseHTML(String html, List fileds) throws IOException {

        //実行時間計測用
        long start = System.currentTimeMillis();
        List<String> fields = fileds;

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode newJsonArrayNode = mapper.createArrayNode();

        try {
            // Get a document object from Html that is passed from the method's argument
            Document doc = Jsoup.connect("https://db.netkeiba.com/?pid=horse_top").get();

            // Define the base URL to which a query parameter will be appended
            String baseUrl = "https://db.netkeiba.com";
            // Extract the elements with "a" tag
            Elements links = doc.getElementsByTag("a");
            // Filter any element with href and its attribute starting with /horse
            // This will be used as a query parameter
            List<Element> urlParam = links.stream()
                    .filter(element -> element.attr("href").startsWith("/horse"))
                    .collect(Collectors.toList());

            for (int i = 0; i < urlParam.size(); i++) {
                // Append the query parameter to the base URL
                String newUrl = baseUrl + urlParam.get(i).attr("href");

                // Reassign the new URL to the doc variable
                doc = Jsoup.connect(newUrl).get();

                //Extract elements based on HTML tags
                Elements elm = doc.select("tbody tr");
                Elements title = doc.select("title");

                //馬名を表示させる準備
                //文字数を大まかにとって、そこから空白があったらそこまでで切り取る。
                String tstr = title.text().substring(0, 10);
                int j = tstr.indexOf(" ");

                if (j == -1) {
                    j = 10;
                }
                String tstrs = tstr.substring(0, j);

                //以下で使用するために文字列を初期化しておく
                String str=null;

                //拡張for文でElementsをTestBeansに格納
                for(Element a : elm) {
                    str = a.text();
                    //除外とか中止とか出走取消は対象外にしたいので
                    if(str.indexOf("除")!=-1 || str.indexOf("取")!=-1 ||str.indexOf("中")!=-1) {
                        continue;
                    }
                    //欲するのはレース情報のみだが、上記のタグの指定だけだと絞り切れなかったので、文字数で判別した。
                    if(str.length()>=70) {

                        String hairetsu[] = str.split(" ");
                        InfoBeans bean = new InfoBeans();
                        bean.setHorseName(urlParam.get(i).text());
                        bean.setPopularity(i + 1);
                        bean.setDate(hairetsu[0]);
                        bean.setPlace(hairetsu[1]);
                        bean.setPlaceName(hairetsu[1].substring(1, 3));
                        bean.setWeather(hairetsu[2]);
                        bean.setRaceName(hairetsu[4]);
                        bean.setHorseNo(Integer.parseInt(hairetsu[7]));
                        bean.setFamous(hairetsu[9]);
                        bean.setScore(hairetsu[10]);
                        bean.setJockey(hairetsu[11]);
                        bean.setCycle(hairetsu[13]);
                        bean.setSituation(hairetsu[14]);
                        bean.setTime(hairetsu[16]);

                        ObjectNode json = JsonConverter.toJsonObject(bean);
                        json.remove("allAsList");
                        newJsonArrayNode.add(json);
                    }
                }
            }

            //例外処理
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        //実行時間計測用
        long end = System.currentTimeMillis();
        System.out.println((end - start)  + "ms");
        System.out.println((end - start) / 1000 + "sec");

//        // Convert the list to Json objects
//        // ObjectMapper to convert an element in a list to a json object
//        ObjectMapper mapper = new ObjectMapper();
//        // ArrayNode to add multiple Json nodes as an array
//        ArrayNode newJsonArrayNode = mapper.createArrayNode();
//
//        for (int i = 0; i < list.size(); i++) {
//            for (int j = 0; j < fields.size(); j++) {
//                String json = mapper.writeValueAsString();
//                List<String> bean = list.get(j).getAllAsList();
//                newJsonNode.put(fields.get(j), bean.get(j));
//            }
//            newJsonArrayNode.add(newJsonNode);
//        }

        // Convert the Json Array to a String object
        String result = JsonConverter.toString(newJsonArrayNode);
        System.out.println(result);

        return result;
    }

}
