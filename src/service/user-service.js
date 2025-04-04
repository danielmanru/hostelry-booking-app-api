import dotenv from 'dotenv';
dotenv.config();
import {validate} from "../validation/validation.js"
import {
  changePasswordValidation,
  getUserValidation,
  loginUserValidation, 
  registerUserValidation,
  resetPasswordValidation,
  updateUserValidation,
  forgetPasswordValidation,
} from "../validation/user-validation.js"
import { prismaClient } from "../application/database.js"
import { ResponseError } from "../error/response-error.js";
import bcrypt from "bcrypt";
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

  const verificationToken = jwt.sign( { email : user.email }, ACCESS_TOKEN_SECRET, { expiresIn : '1h' })
  const verificationLink = `${API_URI}/users/verifyUser?token=${verificationToken}`
  const emailTemplate = await getEmailHtml('verification-email.ejs', { link : verificationLink})

  sendEmail(user.email, 'Verify your email', emailTemplate)

  return prismaClient.user.create({
    data: user,
    select:{
      email : true,
      name: true
    }
  })
};

const sendVerificationEmail =  async (request) => {
  const verificationToken = jwt.sign( { email : request.email }, ACCESS_TOKEN_SECRET, { expiresIn : '1h' })
  const verificationLink = `${API_URI}/users/verifyUser?token=${verificationToken}`
  const emailTemplate = await getEmailHtml('verification-email.ejs', { link : verificationLink})

  sendEmail(request.email, 'Verify your email', emailTemplate)

  return {
    status : "PENDING",
    message : "An verification email is being sent to your email!"
  }
}

const verifyUser = async (request) => {
  const { email } = request.user;
  const user = await prismaClient.user.findUnique({
    where:{
      email : email,
    },
    select : {
      email :true,
      isVerified:true,
      
    }
  });
    
  if (!user) {
    throw new ResponseError(404, 'User is not found!');
  }

  if (user.isVerified === "TRUE") {
    throw new ResponseError(400, 'Your email have been verified.');
  }

  return prismaClient.user.update({
    data : {
      isVerified : "TRUE"
    },
    where : {
      email : email
    },
    select : {
      email : true,
      isVerified : true
    }
  })
}

const generateAccessToken = (payload) =>{
  
  return jwt.sign(payload, ACCESS_TOKEN_SECRET, { expiresIn : '1d' });
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
  const refreshToken = jwt.sign(payload, REFRESH_TOKEN_SECRET, { expiresIn : "7d" })

  await prismaClient.user.update({
    data:{
      refreshToken: refreshToken
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
    throw new ResponseError(404, "user is not found");
  };

  return user;
};

const update = async (request) => {
  const updateRequest = validate(updateUserValidation, request.body);
  console.log(updateRequest)
  const searchUser = await prismaClient.user.count({
    where:{
      email : request.user.email,
    }
  });

  if(searchUser != 1){
    throw new ResponseError(404, "User is not found");
  };

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
  });
};

const forgetPassword = async (request) => {
  const forgetPasswordRequest = validate(forgetPasswordValidation, request);
  const user = await prismaClient.user.findUnique({
    where : {
      email : forgetPasswordRequest.email
    },
    select : {
      email : true
    }
  });

  if (!user) {
    throw new ResponseError(400, 'User is not found!')
  };

  const resetPasswordToken = jwt.sign({email : user.email}, ACCESS_TOKEN_SECRET, {expiresIn : "300s" })
  const resetPasswordLink = `${API_URI}/users/resetPassword?token=${resetPasswordToken}`;
  const emailTemplate = await getEmailHtml('reset-password.ejs', {
    name : user.name,
    link : resetPasswordLink
  });
  
  sendEmail(user.email, "Reset Your Password", emailTemplate);

  return {
    status : "PENDING",
    message : "A reset password link is being sent to your email!"
  }
};

const resetPassword = async (request) => {
  const resetPasswordRequest = validate(resetPasswordValidation, request.body);
  
  const user = await prismaClient.user.count({
    where : {
      email : request.user.email
    }
  });

  if (user != 1) {
    throw new ResponseError(404, 'User is not found!');
  };

  if(resetPasswordRequest.newPassword !== resetPasswordRequest.confirmNewPassword){
      throw new ResponseError(401, "New password and confirm password is different!");
  };
  
  const newPassword = await bcrypt.hash(resetPasswordRequest.newPassword, 10);

  return prismaClient.user.update({
    data : {
      password : newPassword
    },
    where : {
      email : request.user.email
    },
    select : {
      email : true
    }
  });
};

const changePassword = async (request) => {
  const changePasswordRequest = validate(changePasswordValidation, request.body);
  const user = await prismaClient.user.findUnique({
    where:{
      email : request.user.email,
    },
    select : {
      email :true,
      password :true,
      role : true
    }
  });
  
  if(!user){
    throw new ResponseError(404, "User is not found");
  };

  if (changePasswordRequest.currentPassword && changePasswordRequest.newPassword) {
    const isPasswordValid = await bcrypt.compare(changePasswordRequest.currentPassword, user.password);
    if(!isPasswordValid){
      throw new ResponseError(401, "password is wrong");
    };
  };

  const newPassword = await bcrypt.hash(changePasswordRequest.newPassword, 10);
  
  const payload = {
    email : user.email,
    role : user.role
  };

  const newRefreshToken = jwt.sign(payload, REFRESH_TOKEN_SECRET);

  return prismaClient.user.update({
    data:{
      password : newPassword,
      token : newRefreshToken,
    },
    where:{
      email: user.email
    },
    select:{
      token : true,
    }
  });
};

const logout = async (request) => {
  const user = await prismaClient.user.count({
    where:{
      email : request.email,
    },
  });
  
  if(!user){
    throw new ResponseError(404, "User is not found");
  };

  await prismaClient.user.update({
    data : {
      refreshToken : ""
    },
    where : {
      email : request.email
    },
    select : {
      email : true
    }
  });

  return {
    data : {}
  }
};

export default{
  register,
  login,
  get,
  update,
  changePassword,
  refreshToken,
  verifyUser,
  forgetPassword,
  resetPassword,
  sendVerificationEmail,
  logout,
}