# CI status: success

Run: 44482184479 attempt 1
Commit: 2298a179542d0dfa4197e6e5e6cec699dc90e48a

```
> Task :benchmark-client:parseDebugLocalResources
> Task :benchmark-client:generateDebugRFile
> Task :data:compileDebugLibraryResources FROM-CACHE
> Task :data:parseDebugLocalResources FROM-CACHE
> Task :data:generateDebugRFile
> Task :app:processDebugResources
> Task :benchmark-client:checkKotlinGradlePluginConfigurationErrors SKIPPED
> Task :benchmark-client:javaPreCompileDebug
> Task :data:checkKotlinGradlePluginConfigurationErrors SKIPPED
> Task :data:javaPreCompileDebug
> Task :app:javaPreCompileDebug FROM-CACHE
> Task :app:mergeDebugShaders
> Task :app:compileDebugShaders NO-SOURCE
> Task :app:generateDebugAssets UP-TO-DATE
> Task :benchmark-client:mergeDebugShaders
> Task :benchmark-client:compileDebugShaders NO-SOURCE
> Task :benchmark-client:generateDebugAssets UP-TO-DATE
> Task :benchmark-client:mergeDebugAssets
> Task :data:mergeDebugShaders
> Task :data:compileDebugShaders NO-SOURCE
> Task :data:generateDebugAssets UP-TO-DATE
> Task :data:mergeDebugAssets
> Task :app:mergeDebugAssets
> Task :app:compressDebugAssets
> Task :app:desugarDebugFileDependencies
> Task :app:checkDebugDuplicateClasses
> Task :domain:compileKotlin
> Task :domain:compileJava NO-SOURCE
> Task :domain:classes UP-TO-DATE
> Task :domain:jar
> Task :app:mergeExtDexDebug
> Task :benchmark-client:compileDebugKotlin
> Task :benchmark-client:compileDebugJavaWithJavac NO-SOURCE
> Task :benchmark-client:bundleLibCompileToJarDebug
> Task :data:kspDebugKotlin
> Task :benchmark-client:bundleLibRuntimeToJarDebug
> Task :benchmark-client:processDebugJavaRes
> Task :benchmark-client:bundleLibRuntimeToDirDebug
> Task :app:mergeDebugJniLibFolders
> Task :benchmark-client:mergeDebugJniLibFolders
> Task :benchmark-client:mergeDebugNativeLibs NO-SOURCE
> Task :benchmark-client:copyDebugJniLibsProjectOnly
> Task :data:mergeDebugJniLibFolders
> Task :data:mergeDebugNativeLibs NO-SOURCE
> Task :data:copyDebugJniLibsProjectOnly
> Task :app:mergeDebugNativeLibs

> Task :app:stripDebugDebugSymbols
Unable to strip the following libraries, packaging them as they are: libandroidx.graphics.path.so, libdatastore_shared_counter.so. Run with --info option to learn more.

> Task :data:compileDebugKotlin
> Task :data:compileDebugJavaWithJavac NO-SOURCE
> Task :data:bundleLibCompileToJarDebug
> Task :app:kspDebugKotlin SKIPPED
> Task :app:validateSigningDebug
> Task :data:bundleLibRuntimeToJarDebug
> Task :data:processDebugJavaRes
> Task :data:bundleLibRuntimeToDirDebug
> Task :app:mergeLibDexDebug
> Task :app:writeDebugAppMetadata
> Task :app:writeDebugSigningConfigVersions

> Task :app:compileDebugKotlin
w: file:///home/runner/work/Fabrics-Raw/Fabrics-Raw/app/src/main/java/com/alazhartex/costing/ui/navigation/AppNavHost.kt:64:56 'val Icons.Filled.List: ImageVector' is deprecated. Use the AutoMirrored version at Icons.AutoMirrored.Filled.List.
w: file:///home/runner/work/Fabrics-Raw/Fabrics-Raw/app/src/main/java/com/alazhartex/costing/ui/navigation/AppNavHost.kt:89:56 'val Icons.Filled.List: ImageVector' is deprecated. Use the AutoMirrored version at Icons.AutoMirrored.Filled.List.

> Task :app:processDebugJavaRes
> Task :app:mergeDebugJavaResource
> Task :app:compileDebugJavaWithJavac
> Task :app:dexBuilderDebug
> Task :app:mergeDebugGlobalSynthetics
> Task :app:mergeProjectDexDebug
> Task :app:packageDebug
> Task :app:assembleDebug
> Task :app:createDebugApkListingFileRedirect
gradle/actions: Writing build results to /home/runner/work/_temp/.gradle-actions/build-results/build-1795074399628.json

BUILD SUCCESSFUL in 3m 4s
82 actionable tasks: 74 executed, 8 from cache
Configuration cache entry stored.
```
