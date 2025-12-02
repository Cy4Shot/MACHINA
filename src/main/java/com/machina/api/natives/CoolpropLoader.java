package com.machina.api.natives;

public class CoolpropLoader {

    enum Platform {
        WIN("win/CoolProp.dll"),
        DARWIN("macos/libCoolProp.dylib"),
        LINUX("linux/libCoolProp.so"),
        WIN_ARM("winarm/CoolProp.dll");

        String exec;

        private Platform(String exec) {
            this.exec = exec;
        }
    }

    public static String load() {
        return "/natives/" + getPlatform().exec;
    }

    private static Platform getPlatform() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            String arch = System.getProperty("os.arch").toLowerCase();
            if (arch.contains("arm") || arch.contains("aarch64")) {
                return Platform.WIN_ARM;
            }
            return Platform.WIN;
        }
        if (os.contains("mac"))
            return Platform.DARWIN;
        return Platform.LINUX;
    }
}