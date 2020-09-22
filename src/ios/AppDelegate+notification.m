//
//  AppDelegate+notification.m
//  Personas
//
//  Created by Andres Felipe Vasquez Saldarriaga on 22/09/20.
//

#import "AppDelegate+notification.h"
//#import "PushPlugin.h"
#import <objc/runtime.h>
#import <didm_auth_sdk_iOS/didm_auth_sdk_iOS.h>


@implementation AppDelegate (notification)


+ (void)load {
    Method original = class_getInstanceMethod(self, @selector(application:didFinishLaunchingWithOptions:));
    Method todo1 = class_getInstanceMethod(self, @selector(application:todo1DidFinishLaunchingWithOptions:));
    method_exchangeImplementations(original, todo1);
}


- (BOOL)application:(UIApplication *)application todo1DidFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
    UNUserNotificationCenter *center = [UNUserNotificationCenter currentNotificationCenter];
       center.delegate = self;
       [center requestAuthorizationWithOptions:(UNAuthorizationOptionSound | UNAuthorizationOptionAlert | UNAuthorizationOptionBadge) completionHandler:^(BOOL granted, NSError * _Nullable error){
       if(!error){
       [[UIApplication sharedApplication] registerForRemoteNotifications]; }
       }];
       
       
       [center setNotificationCategories:[[DetectID sdk] getUNNotificationActionCategoriesForNotifications]];
    
    return YES;
}

-(void)userNotificationCenter:(UNUserNotificationCenter *)center willPresentNotification:(UNNotification *)notification withCompletionHandler:(void (^)(UNNotificationPresentationOptions options))completionHandler{
[[DetectID sdk] subscribePayload:notification withCompletionHandler:completionHandler];
}
-(void)userNotificationCenter:(UNUserNotificationCenter *)center didReceiveNotificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void(^)())completionHandler{
[[DetectID sdk] handleActionWithIdentifier:response];
}


- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    [[DetectID sdk] receivePushServiceId:deviceToken];
}

- (void)application:(UIApplication*)application didFailToRegisterForRemoteNotificationsWithError:(NSError*)error{ //management error
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler{
    
    dispatch_async(dispatch_get_main_queue(), ^{
        [[DetectID sdk]subscribePayload:userInfo];
    });
}


- (void)application:(UIApplication *)application handleActionWithIdentifier:(NSString *)identifier forRemoteNotification:(NSDictionary *)userInfo completionHandler:(void (^)(void))completionHandler{
[[DetectID sdk] handleActionWithIdentifier:identifier forNotification:userInfo];
completionHandler();
    
}

@end


