import dotenv from 'dotenv';
dotenv.config();
import {validate} from "../validation/validation.js"
import {
  getUserValidation,
  loginUserValidation, 
  registerUserValidation,
  updatePasswordValidation,
  updateUserValidation
} from "../validation/user-validation.js"
import { prismaClient } from "../application/database.js"
import { ResponseError } from "../error/response-error.js";
import bcrypt from "bcrypt";
import {v4 as uuid} from "uuid";
import jwt from "jsonwebtoken";
import { getEmailHtml, sendEmail } from '../lib/mailer.js';

const { ACCESS_TOKEN_SECRET, REFRESH_TOKEN_SECRET, API_URI } = process.env;

const register = async(request) => {
  const user = validate(registerUserValidation, request);
  const countUser = await prismaClient.user.count({
    where:{
      email : user.email
    }
  });

  if(countUser === 1){
    throw new ResponseError(400, "Email already registered");
  }

  user.password = await bcrypt.hash(user.password, 10);

  const verifyToken = jwt.sign( { email }, ACCESS_TOKEN_SECRET, { expiresIn : '300s' })
  const verifyLink = `${API_URI}/users/verify-user?token=${verifyToken}`
  const emailTemplate = await getEmailHtml('verification-email.ejs', { link : verifyLink})
  const sendEmail = sendEmail(user.email, 'Verify your email', emailTemplate)

  return prismaClient.user.create({
    data: user,
    select:{
      email : true,
      name: true
    }
  })
};

const verifyUser = async (request) => {
  const { email } = request.user;
  const user = await prismaClient.user.findUnique({
    where:{
      email : email,
    },
    select : {
      email :true,
      isVerified :true,
    }
  });
    
  if (user != 1) {
    throw new ResponseError(404, 'User is not found!');
  }

  if (user.isVerified === "TRUE") {
    throw new ResponseError(400, ' Your email have been verified.');
  }

  await prismaClient.user.update({
    data : {
      isVerified : "TRUE"
    },
    where : {
      email : email
    }
  })

  return { message : 'Your email have been verified' }
}

const generateAccessToken = (payload) =>{
  
  return jwt.sign(payload, ACCESS_TOKEN_SECRET, { expiresIn : '14d' });
}

const refreshToken = async (request) => {
  const accessToken = generateAccessToken({
    email : request.email,
    role : request.role,
  })
    
  return accessToken
}

const login = async(request) => {
  const loginRequest = validate(loginUserValidation, request);
  const user = await prismaClient.user.findUnique({
    where:{
      email : loginRequest.email
    },
    select:{
      email : true,
      password : true,
      role :true,
    }
  });

  if (!user){
    throw new ResponseError(401, "email or password is wrong");
  };

  const isPasswordValid = await bcrypt.compare(loginRequest.password, user.password);
  if(!isPasswordValid){
    throw new ResponseError(401, "email or password is wrong");
  }

  const payload = {
    email : user.email,
    role : user.role,
  };

  const accessToken = generateAccessToken(payload)
  const refreshToken = jwt.sign(payload, REFRESH_TOKEN_SECRET)

  await prismaClient.user.update({
    data:{
      token: refreshToken
    },
    where:{
      email: user.email
    }
  })
 
  return {
    accesToken : accessToken,
    refreshToken : refreshToken,
  }
};

const get = async (email) => {
  email = validate((getUserValidation), email);

  const user = await prismaClient.user.findUnique({
    where : {
      email : email,
    },
    select : {
      email : true,
      name : true,
      birthDate: true,
      birthMonth : true,
      birthYear : true,
      gender : true,
      phone : true,
    }
  });

  if (!user){
    throw new ResponseError(404, "user is not found")
  }

  return user;
}

const update = async (request) => {
  const updateRequest = validate(updateUserValidation, request.body);
  console.log(updateRequest)
  const searchUser = await prismaClient.user.count({
    where:{
      email : request.user.email,
    }
  })

  if(searchUser != 1){
    throw new ResponseError(404, "User is not found");
  }

  return prismaClient.user.update({
    data: updateRequest,
    where:{
      email: request.user.email
    },
    select:{
      email:true,
      name : true,
      birthDate : true,
      birthMonth : true,
      birthYear : true,
      gender : true,
      phone :true,
    }
  })
}

const forgetPassword = async (request) => {
  const email = validate(forgetPasswordValidation, request)
  const user = await prismaClient.user.findUnique({
    where : {
      email : email
    },
    select : {
      name : true
    }
  });

  if (!user) {
    throw new ResponseError(400, 'User is not found!')
  }

  const resetPasswordToken = jwt.sign({email : email}, ACCESS_TOKEN_SECRET, {expiresIn : "600s" })
  const resetPasswordLink = `${API_URI}/users/forgetPassword?token=${resetPasswordToken}`;
  const emailTemplate = await getEmailHtml('reset-password.ejs', {
    name : user.name,
    link : resetPasswordLink
  })
  
  await sendEmail(email, "Reset Your Password", emailTemplate);

  return { message : 'Reset password email have been sended' }
}

const changePassword = async (request) => {
  const changePasswordRequest = validate(updatePasswordValidation, request.body);
  const user = await prismaClient.user.findUnique({
    where:{
      email : request.user.email,
    },
    select : {
      email :true,
      password :true,
    }
  })
  
  if(!user){
    throw new ResponseError(404, "User is not found");
  }

  if(changePasswordRequest.currentPassword && changePasswordRequest.newPassword){
    const isPasswordValid = await bcrypt.compare(changePasswordRequest.currentPassword, user.password);
    if(!isPasswordValid){
      throw new ResponseError(401, "password is wrong");
    }
  }

  const newPassword = await bcrypt.hash(changePasswordRequest.newPassword, 10);
  const token = uuid().toString();

  return prismaClient.user.update({
    data:{
      password : newPassword,
      token : token,
    },
    where:{
      email: user.email
    },
    select:{
      email: true,
      name : true,
      token : true,
    }
  })
}

export default{
  register,
  login,
  get,
  update,
  changePassword,
  refreshToken,
  verifyUser,
  forgetPassword,
}