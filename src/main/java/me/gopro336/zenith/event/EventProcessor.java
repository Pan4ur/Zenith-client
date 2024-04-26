package me.gopro336.zenith.event;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.api.util.client.PlayerUtils;
import me.gopro336.zenith.event.client.LoadGuiEvent;
import me.gopro336.zenith.event.entity.PlayerUseItemEvent;
import me.gopro336.zenith.event.player.MoveInputEvent;
import me.gopro336.zenith.event.render.Render2DEvent;
import me.gopro336.zenith.event.world.UpdateEvent;
import me.gopro336.zenith.feature.hudElement.Element;
import me.gopro336.zenith.api.util.IGlobals;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.feature.FeatureManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;


public class EventProcessor implements IGlobals
{
	public static boolean inGame = false;

	private void Catch(String Evnt, RuntimeException ex) {
		//   if (!ModProperties.ShowErrors.getValBoolean())
		// return;
		//ex.printStackTrace();
		System.err.println(Evnt+ex);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onForgeEvent(Event event) {
		Zenith.INSTANCE.getEventManager().dispatchEvent(event);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onGui(GuiOpenEvent event) {
		/*if (event.getGui() != null && event.getGui().getClass() == GuiMainMenu.class) {
			event.setGui(new ZenithMainMenu());
		}*/
	}

	@SubscribeEvent
	public void on2dRender(RenderGameOverlayEvent.Post event) {
		if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR) return;
		Zenith.INSTANCE.getEventManager()
				.dispatchEvent(new Render2DEvent(event.getPartialTicks(), new ScaledResolution(Minecraft.getMinecraft())));
		//Zenith.INSTANCE.getPopupManager().onRender();
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		PlayerUtils.lastLookAt = null;
		UpdateEvent updateEvent = UpdateEvent.get(event.phase);
		Zenith.INSTANCE.getEventManager()
				.dispatchEvent(updateEvent);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onClientTick(TickEvent.ClientTickEvent event) {
		FeatureManager.keyListen();
		if (!inGame) {
			return;
		}
		Zenith.INSTANCE.getEventManager()
				.dispatchEvent(event);
		Zenith.clickGUI.onUpdate();
		//Zenith.hudEditor.onUpdate();
		FeatureManager.getToggledFeatures().forEach(Feature::onUpdate);
		FeatureManager.onUpdate();
		/*try {
			Zenith.INSTANCE.getEventManager()
					.dispatchEvent(event);
			Zenith.clickGUI.onUpdate();
			Zenith.hudEditor.onUpdate();
			ModuleManager.getToggledModules().forEach(Module::onUpdate);
			ModuleManager.onUpdate();
		} catch (RuntimeException ex) {
			Catch("TickEvent.ClientTickEvent", ex);
		}*/
	}

	@SubscribeEvent
	public void setLivingEntityUseItemEvent(LivingEntityUseItemEvent livingEntityUseItemEvent) {
		if (livingEntityUseItemEvent.getEntity() instanceof EntityPlayerSP) {
			Zenith.INSTANCE.getEventManager()
					.dispatchEvent(new PlayerUseItemEvent());
		}
	}

	@SubscribeEvent
	public void onInput(InputUpdateEvent event) {
		MoveInputEvent moveInputEvent = new MoveInputEvent(event.getEntityPlayer(), event.getMovementInput());
		Zenith.INSTANCE.getEventManager()
				.dispatchEvent(moveInputEvent);
	}

	@SubscribeEvent
	public void onLoadGui(GuiOpenEvent event) {
		LoadGuiEvent loadGuiEvent = new LoadGuiEvent(event.getGui());
		Zenith.INSTANCE.getEventManager()
				.dispatchEvent(loadGuiEvent);
		event.setGui(loadGuiEvent.getGui());
		event.setCanceled(loadGuiEvent.isCanceled());
	}

	@SubscribeEvent
	public void setClientTickEvent(net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent clientTickEvent) {
		me.gopro336.zenith.event.world.TickEvent tickEvent = me.gopro336.zenith.event.world.TickEvent.Method325(clientTickEvent.phase);
		Zenith.INSTANCE.getEventManager()
				.dispatchEvent(tickEvent);
	}

	@SubscribeEvent
	public void onClientDisconnect(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
		FeatureManager.onClientDisconnect();
	}

	@SubscribeEvent
	public void onClientConnect(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
		FeatureManager.onClientConnect();
	}

	@SubscribeEvent
	public void onRender(RenderGameOverlayEvent.Post event) {
		if (event.isCanceled()) return;

		RenderGameOverlayEvent.ElementType target = RenderGameOverlayEvent.ElementType.EXPERIENCE;
		if (!mc.player.isCreative() && mc.player.getRidingEntity() instanceof AbstractHorse)
			target = RenderGameOverlayEvent.ElementType.HEALTHMOUNT;

		if (event.getType() == target) {
			FeatureManager.getHudElements().stream().filter(Feature::isEnabled).forEach(Element::onRender);
			GL11.glPushMatrix();
			GL11.glPopMatrix();
			releaseGL();
		}
	}

	public static void releaseGL() {
		GlStateManager.enableCull();
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.enableDepth();
	}

	/*@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent event) {
		if (!inGame)
			return;
		try {
			//Component rendering must be done this way in order to keep them rendering outside the hudeditor screen
			ModuleManager.getHudComponents().forEach(Component::render);
			//ModuleManager.getHudComponents().forEach(hudComponents -> hudComponents());
			//Zenith.EVENT_BUS.post(event);
		} catch (RuntimeException ex) {
			Catch("RenderGameOverlayEvent", ex);
		}
	}*/

	@SubscribeEvent
	public void onChatMessage(ClientChatEvent event) {
		if (event.getMessage().startsWith(Zenith.commandManager.getPrefix())) {
			String message = event.getMessage();
			event.setCanceled(true);
			Zenith.commandManager.executeCommand(message.substring(Zenith.commandManager.getPrefix().length()));
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		if (!inGame)
			return;
		try {
			Zenith.INSTANCE.getEventManager()
					.dispatchEvent(event);
		} catch (RuntimeException ex) {
			Catch("RenderWorldLastEvent", ex);
		}
		//ModuleManager.getToggledModules().forEach(Module::onUpdate);
		//ModuleManager.getToggledModules().forEach(Module::onRender3D(event));
		//FeatureManager.getToggledFeatures().forEach(module -> module.onRender3D(event));
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (mc.player == null || mc.world == null) {
			return;
		}
		try {
			Zenith.INSTANCE.getEventManager()
					.dispatchEvent(event);
		} catch (RuntimeException ex) {
			// catch
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onWorldLoad(WorldEvent.Load event) {
		inGame = true;
		try {
			Zenith.INSTANCE.getEventManager()
					.dispatchEvent(event);
		} catch (RuntimeException ex) {
			Catch("onWorldLoad", ex);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onWorldUnload(WorldEvent.Unload event) {
		try {
			inGame = false;
			Zenith.INSTANCE.getEventManager()
					.dispatchEvent(event);
		} catch (RuntimeException ex) {
			Catch("onWorldUnload", ex);
		}
	}

	/*
	phobos skid that im were not using
	@SubscribeEvent
	public void onWorldRender(final RenderWorldLastEvent event) {
		if (event.isCanceled()) {
			return;
		}
		/*EventProcessor.mc.profiler.startSection("zenith");
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.shadeModel(7425);
		GlStateManager.disableDepth();
		GlStateManager.glLineWidth(1.0f);
		//final Render3DEvent render3dEvent = new Render3DEvent(event.getPartialTicks());
		Zenith.moduleManager.onRender3D(render3dEvent);
		GlStateManager.glLineWidth(1.0f);
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.enableDepth();
		GlStateManager.enableCull();
		GlStateManager.enableCull();
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.enableDepth();
		EventProcessor.mc.profiler.endSection();*/
	//}

	/*public void onUnload() {
		MinecraftForge.EVENT_BUS.unregister((Object)this);
	}*/
	
}
