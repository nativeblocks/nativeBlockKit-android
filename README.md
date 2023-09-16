# Nativeblocks android blockKit

## Get started

Root gradle

```bash
repositories {
    google()
    mavenCentral()
    maven {
        url  "https://jitpack.io"
    }
}
```

Module gradle

```bash
dependencies {
    implementation ("com.github.nativeblocks:nativeBlockKit-android:0.1.2")
}
```

In this step, the developer needs to provide the blocks to the Nativeblocks SDK. To accomplish this, the following code should be integrated:

```js
NativeblocksBlockHelper.provideBlocks()
```


Here is version compatibility of blockKit and Nativeblocks core SDK

| Block kit version | Nativeblocks core version |
|-------------------|---------------------------|
| 0.1.2             | 0.1.4                     |
| 0.1.1             | 0.1.3                     |
