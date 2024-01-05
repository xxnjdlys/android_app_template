# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn javax.lang.model.element.Modifier
#noinspection ShrinkerUnresolvedReference
-keep class javax.lang.model.element.Modifier { *; }

#########  immersionbar sdk ##########
 -keep class com.gyf.immersionbar.* {*;}
 -dontwarn com.gyf.immersionbar.**
##########  immersionbar sdk ##########

##########  gson sdk ###########
-keep class com.google.gson.** {*;}
-keep class com.google.** {*;}
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.ymmbj.mz.model.**{*;}
-keep class com.ymmbj.mz.http.ApiResult{*;}
##########  gson sdk ###########

##########  Retrofit sdk ##########
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-dontnote retrofit2.Platform
-keepattributes Signaturdde
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-dontwarn okhttp3.**
-keep class okhttp3.** {*;}
-dontwarn okio.**
-keep class okio.** {*;}
##########  Retrofit sdk ##########
