package com.esri.geoevent.adapter.kishou;

import com.esri.ges.adapter.AdapterDefinitionBase;
import com.esri.ges.adapter.AdapterType;
import com.esri.ges.connector.Connector;
import com.esri.ges.connector.Connector.ConnectorType;
import com.esri.ges.connector.ConnectorProperty;
import com.esri.ges.core.AccessType;
import com.esri.ges.core.ConfigurationException;
import com.esri.ges.core.Uri;
import com.esri.ges.core.geoevent.*;
import com.esri.ges.core.property.LabeledValue;
import com.esri.ges.core.property.PropertyDefinition;
import com.esri.ges.core.property.PropertyException;
import com.esri.ges.core.property.PropertyType;

import java.util.ArrayList;
import java.util.List;


public class KishouInboundAdapterDefinition extends AdapterDefinitionBase {
    private ArrayList<Connector> connectors = new ArrayList<Connector>();

    public KishouInboundAdapterDefinition() throws PropertyException {
        super(AdapterType.INBOUND);
        try {
//        GeoEvent Manager で設定するプロパティの定義
            List<LabeledValue> regionValues = new ArrayList<>();
            regionValues.add(new LabeledValue("気象警報・注意報（市町村等）", "気象警報・注意報（市町村等）"));
            regionValues.add(new LabeledValue("気象警報・注意報（警報注意報種別毎）", "気象警報・注意報（警報注意報種別毎）"));
            regionValues.add(new LabeledValue("気象警報・注意報（府県予報区等）", "気象警報・注意報（府県予報区等）"));
            regionValues.add(new LabeledValue("気象警報・注意報（一次細分区域等）", "気象警報・注意報（一次細分区域等）"));
            regionValues.add(new LabeledValue("気象警報・注意報（市町村等をまとめた地域等）", "気象警報・注意報（市町村等をまとめた地域等）"));
            this.propertyDefinitions.put("region", new PropertyDefinition("region", PropertyType.String, "気象警報・注意報（市町村等）",
                    "気象区域", "観測範囲", Boolean.valueOf(false), Boolean.valueOf(false), regionValues));

            List<LabeledValue> infoTypes = new ArrayList<>();
            infoTypes.add(new LabeledValue("気象警報・注意報", "気象警報・注意報"));
            infoTypes.add(new LabeledValue("気象特別警報・警報・注意報", "気象特別警報・警報・注意報"));
            infoTypes.add(new LabeledValue("地方気象情報", "地方気象情報"));
            infoTypes.add(new LabeledValue("指定河川洪水予報", "指定河川洪水予報"));
            this.propertyDefinitions.put("infoType", new PropertyDefinition("infoType", PropertyType.String, "気象警報・注意報",
                    "気象情報種別", "気象情報種別", Boolean.valueOf(false), Boolean.valueOf(false), infoTypes));

//          ジオイベント定義の作成
            GeoEventDefinition gd = new DefaultGeoEventDefinition();
            gd.setName("KishouXML-Input");
            gd.setAccessType(AccessType.editable);

            // Now we create the list of top-level fields and populate it.
            List<FieldDefinition> topLevelFields = new ArrayList<FieldDefinition>();
            topLevelFields.add(new DefaultFieldDefinition("region_name", FieldType.String));
            topLevelFields.add(new DefaultFieldDefinition("region_code", FieldType.String, "TRACK_ID"));
            topLevelFields.add(new DefaultFieldDefinition("status", FieldType.String));
            topLevelFields.add(new DefaultFieldDefinition("type_lightning", FieldType.String));
            topLevelFields.add(new DefaultFieldDefinition("type_heavyRain", FieldType.String));
            topLevelFields.add(new DefaultFieldDefinition("type_heavySnow", FieldType.String));
            topLevelFields.add(new DefaultFieldDefinition("type_snowStorm", FieldType.String));
            topLevelFields.add(new DefaultFieldDefinition("type_blizzard", FieldType.String));
            topLevelFields.add(new DefaultFieldDefinition("type_flood", FieldType.String));
            topLevelFields.add(new DefaultFieldDefinition("type_strongWind", FieldType.String));
            topLevelFields.add(new DefaultFieldDefinition("type_storm", FieldType.String));
            topLevelFields.add(new DefaultFieldDefinition("type_lowTemp", FieldType.String));
            topLevelFields.add(new DefaultFieldDefinition("type_tidal", FieldType.String));
            topLevelFields.add(new DefaultFieldDefinition("type_wave", FieldType.String));
            topLevelFields.add(new DefaultFieldDefinition("warnings-list", FieldType.String));

            gd.setFieldDefinitions(topLevelFields);
            geoEventDefinitions.put(gd.getName(), gd);

//          作成するコネクターの定義
            Uri adapterUri = new Uri();
            adapterUri.setDomain(getDomain());
            adapterUri.setName(getName());
            adapterUri.setVersion(getVersion());
            Uri transportUri = new Uri();
            transportUri.setDomain("com.esri.ges.transport.inbound");
            transportUri.setName("HTTP");
            transportUri.setVersion(getVersion());

            Connector newConnector = new Connector("kishou-xml-in", "kishou-xml-in", ConnectorType.inbound,
                    adapterUri, transportUri, "気象XMLコネクター", "気象XMLからデータを取得するコネクターです",
                    AccessType.editable);

            ConnectorProperty url = new ConnectorProperty(ConnectorProperty.Source.transport, "clientURL", "http://www.data.jma.go.jp/developer/xml/feed/extra.xml", "URL");
            ConnectorProperty frequency = new ConnectorProperty(ConnectorProperty.Source.transport, "frequency", "30", "頻度 (秒)");
            ConnectorProperty region = new ConnectorProperty(ConnectorProperty.Source.adapter, "region", "気象警報・注意報（市町村等）", "地域");
            ConnectorProperty infoType = new ConnectorProperty(ConnectorProperty.Source.adapter, "infoType", "気象特別警報・警報・注意報", "報種");


            newConnector.addShownProperty(region);
            newConnector.addShownProperty(infoType);
            newConnector.addShownProperty(url);
            newConnector.addShownProperty(frequency);

            connectors.add(newConnector);


        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Connector> getConnectors() {
        return connectors;
    }

    @Override
    public String getName() {
        return "KishouXMLAdapter";
    }

    @Override
    public String getLabel() {
        return "気象XML アダプター";
    }

    @Override
    public String getDomain() {
        return "com.esri.geoevent.adapter.inbound";
    }

    @Override
    public String getDescription() {
        return "気象XML エンドポイントからデータを取得します。";
    }

    @Override
    public String getVersion() {
        return "10.6.1";
    }
}
