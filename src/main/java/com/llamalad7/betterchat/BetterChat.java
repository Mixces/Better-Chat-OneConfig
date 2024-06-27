package com.llamalad7.betterchat;

import cc.polyfrost.oneconfig.utils.commands.CommandManager;
import com.llamalad7.betterchat.command.BetterChatCommand;
import com.llamalad7.betterchat.config.BetterChatConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(
        modid = BetterChat.MODID,
        name = BetterChat.NAME,
        version = BetterChat.VERSION
)
public class BetterChat {

    public static final String MODID = "@ID@";
    public static final String NAME = "@NAME@";
    public static final String VERSION = "@VER@";
    @Mod.Instance(MODID)
    public static BetterChat INSTANCE;
    public static BetterChatConfig config;

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        config = new BetterChatConfig();
        CommandManager.INSTANCE.registerCommand(new BetterChatCommand());
    }

}
