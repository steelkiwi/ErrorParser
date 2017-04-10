# ErrorParser
Simple JSON parser for Android

## Usage

[Parser] removes all nesting and empty objects from JSON, so you can easy get your object by key or position. Class will help you to get object from JSON with different variants.

Example of JSON:
we'll call it myJson
```json
{
  "field1": ["error1", []],
  "field2": ["error3", "error4"],
  "nested_fields": [
    {
      "field3": ["error5", "error6"],
      "field4": ["error7", "error8"],
      "non_fields_error": [[], "error10"]
    }
  ],
  "non_fields_error1": ["error11", "error12"]
}
```



Parser.simpleParsing(myJson)
```java
String simpleParsing(String stringJson) {
    //will return "field1 - error1"
}
```



Parser.simpleParsing(myJson, "field1")
```java
String simpleParsing(String stringJson, String exceptKey) {
    //will return "error1"
}
```



Parser.getMessageByPosition(myJson, 1, 1)
```java
String getMessageByPosition(String source, int keyPosition, int messagePosition) {
    //will return "error4"
}
```



Parser.getMessageByPosition(myJson, "field4", 0)
```java
String getMessageByPosition(String source, String key, int messagePosition) {
    //will return "error7"
}
```



Parser.getKeyWithMessageByPosition(myJson, 3, 1)
```java
String getKeyWithMessageByPosition(String source, int keyPosition, int messagePosition) {
    //will return "field3 - error6"
}
```



Parser.getKeyWithMessageByPosition(myJson, "non_fields_error1", 1)
```java
String getKeyWithMessageByPosition(String source, String key, int messagePosition) {
    //will return "non_fields_error1 - "error12""
}
```



Parser.getMessageByKey(myJson, "non_fields_error")
```java
String getMessageByKey(String source, String key) {
    //will return "error10"
}
```



Parser.getMessageByKey(myJson, "non_fields_error1", 0)
```java
String getMessageByKey(String source, String key, int messagePosition) {
    //will return "error11"
}
```



Parser.getMessageByKeyPosition(myJson, 1)
```java
String getMessageByKeyPosition(String source, int keyPosition) {
    //will return "error3, error4"
}
```



Parser.getClearedJson(myJson)
```java
String getClearedJson(String source) {
    //will return your string JSON without nesting and empty objects
}
```


## License

Copyright 2017 SteelKiwi Inc, steelkiwi.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


[Parser]: <https://github.com/steelkiwi/ErrorParser/blob/master/app/src/main/java/com/steelkiwi/errorparser/Parser.java/>
