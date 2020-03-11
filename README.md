<p align="center"><img src="logo.png" alt="logo" height="250px"></p>

RxBiometric [![Build Status](https://img.shields.io/travis/pwittchen/RxBiometric.svg?branch=master&style=flat-square)](https://travis-ci.org/pwittchen/RxBiometric)  ![Maven Central](https://img.shields.io/maven-central/v/com.github.pwittchen/rxbiometric.svg?style=flat-square) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-RxBiometric-brightgreen.svg?style=flat-square)](https://android-arsenal.com/details/1/7245)
===========
RxJava and RxKotlin bindings for Biometric Prompt (Fingerprint Scanner) on Android (added in Android 9 Pie, API Level 28+)

*If your app is drawing its own fingerprint auth dialogs, you should switch to using the BiometricPrompt API as soon as possible.*

It's an official statement from [Google Android Developers Blog](https://android-developers.googleblog.com/2018/08/introducing-android-9-pie.html). RxBiometric helps you to do that via RxJava stream!

Contents
--------

- [Usage](#usage)
- [Examples](#examples)
- [Download](#download)
- [Tests](#tests)
- [Code style](#code-style)
- [Static code analysis](#static-code-analysis)
- [JavaDoc](#javadoc)
- [Changelog](#changelog)
- [Releasing](#releasing)
- [Mentions](#mentions)
- [References](#references)
- [License](#license)

Usage
-----

Simple library usage in **Kotlin** looks as follows:

```kotlin
RxBiometric
  .title("title")
  .description("description")
  .negativeButtonText("cancel")
  .negativeButtonListener(DialogInterface.OnClickListener { _, _ ->
    showMessage("cancel")
  })
  .executor(mainExecutor)
  .build()
  .authenticate(context)
  .subscribeOn(Schedulers.io())
  .observeOn(AndroidSchedulers.mainThread())
  .subscribeBy(
    onComplete = { showMessage("authenticated!") },
    onError = { showMessage("error") }
  )
```

Library also have validation method in the `Preconditions` class, which you can use to verify if you're able to use Biometric.

```kotlin
Preconditions.hasBiometricSupport(context)
```

There's also `RxPreconditions` class, which has the same method wrapped in RxJava `Single<Boolean>` type,
which you can use to create fluent data flow like in the example below

```kotlin
RxPreconditions
  .hasBiometricSupport(context)
  .flatMapCompletable {
    if (!it) Completable.error(BiometricNotSupported())
    else
      RxBiometric
        .title("title")
        .description("description")
        .negativeButtonText("cancel")
        .negativeButtonListener(DialogInterface.OnClickListener { _, _ ->
          showMessage("cancel")
        })
        .executor(mainExecutor)
        .build()
        .authenticate(context)
  }
  .subscribeOn(Schedulers.io())
  .observeOn(AndroidSchedulers.mainThread())
  .subscribeBy(
    onComplete = { showMessage("authenticated!") },
    onError = {
      when (it) {
        is AuthenticationError -> showMessage("error")
        is AuthenticationFail -> showMessage("fail")
        is AuthenticationHelp -> showMessage("help")
        is BiometricNotSupported -> showMessage("biometric not supported")
        else -> showMessage("other error")
      }
    }
  )
```

If you want to create your own CryptoObject and use it during authentication, then you can call `authenticate(context, cryptoObject)` method instead of `authenticate(context)`.

Of course, **don't forget to dispose** `Disposable` appropriately in the Activity Lifecycle.

Library can be used in the **Java** projects as well. Idea is the same, just syntax will be a bit different.

Examples
--------

Complete example of the working application can be found in the `kotlin-app` directory.

Download
--------

You can depend on the library through Gradle:

```groovy
dependencies {
  implementation 'com.github.pwittchen:rxbiometric:0.1.0'
}
```

Tests
-----

Tests are available in `library/src/test/kotlin/` directory and can be executed on JVM without any emulator or Android device from Android Studio or CLI with the following command:

```
./gradlew test
```

Code style
----------

Code style used in the project is called `SquareAndroid` from Java Code Styles repository by Square available at: https://github.com/square/java-code-styles.

Static code analysis
--------------------

Static code analysis runs Checkstyle, PMD, Lint and Detekt. It can be executed with command:

```
./gradlew check
```

Reports from analysis are generated in `library/build/reports/` directory.

JavaDoc
-------

Documentation can be generated as follows:

```
./gradlew dokka
```

Output will be generated in `library/build/javadoc`

JavaDoc can be viewed on-line at https://pwittchen.github.io/RxBiometric/library/

Changelog
---------

See [CHANGELOG.md](https://github.com/pwittchen/RxBiometric/blob/master/CHANGELOG.md) file.

Releasing
---------

See [RELEASING.md](https://github.com/pwittchen/RxBiometric/blob/master/RELEASING.md) file.

Mentions
--------
- [Android Weekly - issue #324](https://androidweekly.net/issues/issue-324)
- [Android Weekly China - issue #194](https://androidweekly.cn/android-dev-weekly-issue-194/)
- [30 summertime libraries which you don't want to miss in 2018](https://medium.com/@mmbialas/30-summertime-android-libraries-and-tools-which-you-dont-want-to-miss-in-2018-fab053d69503)

References
----------
- https://android-developers.googleblog.com/2018/08/introducing-android-9-pie.html
- https://android-developers.googleblog.com/2018/06/better-biometrics-in-android-p.html
- https://developer.android.com/reference/android/hardware/biometrics/BiometricPrompt
- https://github.com/Kieun/android-biometricprompt
- https://android-developers.googleblog.com/2019/10/one-biometric-api-over-all-android.html

License
-------

    Copyright 2018 Piotr Wittchen

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
