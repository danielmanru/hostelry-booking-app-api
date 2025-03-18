import Joi from "joi";

const registerUserValidation = Joi.object({
  email: Joi.string()
    .email({tlds : { allow : true }})
    .regex(/^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$/)
    .required()
    .messages({
      "string.pattern.base" : "Should not contain uppercase letters!",
    }),
  password : Joi.string()
    .min(8)
    .max(50)
    .regex(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$!%*?&])[A-Za-z\d@#$!%*?&]+$/)
    .required()
    .messages({
      "string.pattern.base" : "Password should contain uppercase, number, and special character",
    }),
  name : Joi.string().max(100).required(),
  role : Joi.string().valid('GUEST', 'HOST', 'ADMIN').required(),
  birthDate : Joi.number()
    .integer()
    .min(1)
    .max(31)
    .required(),
  birthMonth : Joi.number()
    .integer()
    .min(1)
    .max(12)
    .required(),
  birthYear : Joi.number().integer().required(),
  gender : Joi.string().valid('MALE', 'FEMALE').required(),
  phone : Joi.string().max(13).required(),

});

const loginUserValidation = Joi.object({
  email: Joi.string()
    .email({tlds : { allow : true }})
    .regex(/^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$/)
    .required()
    .messages({
      "string.pattern.base" : "Should not contain uppercase letters!",
    }),
  password : Joi.string()
    .min(8)
    .max(50)
    .regex(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$!%*?&])[A-Za-z\d@#$!%*?&]+$/)
    .required()
    .messages({
      "string.pattern.base" : "Password should contain uppercase, number, and special character",
    }),
})

const refreshTokenValidation = Joi.string().required()

const getUserValidation = Joi.string().max(100).required()

const updateUserValidation = Joi.object({
  name : Joi.string().max(100).required(),
  birthDate : Joi.number()
    .integer()
    .min(1)
    .max(31)
    .required(),
  birthMonth : Joi.number()
    .integer()
    .min(1)
    .max(12)
    .required(),
  birthYear : Joi.number().integer().required(),
  gender : Joi.string().valid('MALE', 'FEMALE').required(),
  phone : Joi.string().max(13).required(),
})

const updatePasswordValidation = Joi.object({
  currentPassword : Joi.string()
    .min(8)
    .max(50)
    .regex(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$!%*?&])[A-Za-z\d@#$!%*?&]+$/)
    .optional()
    .messages({
      "string.pattern.base" : "Password should contain uppercase, number, and special character",
  }),
  newPassword : Joi.string()
    .min(8)
    .max(50)
    .regex(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$!%*?&])[A-Za-z\d@#$!%*?&]+$/)
    .optional()
    .messages({
      "string.pattern.base" : "Password should contain uppercase, number, and special character",
  }),
})

export{
  registerUserValidation, 
  loginUserValidation,
  getUserValidation,
  updateUserValidation,
  updatePasswordValidation,
  refreshTokenValidation,
}