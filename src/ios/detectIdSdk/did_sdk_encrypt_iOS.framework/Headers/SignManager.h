//
//  SignManager.h
//  did-sdk-encrypt-iOS
//
//  Created by Elkin Salcedo on 23/01/20.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface SignManager : NSObject

- (NSData *)signWithData:(NSData *)data key:(SecKeyRef)key;
- (bool)verifySignWithMessage:(NSData *)message signature:(NSData *)signature key:(SecKeyRef)key;

@end

NS_ASSUME_NONNULL_END
