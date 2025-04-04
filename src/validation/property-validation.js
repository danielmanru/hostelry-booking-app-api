import joi from "joi";

const addPropertyValidation = joi.object({
  user_id :joi.number().integer().required(),
  property_name : joi.string().max(100).required(),
  city : joi.string().max(100).required(),
  address : joi.string().max(100).required(),
})

const getPropertyValidation = joi.number().integer().required()

export {
  addPropertyValidation,
  getPropertyValidation
}