# Contact API Spec

## Create User API
Endpoint : POST /api/contacts

Headers : 
- Authorization : token

Request Body : 
```json
{
  "first_name" : "Daniel",
  "last_name" : "Manurung",
  "email" : "danielmanurung@gmail.com",
  "phone" : "0938285819209",
}
```
Response Body Success : 
```json
{
  "data": {
    "id":1,
    "first_name" : "Daniel",
    "last_name" : "Manurung",
    "email" : "danielmanurung@gmail.com",
    "phone" : "0938285819209",
  },
}
```
Response Body Errors : 
```json
{
  "errors": {
    "message" : "Email is not valid format"
  },
}
```

## Update User API
Endpoint : PUT /api/contact/:id

Headers : 
- Authorization : token

Request Body : 
```json
{
  "first_name" : "Daniel",
  "last_name" : "Manurung",
  "email" : "danielmanurung@gmail.com",
  "phone" : "0938285819209",
}
```
Response Body Success : 
```json
{
  "data": {
    "id":1,
    "first_name" : "Daniel",
    "last_name" : "Manurung",
    "email" : "danielmanurung@gmail.com",
    "phone" : "0938285819209",
  },
}
```
Response Body Errors : 
```json
{
  "errors": "Email is not valid format"
}
```

## Get User API
Endpoint : GET /api/contacts/:id

Headers : 
- Authorization : token

Response Body Success : 
```json
{
  "data": {
    "id":1,
    "first_name" : "Daniel",
    "last_name" : "Manurung",
    "email" : "danielmanurung@gmail.com",
    "phone" : "0938285819209",
  },
}
```
Response Body Errors : 
```json
{
  "errors": "Contact is not found",
}
```

## Search User API
Endpoint : GET /api/contacts

Headers : 
- Authorization : token

Query Params : 
- name : Search by first_name or last_name, using like, optional
- email : Search by email, using like, optional
- phone : Search by phone, using like, optional
- page : number of page, default 1
- size : size per page, default 10

Response Body Success : 
```json
{
  "data": [
    {
      "id":1,
      "first_name" : "Daniel",
      "last_name" : "Manurung",
      "email" : "danielmanurung@gmail.com",
      "phone" : "0938285819209",
    },
    {
       "id":1,
      "first_name" : "Abdel",
      "last_name" : "Temon",
      "email" : "abdeltemon@gmail.com",
      "phone" : "0938285819276",
    }
  ],
  "paging" :{
    "page" : 1,
    "total_page" : 3,
    "total_item" : 30 ,
  }
}
```
Response Body Errors : 
```json
{
  "errors": "Unauthorized",
}
```

## Remove User API
Endpoint : DELETE /api/contacts/:id

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
  "errors": "Contact is not found",
}
```
