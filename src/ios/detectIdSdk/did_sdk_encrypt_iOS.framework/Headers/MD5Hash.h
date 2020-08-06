//
//  MD5Hash.h
//  did-sdk-encrypt-iOS
//
//  Created by Katerin Vasquez on 22/01/20.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface MD5Hash : NSObject

- (instancetype _Nonnull)initWithToHash:(NSString *)toHash;
- (unsigned char)getHash;
- (NSString *)getStringHashed;

@end

NS_ASSUME_NONNULL_END
