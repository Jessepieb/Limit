'use strict';
const Alexa = require('ask-sdk');
const aws = require('aws-sdk');
const rp = require('request-promise');
var nodemailer = require('nodemailer');
const sleep = require('util').promisify(setTimeout);

var sqs = new aws.SQS({region:'us-west-2'});
var parseString = require('xml2js').parseString;

var lastSpeech;
var lastRepromptSpeech;

const LaunchRequestHandler = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;
      return request.type === 'LaunchRequest';
         // || request.type === 'IntentRequest'
         //   && request.intent.name === 'MainMenuIntent';
   },
   handle(handlerInput) {
      console.log('LaunchRequest');
      let attributes = handlerInput.attributesManager.getSessionAttributes();
      // attributes = {}; // Clear all persistant attributes.
      
      // create persistant data: this way we can save user's name for later use. 
      let name = "David";
      let speech = `Hello ${name}, how can I help you?`;
      let repromptSpeech = `How can I help you? If you don\'t know what I can do, please ask, what can you do?`;
      return handlerInput.responseBuilder
         .speak(speech)
         .reprompt(repromptSpeech)
         .getResponse();
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
      //let attributes = handlerInput.attributesManager.getSessionAttributes();
      // turnOnLamp();

      sendMsgToQueue("lamp_on");      
      let speech = 'Here you go.';
      //setLastSpeech(handlerInput, speech, repromptSpeech);

      return handlerInput.responseBuilder
         .speak(speech)
         .reprompt(speech)
         .getResponse();
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
      //let attributes = handlerInput.attributesManager.getSessionAttributes();
      // turnOffLamp();
      
      sendMsgToQueue("lamp_off");
      let speech = 'Turning off your lamp.';
      //setLastSpeech(handlerInput, speech, repromptSpeech);

      return handlerInput.responseBuilder
         .speak(speech)
         .reprompt(speech)
         .getResponse();
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
      //let attributes = handlerInput.attributesManager.getSessionAttributes();
      // turnUpHeat();

      sendMsgToQueue("heat_up");
      let speech = 'Turning up the heat.';
      //setLastSpeech(handlerInput, speech, repromptSpeech);

      return handlerInput.responseBuilder
         .speak(speech)
         .reprompt(speech)
         .getResponse();
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
      //let attributes = handlerInput.attributesManager.getSessionAttributes();
      // turnOnCooling();

      sendMsgToQueue("cooling_on");
      let speech = 'Turning on the cooling.';
      //setLastSpeech(handlerInput, speech, repromptSpeech);

      return handlerInput.responseBuilder
         .speak(speech)
         .reprompt(speech)
         .getResponse();
   },
};

const CurrentTemperatureIntent = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;

      return request.type === 'IntentRequest'
         && request.intent.name === 'CurrentTemperatureIntent';
   },
   async handle(handlerInput) {
      console.log('CurrentTemperatureIntent');
      //let attributes = handlerInput.attributesManager.getSessionAttributes();

      sendMsgToQueue("request_temp");
      let temp = await getCurrentTemp();
      let speech;
      if (temp != null)
         speech = `The current temperature is ${temp} degrees Celcius.`;
      else 
         speech = "The temperature is currently unavailable.";
      //let speech =  await getCurrentTemp();
      return handlerInput.responseBuilder
         .speak(speech)
         .reprompt(speech)
         .getResponse();
   },
};

// "Get train information" 
// This question will give you information about the trains from your home to work for the next hour.
const TrainInformationIntent = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;

      return request.type === 'IntentRequest'
         && request.intent.name === 'TrainInformationIntent';
   },
   async handle(handlerInput) {
      console.log('TrainInformationIntent');
      //let attributes = handlerInput.attributesManager.getSessionAttributes();

      // let speech = await setInterval(getCurrentTemp, 1000);
      let speech =  await getTrainInfo();
      return handlerInput.responseBuilder
         .speak(speech)
         .reprompt(speech)
         .getResponse();
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
      //let attributes = handlerInput.attributesManager.getSessionAttributes();

      sendPhoneCallEmail();
      let speech = `Calling...`
      return handlerInput.responseBuilder
         .speak(speech)
         .reprompt(speech)
         .getResponse();
   },
};

// ===================================================================
// ---------------------- EVENT ACTION INTENTS -----------------------
// ===================================================================
const IAmHomeIntent = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;

      return request.type === 'IntentRequest'
         && request.intent.name === 'IAmHomeIntent';
   },
   async handle(handlerInput) {
      console.log('IAmHomeIntent');
      //let attributes = handlerInput.attributesManager.getSessionAttributes();

      sendMsgToQueue("lamp_on");
      sendMsgToQueue("request_temp");
      sendTonightMovies();
      let temp = await getCurrentTemp();
      let tempSpeech = '';
      if (temp != null) {
         if (temp >= 23) {
            sendMsgToQueue("cooling_on");
            tempSpeech = 'and cooling';
         }
         else {
            sendMsgToQueue("heat_up");
            tempSpeech = 'and heating';
         }
      }
   
      let speech = `Welcome home. I\'m turning on your lights ${tempSpeech}. 
                  Check your phone for movies playing tonight.`
      return handlerInput.responseBuilder
         .speak(speech)
         .reprompt(speech)
         .getResponse();
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
      //let attributes = handlerInput.attributesManager.getSessionAttributes();

      sendMsgToQueue("lamp_off");
      let speech = `I\'m turning the lights off. Sleep well!`;
      return handlerInput.responseBuilder
         .speak(speech)
         .reprompt(speech)
         .getResponse();
   },
};

// WAKING UP (turn on lights, turn up heat/cooling, water boiler)
const WakingUpIntent = {
   canHandle(handlerInput) {
      const request = handlerInput.requestEnvelope.request;

      return request.type === 'IntentRequest'
         && request.intent.name === 'WakingUpIntent';
   },
   handle(handlerInput) {
      console.log('WakingUpIntent');
      //let attributes = handlerInput.attributesManager.getSessionAttributes();
      let tempSpeech = '';
      sendMsgToQueue("lamp_on");
      sendMsgToQueue("boiler_on");
      sendMsgToQueue("request_temp");
      
      if (temp != null) {
         if (temp >= 23) {
            //turnOnCooling();
            sendMsgToQueue("cooling_on");
            tempSpeech = 'and cooling';
         }
         else {
            //turnUpHeat();
            sendMsgToQueue("heat_up");
            tempSpeech = 'and heating';
         }
      }
      // turnOnLamp();
      // turnOnWaterBoiler();
      let speech = `Goodmorning. I\'m turning on your lights ${tempSpeech}. 
                  I\'ve also turned on your water boiler.`;
      return handlerInput.responseBuilder
         .speak(speech)
         .reprompt(speech)
         .getResponse();
   },
};
// WATCHING A MOVIE (dimmed lighting, in rest of house lights are off)
// READING A BOOK (relexing music in background, good lighting)
// I'M LEAVING (turn off everything, including cooling/heating)


// ===================================================================
// ------------------------- HELPER INTENTS --------------------------
// ===================================================================
const CancelAndStopIntentHandler = {
    canHandle(handlerInput) {
        return handlerInput.requestEnvelope.request.type === 'IntentRequest'
          && (handlerInput.requestEnvelope.request.intent.name === 'AMAZON.CancelIntent'
            || handlerInput.requestEnvelope.request.intent.name === 'AMAZON.StopIntent');
    },
    handle(handlerInput) {
      const speech = 'Thanks for using home control. See you next time!';

      return handlerInput.responseBuilder
        .speak(speech)
        .getResponse();
    },
};

const HelpIntentHandler = {
    canHandle(handlerInput) {
      return handlerInput.requestEnvelope.request.type === 'IntentRequest'
        && handlerInput.requestEnvelope.request.intent.name === 'AMAZON.HelpIntent';
    },
    handle(handlerInput) {
      console.log('HelpIntent');
      let speech = `I can monitor your lights, heating and cooling. 
                  I can also tell you the current temperature, or call you when you lose your phone. 
                  There are also multiple scenes created for you. 
                  Say for example, I am home, goodnight, goodmorning or I am leaving, and see what happens.`;
      
      return handlerInput.responseBuilder
        .speak(speech)
        .reprompt(speech)
        .getResponse();
    },
};

const UnhandledIntent = {
    canHandle(handlerInput) {
      return handlerInput.requestEnvelope.request.type === 'IntentRequest'
        && handlerInput.requestEnvelope.request.intent.name === 'AMAZON.FallbackIntent';
    },
    handle(handlerInput) {
      console.log('UnhandledIntent');
      let speech = 'Something is wrong. You are in the unhandled intent right now...';
      
      return handlerInput.responseBuilder
        .speak(speech)
        .reprompt(speech)
        .getResponse();
    },
};

const SessionEndedHandler = {
  canHandle(handlerInput) {
    const request = handlerInput.requestEnvelope.request;
    return request.type === 'SessionEndedRequest';
  },
  handle(handlerInput) {
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
    console.log(`Error handled: ${error.message}`);
    console.log(`Original request was ${JSON.stringify(request, null, 2)}\n`);

    return handlerInput.responseBuilder
      .speak('Error in index.js')
      .reprompt('Error in index.js')
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
                     TurnOnCoolingIntent,
                     CurrentTemperatureIntent,
                     TrainInformationIntent,
                     IAmHomeIntent,
                     GoingToBedIntent,
                     CallMeIntent,
                     WakingUpIntent,
                     CancelAndStopIntentHandler,
                     HelpIntentHandler,
                     UnhandledIntent,
                     SessionEndedHandler)
   .addErrorHandlers(ErrorHandler)
   //.withTableName('2018_Democracy_Live')
   //.withAutoCreateTable(true)
   .lambda();

// ===================================================================
// ----------------------- HELPER FUNCTIONS --------------------------
// ===================================================================
function sendTonightMovies() {
   var date = new Date();
   var year = date.getFullYear();
   var month = date.getMonth() + 1; // + 1, starting from 0;
   var day  = date.getDate();

   month = (month < 10 ? "0" : "") + month; // 1 becomes 01;
   day = (day < 10 ? "0" : "") + day;

   // sorteer can be 0 (all movies), 1 (filmtips) or 2 (movie of the day)
   var moviesUrl = 'http://api.filmtotaal.nl/filmsoptv.xml?apikey=l2qqhgafuem53h5ecrd4pd9wq2gtb2pp&dag=' + day + '-' + month + '-' + year + '&sorteer=0';
   let options = {
      uri: moviesUrl,
      xml: true
   };

   return rp(options)
      .then(function(xmlResult) {
         // Get movie descriptions:
         var messageToSend = "";
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
               
               messageToSend += movieDesc;
            }
         });
         
         // Send email:
         var transporter = nodemailer.createTransport({
            service: 'hotmail',
            auth: {
               user: 'nabalexa123@hotmail.com',
               pass: 'Informatica4'
            }
         });
          
         var mailOptions = {
            from: 'nabalexa123@hotmail.com',
            to: 'nabalexa123@hotmail.com',
            subject: 'Films voor vanavond',
            html: messageToSend
         };
         
         transporter.sendMail(mailOptions, function(error, info){
            if (error) {
               console.log("Error: " + error);
            } else {
               console.log('Email sent: ' + info.response);
            }
         });

         //return messageToSend; // is this required?
      });
}

function sendMsgToQueue(msg) {
   var sqsParams = {
      MessageBody: JSON.stringify(msg),
      QueueUrl: queueURL
   };

   sqs.sendMessage(sqsParams, function(err, data) {
      if (err) {
         console.log('ERR', err);
      }
      console.log('sqs sendMessage response-data: ' + data);
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

async function getTrainInfo() {
   let from = "Leeuwarden";
   let to = "Heerenveen";
   let dep = "true"; // true for departure - false for arrival
   var date = new Date(); // can be either departure of arrival
   let year = date.getFullYear();
   let month = date.getMonth() + 1; // + 1, starting from 0;
   let day  = date.getDate();
   let hours = date.getHours();
   let minutes = date.getMinutes();
   month = (month < 10 ? "0" : "") + month; // 1 becomes 01;
   day = (day < 10 ? "0" : "") + day;

   let time = `${year}-${month}-${day}T${hours}:${minutes}`; 
   var options = {
      // "async": true,
      // "crossDomain": true,
      // "url": "https://cors-anywhere.herokuapp.com/http://webservices.ns.nl/ns-api-treinplanner?fromStation="
      // + from + "&toStation=" + to + "&Departure=" + dep + "&dateTime=" + time, //Verander prompt() naar een andere input methode.
      "url": "http://webservices.ns.nl/ns-api-treinplanner?fromStation=" + from + "&toStation=" + 
            to + "&Departure=" + dep + "&dateTime=" + time, //Verander prompt() naar een andere input methode.
      "method": "GET",
      "headers": {
         "Authorization": "Basic aGllbTE1MDVAc3R1ZGVudC5uaGwubmw6SUxDcVVCLWRocFhsVU9UTnhJQWdvd1VOX05LOEM2MmhhMkowaTE4ZDV1aWZ4eXVoVFNQd0p3",
         // "cache-control": "no-cache",
         // "Postman-Token": "1b2678c7-ed08-403f-bf14-7db0a5ef205e"
      }
   }

   console.log('url: ' + options.url);

   $.ajax(options).done(function(result) {
      console.log('result: ' + result);
   })
}

function sendPhoneCallEmail() {
   
   
   transporter.sendMail(mailOptions, function(error, info){
      if (error) {
         console.log("Error: " + error);
      } else {
         console.log('Email sent: ' + info.response);
      }
   });
}