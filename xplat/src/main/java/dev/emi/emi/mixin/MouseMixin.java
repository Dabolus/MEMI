package dev.emi.emi.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.emi.emi.runtime.EmiLog;
import dev.emi.emi.screen.EmiScreenManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonInfo;

@Mixin(MouseHandler.class)
public class MouseMixin {
	@Shadow @Final
	private Minecraft minecraft;
	@Shadow
	private double xpos, ypos;
	@Shadow
	private MouseButtonInfo activeButton;
	@Shadow private double accumulatedDX;
	@Shadow private double accumulatedDY;

	@Inject(at = @At(value = "INVOKE",
			target = "net/minecraft/client/gui/screens/Screen.mouseClicked(Lnet/minecraft/client/input/MouseButtonEvent;Z)Z"),
		method = "onButton(JLnet/minecraft/client/input/MouseButtonInfo;I)V", cancellable = true)
	private void onMouseDown(long window, MouseButtonInfo buttonInfo, int action, CallbackInfo info) {
		try {
			Screen screen = minecraft.screen;
			if (screen instanceof AbstractContainerScreen<?> hs) {
				double mx = xpos * minecraft.getWindow().getGuiScaledWidth() / minecraft.getWindow().getScreenWidth();
				double my = ypos * minecraft.getWindow().getGuiScaledHeight() / minecraft.getWindow().getScreenHeight();
				if (EmiScreenManager.mouseClicked(mx, my, buttonInfo.button())) {
					info.cancel();
				}
			}
		} catch (Exception e) {
			EmiLog.error("Error while handling mouse press", e);
		}
	}

	@Inject(at = @At(value = "INVOKE",
			target = "net/minecraft/client/gui/screens/Screen.mouseReleased(Lnet/minecraft/client/input/MouseButtonEvent;)Z"),
		method = "onButton(JLnet/minecraft/client/input/MouseButtonInfo;I)V", cancellable = true)
	private void onMouseUp(long window, MouseButtonInfo buttonInfo, int action, CallbackInfo info) {
		try {
			Screen screen = minecraft.screen;
			if (screen instanceof AbstractContainerScreen<?> hs) {
				double mx = xpos * minecraft.getWindow().getGuiScaledWidth() / minecraft.getWindow().getScreenWidth();
				double my = ypos * minecraft.getWindow().getGuiScaledHeight() / minecraft.getWindow().getScreenHeight();
				if (EmiScreenManager.mouseReleased(mx, my, buttonInfo.button())) {
					info.cancel();
				}
			}
		} catch (Exception e) {
			EmiLog.error("Error while handling mouse release", e);
		}
	}

	@Inject(at = @At(value = "INVOKE",
			target = "net/minecraft/client/gui/screens/Screen.mouseDragged(Lnet/minecraft/client/input/MouseButtonEvent;DD)Z"),
		method = "handleAccumulatedMovement()V")
	private void onMouseDragged(CallbackInfo info) {
		try {
			Screen screen = minecraft.screen;
			if (screen instanceof AbstractContainerScreen<?> hs) {
				double mx = xpos * minecraft.getWindow().getGuiScaledWidth() / minecraft.getWindow().getScreenWidth();
				double my = ypos * minecraft.getWindow().getGuiScaledHeight() / minecraft.getWindow().getScreenHeight();
				double dx = accumulatedDX * minecraft.getWindow().getGuiScaledWidth() / minecraft.getWindow().getScreenWidth();
				double dy = accumulatedDY * minecraft.getWindow().getGuiScaledHeight() / minecraft.getWindow().getScreenHeight();
				EmiScreenManager.mouseDragged(mx, my, activeButton.button(), dx, dy);
			}
		} catch (Exception e) {
			EmiLog.error("Error while handling mouse drag", e);
		}
	}

	@Inject(at = @At(value = "INVOKE",
			target = "net/minecraft/client/gui/screens/Screen.mouseScrolled(DDDD)Z"),
		method = "onScroll(JDD)V", cancellable = true)
	private void onMouseScrolled(long window, double horizontal, double vertical, CallbackInfo info) {
		try {
			Screen screen = minecraft.screen;
			if (screen instanceof AbstractContainerScreen<?> hs) {
				double amount = (minecraft.options.discreteMouseScroll().get() ? Math.signum(vertical) : vertical) * minecraft.options.mouseWheelSensitivity().get();
				double mx = xpos * minecraft.getWindow().getGuiScaledWidth() / minecraft.getWindow().getScreenWidth();
				double my = ypos * minecraft.getWindow().getGuiScaledHeight() / minecraft.getWindow().getScreenHeight();
				if (EmiScreenManager.mouseScrolled(mx, my, amount)) {
					info.cancel();
				}
			}
		} catch (Exception e) {
			EmiLog.error("Error while handling mouse scroll", e);
		}
	}
}
