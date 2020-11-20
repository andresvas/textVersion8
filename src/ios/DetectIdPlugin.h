#ifndef DetectIdPlugin_h
#define DetectIdPlugin_h
#import <Cordova/CDVPlugin.h>
#import <didm_auth_sdk_iOS/didm_auth_sdk_iOS.h>

@interface DetectIdPlugin : CDVPlugin <PushTransactionOpenDelegate,PushTransactionServerResponseDelegate>

- (void)INIT:(CDVInvokedUrlCommand*)command;
- (void)REGISTER_DEVICE_BY_CODE:(CDVInvokedUrlCommand*)command;

- (void)GET_SHARED_DEVICE_ID:(CDVInvokedUrlCommand*)command;
- (void)GET_DEVICE_ID:(CDVInvokedUrlCommand*)command;
- (void)GET_ACCOUNTS:(CDVInvokedUrlCommand*)command;
- (void)SET_ACCOUNT_USERNAME:(CDVInvokedUrlCommand*)command;
- (void)REMOVE_ACCOUNT:(CDVInvokedUrlCommand*)command;
- (void)HAS_ACCOUNTS:(CDVInvokedUrlCommand*)command;

- (void)OTP_GET_TOKEN_VALUE:(CDVInvokedUrlCommand*)command;
- (void)OTP_GET_TOKEN_TIMELEFT:(CDVInvokedUrlCommand*)command;

#endif /* DetectIdPlugin_h */
@end
