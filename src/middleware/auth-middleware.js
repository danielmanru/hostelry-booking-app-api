import jwt from 'jsonwebtoken'
import dotenv from 'dotenv';
import { tokenValidation } from '../validation/user-validation';
dotenv.config();

export const authMiddleware = async (req, res, next) => {
  let token = req.headers['authorization']?.split(' ')[1];
  
  if (token instanceof req.query) token = req.query.token?.split(' ')[1]; 

  token = validate(tokenValidation, token);

  if(token == null){
    return res.status(401).json({
      errors : 'Unauthorized'
    });
  }

  let verifyToken = process.env.ACCESS_TOKEN_SECRET

  if (req.query.tokenType === "refresh-token"){
    verifyToken = process.env.REFRESH_TOKEN_SECRET
  }
  
  jwt.verify(token, verifyToken, (err, user) =>{
    if (err){
      console.log(err)
      return res.status(403).json({
        errors : err
      })
      
    }
    req.user = user;
    next();
  })
}
