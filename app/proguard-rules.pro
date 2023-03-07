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
-keep class com.google.android.material.** { *; }

-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**

-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

-keep class com.gigya.** {*;}
-keep class ymsli.com.ea1h.GigyaResponse
-keep class ymsli.com.ea1h.GigyaResponse.MyData
-keep class io.swagger.** {*;}
-keep class com.mediatek.** {*;}
-keep class com.mediatek.cta.CtaUtils
-keep class io.swagger.client.** {*;}
-keep class ymsli.com.ea1h.** {*;}
-keep class dmax.dialog.** {*;}
-keep class com.google.** {*;}
-dontwarn com.mediatek.**
-dontwarn ymsli.com.ea1h.**
-dontwarn com.google.**
-dontwarn dmax.dialog.**
-dontwarn io.swagger.client.**
-dontwarn com.gigya.**


-keep class okhttp.** { *; }
-keep interface okhttp.** { *; }
-dontwarn okhttp.**
-dontnote okhttp.**

##OKHTTP3
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontnote okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# For using GSON @Expose annotation
-keepattributes *Annotation*

-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
  }
-keep,allowobfuscation @interface com.google.gson.annotations.SerializedName