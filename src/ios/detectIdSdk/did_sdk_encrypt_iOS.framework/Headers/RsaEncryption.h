//
//  RsaEncryption.h
//  did-sdk-encrypt-iOS
//
//  Created by Elkin Salcedo on 7/12/19.
//

#import <Foundation/Foundation.h>

@interface RsaEncryption : NSObject

- (NSData *)encryptWithData:(NSData *)data key:(SecKeyRef)key;
- (NSData *)decryptWithData:(NSData *)data key:(SecKeyRef)key;

@end
