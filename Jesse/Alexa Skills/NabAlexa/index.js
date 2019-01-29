'use strict';
const Alexa = require('ask-sdk');
const aws = require('aws-sdk');
const rp = require('request-promise');
var nodemailer = require('nodemailer');
const sleep = require('util').promisify(setTimeout);


var sqs = new aws.SQS({region:'us-west-2'});
var parseString = require('xml2js').parseString;
var queueURL = "https://sqs.us-west-2.amazonaws.com/568982321218/NabAlexa_Queue";

const LaunchRequestHandler = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;
      return request.type === 'LaunchRequest';
   },
   async handle(handlerInput) {
      console.log('LaunchRequest');
      const request = handlerInput.requestEnvelope.request;
      await getPersistentData(handlerInput);
      let attributes = handlerInput.attributesManager.getSessionAttributes();
      let speech, repromptSpeech;
      if (attributes.username == undefined || attributes.username == null) {
         speech = `Hey, welcome to nabaztag. I would love to learn a bit more about you. 
                  What is your name?`;
         repromptSpeech = `I would love to learn a bit more about you. Let's start by giving me your name.`;
      }
      else {
         speech = `Welcome back ${attributes.username}! How can I help you?`;
         repromptSpeech = `How can I help you? If you don\'t know what I can do, please ask, what can you do?`;
      }
      return speak(handlerInput, speech, repromptSpeech, request.type);
   }
};

// ===================================================================
// ---------------------- SINGLE ACTION INTENTS ----------------------
// ===================================================================
const TurnOnLampIntent = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;
      return request.type === 'IntentRequest'
         && request.intent.name === 'TurnOnLampIntent';
   },
   handle(handlerInput) {
      console.log('TurnOnLampIntent');
      const request = handlerInput.requestEnvelope.request;
      sendMsgToQueue("lamp_on");      
      
      let speech = `I\'m turning on your lights. `;
      let repromptSpeech = `I turned on the lights for you. `;
      return speak(handlerInput, speech, repromptSpeech, request.intent.name);
   },
};

const TurnOffLampIntent = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;
      return request.type === 'IntentRequest'
         && request.intent.name === 'TurnOffLampIntent';
   },
   handle(handlerInput) {
      console.log('TurnOffLampIntent');  
      const request = handlerInput.requestEnvelope.request;   
      sendMsgToQueue("lamp_off");
      
      let speech = `I\'m turning off your lights. `;
      let repromptSpeech = `I turned off the lights for you. `;
      return speak(handlerInput, speech, repromptSpeech, request.intent.name);
   },
};

const TurnUpHeatIntent = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;
      return request.type === 'IntentRequest'
         && request.intent.name === 'TurnUpHeatIntent';
   },
   handle(handlerInput) {
      console.log('TurnUpHeatIntent');
      const request = handlerInput.requestEnvelope.request;
      sendMsgToQueue("heating_on");
      
      let speech = `I\'ve turned up the heat for you. `;
      let repromptSpeech = `I turned up the heating. `
      return speak(handlerInput, speech, repromptSpeech, request.intent.name);
   },
};

const TurnOffHeatIntent = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;
      return request.type === 'IntentRequest'
         && request.intent.name === 'TurnOffHeatIntent';
   },
   handle(handlerInput) {
      console.log('TurnOffHeatIntent');
      const request = handlerInput.requestEnvelope.request;
      sendMsgToQueue("heating_off");
      
      let speech = `I turned off the heating. `;
      let repromptSpeech = `I turned off the heating. `
      return speak(handlerInput, speech, repromptSpeech, request.intent.name);
   },
};

const TurnOnCoolingIntent = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;

      return request.type === 'IntentRequest'
         && request.intent.name === 'TurnOnCoolingIntent';
   },
   handle(handlerInput) {
      console.log('TurnOnCoolingIntent');
      const request = handlerInput.requestEnvelope.request;
      sendMsgToQueue("cooling_on");
      
      let speech = `I\'m turning on the cooling for you. `;
      let repromptSpeech = `I have turned your cooling on. `;
      return speak(handlerInput, speech, repromptSpeech, request.intent.name);
   },
};

const TurnOffCoolingIntent = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;

      return request.type === 'IntentRequest'
         && request.intent.name === 'TurnOffCoolingIntent';
   },
   handle(handlerInput) {
      console.log('TurnOffCoolingIntent');
      const request = handlerInput.requestEnvelope.request;
      sendMsgToQueue("cooling_off");
      
      let speech = `I\'m turning off your cooling. `;
      let repromptSpeech = `I turned off your cooling. `;
      return speak(handlerInput, speech, repromptSpeech, request.intent.name);
   },
};

const WaterBoilerOnIntent = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;

      return request.type === 'IntentRequest'
         && request.intent.name === 'WaterBoilerOnIntent';
   },
   handle(handlerInput) {
      console.log('WaterBoilerOnIntent');
      const request = handlerInput.requestEnvelope.request;
      sendMsgToQueue("boiler_on");
      
      let speech = `I will turn on the water boiler for you. `;
      let repromptSpeech = `I turned on your water boiler. `;
      return speak(handlerInput, speech, repromptSpeech, request.intent.name);
   },
}

const WaterBoilerOffIntent = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;

      return request.type === 'IntentRequest'
         && request.intent.name === 'WaterBoilerOffIntent';
   },
   handle(handlerInput) {
      console.log('WaterBoilerOffIntent');
      const request = handlerInput.requestEnvelope.request;
      sendMsgToQueue("boiler_off");
      
      let speech = `I turned off the water boiler for you. `;
      let repromptSpeech = `I turned off your water boiler. `;
      return speak(handlerInput, speech, repromptSpeech, request.intent.name);
   },
}

const CurrentTemperatureIntent = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;

      return request.type === 'IntentRequest'
         && request.intent.name === 'CurrentTemperatureIntent';
   },
   async handle(handlerInput) {
      console.log('CurrentTemperatureIntent');
      const request = handlerInput.requestEnvelope.request;
      sendMsgToQueue("request_temp");
      let temp = await getCurrentTemp();
      
      let speech, repromptSpeech;
      if (temp != null) {
         speech = `The current temperature is ${temp} degrees Celcius. `;
         repromptSpeech = `The temperature is currently ${temp} degrees Celcius. `;
      }
      else {
         speech = `The temperature is currently unavailable. `;
         repromptSpeech = `The temperature is currently unavailable. `;
      }
      return speak(handlerInput, speech, repromptSpeech, request.intent.name);
   },
};

const TrainInformationIntent = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;

      return request.type === 'IntentRequest'
         && request.intent.name === 'TrainInformationIntent';
   },
   async handle(handlerInput) {
      console.log('TrainInformationIntent');
      const request = handlerInput.requestEnvelope.request;
      const attributes = handlerInput.attributesManager.getSessionAttributes();

      // get current time:
      let curDate = new Date();
      let curYear = curDate.getFullYear();
      let curMonth = curDate.getMonth() + 1; // + 1, starting from 0;
      let curDay  = curDate.getDate();
      let curHour = curDate.getHours() + 1; // Hours is based on England (+1 for Netherlands).
      let curMinutes = curDate.getMinutes();
      curMonth = (curMonth < 10 ? "0" : "") + curMonth; // 1 becomes 01;
      curDay = (curDay < 10 ? "0" : "") + curDay;
      let curTime = `${curYear}-${curMonth}-${curDay}T${curHour}:${curMinutes}`; 

      let trainMessages = await getTrainInfo(curTime);
      attributes.trainHTML = trainMessages.emailMsg;

      let speech = trainMessages.speech;
      return speak(handlerInput, speech, speech, request.intent.name);
   },
}

const CallMeIntent = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;

      return request.type === 'IntentRequest'
         && request.intent.name === 'CallMeIntent';
   },
   handle(handlerInput) {
      console.log('CallMeIntent');
      const request = handlerInput.requestEnvelope.request;
      let emailSubject = 'Call phone';
      let emailContent = 'Call phone';
      let receiver = 'trigger@applet.ifttt.com';
      sendEmail(emailSubject, emailContent, receiver);

      let speech = `I\'m calling your phone right now. `;
      let repromptSpeech = `I\'ve just called your phone. `;
      return speak(handlerInput, speech, repromptSpeech, request.intent.name);
   },
};

const TonightMoviesIntent = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;

      return request.type === 'IntentRequest'
         && request.intent.name === 'TonightMoviesIntent';
   },
   async handle(handlerInput) {
      console.log('TonightMoviesIntent');
      const request = handlerInput.requestEnvelope.request;
      let emailSubject = 'Films voor vanavond';
      let emailContent = await getTonightMovies();
      let receiver = 'nabalexa123@hotmail.com';
      sendEmail(emailSubject, emailContent, receiver);

      let speech = `I\'m sending you an email with the movies playing today. `;
      let repromptSpeech = `I\'ve just sent you an email with the movies playing today. `;
      return speak(handlerInput, speech, repromptSpeech, request.intent.name);
   },
};

const UsernameIntent = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;

      return request.type === 'IntentRequest'
         && request.intent.name === 'UsernameIntent';
   },
   handle(handlerInput) {
      console.log('UsernameIntent');
      const request = handlerInput.requestEnvelope.request;
      const attributes = handlerInput.attributesManager.getSessionAttributes();

      let username = request.intent.slots.username.value;
      attributes.username = username;

      let speech = `Your name is ${username}. Is that correct?`
      let repromptSpeech = `Is your name ${username}?`
      return speak(handlerInput, speech, repromptSpeech, request.intent.name);
   },
};

// ===================================================================
// ------------------- MULTI-EVENT ACTION INTENTS --------------------
// ===================================================================
const IAmHomeIntent = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;
      return request.type === 'IntentRequest'
         && request.intent.name === 'IAmHomeIntent';
   },
   async handle(handlerInput) {
      console.log('IAmHomeIntent');
      const request = handlerInput.requestEnvelope.request;

      sendMsgToQueue("lamp_on");
      sendMsgToQueue("request_temp");

      let emailSubject = 'Films voor vanavond';
      let emailContent = await getTonightMovies();
      let receiver = 'nabalexa123@hotmail.com';
      sendEmail(emailSubject, emailContent, receiver);
      
      let temp = await getCurrentTemp();
      let tempSpeech = '';
      if (temp != null) {
         if (temp >= 23) {
            sendMsgToQueue("cooling_on");
            tempSpeech = 'and cooling';
         }
         else {
            sendMsgToQueue("heating_on");
            tempSpeech = 'and heating';
         }
      }
   
      let speech = `Welcome home. I\'m turning on your lights ${tempSpeech}. 
                  Check your email for movies playing tonight. `;
      let repromptSpeech = `I\'ve turned on the lights ${tempSpeech}.
                           I also sent you an email with movies playing tonight. `;
      return speak(handlerInput, speech, repromptSpeech, request.intent.name);
   },
};

const WakingUpIntent = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;
      return request.type === 'IntentRequest'
         && request.intent.name === 'WakingUpIntent';
   },
   async handle(handlerInput) {
      console.log('WakingUpIntent');
      const request = handlerInput.requestEnvelope.request;
      let attributes = handlerInput.attributesManager.getSessionAttributes();
      let tempSpeech = '';
      sendMsgToQueue("lamp_on");
      sendMsgToQueue("boiler_on");
      sendMsgToQueue("request_temp");

      let temp = await getCurrentTemp();
      if (temp != null) {
         if (temp >= 23) {
            sendMsgToQueue("cooling_on");
            tempSpeech = 'and cooling';
         }
         else {
            sendMsgToQueue("heating_on");
            tempSpeech = 'and heating';
         }
      }

      // Get train info starting from next hour:
      let curDate = new Date();
      let curYear = curDate.getFullYear();
      let curMonth = curDate.getMonth() + 1; // + 1, starting from 0;
      let curDay  = curDate.getDate();
      let curHour = curDate.getHours() + 2; // Hours is based on England (+1 for Netherlands), +1 for next hour.
      let curMinutes = curDate.getMinutes();
      curMonth = (curMonth < 10 ? "0" : "") + curMonth; // 1 becomes 01;
      curDay = (curDay < 10 ? "0" : "") + curDay;
      let nextHourTime = `${curYear}-${curMonth}-${curDay}T${curHour}:${curMinutes}`; 
      
      let trainMessages = await getTrainInfo(nextHourTime);
      attributes.trainHTML = trainMessages.emailMsg;
      
      // email train info:
      let emailSubject = 'Train departure info';
      let emailContent = attributes.trainHTML;
      let receiver = 'nabalexa123@hotmail.com';
      sendEmail(emailSubject, emailContent, receiver);
      
      savePersistentData(handlerInput, attributes); // make sure to save the sessionData for future conversations.
      attributes.trainHTML = undefined;
      let speech = `Goodmorning. I\'m turning on your lights ${tempSpeech}.
                  I\'ve also turned on your water boiler.
                  Check your email for train information. `;
      let repromptSpeech = `I\'ve turned on the lights ${tempSpeech} for you. 
                  I also turned on the water boiler and sent you an email with train information. `;
      return speak(handlerInput, speech, repromptSpeech, request.intent.name);
   },
};

const GoingToBedIntent = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;

      return request.type === 'IntentRequest'
         && request.intent.name === 'GoingToBedIntent';
   },
   handle(handlerInput) {
      console.log('GoingToBedIntent');
      const request = handlerInput.requestEnvelope.request;

      sendMsgToQueue("lamp_off");

      let speech = `I\'m turning the lights off. Sleep well! `;
      let repromptSpeech = `I\'ve turned off the lights for you. `;
      return speak(handlerInput, speech, repromptSpeech, request.intent.name);
   },
};

const LeavingHouseIntent = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;

      return request.type === 'IntentRequest'
         && request.intent.name === 'LeavingHouseIntent';
   },
   async handle(handlerInput) {
      console.log('LeavingHouseIntent');
      const request = handlerInput.requestEnvelope.request;
      
      sendMsgToQueue("lamp_on");
      sendMsgToQueue("request_temp");
      
      let temp = await getCurrentTemp();
      let tempSpeech = '';
      if (temp != null) {
         if (temp >= 23) {
            sendMsgToQueue("cooling_off");
            tempSpeech = ' and cooling';
         }
         else {
            sendMsgToQueue("heating_off");
            tempSpeech = ' and heating';
         }
      }

      let speech = `I\'m turning off the lights${tempSpeech}. `;
      let repromptSpeech = `I\'ve turned off the lights${tempSpeech}. `;
      return speak(handlerInput, speech, repromptSpeech, request.intent.name);
   },
};

// ===================================================================
// ---------------- BUILD-IN/CONVERSATIONAL INTENTS ------------------
// ===================================================================
const CancelAndStopIntentHandler = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;
      return request.type === 'IntentRequest'
         && (request.intent.name === 'AMAZON.CancelIntent'
            || request.intent.name === 'AMAZON.StopIntent');
      },
   handle(handlerInput) {
      console.log('CancelAndStopIntentHandler');
      const attributes = handlerInput.attributesManager.getSessionAttributes();
      savePersistentData(handlerInput, attributes); // make sure to save the sessionData for future conversations.
      
      const speech = 'Thanks for using home control. See you next time!';
      return handlerInput.responseBuilder
         .speak(speech)
         .getResponse();
   },
};

const HelpIntentHandler = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;
      return request.type === 'IntentRequest'
         && request.intent.name === 'AMAZON.HelpIntent';
   },
   handle(handlerInput) {
      console.log('HelpIntent');
      const request = handlerInput.requestEnvelope.request;
      let speech = `I can monitor your lights, heating and cooling. 
                  I can also tell you the current temperature, or call you when you lose your phone. 
                  There are also multiple scenes created for you. 
                  Say for example, I am home, goodnight, goodmorning or I am leaving, and see what happens.`;
      setLastSpeech(handlerInput, speech, speech, request.intent.name);
      return handlerInput.responseBuilder
         .speak(speech)
         .reprompt(speech)
         .getResponse();
   },
};

const YesIntent = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;
      return request.type === 'IntentRequest'
         && request.intent.name === 'YesIntent';
   },
   handle(handlerInput) {
      console.log('YesIntent');
      const request = handlerInput.requestEnvelope.request;
      const attributes = handlerInput.attributesManager.getSessionAttributes();
      let speech;
      let repromptSpeech;
      if (attributes.lastIntent == 'TrainInformationIntent') { 
         let emailSubject = 'Train departure info';
         let emailContent = attributes.trainHTML;
         let receiver = 'nabalexa123@hotmail.com';
         sendEmail(emailSubject, emailContent, receiver);
         attributes.trainHTML = undefined;

         speech = 'I sent you an email containing the train information.';
         repromptSpeech = 'I sent you an email containing the train information.';
      }
      else if (attributes.lastIntent == 'UsernameIntent') {
         let username = attributes.username;
         speech = `Nice to meet you ${username}. How can I help you?`;
         repromptSpeech = 'How can I help you?';
      }
      else {
         speech = "Okay, what do you want me to help you with?";
         repromptSpeech = "What can I do for you? If you don\'t know what I can do, ask me, what can you help me with?";
      }

      savePersistentData(handlerInput, attributes); // make sure to save the sessionData for future conversations.
      return speak(handlerInput, speech, repromptSpeech, request.intent.name);
   },
};

const NoIntent = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;
      return request.type === 'IntentRequest'
         && request.intent.name === 'NoIntent';
   },
   handle(handlerInput) {
      console.log('NoIntent');
      const request = handlerInput.requestEnvelope.request;
      const attributes = handlerInput.attributesManager.getSessionAttributes();

      let speech;
      let repromptSpeech;
      if (attributes.lastIntent == 'TrainInformationIntent') {
         attributes.trainHTML = undefined;
         speech = 'Okay. I\'m not sending you an email.';
         repromptSpeech = speech;
      }
      else if (attributes.lastIntent == 'UsernameIntent') {
         attributes.username = undefined;
         speech = 'Okay, what is your name? If you don\'t want to give me your name, ask me something else.';
         repromptSpeech = speech;
      }
      else {
         return CancelAndStopIntentHandler.handle(handlerInput); // Quit conversation (Alexa asks in this case: can I help you with something else?).
      }

      savePersistentData(handlerInput, attributes); // make sure to save the sessionData for future conversations.
      return speak(handlerInput, speech, repromptSpeech, request.intent.name);
   },
};

const RepeatIntent = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;
      return request.type === 'IntentRequest'
         && request.intent.name === 'RepeatIntent';
   },
   handle(handlerInput) {
      console.log('RepeatIntent');
      let attributes = handlerInput.attributesManager.getSessionAttributes();
      
      return speak(handlerInput, attributes.lastSpeech, attributes.lastRepromptSpeech, attributes.lastIntent);
   },
};

const UnhandledIntent = {
    canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;
      return request.type === 'IntentRequest'
        && request.intent.name === 'AMAZON.FallbackIntent';
    },
    handle(handlerInput) {
      console.log('UnhandledIntent');
      const request = handlerInput.requestEnvelope.request;
      const attributes = handlerInput.attributesManager.getSessionAttributes();
      savePersistentData(handlerInput, attributes); // make sure to save the sessionData for future conversations.

      let speech = 'Oops, something went wrong. Please ask me something else to continue. You can also start by asking, help me.';
      return speak(handlerInput, speech, speech, request.intent.name);
    },
};

const SessionEndedHandler = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;
      return request.type === 'SessionEndedRequest';
   },
   handle(handlerInput) {
      const attributes = handlerInput.attributesManager.getSessionAttributes();
      savePersistentData(handlerInput, attributes); // make sure to save the sessionData for future conversations.
      console.log(`Session ended with reason: ${handlerInput.requestEnvelope.request.reason}`);
      return handlerInput.responseBuilder.getResponse();
   },
};

const ErrorHandler = {
   canHandle() {
      return true;
   },
   handle(handlerInput, error) {
      const request = handlerInput.requestEnvelope.request;
      const attributes = handlerInput.attributesManager.getSessionAttributes();
      savePersistentData(handlerInput, attributes); // make sure to save the sessionData for future conversations.
      console.log(`Error handled: ${error.message}`);
      console.log(`Original request was ${JSON.stringify(request, null, 2)}\n`);

      let speech = 'Oops, something went wrong. Please ask me something else to continue. You can also start by asking, help me.';
      setLastSpeech(handlerInput, speech, speech);
      return handlerInput.responseBuilder
         .speak(speech)
         .reprompt(speech)
         .getResponse();
   },
};

// EXPORT HANDLER FUNCTION
const skillBuilder = Alexa.SkillBuilders.standard();
exports.handler = skillBuilder
   .addRequestHandlers(LaunchRequestHandler,
                     TurnOnLampIntent,
                     TurnOffLampIntent,
                     TurnUpHeatIntent,
                     TurnOffHeatIntent,
                     TurnOnCoolingIntent,
                     TurnOffCoolingIntent,
                     WaterBoilerOnIntent,
                     WaterBoilerOffIntent,
                     CurrentTemperatureIntent,
                     TrainInformationIntent,
                     CallMeIntent,
                     TonightMoviesIntent,
                     UsernameIntent,
                     IAmHomeIntent,
                     WakingUpIntent,
                     GoingToBedIntent,
                     LeavingHouseIntent,
                     CancelAndStopIntentHandler,
                     HelpIntentHandler,
                     YesIntent,
                     NoIntent,
                     RepeatIntent,
                     UnhandledIntent,
                     SessionEndedHandler)
   .addErrorHandlers(ErrorHandler)
   .withTableName('2019_NabAlexa')
   .withAutoCreateTable(true)
   .lambda();

// ===================================================================
// ----------------------- HELPER FUNCTIONS --------------------------
// ===================================================================
function speak(handlerInput, speech, repromptSpeech, intent) {
   const attributes = handlerInput.attributesManager.getSessionAttributes();
   setLastSpeech(handlerInput, speech, repromptSpeech, intent);

   // Don't add a newQuestion in these cases!
   if (attributes.lastIntent == "LaunchRequest" || 
      attributes.lastIntent == "UsernameIntent" ||
      attributes.lastIntent == "RepeatIntent" ||
      attributes.lastIntent == "YesIntent" ||
      attributes.lastIntent == "NoIntent") {
      return handlerInput.responseBuilder
         .speak(speech)
         .reprompt(repromptSpeech)
         .getResponse()
   }

   // In every other case, add a question to the end.
   let newQuestion = ["Can I do anything else for you?",
                     "Can I help you with something else?",
                     "Is there anything else I can help you with?",
                     "What would you like me to do next?",
                     "Do you want me to do something else for you?",
                     "Would you like me to do something else for you?"];
   let newQuesSpeech = newQuestion[Math.floor(Math.random()*newQuestion.length)]; // Choose a random question-version. 
   let newQuesRepromptSpeech = newQuestion[Math.floor(Math.random()*newQuestion.length)];

   return handlerInput.responseBuilder
      .speak(speech + newQuesSpeech)
      .reprompt(repromptSpeech + newQuesRepromptSpeech)
      .getResponse()
}

function setLastSpeech(handlerInput, speech, repromptSpeech, intent) {
   let attributes = handlerInput.attributesManager.getSessionAttributes();
   attributes.lastSpeech = speech;
   attributes.lastRepromptSpeech = repromptSpeech;
   attributes.lastIntent = intent;
}

async function getPersistentData(handlerInput) {
   // Get the persistent data that is saved in DynamoDB-table 'NabAlexa'.
   // Save this data to the sessionAttributes. These attributes can be used during the conversation. 
   return await handlerInput.attributesManager.getPersistentAttributes()
      .then((attributes) => {
         // attributes = {}; // Clear all persistant attributes.
         handlerInput.attributesManager.setSessionAttributes(attributes); // paste pers. attributes to session attributes
      })
      .catch((error) => {
         reject(error);
      });
}

function savePersistentData(handlerInput, attributes) {
   handlerInput.attributesManager.setPersistentAttributes(attributes);
   handlerInput.attributesManager.savePersistentAttributes();
}

// ---------------- Getting information: ----------------
async function getTrainInfo(dateTime) {
   let from = "Leeuwarden";
   let to = "Heerenveen";
   let dep = "true"; // true for departure - false for arrival
   let hour = dateTime.substr(11, 2);
   let minutes = dateTime.substr(14, 2);

   var options = {
      "url": "http://webservices.ns.nl/ns-api-treinplanner?fromStation=" + from + "&toStation=" + 
            to + "&Departure=" + dep + "&dateTime=" + dateTime,
      "method": "GET",
      "headers": {
         "Authorization": "Basic aGllbTE1MDVAc3R1ZGVudC5uaGwubmw6SUxDcVVCLWRocFhsVU9UTnhJQWdvd1VOX05LOEM2MmhhMkowaTE4ZDV1aWZ4eXVoVFNQd0p3"
      }
   }
   console.log('url: ' + options.url);

   return rp(options)
      .then(function(xmlResult) {
         let speech = "";
         let emailMsg = "";
         let travelOptions = [];
         parseString(xmlResult, function (err, JSONResult) {
            for (let i = 0; i < JSONResult.ReisMogelijkheden.ReisMogelijkheid.length; i++) {
               let travelOption = JSONResult.ReisMogelijkheden.ReisMogelijkheid[i];
               let departure = travelOption.ActueleVertrekTijd;
               let arrival = travelOption.ActueleAankomstTijd;
               departure = departure.toString();
               arrival = arrival.toString();

               let tripDepHour = departure.substr(11, 2);
               let tripDepMinutes = departure.substr(14, 2);
               if ((tripDepHour == hour && tripDepMinutes >= minutes) || (tripDepHour == hour+1 && tripDepMinutes <= minutes)) {
                  // Get all the transporation types
                  for (let j = 0; j < travelOption.ReisDeel.length; j++) {
                     let travelPart = travelOption.ReisDeel[j];
                     let transpType = travelPart.VervoerType;
                     
                     // Get location and track for departure and arrival station
                     let lastStop = travelPart.ReisStop.length - 1;
                     var depLocation = travelPart.ReisStop[0].Naam;
                     var arrLocation = travelPart.ReisStop[lastStop].Naam;
                     let depTrack = travelPart.ReisStop[0].Spoor[0]._;
                     let arrTrack = travelPart.ReisStop[lastStop].Spoor[0]._;
                     let depTime = travelPart.ReisStop[0].Tijd;
                     let arrTime = travelPart.ReisStop[lastStop].Tijd;
                     
                     // Get readable time:
                     depTime = depTime.toString();
                     arrTime = arrTime.toString();
                     depTime = `${depTime.substr(11, 2)}:${depTime.substr(14, 2)}`;
                     arrTime = `${arrTime.substr(11, 2)}:${arrTime.substr(14, 2)}`;
                     
                     travelOptions.push(`take the ${transpType} that leaves at ${depTime} on track ${depTrack}. 
                                 This train will arrive at ${arrTime} on track ${arrTrack}.`);
                  }
               }
            }
            
            // Determine what the speech is going to be:
            speech += `There are ${travelOptions.length} options to travel from station ${depLocation} to station ${arrLocation}. `;
            if (travelOptions.length > 2) {
               speech += `The first option is to ${travelOptions[0]}
                           A second option is to ${travelOptions[1]}
                           Do you want me to send you the information of the other trains as well? `;
            } else if (travelOptions.length == 2) {
               speech += `The first option is to ${travelOptions[0]}
                           A second option is to ${travelOptions[1]}
                           Do you want me to send you this information via email? `;
            } else if (travelOptions.length == 1) {
               speech = `There\'s only one train available.
                           You have to ${travelOptions[0]}
                           Do you want me to send you this information via email? `;
            } else if (travelOptions.length == 0) 
               speech = `I\'m sorry, there are no trains leaving in the next hour. `;

            // Determine what the email will be:
            emailMsg += `<h3>Travel options for station ${depLocation} - station ${arrLocation}</h3>`;
            for (let i = 0; i < travelOptions.length; i++) {
               emailMsg += `<p><b>Option ${i+1}:</b><br>
                           ${travelOptions[i]}</p>`;
            }
         });

         let trainMessages = {
            speech: speech,
            emailMsg: emailMsg
         }
         return trainMessages;
      });
}

function getTonightMovies() {
   let date = new Date();
   let year = date.getFullYear();
   let month = date.getMonth() + 1; // + 1, starting from 0;
   let day  = date.getDate();

   month = (month < 10 ? "0" : "") + month; // 1 becomes 01;
   day = (day < 10 ? "0" : "") + day;

   // sorteer can be 0 (all movies), 1 (filmtips) or 2 (movie of the day)
   let moviesUrl = 'http://api.filmtotaal.nl/filmsoptv.xml?apikey=l2qqhgafuem53h5ecrd4pd9wq2gtb2pp&dag=' + day + '-' + month + '-' + year + '&sorteer=0';
   let options = {
      uri: moviesUrl,
      xml: true
   };

   return rp(options)
      .then(function(xmlResult) {
         // Get movie descriptions:
         var moviesHTML = "";
         parseString(xmlResult, function (err, JSONResult) {            
            for (var i = 0; i < JSONResult.filmsoptv.film.length; i++) {
               var title = JSONResult.filmsoptv.film[i].titel;
               var unixTime = JSONResult.filmsoptv.film[i].starttijd;
               var date = new Date(unixTime*1000);
               var startTime = date.getHours() + ':' + date.getMinutes() + ' uur';
               var channel = JSONResult.filmsoptv.film[i].zender;
               var movieSummary = JSONResult.filmsoptv.film[i].synopsis;

               var movieDesc = `<h3>${title} speelt om ${startTime} op ${channel}.</h3>
                                 <p>Omschrijving:</p>
                                 <p>${movieSummary}</p>`;
               
               moviesHTML += movieDesc;
            }
         });
         return moviesHTML;
      });
}

async function requestTempFromQueue() {
   var params = {
      QueueUrl: queueURL,
      MaxNumberOfMessages: 1
   };

   const data = await sqs.receiveMessage(params).promise();
   let temp = null;
   if (data.Messages) {
      console.log("Number of messages received: " + data.Messages.length);
      console.log("Received message: " + JSON.stringify(data.Messages[0]));
      
      var lastMessage = data.Messages[0].Body;
      console.log("Message body: " + lastMessage);

      if (lastMessage.startsWith("temp_", 0)) {
         temp = lastMessage.substr(5, lastMessage.length);

         var deleteParams = {
            QueueUrl: queueURL,
            ReceiptHandle: data.Messages[0].ReceiptHandle
         }
         sqs.deleteMessage(deleteParams, function(err, data) {
            if (err)
               console.log("Delete error: ", err);
            else 
               console.log("Message deleted: ", data);
         })
         return temp;
      }
      else {
         return temp;
      }
   }
   else {
      console.log("No messages received");
      return temp;
   }
}

async function getCurrentTemp() {
   let temp = null;
   for (var i = 0; i < 7; i++) { // 7 requests are approx. 25 seconds max.
      sleep(1000);
      temp = await requestTempFromQueue();

      if (temp != null || i == 10)
         break;
   }
   return temp;
}

// ---------------- Sending to queue or email: ----------------
function sendMsgToQueue(msg) {
   var sqsParams = {
      MessageBody: JSON.stringify(msg),
      QueueUrl: queueURL
   };

   sqs.sendMessage(sqsParams, function(err, data) {
      if (err) {
         console.log('ERR', err);
      }
      console.log(data);
   });
}

function sendEmail(subject, content, receiver) {
   
   
   transporter.sendMail(mailOptions, function(error, info){
      if (error) {
         console.log("Error: " + error);
      } else {
         console.log('Email sent: ' + info.response);
      }
   });
}
