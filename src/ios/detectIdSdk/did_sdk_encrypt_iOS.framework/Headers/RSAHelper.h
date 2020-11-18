//
//  RSAHelper.h
//  did-sdk-encrypt-iOS
//
//  Created by Elkin Salcedo on 20/01/20.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface RSAHelper : NSObject

- (NSMutableDictionary *)buildKeyPairQueryWithPrivateTag:(NSData *)privateTag publicTag:(NSData *)publicTag;

@end

NS_ASSUME_NONNULL_END
