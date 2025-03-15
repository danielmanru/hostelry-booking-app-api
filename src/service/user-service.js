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

  return prismaClient.user.create({
    data: user,
    select:{
      email : true,
      name: true
    }
  })
};

const login = async(request) => {
  const loginRequest = validate(loginUserValidation, request);
  console.log(loginRequest)
  const user = await prismaClient.user.findUnique({
    where:{
      email : loginRequest.email
    },
    select:{
      email : true,
      password : true
    }
  });

  if (!user){
    throw new ResponseError(401, "email or password is wrong");
  };

  const isPasswordValid = await bcrypt.compare(loginRequest.password, user.password);
  if(!isPasswordValid){
    throw new ResponseError(401, "email or password is wrong");
  }

  const token = uuid().toString();

  return prismaClient.user.update({
    data:{
      token: token
    },
    where:{
      email: user.email
    },
    select:{
      token:true,
    }
  })
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
}