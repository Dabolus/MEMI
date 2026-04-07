package dev.emi.emi.runtime;

import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import dev.emi.emi.EmiPort;
import dev.emi.emi.config.EmiConfig;

public class EmiScreenshotRecorder {
	private static final String SCREENSHOTS_DIRNAME = "screenshots";

	/**
	 * Saves a screenshot to the game's `screenshots` directory, doing the appropriate setup so that anything rendered in renderer will be captured
	 * and saved.
	 */
	public static void saveScreenshot(String path, int width, int height, Runnable renderer) {
		saveScreenshotInner(path, width, height, renderer);
	}

	private static void saveScreenshotInner(String path, int width, int height, Runnable renderer) {
		Minecraft client = Minecraft.getInstance();

		// TODO: MC 26.1 screenshot system rewrite needed
		// RenderTarget.bindWrite/unbindWrite, NativeImage.downloadTexture/flipY,
		// TextureTarget constructor, and RenderSystem.bindTexture have all been removed.
		// The new GPU texture pipeline (GpuTexture/GpuTextureView) needs to be used instead.
		EmiLog.warn("Recipe screenshots are not yet supported in MC 26.1");
		client.execute(() -> client.gui.getChat().addClientSystemMessage(
			EmiPort.translatable("screenshot.failure", "Not yet supported in MC 26.1")));
	}

	private static String getScreenshotFilename(File directory, String path) {
		int i = 1;
		while ((new File(directory, path + (i == 1 ? "" : "_" + i) + ".png")).exists()) {
			++i;
		}
		return path + (i == 1 ? "" : "_" + i) + ".png";
	}
}
