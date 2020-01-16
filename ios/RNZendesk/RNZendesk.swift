import UIKit
import Foundation
import CommonUISDK
import ZendeskSDK
import ZendeskCoreSDK

@objc(RNZendesk)
class RNZendesk: RCTEventEmitter {

    override public static func requiresMainQueueSetup() -> Bool {
        return false;
    }
    
    @objc(constantsToExport)
    override func constantsToExport() -> [AnyHashable: Any] {
        return [:]
    }
    
    @objc(supportedEvents)
    override func supportedEvents() -> [String] {
        return []
    }
    
    // Initialization Methods

    @objc(initialize:)
    func initialize(config: [String: Any]) {
        guard
            let appId = config["appId"] as? String,
            let clientId = config["clientId"] as? String,
            let zendeskUrl = config["zendeskUrl"] as? String else { return }
        
        Zendesk.initialize(appId: appId, clientId: clientId, zendeskUrl: zendeskUrl)
        Support.initialize(withZendesk: Zendesk.instance)
    }
    
    // Indentification Methods
    
    @objc(identifyAnonymous:email:)
    func identifyAnonymous(name: String?, email: String?) {
        let identity = Identity.createAnonymous(name: name, email: email)
        Zendesk.instance?.setIdentity(identity)
    }
    
    // UI Methods
    
    @objc(showHelpCenter:)
    func showHelpCenter(with options: [String: Any]) {
        DispatchQueue.main.async {
            let hcConfig = HelpCenterUiConfiguration()

            let config = RequestUiConfiguration()
            if let tags = options["tags"] as? [String] {
                config.tags = tags
            }
            config.subject = (options["hideContactSupport"] as? String) ?? ""

            hcConfig.showContactOptionsOnEmptySearch = (options["hideContactSupport"] as? Bool) ?? false

            let helpCenter = HelpCenterUi.buildHelpCenterOverviewUi(withConfigs: [hcConfig, config])
            
            let uiNavigationController = UINavigationController(rootViewController: helpCenter)
            uiNavigationController.modalPresentationStyle = .fullScreen
            UIApplication.shared.keyWindow?.rootViewController?.present(uiNavigationController, animated: true, completion: nil)
        }
    }
}
