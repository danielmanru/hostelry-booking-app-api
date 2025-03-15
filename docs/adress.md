# Address API Spec

## Create Address API
Endpoint : POST /api/contacts/:contactId/addresses

Headers : 
- Authorization : token

Request Body : 
```json
{
  "street" : "Jati Ausie",
  "city" : "Perth",
  "province" : "Sydney",
  "country" : "Ausie",
  "postal_code" : "252525",
}
```
Response Body Success : 
```json
{
  "data": {
    "id":1,
    "street" : "Jati Ausie",
    "city" : "Perth",
    "province" : "Sydney",
    "country" : "Ausie",
    "postal_code" : "252525",
  },
}
```
Response Body Errors : 
```json
{
  "errors": {
    "message" : "Country is required"
  },
}
```

## Update Address API
Endpoint : PUT /api/contacts/:contactId/addresses/:addressId

Headers : 
- Authorization : token

Request Body : 
```json
{
  "street" : "Jati Ausie",
  "city" : "Perth",
  "province" : "Melbourne",
  "country" : "Ausie",
  "postal_code" : "252525",
}
```
Response Body Success : 
```json
{
  "data": {
    "id":1,
    "street" : "Jati Ausie",
    "city" : "Perth",
    "province" : "Melbourne",
    "country" : "Ausie",
    "postal_code" : "252525",
  },
}
```
Response Body Errors : 
```json
{
  "errors": "Country is required"
}
```

## Get Address API
Endpoint : GET /api/contacts/:contactId/addresses/:addressId

Headers : 
- Authorization : token

Response Body Success : 
```json
{
  "data": {
    "id":1,
    "street" : "Jati Ausie",
    "city" : "Perth",
    "province" : "Melbourne",
    "country" : "Ausie",
    "postal_code" : "252525",
  },
}
```
Response Body Errors : 
```json
{
  "errors": "Contact is not found",
}
```

## List Address API
Endpoint : GET /api/contacts/:contactId/addresses/

Headers : 
- Authorization : token

Response Body Success : 
```json
{
  "data": [
    {
      "id":1,
      "street" : "Jati Ausie",
      "city" : "Perth",
      "province" : "Melbourne",
      "country" : "Ausie",
      "postal_code" : "252525",
    },
    {
      "id":2,
      "street" : "Jati Ausie",
      "city" : "Perth",
      "province" : "Melbourne",
      "country" : "Ausie",
      "postal_code" : "252525",
    }
  ],
}
```
Response Body Errors : 
```json
{
  "errors": "Contact is not found",
}
```

## Remove Address API
Endpoint : DELETE /api/contacts/:contactId/addresses/:addressId

Headers : 
- Authorization : token

Response Body Success : 
```json
{
  "data": "OK,
}
```
Response Body Errors : 
```json
{
  "errors": "Address is not found",
}
```
