# master-thesis-backend-analyser-api

Deployed on Heroku. 


### Documentation 

#### POST request

```
https://master-thesis-web-backend-prod.herokuapp.com/analyse
```
#### Response 

**"containingClass"**: The class that the error is located in. If not class is found, returns "".   
**"suggestion"**: If the error has a suggestion this field is present, and the value is the suggestion.   
**"lineNumber"**: The line number of the error. If no line number can be found, it returns -1.   
**"explanation"**: If the error has an explanation this field is present, and the value is the explanation.   
**"status"**: If the code has error this field will have the value "error", if not it has the value "noerrors".   
**"hasException"**: If the code could not be analysed this field is present, and the value is the exception.

### Examples 
**Curl request**
```
curl --location --request POST 'https://master-thesis-web-backend-prod.herokuapp.com/analyse' \
--header 'Content-Type: text/plain' \
--data-raw 'class A {

    public int method(int a) {
        if (a == 5) ; {return a;}
    }

}'
```

**Response** 
```
{
    "containingClass": "A",
    "suggestion": "if (a == 5) { }",
    "lineNumber": 4,
    "explanation": "You have a semicolon (;) after an if-statement!",
    "status": "errors"
}
```
**Curl request**
```
curl --location --request POST 'https://master-thesis-web-backend-prod.herokuapp.com/analyse' \
--header 'Content-Type: text/plain' \
--data-raw 'class A {'
```

**Response** 
```
{
    "hasException": "(line 1,col 9) Parse error. Found <EOF>, expected one of  \";\" \"<\" \"@\" \"abstract\" \"boolean\" \"byte\" \"char\" \"class\" \"default\" \"double\" \"enum\" \"exports\" \"final\" \"float\" \"int\" \"interface\" \"long\" \"module\" \"native\" \"open\" \"opens\" \"private\" \"protected\" \"provides\" \"public\" \"record\" \"requires\" \"short\" \"static\" \"strictfp\" \"synchronized\" \"to\" \"transient\" \"transitive\" \"uses\" \"void\" \"volatile\" \"with\" \"yield\" \"{\" \"}\" <IDENTIFIER>\n"
}
```

## Architecture 

![arcitecture](https://user-images.githubusercontent.com/48728008/151001283-69cd144b-766d-4972-97be-93d5a03f28a8.png)

