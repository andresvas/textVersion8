//
//  AesEncryption.h
//  did-sdk-encrypt-iOS
//
//  Created by Elkin Salcedo on 7/15/19.
//

#import <Foundation/Foundation.h>
#import <CommonCrypto/CommonCryptor.h>

@interface AesEncryption : NSObject

- (id)initWithIv:(NSData *)iv;
- (NSData *)encryptWithData:(NSData *)data key:(NSString *)key;
- (NSData *)decryptWithData:(NSData *)data key:(NSString *)key;

@end

