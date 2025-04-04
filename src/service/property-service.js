import dotenv from 'dotenv';
dotenv.config();
import {validate} from "../validation/validation.js"
import { prismaClient } from '../application/database.js';
import { 
  addPropertyValidation,
  getPropertyValidation
} from '../validation/property-validation.js';
import { ResponseError } from '../error/response-error.js';

const addProperty = async (request) => {
  const addPropertyRequest = validate(addPropertyValidation, request)

  return prismaClient.property.create({
    data : addPropertyRequest,
    select : {
      id : true, 
      property_name :true
    }
  })
}

const getProperty = async (request) => {
  const getPropertyRequest = validate(getPropertyValidation);

  const property = await prismaClient.property.findMany({
    where : {
      user_id : request.user_id,
    },
    select : {
      property_name : true,
      city : true,
      address : true,
    }  
  })

  if(!property) {
    throw new ResponseError(400, 'Unable to find any properties owned by this user')
  }

  
}

export {
  addProperty
}