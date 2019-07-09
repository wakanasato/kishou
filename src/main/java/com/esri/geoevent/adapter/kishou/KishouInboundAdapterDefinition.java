package com.esri.geoevent.adapter.kishou;

import com.esri.ges.adapter.genericJson.JsonInboundAdapterDefinition;
import com.esri.ges.core.property.LabeledValue;
import com.esri.ges.core.property.PropertyDefinition;
import com.esri.ges.core.property.PropertyException;
import com.esri.ges.core.property.PropertyType;

import java.util.ArrayList;
import java.util.List;


public class KishouInboundAdapterDefinition extends JsonInboundAdapterDefinition {
    public KishouInboundAdapterDefinition() throws PropertyException {
        super();

        List<LabeledValue> regionValues = new ArrayList<>();
        regionValues.add(new LabeledValue("気象警報・注意報（市町村等）", "気象警報・注意報（市町村等）"));
        regionValues.add(new LabeledValue("気象警報・注意報（警報注意報種別毎）", "気象警報・注意報（警報注意報種別毎）"));
        regionValues.add(new LabeledValue("気象警報・注意報（府県予報区等）", "気象警報・注意報（府県予報区等）"));
        regionValues.add(new LabeledValue("気象警報・注意報（一次細分区域等）", "気象警報・注意報（一次細分区域等）"));
        regionValues.add(new LabeledValue("気象警報・注意報（市町村等をまとめた地域等）", "気象警報・注意報（市町村等をまとめた地域等）"));
        this.propertyDefinitions.put("region", new PropertyDefinition("region", PropertyType.String, "気象警報・注意報（市町村等）", "気象区域", "観測範囲", Boolean.valueOf(false), Boolean.valueOf(false), regionValues));

        List<LabeledValue> infoTypes = new ArrayList<>();
        infoTypes.add(new LabeledValue("気象警報・注意報", "気象警報・注意報"));
        infoTypes.add(new LabeledValue("気象特別警報・警報・注意報", "気象特別警報・警報・注意報"));
        infoTypes.add(new LabeledValue("地方気象情報", "地方気象情報"));
        infoTypes.add(new LabeledValue("指定河川洪水予報", "指定河川洪水予報"));
        this.propertyDefinitions.put("infoType", new PropertyDefinition("infoType", PropertyType.String, "気象警報・注意報", "気象情報種別", "気象情報種別", Boolean.valueOf(false), Boolean.valueOf(false), infoTypes));

//        PropertyDefinition charSetValue = null;
//        try {
//            charSetValue = new PropertyDefinition("charSetValue", PropertyType.String, "", "Charset of the html", "Specify the charset of the html page", false, false);
//            propertyDefinitions.put(charSetValue.getPropertyName(), charSetValue);
//            charSetValue = new PropertyDefinition("charSetValue", PropertyType.String, "", "Charset of the html", "Specify the charset of the html page", false, false);
//            propertyDefinitions.put(charSetValue.getPropertyName(), charSetValue);
//        } catch (PropertyException e) {
//            e.printStackTrace();
//        }

//            PropertyDefinition startDate = new PropertyDefinition("startDate", PropertyType.Integer, "", "Start Date", "Add the first date you would like to extract texts from. This will be appended to the base URL.", true, false);
//            propertyDefinitions.put(startDate.getPropertyName(), startDate);
//            PropertyDefinition endDate = new PropertyDefinition("endDate", PropertyType.Integer, "", "End Date", "Add the last date you would like to extract texts from. This will be appended to the base URL.", true, false);
//            propertyDefinitions.put(endDate.getPropertyName(), endDate);

    }

    @Override
    public String getName() {
        return "KishouXML-Adapter";
    }

    @Override
    public String getLabel() {
        return "Kishou XML Adapter";
    }

    @Override
    public String getDomain() {
        return "com.esri.geoevent.adapter";
    }

    @Override
    public String getDescription() {
        return "Extract data from Kishou XML";
    }
}