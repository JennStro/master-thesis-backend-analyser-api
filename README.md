# master-thesis-backend-analyser-api

Deployed on Heroku. 


## Documentation 

### POST request

```
https://master-thesis-web-backend-prod.herokuapp.com/analyse
```
#### Response 

The response gives a list of errors, if any. It will return with an exception if the code could not be analysed.
 
 **"hasException"**: If the code could not be analysed this field is present, and the value is the exception.

The error contains:   
**"containingClass"**: The class that the error is located in. If not class is found, returns "".   
**"suggestion"**: If the error has a suggestion this field is present, and the value is the suggestion.   
**"lineNumber"**: If the error has a line number this field is present, and the value is the line number.     
**"explanation"**: If the error has an explanation this field is present, and the value is the explanation.  
**"type"**: The type of the error.   
**"moreInfoLink"**: Link to more info about the error.   
**"tip"**: If present, a tip to solve the error.

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
  "errors":
    [
        {
          "containingClass":"A",
          "suggestion":"to remove the semicolon after the if-condition: if (a == 5) { // The rest of your code }",
          "type":"master.thesis.backend.errors.SemiColonAfterIfError",
          "explanation":"You have a semicolon (;) after an if-statement! In Python we use a colon (:) here, but you don't need this after if-statement in Java!",
          "lineNumber":4,
          "moreInfoLink":"https://master-thesis-frontend-prod.herokuapp.com/semicolon"
        },
        {
          "containingClass":"A",
          "suggestion":"to add the method @Override public boolean equals(Object o) { // Checks to decide if two objects are equal goes here }",
          "tip":"Tip: Maybe your IDE has something like \"generate equals and hash methods\"?",
          "type":"master.thesis.backend.errors.MissingEqualsMethodError",
          "explanation":"The equals method is missing! If you do not implement the equals method of a class, it will use the default equals method of class Object.",
          "moreInfoLink":"https://master-thesis-frontend-prod.herokuapp.com/equalsoperator"
        }
    ]
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
