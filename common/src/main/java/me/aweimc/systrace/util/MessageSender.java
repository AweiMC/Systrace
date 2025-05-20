package me.aweimc.systrace.util;

import net.minecraft.text.Text;

public interface MessageSender<S> {
    void send(S source, Text message);
}
