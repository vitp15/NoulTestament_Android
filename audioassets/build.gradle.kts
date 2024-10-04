plugins {
    id("com.android.asset-pack")
}

assetPack {
    packName.set("audioassets")
    dynamicDelivery {
        deliveryType.set("install-time")
    }
}
