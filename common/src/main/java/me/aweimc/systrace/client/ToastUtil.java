package me.aweimc.systrace.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.Text;

public class ToastUtil {
    private static final Text TEST_TEXT = Text.translatable("ruok.debug.text.info");
    private static final Text TEST_TITLE = Text.translatable("ruok.debug.text.title");
    private static final MinecraftClient mci = MinecraftClient.getInstance();
    private static final ToastManager tom = mci.getToastManager();
    public static void send(Text title, Text text) {
        if(mci!=null) {
            SystemToast.show(tom,SystemToast.Type.PERIODIC_NOTIFICATION,title,text);
        }
    }
    public static void send(Text title) {
        if(mci!=null) {
            SystemToast.show(tom,SystemToast.Type.PERIODIC_NOTIFICATION,title,Text.empty());
        }
    }
    public static void send(Integer title,Text text) {
        if(mci!=null) {
            SystemToast.show(tom,SystemToast.Type.PERIODIC_NOTIFICATION, Text.of(String.valueOf(title)),text);
        }
    }
    public static void send(String title, String text) {
        if(mci!=null) {
            SystemToast.show(tom,SystemToast.Type.PERIODIC_NOTIFICATION,Text.translatable(title),Text.translatable(text));
        }
    }
    public static void send(Text title, Text text,SystemToast.Type type) {
        if(mci!=null) SystemToast.show(tom,type,title,text);
    }
    public static void test() {
        send(TEST_TEXT,TEST_TITLE);
    }
}
