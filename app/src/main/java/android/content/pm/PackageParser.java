package android.content.pm;

import java.io.File;

public class PackageParser {

    public static ApkLite parseApkLite(File apkFile, int flags)
            throws PackageParserException {
        return null;
    }

    public static class PackageParserException extends Exception {
    }
    public static class ApkLite {
        public final String codePath;
        public final String packageName;
        public final String splitName;
        public boolean isFeatureSplit;
        public final String configForSplit;
        public final String usesSplitName;
        public final int versionCode;
        public final int revisionCode;
        public final int installLocation;
        public final Signature[] signatures;
        public final boolean coreApp;
        public final boolean debuggable;
        public final boolean multiArch;
        public final boolean use32bitAbi;
        public final boolean extractNativeLibs;
        public final boolean isolatedSplits;


        public ApkLite(String codePath, String packageName, String splitName, String configForSplit, String usesSplitName, int versionCode, int revisionCode, int installLocation, Signature[] signatures, boolean coreApp, boolean debuggable, boolean multiArch, boolean use32bitAbi, boolean extractNativeLibs, boolean isolatedSplits) {
            this.codePath = codePath;
            this.packageName = packageName;
            this.splitName = splitName;
            this.configForSplit = configForSplit;
            this.usesSplitName = usesSplitName;
            this.versionCode = versionCode;
            this.revisionCode = revisionCode;
            this.installLocation = installLocation;
            this.signatures = signatures;
            this.coreApp = coreApp;
            this.debuggable = debuggable;
            this.multiArch = multiArch;
            this.use32bitAbi = use32bitAbi;
            this.extractNativeLibs = extractNativeLibs;
            this.isolatedSplits = isolatedSplits;
        }
    }
}


