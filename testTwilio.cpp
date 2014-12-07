#include <stdlib>
#include <iostream>










// Twilio REST API version
const string API_VERSION = "2010-04-01";
// Twilio Account Sid
const string ACCOUNT_SID = "ACe66fdfea3213faac8a681e2538b67eb3"
// Twilio Auth Token
const string ACCOUNT_TOKEN = "05bc9ebdb0ada55926d2ea9aa9b72a38";
// Twilio SMS URL
const string SMS_URL = "http://www.example.com/sms";





Utils utils (ACCOUNT_SID, ACCOUNT_TOKEN);
// vars should contain the POST parameters received. It is a vector of Var structures: key/value.
vector<Var> vars;
vars.push_back(Var("AccountSid", "xxxx"));
vars.push_back(Var("Body", "xxxx"));
//[ add all POST parameters received ]
// signature is the hash we received in the X-Twilio-Signature header
bool valid = utils.validateRequest(signature, SMS_URL, vars);
// if(!valid)
// [ handle invalid request here ]
