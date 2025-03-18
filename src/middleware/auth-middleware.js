import jwt from 'jsonwebtoken'
import dotenv from 'dotenv';
dotenv.config();

export const authMiddleware = async (req, res, next) => {
  const token = req.headers['authorization']?.split(' ')[1];
  
  if(token == null){
    return res.status(401).json({
      errors : 'Unauthorized'
    });
  }

  let verifyToken = process.env.ACCESS_TOKEN_SECRET

  if (req.body.tokenType === "refresh-token"){
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
