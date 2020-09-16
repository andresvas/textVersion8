/********* DetectIdPlugin.m Cordova Plugin Implementation *******/
//
//  DetectIdPlugin.h
//
#import <Foundation/Foundation.h>
#import "DetectIdPlugin.h"

@implementation DetectIdPlugin
NSString * DATE_FORMAT = @"MM/dd/yyyy, HH:mm:ss a";
BOOL mMobileAPIInitialized;
NSString *message;
NSString *callbackId;
int timeDisplayed;
bool isRunning = false;
CDVInvokedUrlCommand* lastCallback;

- (void)pluginInitialize{
    //[[DetectID sdk] enableRegistrationServerResponseAlerts:false]; DEPRECATED
    [[DetectID sdk] enableSecureCertificateValidationProtocol:false];
    [[DetectID sdk] OTP_API];
}

- (void)INIT:(CDVInvokedUrlCommand*)command {
    [self.commandDelegate runInBackground:^{
        if( [command.arguments count] == 1 && [command.arguments objectAtIndex:0] != (id)[NSNull null]){
            lastCallback = command;
            //InitParams *params = [[[InitParamsBuilder new] buildDidUrl:[command.arguments objectAtIndex:0]] buildParams]; DISABLED

           // [[DetectID sdk] initDIDServerWithParams: (InitParams *) params ];
            [[DetectID sdk] didInit];
            
            mMobileAPIInitialized = true;
            CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
            [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        } else{
            [self sendErrorResult:@"Invalid Arguments" callbackId:command.callbackId];
        }
    }];
}

- (void)REGISTER_DEVICE_BY_CODE:(CDVInvokedUrlCommand*)command {
    if( [command.arguments count] == 1 && [command.arguments objectAtIndex:0] != (id)[NSNull null]){
        lastCallback = command;
        [[DetectID sdk] setDeviceRegistrationServerResponseDelegate:(id)self];

        #pragma mark To do: review this method TODO1.
       // [[DetectID sdk] enableRegistrationServerResponseAlerts:false]; DEPRECATED
        [[DetectID sdk] enableSecureCertificateValidationProtocol:false];

       // [[DetectID sdk] deviceRegistrationByCode:[command.arguments objectAtIndex:0]]; DEPRECATED
        NSString *finalURL = [NSString stringWithFormat:@"%@%@", @"https://otp.bancolombia.com/detect/public/registration/mobileServices.htm?code=", [command.arguments objectAtIndex:0]] ;
        [[DetectID sdk] didRegistrationWithUrl:finalURL];
        
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_NO_RESULT];
        [pluginResult setKeepCallback: [NSNumber numberWithInt:1]];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    } else{
        [self sendErrorResult:@"Invalid Arguments" callbackId:command.callbackId];
    }
}

- (void)onRegistrationResponse:(NSString *) result{
    if(lastCallback != (id)[NSNull null]){
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:result];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:lastCallback.callbackId];
    }
}

- (void)GET_SHARED_DEVICE_ID:(CDVInvokedUrlCommand*)command {
    [self.commandDelegate runInBackground:^{
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:[[DetectID sdk] getSharedDeviceID]];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)GET_DEVICE_ID:(CDVInvokedUrlCommand*)command {
    [self.commandDelegate runInBackground:^{
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:[[DetectID sdk] getDeviceID]];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)GET_ACCOUNTS:(CDVInvokedUrlCommand*)command {
    [self.commandDelegate runInBackground:^{
        @try {
            NSArray* accounts = [[DetectID sdk] getAccounts];
            NSMutableArray* retAccounts = [[NSMutableArray alloc] init];
            for (Account* acc in accounts) {
                [retAccounts addObject:acc.username];
            }
//            NSData *jsonData = [NSJSONSerialization dataWithJSONObject:retAccounts options:NSJSONWritingPrettyPrinted error:&e];
//            NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
            CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArray:retAccounts];
            [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        }@catch (NSException *exception) {
            NSLog(@"%@", exception.reason);
            [self sendErrorResult:exception.reason callbackId:command.callbackId];
        }
    }];
}

- (void)REMOVE_ACCOUNT:(CDVInvokedUrlCommand*)command {
    [self.commandDelegate runInBackground:^{
        @try {
            if( [command.arguments count] == 1
               && [command.arguments objectAtIndex:0] != (id)[NSNull null]){
                Account * acc = [self getAccount:[command.arguments objectAtIndex:0]];
                if(acc != nil){
                    [[DetectID sdk] removeAccount:acc];
                    [self sendSuccess:command.callbackId];
                }else{
                    [self sendErrorResult:@"Account not found" callbackId:command.callbackId];
                }
            } else{
                [self sendErrorResult:@"Invalid Arguments" callbackId:command.callbackId];
            }
        }@catch (NSException *exception) {
            NSLog(@"%@", exception.reason);
            [self sendErrorResult:exception.reason callbackId:command.callbackId];
        }
    }];
}

- (void)SET_ACCOUNT_USERNAME:(CDVInvokedUrlCommand*)command {
    [self.commandDelegate runInBackground:^{
        @try {
            if( [command.arguments count] == 2
               && [command.arguments objectAtIndex:0] != (id)[NSNull null]
               && [command.arguments objectAtIndex:1] != (id)[NSNull null]){
                NSString * oldUsername = [command.arguments objectAtIndex:0] ;
                NSString * newUsername = [command.arguments objectAtIndex:1] ;
                Account * acc = [self getAccount:oldUsername];
                if(acc != nil){
                    [[DetectID sdk] setAccountUsername:newUsername forAccount:acc];
                    [self sendSuccess:command.callbackId];
                }else{
                    [self sendErrorResult:@"Account with empty username not found" callbackId:command.callbackId];
                }
            } else{
                [self sendErrorResult:@"Invalid Arguments" callbackId:command.callbackId];
            }
        }@catch (NSException *exception) {
            NSLog(@"%@", exception.reason);
            [self sendErrorResult:exception.reason callbackId:command.callbackId];
        }
    }];
}

- (void)HAS_ACCOUNTS:(CDVInvokedUrlCommand*)command {
    [self.commandDelegate runInBackground:^{
        @try {
            if( [command.arguments count] == 0){
                NSString * message=@"false";
                if([[[DetectID sdk] getAccounts] count] > 0){
                    message=@"true";
                }
                CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:message];
                [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
            } else{
                [self sendErrorResult:@"Invalid Arguments" callbackId:command.callbackId];
            }
        }@catch (NSException *exception) {
            NSLog(@"%@", exception.reason);
            [self sendErrorResult:exception.reason callbackId:command.callbackId];
        }
    }];
}

- (void)OTP_GET_TOKEN_VALUE:(CDVInvokedUrlCommand*)command {
    [self.commandDelegate runInBackground:^{
        @try {
            if( [command.arguments count] == 1
               && [command.arguments objectAtIndex:0] != (id)[NSNull null]){
                Account * acc = [self getAccount:[command.arguments objectAtIndex:0]];
                if(acc != nil){
                    NSString * token = [[[DetectID sdk] OTP_API] getTokenValue:acc];
                    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString: token];
                    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
                }else{
                    [self sendErrorResult:@"Account not found" callbackId:command.callbackId];
                }
            } else{
                [self sendErrorResult:@"Invalid Arguments" callbackId:command.callbackId];
            }
        }@catch (NSException *exception) {
            NSLog(@"%@", exception.reason);
            [self sendErrorResult:exception.reason callbackId:command.callbackId];
        }
    }];
}

- (void)OTP_GET_TOKEN_TIMELEFT:(CDVInvokedUrlCommand*)command {
    [self.commandDelegate runInBackground:^{
        @try {
            if( [command.arguments count] == 1
               && [command.arguments objectAtIndex:0] != (id)[NSNull null]){
                Account * acc = [self getAccount:[command.arguments objectAtIndex:0]];
                if(acc != nil){
                    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsInt:[[[DetectID sdk] OTP_API] getTokenTimeStepValue:acc]];
                    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
                }else{
                    [self sendErrorResult:@"Account not found"callbackId:command.callbackId];
                }
            } else{
                [self sendErrorResult:@"Invalid Arguments" callbackId:command.callbackId];
            }
        }@catch (NSException *exception) {
            NSLog(@"%@", exception.reason);
            [self sendErrorResult:exception.reason callbackId:command.callbackId];
        }
    }];
}

-(Account*)getAccount:(NSString*)withUsername{
    NSArray* accounts = [[DetectID sdk] getAccounts];
    for(Account* acc in accounts){
        if([withUsername isEqualToString:acc.username] || (acc.username == nil && [withUsername isEqualToString:@""])){
            return acc;
        }
    }
    return nil;
}

-(void)sendErrorResult:(NSString *)message callbackId:(NSString *)callbackId {
    NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];
    [dictionary setObject:message forKey:@"errorMessage"];
    [dictionary setObject:[NSNumber numberWithInt:1] forKey:@"errorCode"];
    [dictionary setObject:[NSNumber numberWithBool:false] forKey:@"success"];
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                                      messageAsString:[dictionary description]];

    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

-(void)sendSuccessResult:(NSMutableDictionary *)dictionary callbackId:(NSString *)callbackId {
    NSError *error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dictionary
                                                       options:0 //NSJSONWritingPrettyPrinted // Pass 0 if you don't care about the readability of the generated string
                                                         error:&error];
    NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                                                      messageAsString:jsonString];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

-(void)sendSuccess:(NSString *)callbackId {
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}
//
//+(NSDate *)parseDate:(NSString *)date{
//    NSDateFormatter* formatter = [[NSDateFormatter alloc] init];
//    formatter.dateFormat = @"yyyy-MM-dd HH:mm:ss";
//    return [formatter dateFromString:date];
//}

-(long)getEpochFromDateString:(NSString *)dateS{
    NSDateFormatter* formatter = [[NSDateFormatter alloc] init];
    formatter.dateFormat = DATE_FORMAT;
    return [[formatter dateFromString:dateS] timeIntervalSince1970];
}

-(NSDate *)getDateFromEpoch:(NSNumber *)dateL{
    return [NSDate dateWithTimeIntervalSince1970:[dateL longValue]];
}

-(NSString *)getDateStringFromEpoch:(NSNumber *)dateL{
    NSDateFormatter* formatter = [[NSDateFormatter alloc] init];
    formatter.dateFormat = @"MM/dd/yyyy, HH:mm:ss a";
    return [formatter stringFromDate:[self getDateFromEpoch:dateL]];
}

-(NSDate *)getDateFromEpochL:(long)dateL{
    return [NSDate dateWithTimeIntervalSince1970:dateL];
}

-(NSString *)getDateStringFromEpochL:(long)dateL{
    NSDateFormatter* formatter = [[NSDateFormatter alloc] init];
    formatter.dateFormat = @"MM/dd/yyyy, HH:mm:ss a";
    return [formatter stringFromDate:[self getDateFromEpochL:dateL]];
}

@end
