# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/nishtha/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

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
-dontobfuscate
-keepattributes Signature
-keepattributes Annotation
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keep class com.apps.nishtha.lyrics.pojoforid.*
-keep class com.apps.nishtha.lyrics.pojoforlyrics.*
-keep public class com.google.gson.**
-keep public class com.google.gson.** {public private protected *;}
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.Fragment
-keep public class * extends android.content.AppCompatActivity
#-keep class com.apps.nishtha.lyrics.pojoforid.details
#-keep class com.apps.nishtha.lyrics.pojoforid.message
#-keep class com.apps.nishtha.lyrics.pojoforid.track
#-keep class com.apps.nishtha.lyrics.pojoforid.tracklist
#-keep class com.apps.nishtha.lyrics.pojolyrics.tracklist

-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**