# Proguard rules for Jalan Islam app
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses,EnclosingMethod

-keepclassmembers class * {
    @androidx.room.* *;
}

# Keep Gson DTOs and data models (Quran API, offline cache, Prayer models)
-keep class com.jalanislam.app.data.quran.** { *; }
-keep class com.jalanislam.app.data.model.** { *; }
-keep class com.jalanislam.app.data.adzan.** { *; }
-keepclassmembers class com.jalanislam.app.data.quran.QuranApiClient$** { *; }

# Keep BroadcastReceivers and Services declared in AndroidManifest.xml
-keep class com.jalanislam.app.alarm.** { *; }
