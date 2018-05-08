package json.parser;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import java.util.Arrays;

import json.Json;
import json.domain.JsonNode;
import json.domain.JsonTree;

import org.junit.Test;

public class ParserTest {
    
    private Json json = new Json();
    
    @Test
    public void parse1() throws Exception {
        JsonTree jsonTree = json.getFromFile("/json/json-1.json");
        assertTrue(jsonTree.getIntegerList("nums").containsAll(Arrays.asList(new Integer[]{1,2,3,4,5})));
        assertEquals(5, jsonTree.getInt("total"));
    }
    
    @Test
    public void parse2() throws Exception {
        JsonTree jsonTree = json.getFromFile("/json/json-2.json");
        JsonNode.NodeList nodes = jsonTree.getNodeList("objs");
        assertEquals("item1", nodes.getNode(0).getKey());
        assertEquals(1, nodes.getNode(0).getInt("item1"));
        assertEquals("item2", nodes.getNode(1).getKey());
        assertEquals(2, nodes.getNode(1).getInt("item2"));
        assertEquals(2, jsonTree.getInt("total"));
    }
    
    @Test
    public void parse3() throws Exception {
        JsonTree jsonTree = json.getFromFile("/json/json-3.json");
        assertEquals(1, jsonTree.getNodeList("nestedlist").getNodeList(0).getNodeList(0).getInt(0));
        assertEquals("item", jsonTree.getNodeList("nestedlist").getNodeList(0).getNodeList(0).getNode(1).getKey());
        assertEquals(1, jsonTree.getNodeList("nestedlist").getNodeList(0).getNodeList(0).getNode(1).getInt("item"));
    }
    
    @Test
    public void parse5() throws Exception {
        JsonTree jsonTree = json.getFromFile("/json/json-5.json");
        assertTrue(jsonTree.containsKey("root"));
        assertTrue(jsonTree.getNode("root").containsKey("child1"));
        assertTrue(jsonTree.getNode("root").getNode("child1").containsKey("child2"));
        assertEquals(2,jsonTree.getNode("root").getNode("child1").getInt("child2"));
    }
    
    @Test
    public void parse6() throws Exception {
        JsonTree jsonTree = json.getFromFile("/json/json-6.json");
        assertTrue(jsonTree.containsKey("root"));
        assertTrue(jsonTree.getNode("root").containsKey("child1"));
        assertTrue(jsonTree.getNode("root").getNode("child1").containsKey("child2"));
        JsonNode.NodeList nodes = jsonTree.getNode("root").getNode("child1").getNode("child2").getNodeList();
        assertEquals(1, nodes.getInt(0));
        assertEquals(2, nodes.getInt(1));
        assertEquals(3, nodes.getInt(2));
    }
    
    @Test
    public void parse7() throws Exception {
        JsonTree jsonTree = json.getFromFile("/json/json-7.json");
        
        assertTrue(jsonTree.containsKeys("_id","index","guid","isActive","balance","picture","age","eyeColor","name","gender","company","email","phone",
            "address","about","registered","latitude","longitude","tags","friends","greeting","favoriteFruit"));
        assertEquals("5aeda580af2eab6bed03f069",jsonTree.getString("_id"));
        assertEquals(0,jsonTree.getInt("index"));
        assertEquals("4862706a-be3c-49d6-b9b9-d4f351ff17e3",jsonTree.getString("guid"));
        assertEquals((Boolean)true, jsonTree.getBoolean("isActive"));
        assertEquals("$2,565.67",jsonTree.getString("balance"));
        assertEquals("http://placehold.it/32x32",jsonTree.getString("picture"));
        assertEquals(39,jsonTree.getInt("age"));
        assertEquals("green",jsonTree.getString("eyeColor"));
        assertEquals("Raymond Leblanc",jsonTree.getString("name"));
        assertEquals("male",jsonTree.getString("gender"));
        assertEquals("AMTAP",jsonTree.getString("company"));
        assertEquals("raymondleblanc@amtap.com",jsonTree.getString("email"));
        assertEquals("+1 (909) 535-2438",jsonTree.getString("phone"));
        assertEquals("546 Fleet Walk, Hegins, Alabama, 4440",jsonTree.getString("address"));
        assertEquals("Ex eu qui est eiusmod aute officia eiusmod fugiat et sit quis ipsum pariatur. Incididunt adipisicing sunt cupidatat reprehenderit sint adipisicing elit adipisicing eiusmod aute laborum. Velit deserunt reprehenderit irure nisi sit eiusmod adipisicing reprehenderit Lorem.",
            jsonTree.getString("about"));
        assertEquals("2017-05-21T07:02:54 -01:00",jsonTree.getString("registered"));
        assertEquals(-40.151031,jsonTree.getDouble("latitude"));
        assertEquals(1.240083,jsonTree.getDouble("longitude"));
        
        assertEquals("sit",jsonTree.getStringList("tags").get(0));
        assertEquals("enim",jsonTree.getStringList("tags").get(1));
        assertEquals("consequat",jsonTree.getStringList("tags").get(2));
        assertEquals("voluptate",jsonTree.getStringList("tags").get(3));
        assertEquals("ex",jsonTree.getStringList("tags").get(4));
        assertEquals("ut",jsonTree.getStringList("tags").get(5));
        assertEquals("ullamco",jsonTree.getStringList("tags").get(6));
        
        assertTrue(jsonTree.getNodeList("friends").getNode(0).containsKey("id"));
        assertTrue(jsonTree.getNodeList("friends").getNode(0).containsKey("name"));
        assertEquals(2, jsonTree.getNodeList("friends").getNode(0).getTotalKeys());
        assertEquals(0, jsonTree.getNodeList("friends").getNode(0).getInt("id"));
        assertEquals("Sykes Bird", jsonTree.getNodeList("friends").getNode(0).getString("name"));
        assertTrue(jsonTree.getNodeList("friends").getNode(1).containsKey("id"));
        assertTrue(jsonTree.getNodeList("friends").getNode(1).containsKey("name"));
        assertEquals(1, jsonTree.getNodeList("friends").getNode(1).getInt("id"));
        assertEquals("Barnett Wilkins", jsonTree.getNodeList("friends").getNode(1).getString("name"));
        assertTrue(jsonTree.getNodeList("friends").getNode(2).containsKey("id"));
        assertTrue(jsonTree.getNodeList("friends").getNode(2).containsKey("name"));
        assertEquals(2, jsonTree.getNodeList("friends").getNode(2).getInt("id"));
        assertEquals("Erin Mendoza", jsonTree.getNodeList("friends").getNode(2).getString("name"));
        
        assertEquals("Hello, Raymond Leblanc! You have 2 unread messages.",jsonTree.getString("greeting"));
        assertEquals("strawberry",jsonTree.getString("favoriteFruit"));
    }
    
    @Test
    public void parse8() {
        JsonTree jsonTree = json.getFromFile("/json/json-8.json");
        JsonNode root = jsonTree.getRoot();
        assertEquals(7, root.getNodeList().list.size());
        assertEquals("5aeda580af2eab6bed03f069", root.getNodeList().getNode(0).getString("_id"));
        assertEquals("5aeda580a27c23f9a06de32f", root.getNodeList().getNode(1).getString("_id"));
        assertEquals("5aeda580232f47e6b8d4737d", root.getNodeList().getNode(2).getString("_id"));
        assertEquals("5aeda580516311eb01fb19f9", root.getNodeList().getNode(3).getString("_id"));
        assertEquals("5aeda5803e5dcb1064a81c4d", root.getNodeList().getNode(4).getString("_id"));
        assertEquals("5aeda58072785318ece00831", root.getNodeList().getNode(5).getString("_id"));
        assertEquals("5aeda5806875e4243be3fc9e", root.getNodeList().getNode(6).getString("_id"));
    }
    
}
