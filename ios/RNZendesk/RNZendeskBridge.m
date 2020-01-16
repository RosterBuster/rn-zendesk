#import "RNZendeskBridge.h"
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_REMAP_MODULE(RNZendesk, RNZendesk, NSObject)

// Initialization

RCT_EXTERN_METHOD(initialize:(NSDictionary *)config);

// Indentification

RCT_EXTERN_METHOD(identifyAnonymous:(NSString *)name email:(NSString *)email);

// UI Methods

RCT_EXTERN_METHOD(showHelpCenter:(NSDictionary *)options);

@end
