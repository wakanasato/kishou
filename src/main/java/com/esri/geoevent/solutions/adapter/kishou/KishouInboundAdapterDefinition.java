package com.esri.geoevent.solutions.adapter.kishou;

import com.esri.ges.adapter.genericJson.JsonInboundAdapterDefinition;
import com.esri.ges.core.property.PropertyException;


public class KishouInboundAdapterDefinition extends JsonInboundAdapterDefinition {
    public KishouInboundAdapterDefinition() throws PropertyException {
        super();

//        PropertyDefinition fields = new PropertyDefinition("fields", PropertyType.String, "", "Field name(s)", "Add comma-separated field names here", true, false);
//        propertyDefinitions.put(fields.getPropertyName(), fields);

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