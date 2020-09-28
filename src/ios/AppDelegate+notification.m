//
//  appDelegate+notification.m
//  Personas
//
//  Created by Andres Felipe Vasquez Saldarriaga on 22/09/20.
//

#import "appDelegate+notification.h"
//#import "PushPlugin.h"
#import <objc/runtime.h>
//#import <didm_auth_sdk_iOS/didm_auth_sdk_iOS.h>


#define kApplicationInBackgroundKey @"applicationInBackground"

@implementation AppDelegate (notification)


+ (void)load {
    Method original = class_getInstanceMethod(self, @selector(application:didFinishLaunchingWithOptions:));
    Method todo1 = class_getInstanceMethod(self, @selector(application:todo1DidFinishLaunchingWithOptions:));
    method_exchangeImplementations(original, todo1);
}

- (BOOL)application:(UIApplication *)application todo1DidFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
    [self application:application todo1DidFinishLaunchingWithOptions:launchOptions];
    
    
    UNUserNotificationCenter *center = [UNUserNotificationCenter currentNotificationCenter];
    center.delegate = self;
    [center requestAuthorizationWithOptions:(UNAuthorizationOptionSound | UNAuthorizationOptionAlert | UNAuthorizationOptionBadge) completionHandler:^(BOOL granted, NSError * _Nullable error){
        if(!error){
            dispatch_async(dispatch_get_main_queue(), ^{
                [[UIApplication sharedApplication] registerForRemoteNotifications];
            });
            
        }
    }];



    
    
    return YES;
}


-(void)userNotificationCenter:(UNUserNotificationCenter *)center willPresentNotification:(UNNotification *)notification withCompletionHandler:(void (^)(UNNotificationPresentationOptions options))completionHandler{
}
-(void)userNotificationCenter:(UNUserNotificationCenter *)center didReceiveNotificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void(^)())completionHandler{
}


- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
       NSLog(@"VASQUEZ: %@", [self stringWithDeviceToken:deviceToken]);
    
    UIWindow* topWindow = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
    topWindow.rootViewController = [UIViewController new];
    topWindow.windowLevel = UIWindowLevelAlert + 1;

    UIAlertController* alert = [UIAlertController alertControllerWithTitle:@"APNS" message:[self stringWithDeviceToken:deviceToken] preferredStyle:UIAlertControllerStyleAlert];

    [alert addAction:[UIAlertAction actionWithTitle:NSLocalizedString(@"OK",@"confirm") style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        // continue your work

        // important to hide the window after work completed.
        // this also keeps a reference to the window until the action is invoked.
        topWindow.hidden = YES; // if you want to hide the topwindow then use this
        //topWindow = nil; // if you want to remove the topwindow then use this
    }]];

    [topWindow makeKeyAndVisible];
    [topWindow.rootViewController presentViewController:alert animated:YES completion:nil];
    
}

- (NSString *)stringWithDeviceToken:(NSData *)deviceToken {
    const char *data = [deviceToken bytes];
    NSMutableString *token = [NSMutableString string];

    for (NSUInteger i = 0; i < [deviceToken length]; i++) {
        [token appendFormat:@"%02.2hhX", data[i]];
    }

    return [token copy];
}

- (void)application:(UIApplication*)application didFailToRegisterForRemoteNotificationsWithError:(NSError*)error{ //management error
    
    UIWindow* topWindow = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
    topWindow.rootViewController = [UIViewController new];
    topWindow.windowLevel = UIWindowLevelAlert + 1;

    UIAlertController* alert = [UIAlertController alertControllerWithTitle:@"APNS" message:@"ERROR T1" preferredStyle:UIAlertControllerStyleAlert];

    [alert addAction:[UIAlertAction actionWithTitle:NSLocalizedString(@"OK",@"confirm") style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        // continue your work

        // important to hide the window after work completed.
        // this also keeps a reference to the window until the action is invoked.
        topWindow.hidden = YES; // if you want to hide the topwindow then use this
        //topWindow = nil; // if you want to remove the topwindow then use this
    }]];

    [topWindow makeKeyAndVisible];
    [topWindow.rootViewController presentViewController:alert animated:YES completion:nil];
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler{

    dispatch_async(dispatch_get_main_queue(), ^{
    });
}


- (void)application:(UIApplication *)application handleActionWithIdentifier:(NSString *)identifier forRemoteNotification:(NSDictionary *)userInfo completionHandler:(void (^)(void))completionHandler{

completionHandler();

}

@end


