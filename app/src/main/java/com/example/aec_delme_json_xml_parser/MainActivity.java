package com.example.aec_delme_json_xml_parser;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.Chip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    Chip json_btn,xml_btn;
    TextView dataField;
    ListView ListDataField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializer();
        xml_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xml_btn.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.checked)));
                json_btn.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.white)));
                Toast.makeText(getApplicationContext(),"XML",Toast.LENGTH_LONG).show();
                try {
                    perseXML();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        json_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xml_btn.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.white)));
                json_btn.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.checked)));
                Toast.makeText(getApplicationContext(),"JSON",Toast.LENGTH_LONG).show();
                try {
                    parseJSON();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    private void parseJSON() throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("city.json");
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            String jsonString = new String(bytes);
            JSONArray jsonArray = new JSONArray(jsonString);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("JSON DATA");
            stringBuilder.append("\n--------------------");
            if (jsonArray.length()>0){
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject city = jsonArray.getJSONObject(i);
                    stringBuilder.append("\nCITY_NAME: "+city.getString("city"));
                    stringBuilder.append("\nCITY_TEMP: "+city.getString("temp"));
                    stringBuilder.append("\nCITY_LAT: "+city.getString("lat"));
                    stringBuilder.append("\nCITY_LONGI: "+city.getString("longi"));
                    stringBuilder.append("\n--------------------");
                }
            }else {
                stringBuilder.append("\nTHERE IS NO JSON DATA");
            }
            dataField.setText(stringBuilder.toString());

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                assert inputStream != null;
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void perseXML() throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("city.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            document.normalize();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("XML DATA");
            stringBuilder.append("\n--------------------");
            NodeList nodeList = document.getElementsByTagName("city");
//            Toast.makeText(getApplicationContext(),String.valueOf(nodeList.getLength()),Toast.LENGTH_LONG).show();
            if (nodeList.getLength()>0){
                for(int i=0;i<nodeList.getLength();i++){
                    Node node = nodeList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE){
                        Element element = (Element) node;
                        stringBuilder.append("\ncity name:"+element.getElementsByTagName("name").item(0).getTextContent()+"\n");
                        stringBuilder.append("\ncity temp:"+element.getElementsByTagName("temp").item(0).getTextContent()+"\n");
                        stringBuilder.append("\ncity latitude:"+element.getElementsByTagName("latitude").item(0).getTextContent()+"\n");
                        stringBuilder.append("\ncity longitude:"+element.getElementsByTagName("longitude").item(0).getTextContent()+"\n");
                        stringBuilder.append("\n-------------------");
                    }
                }
                dataField.setText(stringBuilder.toString());
            }else {
                stringBuilder.append("\nTHERE NO XML DATA");
                dataField.setText(stringBuilder.toString());
            }

        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                assert inputStream != null;
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void initializer(){
        json_btn = findViewById(R.id.json_btn);
        xml_btn = findViewById(R.id.xml_btn);
        dataField = findViewById(R.id.dataField);
        ListDataField = findViewById(R.id.ListDataField);
    }
}