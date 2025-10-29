# KMP-CMP Book OpenAPP <img src="https://github.com/Abtinz/KMP-CMP-BookOpenApp/blob/master/composeApp/src/androidMain/res/drawable/icon.png" alt="app-icon" width="30" height="30"/>  <img src="https://github.com/Abtinz/KMP-CMP-BookOpenApp/blob/master/assets/android.png" alt="android" width="40" height="40"/> <img src="https://github.com/Abtinz/KMP-CMP-BookOpenApp/blob/master/assets/ios.png" alt="ios" width="30" height="30"/> 

Bookpedia is a simple, cross-platform reading companion built using Jetpack Compose Multiplatform, offering a unified experience across Android, iOS, desktop and web. With a cross platfrom UI, it enables users to browse and ransack books, save them inside room database files, see all essential infromaiton about each desired book.

* Note: Bookpedia is developed based on Mr. Philipp Lackner course.

<img src="https://github.com/Abtinz/KMP-CMP-BookOpenApp/blob/master/assets/demo.png" alt="demo"/>

Demo is availabe here: https://github.com/Abtinz/KMP-CMP-BookOpenApp/blob/master/assets/demo.mov

# <img src="https://github.com/Abtinz/KMP-CMP-BookOpenApp/blob/master/assets/cmp.png" alt="cmp" width="30" height="30"/>  Compose Multi-Platform 

Compose Multiplatform (often abbreviated as KMP Compose or Compose MP) is a framework developed by JetBrains that allows developers to create cross-platform user interfaces using a single Kotlin codebase. It’s based on Jetpack Compose (originally built for Android UI) and extends it to support desktop, web, and iOS platforms.

# <img src="https://github.com/Abtinz/KMP-CMP-BookOpenApp/blob/master/assets/ktor.png" alt="ktor" width="50" height="50"/> Ktor
Ktor is a cross-platform networking framework developed by JetBrains (the same company behind Kotlin and Compose Multiplatform).
It allows developers to build client-server applications — both HTTP clients (for making API calls) and servers (for hosting APIs) — using a single Kotlin codebase that runs on Android, iOS, desktop, web, and backend.

# <img src="https://github.com/Abtinz/KMP-CMP-BookOpenApp/blob/master/assets/koin.png" alt="koin" width="30" height="30"/> Koin

Koin is a lightweight dependency-injection (DI) framework written entirely in Kotlin. It uses a Kotlin DSL to define modules and dependencies (for example, module { single { MyRepository() } }) and does not rely on heavy code-generation or annotation-based wiring. It is designed to be pragmatic and developer-friendly, making it easier to get started with DI in Kotlin projects.

## Koin in Kotlin Multiplatform (KMP)

In a KMP project you often share logic (business layer, services, repositories) across platforms such as Android, iOS, desktop or web. Koin supports this scenario by offering multiplatform compatibility: you can define your dependency-modules in the commonMain (shared code) and then include platform-specific modules in each target. With this you get one unified DI container for all platforms, reducing duplication of wiring logic and improving maintenance.
# Acknowledment

I would like to extend my heartfelt thanks to Philipp Lackner for his incredibly useful and inspiring Android development crash courses specially this helpful CMP project which is accessible here:

* KMP-CMP-PL free course on youtube: https://www.youtube.com/watch?v=WT9-4DXUqsM
* source code: https://github.com/philipplackner/CMP-Bookpedia 

Also, I’d like to extend my sincere thanks to the Open Library team for their free, open API tools—specifically their Books API—which our application relies on to search, retrieve metadata, and integrate book information seamlessly:

* Source API: https://openlibrary.org/dev/docs/api/search 
