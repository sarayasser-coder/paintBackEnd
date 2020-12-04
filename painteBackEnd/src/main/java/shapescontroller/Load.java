package shapescontroller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@CrossOrigin(origins = "http://localhost:8080")
public class Load {

    @CrossOrigin(origins = "http://localhost:8080")
    public FileReturn XMLReader(String path,String name) {
       
        	if(name.contains("/")||
        			name.contains("\\")
        			||name.contains(":")||
        			name.contains("*")||
        			name.contains("?")||
        			name.contains("<")||
        			name.contains(">")||name.contains("|")||
        			name.contains("/")){
        		return null;
        		
        	}

        String xmlFilePath = path + "\\" + name + ".xml";

        try{
            File file = new File(xmlFilePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            //Load the input XML document , parse it and return an instance of the document class
            Document document = builder.parse(file);
            NodeList nodelist = document.getDocumentElement().getChildNodes();
            int shapesNum = 0;
            int stepsNum = 0;

            for (int i = 0;i<nodelist.getLength();i++){
                Node node = nodelist.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    if (((Element) node).hasAttribute("id")) {
                        shapesNum++;
                    } else if (((Element) node).hasAttribute("step")) {
                        stepsNum++;
                    }
                }
            }
            Object[][] shapes = new Object[shapesNum][9];
            Object[] steps = new String[stepsNum];
            for (int i = 0;i<nodelist.getLength();i++) {
                Node node = nodelist.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) node;
                    if (((Element) node).hasAttribute("id")) {
                        //Get the value of ID attribute
                        int ID = Integer.parseInt(node.getAttributes().getNamedItem("id").getNodeValue());
                        shapes[i][0] = ID;
                        //Get the value of all sub-elements
                        float x = Float.parseFloat(elem.getElementsByTagName("x").item(0).getChildNodes().item(0).getNodeValue());
                        shapes[i][1] = x;
                        float y = Float.parseFloat(elem.getElementsByTagName("y").item(0).getChildNodes().item(0).getNodeValue());
                        shapes[i][2] = y;
                        float x1 = Float.parseFloat(elem.getElementsByTagName("x1").item(0).getChildNodes().item(0).getNodeValue());
                        shapes[i][3] = x1;
                        float y1 = Float.parseFloat(elem.getElementsByTagName("y1").item(0).getChildNodes().item(0).getNodeValue());
                        shapes[i][4] = y1;
                        String color = elem.getElementsByTagName("color").item(0).getChildNodes().item(0).getNodeValue();
                        shapes[i][5] = color;
                        String LineThickness = elem.getElementsByTagName("LineThickness").item(0).getChildNodes().item(0).getNodeValue();
                        shapes[i][6] = LineThickness;
                        boolean filled = Boolean.parseBoolean(elem.getElementsByTagName("filled").item(0).getChildNodes().item(0).getNodeValue());
                        shapes[i][7] = filled;
                        String shapeType = elem.getElementsByTagName("shapeType").item(0).getChildNodes().item(0).getNodeValue();
                        shapes[i][8] = shapeType;

                    } else if (((Element) node).hasAttribute("step")) {
                       Object step = node.getAttributes().getNamedItem("step").getNodeValue();
                        steps[i - shapesNum ] = step;
                    }
                }
            }
            return new FileReturn(shapes,steps);

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    @CrossOrigin(origins = "http://localhost:8080")
    public FileReturn JSONReader(String path,String name) throws IOException, ParseException {
    	if(name.contains("/")||
    			name.contains("\\")
    			||name.contains(":")||
    			name.contains("*")||
    			name.contains("?")||
    			name.contains("<")||
    			name.contains(">")||name.contains("|")||
    			name.contains("/")){
    		return null;
    		
    	}
        String jsonFilePath = path +"\\"+name+".json";
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader(jsonFilePath);
        JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
        JSONArray jsonShapes = (JSONArray)jsonObject.get("shapes");
        JSONArray jsonSteps = (JSONArray)jsonObject.get("steps");
        Object [][] shapes = new Object[jsonShapes.size()][9];
        Object[] steps = new Object[jsonSteps.size()];
        for (int i = 0;i<jsonShapes.size();i++){
            JSONObject object = new JSONObject();
            object = (JSONObject) jsonShapes.get(i);
            shapes[i][0] = object.get("id");
            shapes[i][1] = object.get("x");
            shapes[i][2] = object.get("y");
            shapes[i][3] = object.get("x1");
            shapes[i][4] = object.get("y1");
            shapes[i][5] = object.get("color");
            shapes[i][6] = object.get("LineThickness");
            shapes[i][7] = object.get("filled");
            shapes[i][8] = object.get("shapeType");
        }
        for (int i = 0;i<jsonSteps.size();i++){
            JSONObject object = new JSONObject();
            object = (JSONObject) jsonSteps.get(i);
            steps[i] = object.get("step"+i);

        }
        return new FileReturn(shapes,steps);
    }
}
