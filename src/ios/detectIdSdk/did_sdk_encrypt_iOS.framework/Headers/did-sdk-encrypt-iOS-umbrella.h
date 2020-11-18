#ifdef __OBJC__
#import <UIKit/UIKit.h>
#else
#ifndef FOUNDATION_EXPORT
#if defined(__cplusplus)
#define FOUNDATION_EXPORT extern "C"
#else
#define FOUNDATION_EXPORT extern
#endif
#endif
#endif

#import "DeviceInfo.h"
#import "EncryptAes.h"
#import "EncryptRSA.h"
#import "RsaEncryption.h"
#import "AesEncryption.h"
#import "NSData+Encryption.h"
#import "NSString+Encryption.h"
#import "Sha1Hash.h"
#import "MD5Hash.h"
#import "RSAHelper.h"
#import "SignManager.h"
#import "KeyManager.h"
#import "CatchError.h"

FOUNDATION_EXPORT double did_sdk_encrypt_iOSVersionNumber;
FOUNDATION_EXPORT const unsigned char did_sdk_encrypt_iOSVersionString[];

