//
//  Offline_Authentication_API.h
//  didm_auth_sdk_iOS
//
//  Created by Katerin Vasquez on 6/03/20.
//  Copyright © 2020 Easy Solutions, Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

@import did_sdk_offline_ios;

NS_ASSUME_NONNULL_BEGIN

@interface Offline_Authentication_API : NSObject

@property (nonatomic, assign) id<OfflineAuthProtocol>offlineAuthDelegate;

- (void)getTokenValueWithQrCode:(NSString *)offlineQrCode;

@end

NS_ASSUME_NONNULL_END
