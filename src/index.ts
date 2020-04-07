import { NativeModules } from "react-native";

const { RNZendesk } = NativeModules;

/**
 * required configuration to initialize zendesk
 */
export interface Config {
  appId: string;
  clientId: string;
  zendeskUrl: string;
}

export enum ZDKHelpCenterOverviewGroupType {
  DEFAULT = 0,
  SECTION = 1,
  CATEGORY = 2
}

/**
 * options when launching the help center
 */
export interface HelpCenterOptions extends NewTicketOptions {
  hideContactSupport?: boolean;
  groupType: ZDKHelpCenterOverviewGroupType;
  groupIds: Array<number>;
}

interface NewTicketOptions {
  tags?: string[];
  subject?: string;
}

/**
 * initialize zendesk with provided configuration
 *
 * @see https://developer.zendesk.com/embeddables/docs/ios_support_sdk/sdk_initialize#initializing-the-support-sdk-required
 * @param config appId, cliendId, zendeskUrl
 */
export function initialize(config: Config) {
  RNZendesk.initialize(config);
}

/**
 * set user details
 * Note: zendesk should be initialized before accessing `identifyAnonymous`
 *
 * @param name name of the user
 * @param email email of the user
 */
export function identifyAnonymous(name?: string, email?: string) {
  RNZendesk.identifyAnonymous(name, email);
}

/**
 * launch help center
 * Note: zendesk should be initialized before accessing `showHelpCenter`
 *
 * @param options
 */
export function showHelpCenter(options: HelpCenterOptions) {
  RNZendesk.showHelpCenter(options);
}
