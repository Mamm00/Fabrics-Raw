# Kotlinx serialization keeps generated serializers
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**
-keepclassmembers class **$$serializer { *; }
-keepclasseswithmembers class * {
    @kotlinx.serialization.Serializable <methods>;
}
# Room generated implementations
-keep class androidx.room.** { *; }
# Ktor client engine
-dontwarn org.slf4j.**
