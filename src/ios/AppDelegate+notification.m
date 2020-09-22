//
//  appDelegate+notification.m
//  Personas
//
//  Created by Andres Felipe Vasquez Saldarriaga on 22/09/20.
//

#import "appDelegate+notification.h"
//#import "PushPlugin.h"
#import <objc/runtime.h>

#define kApplicationInBackgroundKey @"applicationInBackground"

@implementation AppDelegate (notification)


+ (void)load {
    Method original = class_getInstanceMethod(self, @selector(application:didFinishLaunchingWithOptions:));
    Method todo1 = class_getInstanceMethod(self, @selector(application:todo1DidFinishLaunchingWithOptions:));
    method_exchangeImplementations(original, todo1);
}

- (BOOL)application:(UIApplication *)application todo1DidFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
    [self application:application todo1DidFinishLaunchingWithOptions:launchOptions];

    
    
    return YES;
}

@end


