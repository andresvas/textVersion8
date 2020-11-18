//
//  WBTry.h
//  Try
//
//  Created by Jacob Berkman on 2014-08-20.
//

#import <Foundation/Foundation.h>

@interface CatchError : NSObject

+ (void)tryBlock:(nonnull void (^)(void))tryBlock  catchAndRethrowBlock:(nullable BOOL (^)(_Nonnull id))catchAndRethrowBlock finallyBlock:(nullable void (^)(void))finallyBlock;

@end
