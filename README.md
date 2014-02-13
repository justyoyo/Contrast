Contrast
========

Lightweight quick response code (QRCode) library for Android.

### Introduction

Unhappy with how ZXing is a massive bloated project which is very inflexible in its approach.

We only need writing ability at this point in time, so we stripped back to basics.
Not only is this library much lighter weight, but due to its modular approach over ZXing future
improvements allow you to include barcodes that _you_ only care about.


### Dependency

_Coming soon when time allows_

At the moment you can checkout and include in your project standard gradle way

settings.gradle

```groovy
include ':qrcode'
```

build.gradle:

```groovy
compile project(':qrcode')
```


### Thanks

Based heavily on the follow code bases:

 * [ZXing](https://github.com/zxing/zxing)
 * [android-quick-response-code](https://code.google.com/p/android-quick-response-code/)


