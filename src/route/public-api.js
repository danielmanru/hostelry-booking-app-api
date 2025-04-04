import express from "express";
import userController from "../controller/user-controller.js";

const publicRouter = new express.Router();
publicRouter.post('/api/users/forgetPassword', userController.forgetPassword)
publicRouter.post('/api/users/register', userController.register)
publicRouter.post('/api/users/login', userController.login)

export{
  publicRouter
}