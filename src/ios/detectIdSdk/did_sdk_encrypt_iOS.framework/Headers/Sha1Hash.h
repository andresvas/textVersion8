//
//  Sha1Hash.h
//  did-sdk-encrypt-iOS
//
//  Created by Katerin Vasquez on 22/01/20.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface Sha1Hash : NSObject

- (instancetype _Nonnull)initWithToHash:(NSData *)toHash;
- (uint8_t *)getHash;
- (NSString *)getStringHashed;

@end

NS_ASSUME_NONNULL_END
