# java-json-parser

A fast and simple json parser for Java. 

## Examples

Create the parser

```
private json.Json jsonParser = new json.Json();
```

Parse and access fields:

```
Json json = jsonParser.getFromString("{\"root\": {\"name\": \"nick\", \"height\": 179, \"occupation\":{ \"role\":\"dev\" } } }");
json.getNode("root").getString("name"); // nick
json.getNode("root").getInt("height"); // 179
json.getNode("root").getNode("occupation").getString("role"); // dev
```

Get values from an array:

```
Json json = jsonParser.getFromString("[ {\"total\": 9.99 } ]");
json.getNodeList().getNode(0).getDouble("total") // 9.99
```

Getters cast values so they are ready to use:

```
String data = "{
    \"tags\": [
        \"sit\",
        \"enim\",
        \"consequat\",
        \"voluptate\",
        \"ex\",
        \"ut\",
        \"ullamco\"
    ]
}"
Json json = jsonParser.getFromString(data);
json.getStringList("tags").get(0); // "sit"
```

## Contributing

Open to pull requests.