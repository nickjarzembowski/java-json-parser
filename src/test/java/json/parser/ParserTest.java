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
        JsonTree jsonTree = json.get("/json/json-1.json");
        assertTrue(jsonTree.getRoot().getNode("nums").getIntegerList().containsAll(Arrays.asList(new Integer[]{1,2,3,4,5})));
        assertEquals(5, jsonTree.getRoot().getInt("total"));
    }
    
    @Test
    public void parse2() throws Exception {
        JsonTree jsonTree = json.get("/json/json-2.json");
        JsonNode.NodeList nodes = jsonTree.getRoot().getNode("objs").getNodeList();
        assertEquals("item1", nodes.getNode(0).getKey());
        assertEquals(1, nodes.getNode(0).getInt("item1"));
        assertEquals("item2", nodes.getNode(1).getKey());
        assertEquals(2, nodes.getNode(1).getInt("item2"));
        assertEquals(2, jsonTree.getRoot().getInt("total"));
    }
    
    @Test
    public void parse3() throws Exception {
        JsonTree jsonTree = json.get("/json/json-3.json");
        assertEquals(1, jsonTree.getRoot().getNode("nestedlist").getNodeList().getNode(0).getNodeList().getNode(0).getNodeList().getInt(0));
        assertEquals("item", jsonTree.getRoot().getNode("nestedlist").getNodeList().getNode(0).getNodeList().getNode(0).getNodeList().getNode(1).getKey());
        assertEquals(1, jsonTree.getRoot().getNode("nestedlist").getNodeList().getNode(0).getNodeList().getNode(0).getNodeList().getNode(1).getInt("item"));
    }
    
    @Test
    public void parse5() throws Exception {
        JsonTree jsonTree = json.get("/json/json-5.json");
        assertTrue(jsonTree.getRoot().containsKey("root"));
        assertTrue(jsonTree.getRoot().getNode("root").containsKey("child1"));
        assertTrue(jsonTree.getRoot().getNode("root").getNode("child1").containsKey("child2"));
        assertEquals(2,jsonTree.getRoot().getNode("root").getNode("child1").getInt("child2"));
    }
    
    @Test
    public void parse6() throws Exception {
        JsonTree jsonTree = json.get("/json/json-6.json");
        assertTrue(jsonTree.getRoot().containsKey("root"));
        assertTrue(jsonTree.getRoot().getNode("root").containsKey("child1"));
        assertTrue(jsonTree.getRoot().getNode("root").getNode("child1").containsKey("child2"));
        JsonNode.NodeList nodes = jsonTree.getRoot().getNode("root").getNode("child1").getNode("child2").getNodeList();
        assertEquals(1, nodes.getInt(0));
        assertEquals(2, nodes.getInt(1));
        assertEquals(3, nodes.getInt(2));
    }
    
    @Test
    public void parse7() throws Exception {
        JsonTree jsonTree = json.get("/json/json-7.json");
        JsonNode root = jsonTree.getRoot();
        assertTrue(root.containsKeys("_id","index","guid","isActive","balance","picture","age","eyeColor","name","gender","company","email","phone",
            "address","about","registered","latitude","longitude","tags","friends","greeting","favoriteFruit"));
        assertEquals("5aeda580af2eab6bed03f069",root.getString("_id"));
        assertEquals(0,root.getInt("index"));
        assertEquals("4862706a-be3c-49d6-b9b9-d4f351ff17e3",root.getString("guid"));
        assertEquals((Boolean)true, root.getBoolean("isActive"));
        assertEquals("$2,565.67",root.getString("balance"));
        assertEquals("http://placehold.it/32x32",root.getString("picture"));
        assertEquals(39,root.getInt("age"));
        assertEquals("green",root.getString("eyeColor"));
        assertEquals("Raymond Leblanc",root.getString("name"));
        assertEquals("male",root.getString("gender"));
        assertEquals("AMTAP",root.getString("company"));
        assertEquals("raymondleblanc@amtap.com",root.getString("email"));
        assertEquals("+1 (909) 535-2438",root.getString("phone"));
        assertEquals("546 Fleet Walk, Hegins, Alabama, 4440",root.getString("address"));
        assertEquals("Ex eu qui est eiusmod aute officia eiusmod fugiat et sit quis ipsum pariatur. Incididunt adipisicing sunt cupidatat reprehenderit sint adipisicing elit adipisicing eiusmod aute laborum. Velit deserunt reprehenderit irure nisi sit eiusmod adipisicing reprehenderit Lorem.",root.getString("about"));
        assertEquals("2017-05-21T07:02:54 -01:00",root.getString("registered"));
        assertEquals(-40.151031,root.getDouble("latitude"));
        assertEquals(1.240083,root.getDouble("longitude"));
        
        assertEquals("sit",root.getNode("tags").getStringList().get(0));
        assertEquals("enim",root.getNode("tags").getStringList().get(1));
        assertEquals("consequat",root.getNode("tags").getStringList().get(2));
        assertEquals("voluptate",root.getNode("tags").getStringList().get(3));
        assertEquals("ex",root.getNode("tags").getStringList().get(4));
        assertEquals("ut",root.getNode("tags").getStringList().get(5));
        assertEquals("ullamco",root.getNode("tags").getStringList().get(6));
        
        assertTrue(root.getNode("friends").getNodeList().getNode(0).containsKey("id"));
        assertTrue(root.getNode("friends").getNodeList().getNode(0).containsKey("name"));
        assertEquals(0, root.getNode("friends").getNodeList().getNode(0).getInt("id"));
        assertEquals("Sykes Bird", root.getNode("friends").getNodeList().getNode(0).getString("name"));
        assertTrue(root.getNode("friends").getNodeList().getNode(1).containsKey("id"));
        assertTrue(root.getNode("friends").getNodeList().getNode(1).containsKey("name"));
        assertEquals(1, root.getNode("friends").getNodeList().getNode(1).getInt("id"));
        assertEquals("Barnett Wilkins", root.getNode("friends").getNodeList().getNode(1).getString("name"));
        assertTrue(root.getNode("friends").getNodeList().getNode(2).containsKey("id"));
        assertTrue(root.getNode("friends").getNodeList().getNode(2).containsKey("name"));
        assertEquals(2, root.getNode("friends").getNodeList().getNode(2).getInt("id"));
        
        assertEquals("Erin Mendoza", root.getNode("friends").getNodeList().getNode(2).getString("name"));
        assertEquals("Hello, Raymond Leblanc! You have 2 unread messages.",root.getString("greeting"));
        assertEquals("strawberry",root.getString("favoriteFruit"));
    }
    
    @Test
    public void parse8() {
        JsonTree jsonTree = json.get("/json/json-8.json");
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
