import express from "express";
import userController from "../controller/user-controller.js";
import { authMiddleware } from "../middleware/auth-middleware.js";

const userRouter = new express.Router();
userRouter.use(authMiddleware)
userRouter.get('/api/users/current', userController.get)
userRouter.get('/api/users/verifyUser', userController.verifyUser)
userRouter.get('/api/refreshToken', userController.refreshToken)
userRouter.post('/api/forgetPassword', userController.forgetPassword)
userRouter.put('/api/users/current/updateUserDetail', userController.update)
userRouter.put('/api/users/current/changePassword', userController.changePassword)

export{
  userRouter
}