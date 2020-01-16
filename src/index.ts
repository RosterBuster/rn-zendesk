import { NativeModules } from "react-native";

const { RNZendesk } = NativeModules;

// Interfaces
interface Config {
  appId: string;
  clientId: string;
  zendeskUrl: string;
}

interface HelpCenterOptions extends NewTicketOptions {
  hideContactSupport?: boolean;
}

interface NewTicketOptions {
  tags?: string[];
  subject?: string;
}

// Initialization

export function initialize(config: Config) {
  RNZendesk.initialize(config);
}

// Indentification

export function identifyAnonymous(name?: string, email?: string) {
  RNZendesk.identifyAnonymous(name, email);
}

// UI Methods

export function showHelpCenter(options: HelpCenterOptions) {
  RNZendesk.showHelpCenter(options);
}
