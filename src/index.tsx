import { NativeModules } from 'react-native';


type BrainlinkType = {
    isBluetoothOn(callback: Function): void;
    start(): void;
    stop(): void;
    setDevice(name: string): void
    scanBluetoothDevices(): void
    /**
     * Adds a subscription keyed by an event type.
     *
     */
    addSubscription(eventType: string, subscription: any): any;

    /**
     * Removes a bulk set of the subscriptions.
     *
     * @param eventType - Optional name of the event type whose
     *   registered supscriptions to remove, if null remove all subscriptions.
     */
    removeAllSubscriptions(eventType?: string): void;

    /**
     * Removes a specific subscription. Instead of calling this function, call
     * `subscription.remove()` directly.
     *
     */
    removeSubscription(subscription: any): void;

    /**
     * Returns the array of subscriptions that are currently registered for the
     * given event type.
     *
     * Note: This array can be potentially sparse as subscriptions are deleted
     * from it when they are removed.
     *
     */
    getSubscriptionsForType(eventType: string): any[];
};

const { Brainlink } = NativeModules;

export default Brainlink as BrainlinkType;
