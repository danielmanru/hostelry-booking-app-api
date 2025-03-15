# User API Spec

## Registe User API
Endpoint : POST /api/users
Request Body : 
```json
{
  "email" : "johndoe@gmail.com",
  "password" : "klendestin",
  "name" : "John Doe"
}
```
Response Body Success : 
```json
{
  "data": {
    "email" : "johndoe@gmail.com",
    "name" : "John Does"
  },
}
```
Response Body Errors : 
```json
{
  "errors": {
    "message" : "Username already registered"
  },
}
```

## Login User API
Endpoint : POST /api/users/login
Request Body : 
```json
{
  "email" : "johndoe@gmail.com",
  "password" : "klendestin",
}
```
Response Body Success : 
```json
{
  "data": {
    "token" : "unique-token",
  },
}
```
Response Body Errors : 
```json
{
  "errors": "username or passwort wrong"
}
```

## Update User API
Endpoint : PATCH /api/users/current

Headers : 
- Authorization : token

Request Body : 
```json
{
  "name" : "John Sagala", //optional
  "password" : "pisikon", //optional
}
```
Response Body Success : 
```json
{
  "data": {
    "email" : "johndoe@gmail.com",
    "name": "John Sagala",
  },
}
```
Response Body Errors : 
```json
{
  "errors": "Name length max 100",
}
```

## Get User API
Endpoint : GET /api/users/current

Headers : 
- Authorization : token

Response Body Success : 
```json
{
  "data": {
    "email" : "johndoe@gmail.com",
    "name": "John Sagala",
  },
}
```
Response Body Errors : 
```json
{
  "errors": "Unauthorized",
}
```

## Logout User API
Endpoint : DELETE /api/users/logout

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
  "errors": "Unauthorized",
}
```
