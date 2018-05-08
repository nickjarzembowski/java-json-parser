package json.parser;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import java.util.Arrays;

import json.domain.JsonNode;
import json.domain.Json;

import org.junit.Test;

public class ParserTest {
    
    private json.Json json = new json.Json();
    
    @Test
    public void parse1() throws Exception {
        Json json = this.json.getFromFile("/json/json-1.json");
        assertTrue(json.getIntegerList("nums").containsAll(Arrays.asList(new Integer[]{1,2,3,4,5})));
        assertEquals(5, json.getInt("total"));
    }
    
    @Test
    public void parse2() throws Exception {
        Json json = this.json.getFromFile("/json/json-2.json");
        JsonNode.NodeList nodes = json.getNodeList("objs");
        assertEquals("item1", nodes.getNode(0).getKey());
        assertEquals(1, nodes.getNode(0).getInt("item1"));
        assertEquals("item2", nodes.getNode(1).getKey());
        assertEquals(2, nodes.getNode(1).getInt("item2"));
        assertEquals(2, json.getInt("total"));
    }
    
    @Test
    public void parse3() throws Exception {
        Json json = this.json.getFromFile("/json/json-3.json");
        assertEquals(1, json.getNodeList("nestedlist").getNodeList(0).getNodeList(0).getInt(0));
        assertEquals("item", json.getNodeList("nestedlist").getNodeList(0).getNodeList(0).getNode(1).getKey());
        assertEquals(1, json.getNodeList("nestedlist").getNodeList(0).getNodeList(0).getNode(1).getInt("item"));
    }
    
    @Test
    public void parse5() throws Exception {
        Json json = this.json.getFromFile("/json/json-5.json");
        assertTrue(json.containsKey("root"));
        assertTrue(json.getNode("root").containsKey("child1"));
        assertTrue(json.getNode("root").getNode("child1").containsKey("child2"));
        assertEquals(2, json.getNode("root").getNode("child1").getInt("child2"));
    }
    
    @Test
    public void parse6() throws Exception {
        Json json = this.json.getFromFile("/json/json-6.json");
        assertTrue(json.containsKey("root"));
        assertTrue(json.getNode("root").containsKey("child1"));
        assertTrue(json.getNode("root").getNode("child1").containsKey("child2"));
        JsonNode.NodeList nodes = json.getNode("root").getNode("child1").getNode("child2").getNodeList();
        assertEquals(1, nodes.getInt(0));
        assertEquals(2, nodes.getInt(1));
        assertEquals(3, nodes.getInt(2));
    }
    
    @Test
    public void parse7() throws Exception {
        Json json = this.json.getFromFile("/json/json-7.json");
        
        assertTrue(json.containsKeys("_id","index","guid","isActive","balance","picture","age","eyeColor","name","gender","company","email","phone",
            "address","about","registered","latitude","longitude","tags","friends","greeting","favoriteFruit"));
        assertEquals("5aeda580af2eab6bed03f069", json.getString("_id"));
        assertEquals(0, json.getInt("index"));
        assertEquals("4862706a-be3c-49d6-b9b9-d4f351ff17e3", json.getString("guid"));
        assertEquals((Boolean)true, json.getBoolean("isActive"));
        assertEquals("$2,565.67", json.getString("balance"));
        assertEquals("http://placehold.it/32x32", json.getString("picture"));
        assertEquals(39, json.getInt("age"));
        assertEquals("green", json.getString("eyeColor"));
        assertEquals("Raymond Leblanc", json.getString("name"));
        assertEquals("male", json.getString("gender"));
        assertEquals("AMTAP", json.getString("company"));
        assertEquals("raymondleblanc@amtap.com", json.getString("email"));
        assertEquals("+1 (909) 535-2438", json.getString("phone"));
        assertEquals("546 Fleet Walk, Hegins, Alabama, 4440", json.getString("address"));
        assertEquals("Ex eu qui est eiusmod aute officia eiusmod fugiat et sit quis ipsum pariatur. Incididunt adipisicing sunt cupidatat reprehenderit sint adipisicing elit adipisicing eiusmod aute laborum. Velit deserunt reprehenderit irure nisi sit eiusmod adipisicing reprehenderit Lorem.",
            json.getString("about"));
        assertEquals("2017-05-21T07:02:54 -01:00", json.getString("registered"));
        assertEquals(-40.151031, json.getDouble("latitude"));
        assertEquals(1.240083, json.getDouble("longitude"));
        
        assertEquals("sit", json.getStringList("tags").get(0));
        assertEquals("enim", json.getStringList("tags").get(1));
        assertEquals("consequat", json.getStringList("tags").get(2));
        assertEquals("voluptate", json.getStringList("tags").get(3));
        assertEquals("ex", json.getStringList("tags").get(4));
        assertEquals("ut", json.getStringList("tags").get(5));
        assertEquals("ullamco", json.getStringList("tags").get(6));
        
        assertTrue(json.getNodeList("friends").getNode(0).containsKey("id"));
        assertTrue(json.getNodeList("friends").getNode(0).containsKey("name"));
        assertEquals(2, json.getNodeList("friends").getNode(0).getTotalKeys());
        assertEquals(0, json.getNodeList("friends").getNode(0).getInt("id"));
        assertEquals("Sykes Bird", json.getNodeList("friends").getNode(0).getString("name"));
        assertTrue(json.getNodeList("friends").getNode(1).containsKey("id"));
        assertTrue(json.getNodeList("friends").getNode(1).containsKey("name"));
        assertEquals(1, json.getNodeList("friends").getNode(1).getInt("id"));
        assertEquals("Barnett Wilkins", json.getNodeList("friends").getNode(1).getString("name"));
        assertTrue(json.getNodeList("friends").getNode(2).containsKey("id"));
        assertTrue(json.getNodeList("friends").getNode(2).containsKey("name"));
        assertEquals(2, json.getNodeList("friends").getNode(2).getInt("id"));
        assertEquals("Erin Mendoza", json.getNodeList("friends").getNode(2).getString("name"));
        
        assertEquals("Hello, Raymond Leblanc! You have 2 unread messages.", json.getString("greeting"));
        assertEquals("strawberry", json.getString("favoriteFruit"));
    }
    
    @Test
    public void parse8() {
        Json root = this.json.getFromFile("/json/json-8.json");
        assertEquals(7, root.getNodeList().list.size());
        assertEquals("5aeda580af2eab6bed03f069", root.getNodeList().getNode(0).getString("_id"));
        assertEquals("5aeda580a27c23f9a06de32f", root.getNodeList().getNode(1).getString("_id"));
        assertEquals("5aeda580232f47e6b8d4737d", root.getNodeList().getNode(2).getString("_id"));
        assertEquals("5aeda580516311eb01fb19f9", root.getNodeList().getNode(3).getString("_id"));
        assertEquals("5aeda5803e5dcb1064a81c4d", root.getNodeList().getNode(4).getString("_id"));
        assertEquals("5aeda58072785318ece00831", root.getNodeList().getNode(5).getString("_id"));
        assertEquals("5aeda5806875e4243be3fc9e", root.getNodeList().getNode(6).getString("_id"));
    }
    
    @Test
    public void parse9() {
        Json json = this.json.getFromString("{\"root\":    {\"name\": \"nick\", \"height\": 177, \"occupation\":{ \"role\":\"dev\" } } }");
        assertEquals("nick", json.getNode("root").getString("name"));
        assertEquals(177, json.getNode("root").getInt("height"));
        assertEquals("dev", json.getNode("root").getNode("occupation").getString("role"));
    }
    
    @Test
    public void parse10() {
        Json json = this.json.getFromString("[ {\"name\": \"nick\"} ]");
        assertEquals("nick",json.getNodeList().getNode(0).getString("name"));
    }
    
}
