package com.llamalad7.betterchat.utils;

/**
 * Taken from BetterChat under LGPL 3.0
 * <a href="https://github.com/LlamaLad7/Better-Chat/blob/1.8.9/LICENSE">https://github.com/LlamaLad7/Better-Chat/blob/1.8.9/LICENSE</a>
 */
public class AnimationTools {

    public static float clamp(float number, float min, float max) {
        return number < min ? min : Math.min(number, max);
    }

}