package com.llamalad7.betterchat.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import com.llamalad7.betterchat.BetterChat;

public class BetterChatConfig extends Config {

    @Switch(
            name = "Remove Chat Background"
    )
    public static boolean removeBg = false;

    @Switch(
            name = "Keep Background When Chat is Open"
    )
    public static boolean removeBgOnlyWhenClosed = false;

    @Switch(
            name = "Smooth Chat"
    )
    public static boolean smoothChat = true;

    @Switch(
            name = "Move Chat Above Hearts"
    )
    public static boolean chatPosition = false;

    public BetterChatConfig() {
        super(new Mod(BetterChat.NAME, ModType.UTIL_QOL), BetterChat.MODID + ".json");
        initialize();

        addDependency("removeBgOnlyWhenClosed", "removeBg");
    }

}

