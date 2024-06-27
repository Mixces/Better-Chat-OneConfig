package com.llamalad7.betterchat.command;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import com.llamalad7.betterchat.BetterChat;

@Command(
        value = BetterChat.MODID,
        description = "Access the " + BetterChat.NAME + " GUI."
)
public class BetterChatCommand {

    @Main
    private void handle() {
        BetterChat.config.openGui();
    }

}