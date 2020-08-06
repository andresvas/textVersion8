//
//  KeyManager.h
//  did-sdk-encrypt-iOS
//
//  Created by Elkin Salcedo on 23/01/20.
//

#import <Foundation/Foundation.h>
#import "RSAHelper.h"

NS_ASSUME_NONNULL_BEGIN

@interface KeyManager : NSObject

@property (nonatomic, strong) RSAHelper *rsaHelper;
    
- (instancetype)init;
- (instancetype)initWithRSAHelper:(RSAHelper *)rsaHelper;
- (void)generateRSAKeyPairWithPublicTag:(NSData *)publicTag privateTag:(NSData *)privateTag completion:(void (^)(BOOL completed))handler;
- (NSMutableData *)generateServerRSAPublicKeyWithExponent:(NSString *)exponent modulus:(NSString *)modulus;
- (NSDictionary *)getInfoKeyPublic:(NSData *)publicKey;

@end

NS_ASSUME_NONNULL_END
