import userService from "../service/user-service.js";
import { refreshTokenValidation } from "../validation/user-validation.js";
import {validate} from "../validation/validation.js"

const register = async (req, res, next) => {
  try{
    const result =  await userService.register(req.body);
    res.status(200).json({
      data : result
    })
  }catch(e){
    next(e);
  }
};

const login = async(req, res, next) => {
  try{
    const result = await userService.login(req.body);
    res.status(200).json({
      data : result
    })
  }catch(e){
    next(e)
  }
}

const refreshToken = async(req, res, next) => {
  try{
    validate(refreshTokenValidation, req.headers['authorization']?.split(' ')[1]);
    const result  = await userService.refreshToken(req.user);
    res.status(200).json({
      accessToken : result
    })
  }catch(e){
    next(e);
  }
}

const get = async (req, res, next) => {
  try{
    const email =  req.user.email;
    const result = await userService.get(email);
    res.status(200).json({
      data : result
    });
  }catch(e){
    next(e);
  };
};

const update = async (req, res, next) => {
  try{
    const result = await userService.update(req);
    res.status(200).json({
      data : result
    });
  }catch(e){
    next(e);
  };
};

const changePassword = async (req, res, next) => {
  try{
    const result = await userService.changePassword(req);
    res.status(200).json({
      data : result
    });
  }catch(e){
    next(e);
  };
};

export default{
  register,
  login,
  get,
  update,
  changePassword,
  refreshToken,
}