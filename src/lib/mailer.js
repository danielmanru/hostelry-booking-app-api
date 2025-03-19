import nodemailer from "nodemailer";
import { google } from "googleapis";
import ejs from "ejs";
import { ResponseError } from "../error/response-error";

const { 
  GOOGLE_CLIENT_ID,
  GOOGLE_CLIENT_SECRET,
  GMAIL_REFRESH_TOKEN,
  SENDER_EMAIL,
} = process.env;

const oAuth2Client = new google.auth.OAuth2(
  GOOGLE_CLIENT_ID,
  GOOGLE_CLIENT_SECRET,
);

oAuth2Client.setCredentials( { refresh_token : GMAIL_REFRESH_TOKEN } );

const sendEmail = async (to, subject, html) => {
  try{
    const accessToken = await oAuth2Client.getAccessToken();
    const transport = nodemailer.createTransport({
      service : 'gmail',
      auth : {
        type : 'OAuth2',
        user : SENDER_EMAIL,
        clientId : GOOGLE_CLIENT_ID,
        clientSecret : GOOGLE_CLIENT_SECRET,
        refreshToken : GMAIL_REFRESH_TOKEN,
        accessToken : accessToken,
      }
    });

    const mailOptions = {
      to,
      subject,
      html,
    };

    const response = await transport.sendMail(mailOptions)

    return response;
  } catch (err) {
    next(err);
  }
}

const getEmailHtml = async (filename, data) => {
  try{
    const path = __dirname + '../views/email/' + filename;

    ejs.renderFIle(path, data, (err, data) => {
      if (err) {
        throw new ResponseError(400, err);
      } 

      return data;
    })
  } catch(err) {
    next(err);
  }
};

export {
  sendEmail,
  getEmailHtml,
};