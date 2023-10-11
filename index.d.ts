declare module 'react-native-appmetrica-reborn' {
    export type AppMetricaConfig = {
        apiKey: string,
        appVersion?: string,
        crashReporting?: boolean,
        firstActivationAsUpdate?: boolean,
        location?: Location,
        locationTracking?: boolean,
        logs?: boolean,
        sessionTimeout?: number,
        statisticsSending?: boolean,
        preloadInfo?: PreloadInfo,
        // Only Android
        installedAppCollecting?: boolean,
        maxReportsInDatabaseCount?: number,
        nativeCrashReporting?: boolean,
        // Only iOS
        activationAsSessionStart?: boolean,
        sessionsAutoTracking?: boolean,
    }

    export type PreloadInfo = {
        trackingId: string,
        additionalInfo?: Object,
    }

    export type Location = {
        latitude: number,
        longitude: number,
        altitude?: number,
        accuracy?: number,
        course?: number,
        speed?: number,
        timestamp?: number
    }

    export type AppMetricaDeviceIdReason = 'UNKNOWN' | 'NETWORK' | 'INVALID_RESPONSE';

    export type AppMetricaProduct = {
        name: string,
        price: string,
        screenName?: string,
        prevScreenName?: string,
        currency: string,
        quantity?: string,
        sku?: string,
        searchQuery?: string
    };

    export type AppMetricaScreen = {
        screenName: string,
        prevScreenName?: string,
        searchQuery?: string,
        categories?: string[]
    };

    export default class YandexMetrica {
        static activate(config: AppMetricaConfig): Promise<void>;
        static initPush(): void;
        static reportEvent(eventName: string, attributes?: null | Object): Promise<void>;
        static getLibraryApiLevel(): Promise<void>;
        static getLibraryVersion(): Promise<void>;
        static pauseSession(): void;
        static reportAppOpen(deeplink: string): void;
        static reportError(error: string, reason: Object): void;
        static reportReferralUrl(referralUrl: string): void;
        static requestAppMetricaDeviceID(listener: (deviceId?: string, reason?: AppMetricaDeviceIdReason) => void): void;
        static resumeSession(): void;
        static sendEventsBuffer(): void;
        static setLocation(location: Location): void;
        static setLocationTracking(enabled: boolean): void;
        static setStatisticsSending(enabled: boolean): void;
        static setUserProfileID(userProfileID: string): void;
        // Ecommerce
        static showScreen(screen: AppMetricaScreen): Promise<void>
        static showScreenWithCategories(screen: AppMetricaScreen, categories: string[]): Promise<void>
        static showProductCard(product: AppMetricaProduct): Promise<void>
        static beginCheckout(products: AppMetricaProduct[], identifier: string): Promise<void>
        static addToCart(product: AppMetricaProduct): Promise<void>
        static removeFromCart(product: AppMetricaProduct): Promise<void>
        static finishCheckout(products: AppMetricaProduct[], identifier: string): Promise<void>
    }
}
