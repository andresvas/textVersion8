//
//  NSString+Encryption.h
//  did-sdk-encrypt-iOS
//
//  Created by Elkin Salcedo on 17/01/20.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSString (Encryption)

- (NSData *)getDataFromHexString;
- (NSString *)getString:(NSData *)data;

@end

NS_ASSUME_NONNULL_END
