package com.llamalad7.betterchat.mixin;

import com.llamalad7.betterchat.BetterChat;
import com.llamalad7.betterchat.config.BetterChatConfig;
import com.llamalad7.betterchat.utils.AnimationTools;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Taken from BetterChat under LGPL 3.0
 * <a href="https://github.com/LlamaLad7/Better-Chat/blob/1.8.9/LICENSE">https://github.com/LlamaLad7/Better-Chat/blob/1.8.9/LICENSE</a>
 */
@Mixin(GuiNewChat.class)
public abstract class GuiNewChatMixin extends Gui {

    @Shadow public abstract boolean getChatOpen();
    @Shadow private boolean isScrolled;
    @Shadow public abstract float getChatScale();
    @Unique private float betterChat$percentComplete;
    @Unique private int betterChat$newLines;
    @Unique private long betterChat$prevMillis = System.currentTimeMillis();
    @Unique private float betterChat$animationPercent;
    @Unique private int betterChat$lineBeingDrawn;

    @Inject(
            method = "drawChat",
            at = @At(
                    value = "HEAD"
            )
    )
    private void betterChat$modifyChatRendering(CallbackInfo ci) {
        if (!BetterChat.config.enabled) { return; }
        long current = System.currentTimeMillis();
        long diff = current - betterChat$prevMillis;
        betterChat$prevMillis = current;
        if (betterChat$percentComplete < 1) betterChat$percentComplete += 0.004f * diff;
        betterChat$percentComplete = AnimationTools.clamp(betterChat$percentComplete, 0, 1);
        float t = betterChat$percentComplete;
        betterChat$animationPercent = AnimationTools.clamp(1 - (--t) * t * t * t, 0, 1);
    }

    @Inject(
            method = "drawChat",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;pushMatrix()V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            )
    )
    private void betterChat$translate(CallbackInfo ci) {
        float y = BetterChatConfig.chatPosition && BetterChat.config.enabled ? -12 : 0;
        if (BetterChatConfig.smoothChat && BetterChat.config.enabled && !this.isScrolled) {
            y += (9 - 9 * betterChat$animationPercent) * this.getChatScale();
        }
        GlStateManager.translate(0, y, 0);
    }

    @Redirect(
            method = "drawChat",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V",
                    ordinal = 0
            )
    )
    private void betterChat$transparentBackground(int left, int top, int right, int bottom, int color) {
        if ((!BetterChatConfig.removeBg || !BetterChat.config.enabled) || (BetterChatConfig.removeBgOnlyWhenClosed && getChatOpen())) {
            drawRect(left, top, right, bottom, color);
        }
    }

    @ModifyArg(
            method = "drawChat",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;get(I)Ljava/lang/Object;",
                    ordinal = 0,
                    remap = false
            ),
            index = 0
    )
    private int betterChat$getLineBeingDrawn(int line) {
        betterChat$lineBeingDrawn = line;
        return line;
    }

    @ModifyArg(
            method = "drawChat",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"),
            index = 3
    )
    private int betterChat$modifyTextOpacity(int original) {
        if (BetterChatConfig.smoothChat && BetterChat.config.enabled  && betterChat$lineBeingDrawn <= betterChat$newLines) {
            int opacity = (original >> 24) & 0xFF;
            opacity *= (int) betterChat$animationPercent;
            return (original & ~(0xFF << 24)) | (opacity << 24);
        } else {
            return original;
        }
    }

    @Inject(
            method = "printChatMessageWithOptionalDeletion",
            at = @At(
                    value = "HEAD"
            )
    )
    private void betterChat$resetPercentage(CallbackInfo ci) {
        betterChat$percentComplete = 0;
    }

    @ModifyVariable(
            method = "setChatLine",
            at = @At(
                    value = "STORE"
            ),
            ordinal = 0
    )
    private List<IChatComponent> betterChat$setNewLines(List<IChatComponent> original) {
        betterChat$newLines = original.size() - 1;
        return original;
    }

    @ModifyVariable(
            method = "getChatComponent",
            at = @At(
                    value = "STORE",
                    ordinal = 0
            ),
            ordinal = 4
    )
    private int betterChat$modifyY(int original) {
        return BetterChatConfig.chatPosition && BetterChat.config.enabled ? original - 12 : original;
    }

}