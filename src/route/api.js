import express from "express";
import userController from "../controller/user-controller.js";
import { authMiddleware } from "../middleware/auth-middleware.js";
import { roles } from "../utils/roles.js";

const userRouter = new express.Router();
userRouter.use(authMiddleware())
userRouter.get('/api/users/current', userController.get)
userRouter.get('/api/users/current/sendVerificationEmail', userController.sendVerificationEmail)
userRouter.get('/api/users/verifyUser', userController.verifyUser)
userRouter.get('/api/users/current/refreshToken', userController.refreshToken)
userRouter.post('/api/users/resetPassword', userController.resetPassword)
userRouter.put('/api/users/current/updateUserDetail', userController.update)
userRouter.post('/api/users/current/changePassword', userController.changePassword)
userRouter.get('/api/users/current/logout', userController.logout)

export{
  userRouter
}