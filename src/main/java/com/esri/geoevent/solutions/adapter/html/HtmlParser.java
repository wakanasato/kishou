package com.esri.geoevent.solutions.adapter.html;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.text.html.HTML;
import java.io.IOException;

import java.util.*;
import java.util.stream.Collectors;


public class HtmlParser  {

    // Write codes below to specify what data you want from the html

    List<InfoBeans> list = new ArrayList<InfoBeans>();

    List<InfoBeans> parseHTML(String Html) throws IOException {

        //実行時間計測用
        long start = System.currentTimeMillis();

        try {
                // 引数の String オブジェクトを HTML オブジェクトにパース
                Document doc = Jsoup.parse(Html);

                //Elements B = A.select("タグ"); この形でソースに含まれるタグで指定された範囲を書き出す
                Elements elm = doc.select("tbody tr");
                Elements title = doc.select("title");

                //馬名を表示させる準備
                //文字数を大まかにとって、そこから空白があったらそこまでで切り取る。
                String tstr = title.text().substring(0, 10);
                int i = tstr.indexOf(" ");

                if(i==-1) {
                    i=10;
                }
                String tstrs = tstr.substring(0, i);

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
                        bean.setDate(hairetsu[0]);
                        bean.setPlace(hairetsu[1]);
                        bean.setWeather(hairetsu[2]);
                        bean.setRaceName(hairetsu[4]);
                        bean.setHorseNo(hairetsu[7]);
                        bean.setFamous(hairetsu[9]);
                        bean.setScore(hairetsu[10]);
                        bean.setJockey(hairetsu[11]);
                        bean.setCycle(hairetsu[13]);
                        bean.setSituation(hairetsu[14]);
                        bean.setTime(hairetsu[16]);

                        //listに格納
                        list.add(bean);

                    }
            }

            //例外処理
        } catch(NumberFormatException e) {
            e.printStackTrace();
        }
        //実行時間計測用
        long end = System.currentTimeMillis();
        System.out.println((end - start)  + "ms");
        System.out.println((end-start)/1000 + "秒");

        return list;
    }
}
